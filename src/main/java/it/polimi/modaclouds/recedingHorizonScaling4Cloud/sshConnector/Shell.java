package it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shell {

	protected static final Logger journal = LoggerFactory.getLogger(Shell.class);

	public static final int SSH_PORT = 22;

	protected String ip;
	protected String user;
	protected String password;
	protected String key;

	public Shell(String ip, String user, String password, String key) {
		this.ip = ip;
		this.user = user;
		this.password = password;
		this.key = key;
	}

	public Shell() {
		this(ConfigManager.SSH_HOST, ConfigManager.SSH_USER_NAME, ConfigManager.SSH_PASSWORD, null);
	}
	
	public List<String> exec(String command) throws Exception {
		long init = System.currentTimeMillis();
		
		List<String> res = null;
		if (ConfigManager.isRunningLocally())
			res = LocalShell.exec(command);
		else
			res = RemoteShell.exec(ip, user, password, key, command);
		
		long duration = System.currentTimeMillis() - init;
		journal.debug("Executed `{}` on {} in {}", command, ip, durationToString(duration));
		
		return res;
	}
	
	public void receiveFile(String lfile, String rfile) throws Exception {
		long init = System.currentTimeMillis();
		
		if (ConfigManager.isRunningLocally())
			LocalShell.receiveFile(lfile, rfile);
		else
			RemoteShell.receiveFile(ip, user, password, key, lfile, rfile);
		
		long duration = System.currentTimeMillis() - init;
		journal.debug("File `{}` received from {} in {}", rfile, ip, durationToString(duration));
	}
	
	public void sendFile(String lfile, String rfile) throws Exception {
		long init = System.currentTimeMillis();
		
		if (ConfigManager.isRunningLocally())
			LocalShell.sendFile(lfile, rfile);
		else
			RemoteShell.sendFile(ip, user, password, key, lfile, rfile);
		
		long duration = System.currentTimeMillis() - init;
		journal.debug("File `{}` sent to {} in {}", lfile, ip, durationToString(duration));
	}

	
	public static String durationToString(long duration) {
		StringBuilder sb = new StringBuilder();
		{
			int res = (int) TimeUnit.MILLISECONDS.toSeconds(duration);
			if (res > 60 * 60) {
				sb.append(res / (60 * 60));
				sb.append(" h ");
				res = res % (60 * 60);
			}
			if (res > 60) {
				sb.append(res / 60);
				sb.append(" m ");
				res = res % 60;
			}
			sb.append(res);
			sb.append(" s");
		}

		return sb.toString();
	}

}
