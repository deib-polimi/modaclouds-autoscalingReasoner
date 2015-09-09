package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ExecSSH {
	
	@SuppressWarnings("unused")
	private static final Logger journal = LoggerFactory
			.getLogger(ExecSSH.class);

	public String ScpUserName;
	public String ScpHost;
	public String ScpPasswd;

	public String optLauncher;

	public ExecSSH() {
		

			ScpUserName = ConfigManager.SSH_USER_NAME;
			ScpHost = ConfigManager.SSH_HOST;
			ScpPasswd = ConfigManager.SSH_PASSWORD;
			optLauncher= ConfigManager.OPTIMIZATION_LAUNCHER;
	}
	
	public List<String> mainExec() throws Exception {
		return mainExec("bash " + this.optLauncher);
	}

	public List<String> mainExec(String command) throws Exception {
		
		if (ConfigManager.isRunningLocally())
			return localExec(command);
		
		List<String> res = new ArrayList<String>();

		JSch jsch = new JSch();
		Session session = jsch.getSession(this.ScpUserName, this.ScpHost, 22);

		if (this.ScpPasswd == "")
			this.ScpPasswd = JOptionPane.showInputDialog("Enter password");
		session.setPassword(this.ScpPasswd);

		session.setConfig("StrictHostKeyChecking", "no");
		
		
		session.connect();

		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);
		channel.setInputStream(null);
		//((ChannelExec) channel).setErrStream(System.err);

		
		InputStream in = channel.getInputStream();
		channel.connect();
		byte[] tmp = new byte[1024];

		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				res.add(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				res.add("exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		
		channel.disconnect();
		session.disconnect();
		
		return res;
	}
	
	public List<String> localExec(String command) throws Exception {
		List<String> res = new ArrayList<String>();
		ProcessBuilder pb = new ProcessBuilder(command.split(" "));
		pb.redirectErrorStream(true);
		
		Process p = pb.start();
		BufferedReader stream = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = stream.readLine(); 
		while (line != null) {
			res.add(line);
			line = stream.readLine();
		}
		stream.close();
		
		return res;
	}
}
