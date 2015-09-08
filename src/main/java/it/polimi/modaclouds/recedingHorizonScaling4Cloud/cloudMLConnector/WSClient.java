package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.CloudMLReturnedModelException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSClient extends WebSocketClient {
	private Boolean connected = false;
	private static final Logger journal = LoggerFactory
			.getLogger(WSClient.class);

	public WSClient(URI serverURI) throws InterruptedException {
		super(serverURI, new Draft_17());
	}

	
	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		journal.info(">> Connected to the CloudML server");
		connected = true;
	}

	@Override
	public void onMessage(String s) {
		
		journal.info("Received message from CloudML server");

		if(s.equals("!ack {fromPeer: org.cloudml.facade.commands.ScaleOut, status: completed}")){
			journal.info("Scale out completed! Asking CloudML for the deployment model");
			this.send("!getSnapshot { path : / }");

		}else if(s.contains("return of GetSnapshot")){
			journal.info("Received deployment model from CloudML");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(s.substring(27));
				JSONArray instances=jsonObject.getJSONArray("vmInstances");
				ModelManager.updateDeploymentInfo(instances);
			} catch (JSONException | TierNotFoudException | CloudMLReturnedModelException e) {
				journal.error("Error while parsing the deployment info returned by CloudML.", e);
			}
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		journal.info(">> Disconnected from the CloudML server {}", s);
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
