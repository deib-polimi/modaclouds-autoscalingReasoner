package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nicolasf on 19.02.15.
 */
public class ScalingWSClient extends WebSocketClient {
	private Boolean connected = false;
	private boolean forwardOutput=false;
	private static final Logger journal = Logger
			.getLogger(ScalingWSClient.class.getName());

	public ScalingWSClient(URI serverURI) throws InterruptedException {
		super(serverURI, new Draft_17());
	}
	
	public void setForwardOutput(){
		this.forwardOutput=true;
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		journal.log(Level.INFO, ">> Connected to the CloudML server");
		connected = true;
	}

	@Override
	public void onMessage(String s) {
		
		if(this.forwardOutput){
			CloudMLAdapter.receiveDeploymentModel(s);
			this.forwardOutput=false;
		}else{
			journal.log(Level.INFO, ">> " + s);
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		journal.log(Level.INFO, ">> Disconnected from the CloudML server " + s);
		connected = false;
		this.close();
	}

	@Override
	public void onError(Exception e) {
	}

	public Boolean getConnected() {
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
}
