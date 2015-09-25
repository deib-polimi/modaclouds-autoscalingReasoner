package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLCall.CloudML;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CloudMLAdapter {
	private static final Logger journal = LoggerFactory
			.getLogger(CloudMLAdapter.class);
	private CloudML client;
	
	public CloudMLAdapter(){
		
		journal.info("Connecting to CloudML Web Socket server");
		
		boolean goOn = true;
		while (goOn) {
			try {
				client = CloudMLCall.getCloudML(
						ConfigManager.CLOUDML_WEBSOCKET_IP,
						Integer.parseInt(ConfigManager.CLOUDML_WEBSOCKET_PORT));
				goOn = false;
			} catch (Exception e) {
				journal.info("Retrying in 10 seconds...");
				try { Thread.sleep(10000); } catch (Exception e1) { }
			}
		}
	}
	
	public void scaleOut(String vmId, int times) {
		client.scale(vmId, times);
	}
	
	public void getDeploymentModel() {
		client.updateStatus();
	}
	
	public void stopInstances(String instances) {
		client.stopInstances(instances);
	}
	
	public void startInstances(String instances) {
		client.startInstances(instances);
	}

}
