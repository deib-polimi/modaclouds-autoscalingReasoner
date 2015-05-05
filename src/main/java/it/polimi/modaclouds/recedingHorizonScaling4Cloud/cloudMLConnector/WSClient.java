package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationInitializer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.DeploymentException;


public class WSClient extends WebSocketClient {
	private Boolean connected = false;
	private Object waiting;
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
		
		if(s.contains("ack") & s.contains("ScaleOut")){
			System.out.println(s);

			synchronized(waiting) {
				System.out.println("ricevuto ack di scaleout completato, notifico a cloudmlAdapter");
		          waiting.notify();
				  this.setWaiting(null);
		    }
		}else if(s.contains("return of GetSnapshot") && !s.contains("status")){

		System.out.println("ricevuto snapshot del deployment model");
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(s.substring(27));
			JSONArray instances=jsonObject.getJSONArray("vmInstances");
			System.out.println("sto per chiamare l update runtime environment");
			ModelManager.updateRuntimeEnv(instances);
		} catch (JSONException | TierNotFoudException e) {
			e.printStackTrace();
		}
		}else if(s.contains("ack") & s.contains("StopComponent")){
			System.out.println(s);

		}else if(s.contains("ack") & s.contains("StartComponent")){
			System.out.println(s);

		}else if (s.contains("return of GetSnapshot") && s.contains("status")){
			
			System.out.println("received instance information");
			String[] lines=s.split("\n");
			
			String id=null;
			String status=null;
		
			for(int i=0; i<lines.length; i++){
				String line=lines[i];
				if(line.contains("id:")){
					id=line.split(": ")[1];
				}else if(line.contains("status:")){
					status=line.split(": ")[1];
				}
			}
				
			if(id!=null){
				if(status.equals("STOPPED")){
					String tier=ModelManager.getTierIdByInstanceId(id);
					if(tier!=null){
					try {
						ModelManager.stopInstance(id,tier);
					} catch (TierNotFoudException e) {
						e.printStackTrace();
					}
					}else{
						System.out.println("Error with the deployment model: received information about an instance not associated with any tie (unknown instance id)");
					}
				}
				
			}else{
				System.out.println("Error with the deployment model: received instance information without an associated instance id");
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

	public Object getWaiting() {
		return waiting;
	}

	public void setWaiting(Object waiting) {
		this.waiting = waiting;
	}
}
