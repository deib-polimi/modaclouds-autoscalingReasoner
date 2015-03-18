package it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Functionality;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class FakeObserver {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	
	public FakeObserver(){
		
		
	}
	
	public void startCollectData(Containers model, int optimizationHorizon) throws NumberFormatException{
		
		while(true){
			try{
				BufferedReader br = 
	                    new BufferedReader(new InputStreamReader(System.in));
	
				String input;
				
				for(Container c:model.getContainer()){
					for(ApplicationTier t:c.getApplicationTier()){
						
						System.out.println("RACCOLGO DATI DI RUNTIME PER APPLICATIO TIER: "+t.getId());

						
						for(Functionality f:t.getFunctionality()){
							System.out.println("Inserimento dati per funzionalità: "+f.getId());
							
							System.out.println("inserire demand stimata");
							input=br.readLine();
							ModelManager.updateDemand(t.getId(), Float.parseFloat(input));
	
							
							for(int i=1; i<=optimizationHorizon;i++){
								System.out.println("inserire previsione workload "+i+" timestep in avanti");

								input=br.readLine();
								ModelManager.updateWorkloadPrediction(t.getId(), "WorkloadForecast"+i, Float.parseFloat(input));
	
							}
						}
					}
				}
		 
			}catch(IOException io){
				io.printStackTrace();
			}
		}
	}

}
