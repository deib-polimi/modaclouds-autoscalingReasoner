/**
 * Copyright ${year} deib-polimi
 * Contact: deib-polimi <giovannipaolo.gibilisco@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ssh;

import com.jcraft.jsch.*;

import util.ConfigDictionary;
import it.polimi.modaclouds.space4cloud.milp.ssh.ScpTo.MyUserInfo;

import javax.swing.*;

import java.io.*;

//this class is used to download files from AMPL server
public class ScpFrom {

	// login to AMPL server
	public String ScpUserName;
	// AMPL server's address
	public String ScpHost;
	// password for account on AMPL server
	public String ScpPasswd;

	// constructor
	public ScpFrom() {
		ScpUserName = ConfigDictionary.SSH_USER_NAME;
		ScpHost = ConfigDictionary.SSH_HOST;
		ScpPasswd = ConfigDictionary.SSH_PASSWORD;
	}

	// main execution function
	// coping RFile on AMPL server in LFile on local machine
	public void receivefile(String LFile, String RFile) {
		FileOutputStream fos = null;
		try {

			String lfile = LFile;
			String rfile = RFile;

			// creating session with username, server's address and port (22 by
			// default)
			JSch jsch = new JSch();
			Session session = jsch.getSession(this.ScpUserName, this.ScpHost, 22);

			// receiving user password if it was not collected before
			if (this.ScpPasswd == "")
				this.ScpPasswd = JOptionPane.showInputDialog("Enter password");
			session.setPassword(this.ScpPasswd);

			// this class sets visual forms for interactions with users
			// required by implementation
			UserInfo ui = new MyUserInfo() {
				public void showMessage(String message) {
					JOptionPane.showMessageDialog(null, message);
				}

				public boolean promptYesNo(String message) {
					Object[] options = { "yes", "no" };
					int foo = JOptionPane.showOptionDialog(null, message,
							"Warning", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options,
							options[0]);
					return foo == 0;
				}
			};
			String prefix = null;
			if (new File(lfile).isDirectory()) {
				prefix = lfile + File.separator;
			}
			session.setUserInfo(ui);
			// disabling of certificate checks
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			// exec 'scp -f rfile' remotely
			String command = "scp -f " + rfile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			// reading channel
			while (true) {
				int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						break;
					}
					if (buf[0] == ' ')
						break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
				fos = new FileOutputStream(prefix == null ? lfile : prefix
						+ file);
				int foo;
				while (true) {
					if (buf.length < filesize)
						foo = buf.length;
					else
						foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L)
						break;
				}
				fos.close();
				fos = null;

				if (checkAck(in) != 0) {
					System.exit(0);
				}

				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
			}

			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ee) {
			}
		}
	}

	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
}