package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CloudMLAdapterOld {
	private static final Logger journal = LoggerFactory
			.getLogger(CloudMLAdapterOld.class);
	private WSClient wsClient;
	private Thread t;
	
	public CloudMLAdapterOld(){
		
		String serverURI="ws://"+ConfigManager.CLOUDML_WEBSOCKET_IP+":"+
				ConfigManager.CLOUDML_WEBSOCKET_PORT;
		try {
		journal.info("Connecting to CloudML Web Socket server");
		wsClient=new WSClient(serverURI);
		t =new Thread(wsClient);
		t.start();
		while(!wsClient.isConnected()){
		Thread.sleep(2000);
		}
		} catch (InterruptedException e) {
			journal.error("Error while waiting.", e);
		}

		
	}
	
	
	
	public void scaleOut(String vmId, int times) {			
		
			wsClient.send("!listenToAny");
		 	wsClient.send("!extended { name: ScaleOut, params: ["+vmId+","+times+"] }");
			
	}
	
	public void getDeploymentModel() {
		wsClient.send("!listenToAny");
		wsClient.send("!getSnapshot { path : / }");
	}
	
	public void createImage(String instanceId) {
		wsClient.send("!listenToAny");
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
		wsClient.send("!listenToAny");
		wsClient.send("!getSnapshot\n"+
						"path : /componentInstances[id='"+id+"']");
	}

}
