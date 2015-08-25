package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CloudMLAdapter {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	private WSClient wsClient;
	private Thread t;
	
	public CloudMLAdapter(){
		
		String serverURI="ws://"+ConfigManager.CLOUDML_WEBSOCKET_IP+":"+
				ConfigManager.CLOUDML_WEBSOCKET_PORT;
		try {
		wsClient=new WSClient(new URI(serverURI));
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
	
	
	
	public void scaleOut(String vmId, int times) {			
		
			wsClient.send("!listenToAny");

		 	wsClient.send("!extended { name: ScaleOut, params: ["+vmId+","+times+"] }");
			
	}
	
	public void getDeploymentModel() {
		wsClient.send("!getSnapshot { path : / }");
	}
	
	public void createImage(String instanceId) {
		wsClient.send("!extended { name: Image, params: ["+instanceId+"] }");
	}
	
	public void stopInstances(String instances){
		wsClient.send("!listenToAny");
		wsClient.send("!extended { name: StopComponent, params: ["+instances+"] }");
			
	}
	
	public void startInstances(String instances){
		wsClient.send("!listenToAny");
		wsClient.send("!extended { name: StartComponent, params: ["+instances+"] }");
	}
	
	public void getInstanceInfo(String id){
		wsClient.send("!getSnapshot\n"+
						"path : /componentInstances[id='"+id+"']");
	}

}
