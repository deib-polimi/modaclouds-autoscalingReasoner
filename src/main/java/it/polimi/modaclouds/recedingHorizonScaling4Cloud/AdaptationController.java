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
package it.polimi.modaclouds.recedingHorizonScaling4Cloud;

import it.polimi.modaclouds.qos_models.schema.ApplicationTier;
import it.polimi.modaclouds.qos_models.schema.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptationController extends TimerTask {
	private static final Logger journal = LoggerFactory
			.getLogger(AdaptationController.class);
	
	private Container toAdapt;

	
	public AdaptationController(Container toSet){
		toAdapt=toSet;
	}
	
	public void run(){
		CloudMLAdapter cloudml=new CloudMLAdapter();
		
		journal.info("Here is a controller starting an adaptation step for container: {}", toAdapt.getId());
		
		//asking cloudml for the current deployment model; it will be checked that the internal info are compliant with	
		//those returned by cloudml (like the status of each VM)
		cloudml.getDeploymentModel();
		
		//waiting for the deployment model to be received and processed
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			journal.error("Error while waiting.", e1);
		}
		
		//checking if a new tier has been deployed during the last timestep and need to have the instanceToScale set;
		//if this is the first step for the application tier being deployed it will be found with running instance
		//but with no instance used for scale associated becuase the running instances have be found by the getDeployment
		//call at the beginning of this threas. The controller initializes the tier instance used for scale.
		
		for(ApplicationTier tier: toAdapt.getApplicationTier()){
			if(ModelManager.getInstanceToScale(tier.getId())==null & tier.getInstances().size()>0){
				journal.info("First time for the tier being deployed; initializing the instance used for scale");
				ModelManager.initializeUsedForScale(tier.getId());
			}
		}

		if (shouldPerformTest(toAdapt)) {
			//writing dynamic input files for the current timestep and container
			journal.info("Writing the dynamic input files for the current timestep");
			OptimizationInputWriter siw= new OptimizationInputWriter();
			siw.writeDynamicInput(toAdapt);
			
			//launching optimization
			journal.info("Launching the optimization in the remote server via ssh");
			SshAdapter.executeOptimization(toAdapt);
			
			//parsing the output file
			journal.info("Parsing the optimization output and getting the result");
			OptimizationOutputParser outputParser= new OptimizationOutputParser();
			int[] algorithmResult=outputParser.parseExecutionOutput(toAdapt);
			
			//store the current dynamic input file and the output file at the end of the iteration
			journal.info("Storing the dynamic input files for the current timestep");
			List<String> toStore=new ArrayList<String>();
			toStore.add(OptimizationInputWriter.DYNAMIC_INPUT_FILE_NAME + ".dat");
			toStore.add(OptimizationOutputParser.OUTPUT_FILE_NAME + ".out");
			try {
				OptimizationInputWriter.storeFiles(toStore, toAdapt.getId());
			} catch (ProjectFileSystemException e) {
				journal.error("Error while storing the files.", e);
			}
			
			//analyse the optimization output and enact adaptation actions
			journal.info("Start checking the result for each application tier");
			try {
						
				for(ApplicationTier tier: toAdapt.getApplicationTier()){
						
					journal.info("Checking the optimization result for application tier: {}", tier.getId());
	
					
					int tierResult=algorithmResult[tier.getClassIndex()-1];
					List<String> expiring=ModelManager.getExpiringInstances(tier.getId(), 1);
					int numOfInstancesToAdd=tierResult-expiring.size();
					
					journal.info("Number of new instances to add at the next timestep as suggested by the optimization={}", tierResult);
					journal.info("Number of expiring instance for the current application tier={}", expiring.size());
					
					if(numOfInstancesToAdd>0){
	
						//SUMMARY
						//all the expiring instances need to be renewed (left running and restart the internal time)
						//and a number of instances equal to numOfInstancesToAdd need to be added to the deployment.
						//Among these, as far as there are stopped instances available, the controller restart the stopped instance.
						//When no stopped instances are available anymore, but there are still some instances to add to the deployment
						//(instance to scale is still numOfInstancesToAdd than 0), these last will be added scaling out.
						journal.info("Being the number of expiring instances less then the number of instance to add suggested by the optimization"
								+ "all the expiring instances are going to be renwed and the remaining needed are going to be started/scaled out");
	
						
						//renewing the expiring instances
						journal.info("Rewneing all the expiring instances");
	
						for(String toRenew: expiring){
							ModelManager.renewRunningInstance(toRenew);
						}
						
						//determine the number of stopped instances to restart 
						//and the number of instances to be added scaling out
						journal.info("Determining the number of stopped instances to restart and the number of instances that instead need to be scaled out");
						List<String> stoppedInstances=ModelManager.getStoppedInstances(tier.getId());
	
						int numOfInstancesToRestart;
						int numOfInstancesToScaleOut;
						
						
						if(numOfInstancesToAdd>=stoppedInstances.size()){
							numOfInstancesToRestart=stoppedInstances.size();
							numOfInstancesToScaleOut=numOfInstancesToAdd-numOfInstancesToRestart;
						}else{
							numOfInstancesToRestart=numOfInstancesToAdd;
							numOfInstancesToScaleOut=0;
						}
						
						journal.info("Number of stopped instances to restart={}", numOfInstancesToRestart);
						journal.info("Number of stopped instances to scale out={}", numOfInstancesToScaleOut);
	
						
						//restarting the stopped necessary instances (if any)
						//NOTE: numOfInstancesToRestart is assured to be less then stoppedInstences.size() from the calculus above
						journal.info("Restarting instances");
						if(numOfInstancesToRestart>0){
							List<String> instanceToRestart=new ArrayList<String>();
							for(int i=0; i<numOfInstancesToRestart;i++){
								instanceToRestart.add(stoppedInstances.get(i));
							}
							
							ModelManager.startInstance(instanceToRestart);
						}
						
						//scaling the remaining instances to add (if any)	
						journal.info("Scaling out instanes");
						if(numOfInstancesToScaleOut>0){
	//						cloudml.scaleOut(ModelManager.getInstanceToScale(tier.getId()), numOfInstancesToScaleOut);
							cloudml.scaleOut(tier.getId(), numOfInstancesToScaleOut);
							//cloudml.scaleOut(ModelManager.getInstanceToScale(tier.getId()), 1);
						}
						
					}
					else if (numOfInstancesToAdd<0){
						
						//SUMMARY
						// a number of instances equal to numOfInstancesToAdd among those expiring have to be renewed.
						// the remaining expiring instances have to be stopped. If among these there is the currently used to scale
						// after it is stopped the instance used to scale need to be initialized.
						int numOfInstancesToRenew=tierResult;
						
						//creating a list of instacesToRenew and a list of instancesToStop
						List<String> instancesToRenew=new ArrayList<String>();
						List<String> instancesToStop=new ArrayList<String>();
						
						Iterator<String> iter=expiring.iterator();
						
						//leveraging the fact that numOfInstancesToRenew+numOfInstanceToStop=expiring.size()
						while(iter.hasNext()){
							if(numOfInstancesToRenew>0){
								instancesToRenew.add(iter.next());
								iter.remove();
								numOfInstancesToRenew--;
							}else{
								instancesToStop.add(iter.next());
								iter.remove();
							}
						}
					
						//performing the following control to avoid to stop all the running instance if for example there is no workload:
						//if there are not instances to renew (or all the expiring instances have to be stopped)
						// and
						//the number of running instances is equal to the number of expiring instances (all the running instances are going to be stopped)
						//leave the newest running instance running
						if(instancesToRenew.size()==0 & 
								ModelManager.getRunningInstances(tier.getId()).size()==expiring.size()){
							String instanceToLeave=ModelManager.getNewestRunningInstance(tier.getId());
							
							iter=instancesToStop.iterator();
							
							while(iter.hasNext()){
								String toStop=iter.next();
								if(toStop.equals(instanceToLeave)){
									iter.remove();
								}
								
							}
						}
						
						//renewing
						for(String instance: instancesToRenew){
							ModelManager.renewRunningInstance(instance);
						}
						
						//stopping
						ModelManager.stopInstances(instancesToStop);
						
						//taking care of the instance used to scale
						for(String instance: instancesToStop){
							if(instance.equals(ModelManager.getInstanceToScale(tier.getId()))){
								ModelManager.initializeUsedForScale(tier.getId());
							}
						}
					} 
					else{
						//simply renewing all the expiring instances
						journal.info("Being the number of expering instances equal to the number of instances to add as suggeted by the optimization"
								+ "all the expiring instances will simply be renewed");
	
						for(String toRenew: expiring){
							ModelManager.renewRunningInstance(toRenew);
						}
					}
					
				}
				
	
	
			} catch (TierNotFoudException e) {
				journal.error("Tier not found.", e);
			}
		} else {
			journal.info("The test still cannot be executed. Renewing all the running instances.");
			for (ApplicationTier tier: toAdapt.getApplicationTier()) {
				List<String> expiring = ModelManager.getExpiringInstances(tier.getId(), 1);
			
				for (String toRenew : expiring) {
					ModelManager.renewRunningInstance(toRenew);
				}
			}
		}
		
	}
	
	private boolean shouldPerformTest(Container c) {
		for (ApplicationTier tier : c.getApplicationTier()) {
			if (tier.getInstances() != null && tier.getInstances().size() > 0)
				return true;
		}
		return false;
	}


}
