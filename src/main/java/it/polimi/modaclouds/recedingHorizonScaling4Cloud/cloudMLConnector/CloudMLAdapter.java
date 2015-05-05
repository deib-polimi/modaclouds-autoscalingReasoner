package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
		
			System.out.println("Scaling out "+times+" instances");
			wsClient.send("!listenToAny");
		
			/*
			for(int i=1; i<=times;i++){
				while(wsClient.getWaiting()!=null){
					journal.log(Level.INFO, "WS CLIENT BUSY");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			 	wsClient.setWaiting(this);
				wsClient.send("!extended { name: ScaleOut, params: ["+command.getVmId()+"] }");
				
				synchronized (this){
					  try {
						  System.out.println("scale out command sent. CloudMLAdapter wait for a response");
					     this.wait();
					  } catch (InterruptedException e) {
					  }
				}
				System.out.println("CloudMLAdapter wake up, single scale out action completed");
				this.getDeploymentModel();
			}
			*/	
			/*
			while(wsClient.getWaiting()!=null){
				journal.log(Level.INFO, "WS CLIENT BUSY");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		 	wsClient.setWaiting(this);
			*/
		 	wsClient.send("!extended { name: ScaleOut, params: ["+command.getVmId()+","+times+"] }");
			
		 	/*
			synchronized (this){
				  try {
					  System.out.println("scale out command sent. CloudMLAdapter wait for a response");
				     this.wait();
				  } catch (InterruptedException e) {
				  }
			}
			*/
		 	try {
				Thread.sleep(270000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Calling getDeploymentModel");
			this.getDeploymentModel();
			
	}
	
	public void getDeploymentModel() {
		wsClient.send("!getSnapshot { path : / }");
	}
	
	public void getInstanceInfo(String id){
		wsClient.send("!getSnapshot\n"+
						"path : /componentInstances[id='"+id+"']");
	}
	
	public void createImage(String instanceId) {
		wsClient.send("!extended { name: Image, params: ["+instanceId+"] }");
	}
	
	public void stopInstance(List<String> instances){
		wsClient.send("!listenToAny");
		
		/*
			while(wsClient.getWaiting()!=null){
				journal.log(Level.INFO, "WS CLIENT BUSY");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		 	wsClient.setWaiting(this);
			wsClient.send("!extended { name: StopComponent, params: ["+instanceId+"] }");
			synchronized (this){
				  try {
				     this.wait();
				  } catch (InterruptedException e) {
				  }
			}
			
			System.out.println("CloudML adapter wake up, single stop command completed");
		*/
		/*
		while(wsClient.getWaiting()!=null){
			journal.log(Level.INFO, "WS CLIENT BUSY");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	 	wsClient.setWaiting(this);
	 	*/
	 	String toSend="";
	 	for(String instance: instances ){
	 		if(toSend.equals("")){
	 			toSend+=instance;
	 		}else{
	 			toSend+=","+instance;
	 		}
	 	}
	 	
		wsClient.send("!extended { name: StopComponent, params: ["+toSend+"] }");
			
	}
	
	public void startInstance(List<String> instances){
		
		for(String instanceId: instances){
			System.out.println("restarting instance "+instanceId);
		}
		wsClient.send("!listenToAny");
		
		/*
			while(wsClient.getWaiting()!=null){
				journal.log(Level.INFO, "WS CLIENT BUSY");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		 	wsClient.setWaiting(this);
			wsClient.send("!extended { name: StartComponent, params: ["+instanceId+"] }");
			
			synchronized (this){
				  try {
				     this.wait();
				  } catch (InterruptedException e) {
				  }
			}
			
			System.out.println("CloudML adapter wake up, single start command completed");
		*/
		
		//wsClient.setWaiting(this);
	 	String toSend="";
	 	for(String instance: instances ){
	 		if(toSend.equals("")){
	 			toSend+=instance;
	 		}else{
	 			toSend+=","+instance;
	 		}
	 	}
 		System.out.println(toSend);

		wsClient.send("!extended { name: StartComponent, params: ["+toSend+"] }");
	}

}
