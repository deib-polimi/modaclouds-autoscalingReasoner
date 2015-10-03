/**
 * Copyright (C) 2014 Politecnico di Milano (michele.guerriero@mail.polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		try {
			int limit = Integer.parseInt(ConfigManager.SCALE_LIMIT);
			if (times > limit) {
				journal.warn("You've requested to scale too many instances at once ({}). Falling back to the limit ({}).", times, limit);
				times = limit;
			}
		} catch (Exception e) { }
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
