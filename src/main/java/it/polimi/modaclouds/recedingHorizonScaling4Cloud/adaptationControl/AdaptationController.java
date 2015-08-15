package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
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

import org.cloudml.facade.commands.ScaleOut;

public class AdaptationController implements Runnable {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	
	private Container toAdapt;
	
	public void run(){
		
		CloudMLAdapter cloudml;
		
		try {
			
			System.out.println("starting adaptation step for container: "+toAdapt.getId());
			
			for(ApplicationTier t:toAdapt.getApplicationTier()){
				int index=ModelManager.getClassIndex(t.getId());
				for(int j=1; j<=ModelManager.getOptimizationWindow();j++)
					OptimizationInputWriter.writeFile("initialVM.dat", toAdapt.getId(), "let Nond["+index+","+(1+j)+"]:=\n"+ModelManager.getAvailableInstances(t.getId(), j).size()+"\n;");
			}
			
			
			
			
			
			SshAdapter.executeOptimization(toAdapt);
			
			OptimizationOutputParser outputParser= new OptimizationOutputParser();
			int[] algorithmResult=outputParser.parseExecutionOutput("executions/execution_"+toAdapt.getId()+"/IaaS_1/output.out", toAdapt.getApplicationTier().size());
			
			//store the current dynamic input file and the output file at the end of the iteration
			List<String> toStore=new ArrayList<String>();
			toStore.add("initialVM.dat");
			toStore.add("mu.dat");
			toStore.add("output.out");
			for(ApplicationTier t: toAdapt.getApplicationTier()){
				toStore.add("workload_class"+ModelManager.getClassIndex(t.getId())+".dat");
			}
			try {
				OptimizationInputWriter.storeFile(toStore, toAdapt.getId());
			} catch (ProjectFileSystemException e) {
				e.printStackTrace();
			}
			
			
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			
			cloudml=new CloudMLAdapter();
			
			for(ApplicationTier tier: toAdapt.getApplicationTier()){
				int tierResult=algorithmResult[ModelManager.getClassIndex(tier.getId())-1];
				List<String> expiring=ModelManager.getExpiringInstances(tier.getId(), 1);
				int instanceToScale=tierResult-expiring.size();

				
				System.out.println("number of new instance to start reported by the algorithm: "+tierResult);
				System.out.println("number of expiring instance for the current tier: "+expiring.size());
				System.out.println("number of new instance to start for the tier "+tier.getId()+" = "+instanceToScale);
				
				
				if(instanceToScale>0){
					List<String> stoppedInstances=ModelManager.getStoppedInstances(tier.getId());
					System.out.println("number of stopped instances for the tier "+tier.getId()+" = "+stoppedInstances.size());
					int toRestart;
					
					if(instanceToScale>=stoppedInstances.size()){
						System.out.println("Number of new running instances needed is greater or equal than the number of stopped instance;"
								+ " all the stopped instances will be restarted and the remaining will be scaled out");
						toRestart=stoppedInstances.size();
					}else{
						System.out.println("Number of new running instance is less than the stopped instances; "
								+ "restarting some stopping instances is enough to get the required capacity");
						toRestart=instanceToScale;
					}
					
					System.out.println("In particular "+toRestart+" instances will be restarted");
					/*
					for(int i=1; i<=toRestart; i++){
						cloudml.startInstance(stoppedInstances.get(i-1));
						ModelManager.addInstance(stoppedInstances.get(i-1), tier.getId());
						System.out.println("Instance "+stoppedInstances.get(i-1)+" successfully restarted");
					}
					*/
					List<String> toSend=new ArrayList<String>();
					for(int i=1; i<=toRestart; i++){
						toSend.add(stoppedInstances.get(i-1));
					}
					cloudml.startInstance(toSend);
					
					for(String toAdd: toSend){
						ModelManager.addInstance(toAdd, tier.getId(), "RUNNING");
					}
					
					for(String toAdd: toSend){
						System.out.println("Instance "+toAdd+" successfully restarted");

					}
					
					
					int toScaleOut=instanceToScale-toRestart;
					
					System.out.println("After instances restarting there are still "+toScaleOut+" in order to meet the required computing capacity");
					System.out.println("Scaling out "+toScaleOut+" new instances");
					if(toScaleOut>0){
						ScaleOut command=new ScaleOut(ModelManager.getInstanceToScale(tier.getId()));
						cloudml.scaleOut(command, toScaleOut);
						
						System.out.println("Scaling actions successfully completed");
					}
					
				}else if (instanceToScale<0){
					int instanceToStop=-instanceToScale;
					
					System.out.println("numer of instance to start is less than zero; stopping "+instanceToStop+" instances");
					Iterator<String> iter=expiring.iterator();
					/*
					while(instanceToStop>0 & iter.hasNext()){
						String toStop=iter.next();
						if(!toStop.equals(ModelManager.getApplicationTierAtRuntime(tier.getId()).getInstanceToScale())){
							System.out.println("stopping instance: "+toStop);
							cloudml.stopInstance(toStop);
							ModelManager.stopInstance(toStop, tier.getId());
							iter.remove();
							instanceToStop--;
							System.out.println("number of instances still to stop: "+instanceToStop);
							System.out.println("number of expiring instances still available to be stopped: "+expiring.size());
						}else{
							System.out.println("the pointed instace to scale is expiring. Right now we avoid to stop it until strictly necessary");
							iter.remove();
							System.out.println("number of instances still to stop: "+instanceToStop);
							System.out.println("number of remaning expiring instance: "+expiring.size());
						}
					}
					*/
					List<String> toSend=new ArrayList<String>();
					while(instanceToStop>0 & iter.hasNext()){
						String toStop=iter.next();
						if(!toStop.equals(ModelManager.getInstanceToScale(tier.getId()))){
							System.out.println("stopping instance: "+toStop);
							//
							toSend.add(toStop);
							iter.remove();
							instanceToStop--;
							System.out.println("number of instances still to stop: "+instanceToStop);
							System.out.println("number of expiring instances still available to be stopped: "+expiring.size());
						}else{
							System.out.println("the pointed instace to scale is expiring. Right now we avoid to stop it until strictly necessary");
							iter.remove();
							System.out.println("number of instances still to stop: "+instanceToStop);
							System.out.println("number of remaning expiring instance: "+expiring.size());
						}
					}
					
					cloudml.stopInstance(toSend);
					
					for(String toStop: toSend){
						ModelManager.stopInstance(toStop);

					}

					
					if(instanceToStop>0){
						System.out.println("the instance pointed for the scale was expiring and the number of expiring instances was equal "
								+ "to the instances to stop. Autoscaling Reasoner need to stop also the pointed instance to scale and to select a new one.");
						String pointedInstanceToScale=ModelManager.getInstanceToScale(tier.getId());
						List<String> stopCmd=new ArrayList<String>();
						stopCmd.add(pointedInstanceToScale);
						cloudml.stopInstance(stopCmd);
						ModelManager.stopInstance(pointedInstanceToScale);				
						String lastInstanceCreated=ModelManager.getNewestRunningInstance(tier.getId());
						ModelManager.setInstanceToScale(lastInstanceCreated);
						cloudml.createImage(lastInstanceCreated);

					}
					
					for(int i=1; i<=expiring.size(); i++){
						System.out.println("restarting timers of the reamining expiring instances: "+expiring.size());
						ModelManager.addInstance(expiring.get(i), tier.getId(), "RUNNING");
						
					}
				} else{
					System.out.println("all the expiring instance timers have to be restarted");
					System.out.println("number of expiring instance to restart:"+expiring.size());
					Iterator<String> iter=expiring.iterator();
					
					while(iter.hasNext()){
						String toRestart=iter.next();
						ModelManager.addInstance(toRestart, tier.getId(), "RUNNING");
					}
				}
				
			}
			


		} catch (TierNotFoudException e) {
			e.printStackTrace();
		}
	
	}


	public Container getToAdapt() {
		return toAdapt;
	}

	public void setToAdapt(Container toAdapt) {
		this.toAdapt = toAdapt;
	}
}
