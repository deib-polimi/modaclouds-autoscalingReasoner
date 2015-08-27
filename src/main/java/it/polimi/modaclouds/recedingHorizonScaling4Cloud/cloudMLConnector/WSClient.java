package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.CloudMLReturnedModelException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WSClient extends WebSocketClient {
	private Boolean connected = false;
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());

	public WSClient(URI serverURI) throws InterruptedException {
		super(serverURI, new Draft_17());
	}

	
	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		journal.log(Level.INFO, ">> Connected to the CloudML server");
		connected = true;
	}

	@Override
	public void onMessage(String s) {
		
		journal.log(Level.INFO, "Received message from CloudML server");

		if(s.contains("ack") & s.contains("ScaleOut")){
			journal.log(Level.INFO, "Scale out completed! Sking CloudML for the deployment model");
			this.send("!getSnapshot { path : / }");

		}else if(s.contains("return of GetSnapshot")){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(s.substring(27));
			JSONArray instances=jsonObject.getJSONArray("vmInstances");
			ModelManager.updateDeploymentInfo(instances);
		} catch (JSONException | TierNotFoudException | CloudMLReturnedModelException e) {
			e.printStackTrace();
		}
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
