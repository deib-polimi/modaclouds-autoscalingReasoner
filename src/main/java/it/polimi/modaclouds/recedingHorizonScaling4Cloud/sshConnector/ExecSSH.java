package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class ExecSSH {

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

	public List<String> mainExec() {
		List<String> res = new ArrayList<String>();
		try {

			JSch jsch = new JSch();
			Session session = jsch.getSession(this.ScpUserName, this.ScpHost, 22);

			if (this.ScpPasswd == "")
				this.ScpPasswd = JOptionPane.showInputDialog("Enter password");
			session.setPassword(this.ScpPasswd);

			session.setConfig("StrictHostKeyChecking", "no");
			
			
			session.connect();

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand("bash " 
					+ this.optLauncher);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
