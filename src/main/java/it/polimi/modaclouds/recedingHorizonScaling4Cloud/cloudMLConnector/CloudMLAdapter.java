package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import java.net.URI;
import java.net.URISyntaxException;

import org.cloudml.facade.RemoteFacade;
import org.cloudml.facade.commands.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class CloudMLAdapter {
	
	private RemoteFacade facade=new RemoteFacade("ws://127.0.0.1:9000");
	private ScalingWSClient wsClient;
	private Thread t;
	
	public CloudMLAdapter(String serverURI){
		
		try {
		wsClient=new ScalingWSClient(new URI(serverURI));
		t =new Thread(wsClient);
		t.start();
		while(!wsClient.getConnected()){
		Thread.sleep(2000);
		}
		} catch (URISyntaxException e) {
		e.printStackTrace();
		} catch (InterruptedException e) {
		e.printStackTrace();
		}
	}
	
	public void scaleOut(ScaleOut command) {
		wsClient.send("!extended { name: ScaleOut, params: ["+command.getVmId()+"] }");
	}
	
	public void getDeploymentModel() {
		wsClient.setForwardOutput();
		wsClient.send("!getSnapshot { path : / }");
	}
	
	public static void receiveDeploymentModel(String model){
		try {  
			
			JSONObject jsonObject = new JSONObject(model.substring(27));
				System.out.println("\n\njsonArray: " + jsonObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
}
