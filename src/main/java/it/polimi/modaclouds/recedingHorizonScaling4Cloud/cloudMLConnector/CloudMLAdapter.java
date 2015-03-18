package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.facade.commands.ScaleOut;


public class CloudMLAdapter {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	private WSClient wsClient;
	private Thread t;
	
	public CloudMLAdapter(String serverURI){
						
		
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
	
	public void scaleOut(ScaleOut command, int times) {			
		
			wsClient.send("!listenToAny");
		
			for(int i=1; i<=times;i++){
				while(wsClient.getWaiting()!=null){
					journal.log(Level.INFO, "WS CLIENT BUDY");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			 	wsClient.setWaiting(this);
				wsClient.send("!extended { name: ScaleOut, params: ["+command.getVmId()+"] }");
				
				synchronized (this){
					  try {
					     this.wait();
					  } catch (InterruptedException e) {
					        //when the object is interrupted
					   }
				}
			}		
	}
	
	public void getDeploymentModel() {
		wsClient.send("!getSnapshot { path : / }");
	}

}
