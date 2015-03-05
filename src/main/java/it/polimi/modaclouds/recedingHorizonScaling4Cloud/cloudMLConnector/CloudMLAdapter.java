package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.cloudml.facade.RemoteFacade;
import org.cloudml.facade.commands.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class CloudMLAdapter {
	
	private static ScalingWSClient wsClient;
	private Thread t;
	private static String lastCommand;
	
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
		
		wsClient.send("!getSnapshot { path : / }");
	}
	
	public void updateRunningInstances(String tierId){
		
	}
}
