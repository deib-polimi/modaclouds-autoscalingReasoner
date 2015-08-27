package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

public class AdaptationController extends TimerTask {
	private static final Logger journal = Logger
			.getLogger(AdaptationController.class.getName());
	
	private Container toAdapt;

	
	public AdaptationController(Container toSet){
		toAdapt=toSet;
	}
	
	public void run(){
		
		journal.log(Level.INFO,"Here is a controller starting an adaptation step for container: "+toAdapt.getId());
		
		
		//writing dynamic input files for the current timestep and container
		journal.log(Level.INFO,"Writing the dynamic input files for the current timestep");
		OptimizationInputWriter siw= new OptimizationInputWriter();
		siw.writeDynamicInput(toAdapt);
		
		//launching optimization
		journal.log(Level.INFO,"Launching the optimization in the remote server via ssh");
		SshAdapter.executeOptimization(toAdapt);
		
		//parsing the output file
		journal.log(Level.INFO,"Parsing the optimization output and getting the result");
		OptimizationOutputParser outputParser= new OptimizationOutputParser();
		int[] algorithmResult=outputParser.parseExecutionOutput("executions/execution_"+toAdapt.getId()+"/IaaS_1/output.out", toAdapt.getApplicationTier().size());
		
		//store the current dynamic input file and the output file at the end of the iteration
		journal.log(Level.INFO,"Storing the dynamic input files for the current timestep");
		List<String> toStore=new ArrayList<String>();
		toStore.add("initialVM.dat");
		toStore.add("mu.dat");
		toStore.add("Delay.dat");
		toStore.add("output.out");
		toStore.add("Rcross.dat");
		for(ApplicationTier t: toAdapt.getApplicationTier()){
			toStore.add("workload_class"+t.getClassIndex()+".dat");
		}
		try {
			OptimizationInputWriter.storeFile(toStore, toAdapt.getId());
		} catch (ProjectFileSystemException e) {
			e.printStackTrace();
		}
		
		//analyse the optimization output and enact adaptation actions
		journal.log(Level.INFO,"Start checking the result for each application tier");
		try {
					
			for(ApplicationTier tier: toAdapt.getApplicationTier()){
				
				journal.log(Level.INFO,"Checking the optimization result for application tier: "+tier.getId());

				
				int tierResult=algorithmResult[tier.getClassIndex()-1];
				List<String> expiring=ModelManager.getExpiringInstances(tier.getId(), 1);
				int numOfInstancesToAdd=tierResult-expiring.size();
				
				journal.log(Level.INFO,"Number of new instances to add at the next timestep as suggested by the optimization="+tierResult);
				journal.log(Level.INFO,"Number of expiring instance for the current application tier="+expiring.size());
				
				if(numOfInstancesToAdd>0){

					//SUMMARY
					//all the expiring instances need to be renewed (left running and restart the internal time)
					//and a number of instances equal to numOfInstancesToAdd need to be added to the deployment.
					//Among these, as far as there are stopped instances available, the controller restart the stopped instance.
					//When no stopped instances are available anymore, but there are still some instances to add to the deployment
					//(instance to scale is still numOfInstancesToAdd than 0), these last will be added scaling out.
					journal.log(Level.INFO,"Being the number of expiring instances less then the number of instance to add suggested by the optimization"
							+ "all the expiring instances are going to be renwed and the remaining needed are going to be started/scaled out");

					
					//renewing the expiring instances
					journal.log(Level.INFO,"Rewneing all the expiring instances");

					for(String toRenew: expiring){
						ModelManager.renewRunningInstance(toRenew);
					}
					
					//determine the number of stopped instances to restart 
					//and the number of instances to be added scaling out
					journal.log(Level.INFO,"Determining the number of stopped instances to restart and the number of instances that instead need to be scaled out");
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
					
					journal.log(Level.INFO,"Number of stopped instances to restart="+numOfInstancesToRestart);
					journal.log(Level.INFO,"Number of stopped instances to scale out="+numOfInstancesToScaleOut);

					
					//restarting the stopped necessary instances (if any)
					//NOTE: numOfInstancesToRestart is assured to be less then stoppedInstences.size() from the calculus above
					journal.log(Level.INFO,"Restarting instances");
					if(numOfInstancesToRestart>0){
						List<String> instanceToRestart=new ArrayList<String>();
						for(int i=0; i<numOfInstancesToRestart;i++){
							instanceToRestart.add(stoppedInstances.get(i));
						}
						
						ModelManager.startInstance(instanceToRestart);
					}
					
					//scaling the remaining instances to add (if any)	
					journal.log(Level.INFO,"Scaling out instanes");
					if(numOfInstancesToScaleOut>0){
						CloudMLAdapter cloudml=new CloudMLAdapter();
						cloudml.scaleOut(ModelManager.getInstanceToScale(tier.getId()), numOfInstancesToScaleOut);
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
					journal.log(Level.INFO,"Being the number of expering instances equal to the number of instances to add as suggeted by the optimization"
							+ "all the expiring instances will simply be renewed");

					for(String toRenew: expiring){
						ModelManager.renewRunningInstance(toRenew);
					}
				}
				
			}
			


		} catch (TierNotFoudException e) {
			e.printStackTrace();
		}
		
	}


}
