/**
 * Copyright (C) 2014 Politecnico di Milano (michele.guerriero@mail.polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class RemoteShell {
	
	protected static final Logger journal = LoggerFactory.getLogger(RemoteShell.class);

	public static List<String> exec(String ip, String user, String password, String key, String command) throws Exception {
		final List<String> res = new ArrayList<String>();

		// creating session with username, server's address and port (22 by
		// default)
		JSch jsch = new JSch();

		if (key != null)
			jsch.addIdentity(key);

		Session session = jsch.getSession(user, ip, 22);
		session.setPassword(password);

		// disabling of certificate checks
		session.setConfig("StrictHostKeyChecking", "no");
		// creating connection
		session.connect();

		// creating channel in execution mod
		final Channel channel = session.openChannel("exec");
		// sending command which runs bash-script in UploadPath directory
		((ChannelExec) channel).setCommand(command);
		// taking input stream
		channel.setInputStream(null);
		// connecting channel
		channel.connect();

		Thread in = new Thread() {
			public void run() {
				try (Scanner sc = new Scanner(channel.getInputStream())) {
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						journal.trace(line);
						res.add(line);
					}
				} catch (Exception e) {
					journal.error("Error while considering the input stream.", e);
				}
			}
		};
		in.start();

		Thread err = new Thread() {
			public void run() {
				try (Scanner sc = new Scanner(((ChannelExec) channel).getErrStream())) {
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						journal.trace(line);
						res.add(line);
					}
				} catch (Exception e) {
					journal.error("Error while considering the error stream.", e);
				}
			}
		};
		err.start();

		in.join();
		err.join();
		if (channel.isClosed())
			res.add("exit-status: " + channel.getExitStatus());

		// closing connection
		channel.disconnect();
		session.disconnect();
		
		return res;
	}

	public static void receiveFile(String ip, String user, String password, String key, String lfile, String rfile) throws Exception {
		FileOutputStream fos = null;
		try {
			// creating session with username, server's address and port (22 by
			// default)
			JSch jsch = new JSch();

			if (key != null)
				jsch.addIdentity(key);

			Session session = jsch.getSession(user, ip, 22);
			session.setPassword(password);

			String prefix = null;
			if (new File(lfile).isDirectory()) {
				prefix = lfile + File.separator;
			}
			// session.setUserInfo(ui);
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
				fos = new FileOutputStream(prefix == null ? lfile : prefix + file);
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
			journal.error("Error while performing the command.", e);
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ee) {
			}
		}
	}

	public static void sendFile(String ip, String user, String password, String key, String lfile, String rfile) throws Exception {
		FileInputStream fis = null;
		try {
			// creating session with username, server's address and port (22 by
			// default)
			JSch jsch = new JSch();

			if (key != null)
				jsch.addIdentity(key);

			Session session = jsch.getSession(user, ip, 22);
			session.setPassword(password);

			// disabling of certificate checks
			session.setConfig("StrictHostKeyChecking", "no");
			// creating connection
			session.connect();

			boolean ptimestamp = true;
			// exec 'scp -t rfile' remotely
			String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rfile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			// connecting channel
			channel.connect();

			if (checkAck(in) != 0) {
				System.exit(0);
			}

			File _lfile = new File(lfile);

			if (ptimestamp) {
				command = "T" + (_lfile.lastModified() / 1000) + " 0";
				command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					System.exit(0);
				}
			}
			// send "C0644 filesize filename", where filename should not include
			// '/'
			long filesize = _lfile.length();
			command = "C0644 " + filesize + " ";
			if (lfile.lastIndexOf('/') > 0) {
				command += lfile.substring(lfile.lastIndexOf('/') + 1);
			} else {
				command += lfile;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
			// send a content of lfile
			fis = new FileInputStream(lfile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len);
			}
			fis.close();
			fis = null;
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
			out.close();

			channel.disconnect();
			session.disconnect();

		} catch (Exception e) {
			journal.error("Error while performing the command.", e);
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ee) {
			}
		}
	}
	
	private static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
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
				journal.error(sb.toString());
			}
			if (b == 2) { // fatal error
				journal.error(sb.toString());
			}
		}
		return b;
	}

}
