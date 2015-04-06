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
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigDictionary;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

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
				int index=ModelManager.getApplicationTierAtRuntime(t.getId()).getAlgorithmIndex();
				for(int j=1; j<=ModelManager.getOptimizationWindow();j++)
					OptimizationInputWriter.writeFile("initialVM.dat", toAdapt.getId(), "let Nond["+index+","+(1+j)+"]:=\n"+ModelManager.getAvailableInstances(t, j).size()+"\n;");
			}
			
			
			
			
			
			SshAdapter.executeOptimization(toAdapt);
			
			////////////////////////////////////////////////////////////////////////////	
			
			OptimizationOutputParser outputParser= new OptimizationOutputParser();
			int[] algorithmResult=outputParser.parseExecutionOutput("executions/execution_"+toAdapt.getId()+"/IaaS_1/output.out", toAdapt.getApplicationTier().size());
			
			//delete current dynamic input file at the end of the adaptation step
			/*
			OptimizationInputWriter.deleteFile("initialVM.dat", toAdapt.getId());
			OptimizationInputWriter.deleteFile("mu.dat", toAdapt.getId());

			for(ApplicationTier t: toAdapt.getApplicationTier()){
				OptimizationInputWriter.deleteFile("workload_class"+ModelManager.getApplicationTierAtRuntime(t.getId()).getAlgorithmIndex()+".dat", toAdapt.getId());
			}
			*/
			
			//store the current dynamic input file and the output file at the end of the iteration
			List<String> toStore=new ArrayList<String>();
			toStore.add("initialVM.dat");
			toStore.add("mu.dat");
			toStore.add("output.out");
			for(ApplicationTier t: toAdapt.getApplicationTier()){
				toStore.add("workload_class"+ModelManager.getApplicationTierAtRuntime(t.getId()).getAlgorithmIndex()+".dat");
			}
			try {
				OptimizationInputWriter.storeFile(toStore, toAdapt.getId());
			} catch (ProjectFileSystemException e) {
				e.printStackTrace();
			}
			
			cloudml=new CloudMLAdapter("ws://"+ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketIP)+":"+
					ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketPort)+"");
			
			for(ApplicationTier tier: toAdapt.getApplicationTier()){
				int tierResult=algorithmResult[ModelManager.getApplicationTierAtRuntime(tier.getId()).getAlgorithmIndex()-1];
				List<String> expiring=ModelManager.getExpiringInstances(tier, 1);
				int instanceToScale=tierResult-expiring.size();

				System.out.println("number of new instance to start reported by the algorithm: "+tierResult);
				System.out.println("number of expiring instance for the current tier: "+expiring.size());
				System.out.println("number of new instance to start for the tier "+tier.getId()+" = "+instanceToScale);
				if(instanceToScale>0){
					System.out.println("number of instaces to start is greater than zero; starting "+instanceToScale+" new instances");
					ScaleOut command=new ScaleOut(ModelManager.getApplicationTierAtRuntime(tier.getId()).getInstanceToScale());
					cloudml.scaleOut(command, instanceToScale);		
				}else if (instanceToScale<0){
					int instanceToStop=-instanceToScale;
					
					System.out.println("numer of instance to start is less than zero; stopping "+instanceToStop+" instances");
					Iterator<String> iter=expiring.iterator();
					
					while(instanceToStop>0 & iter.hasNext()){
						String toTerminate=iter.next();
						if(!toTerminate.equals(ModelManager.getApplicationTierAtRuntime(tier.getId()).getInstanceToScale())){
							System.out.println("stopping instance: "+toTerminate);
							cloudml.terminate(toTerminate);
							ModelManager.deleteInstance(toTerminate, tier.getId());
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
					
					if(instanceToStop>0){
						System.out.println("the instance pointed for the scale was expiring and the number of expiring instances was equal "
								+ "to the instances to stop. Autoscaling Reasoner need to stop also the pointed instance to scale and to select a new one.");
						String pointedInstanceToScale=ModelManager.getApplicationTierAtRuntime(tier.getId()).getInstanceToScale();
						cloudml.terminate(pointedInstanceToScale);
						ModelManager.deleteInstance(pointedInstanceToScale, tier.getId());				
						String lastInstanceCreated=ModelManager.getLastInstanceCreated(tier.getId());
						ModelManager.getApplicationTierAtRuntime(tier.getId()).setInstanceToScale(lastInstanceCreated);
						cloudml.createImage(lastInstanceCreated);

					}
					
					for(int i=1; i<=expiring.size(); i++){
						System.out.println("restarting timers of the reamining expiring instances: "+expiring.size());
						ModelManager.addInstance(expiring.get(i), tier.getId());
						
					}
				} else{
					System.out.println("all the expiring instance timers have to be restarted");
					Iterator<String> iter=expiring.iterator();
					
					while(iter.hasNext()){
						String toRestart=iter.next();
						ModelManager.addInstance(toRestart, tier.getId());
					}
				}
				
			}
			

			ModelManager.printModel();

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
