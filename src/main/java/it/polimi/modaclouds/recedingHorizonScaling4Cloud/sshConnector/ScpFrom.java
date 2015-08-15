package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;



public class ScpFrom {

	public String ScpUserName;
	public String ScpHost;
	public String ScpPasswd;

	public ScpFrom() {
		ScpUserName = ConfigManager.SSH_USER_NAME;
		ScpHost = ConfigManager.SSH_HOST;
		ScpPasswd = ConfigManager.SSH_PASSWORD;
	}

	public void receivefile(String LFile, String RFile) {
		FileOutputStream fos = null;
		try {

			String lfile = LFile;
			String rfile = RFile;

			JSch jsch = new JSch();
			Session session = jsch.getSession(this.ScpUserName, this.ScpHost, 22);

			if (this.ScpPasswd == "")
				this.ScpPasswd = JOptionPane.showInputDialog("Enter password");
			session.setPassword(this.ScpPasswd);

			String prefix = null;
			if (new File(lfile).isDirectory()) {
				prefix = lfile + File.separator;
			}
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			String command = "scp -f " + rfile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
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
