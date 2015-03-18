package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigDictionary;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.facade.commands.ScaleOut;

public class AdaptationController implements Runnable {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	
	private Container toAdapt;
	
	public void run(){
		
		CloudMLAdapter cloudml;
		
		try {
			
			journal.log(Level.INFO, "starting adaptation step for container: "+toAdapt.getId());
			
			Map<String,List<String>> expiring=ModelManager.getNextTimestepExpiringInstances(toAdapt.getApplicationTier());
			Map<String,List<String>> available=ModelManager.getNextTimestepAvailableInstances(toAdapt.getApplicationTier());
			
			for(ApplicationTier t:toAdapt.getApplicationTier()){
				t.setInitialNumberOfVMs(available.get(t.getId()).size());
				journal.log(Level.INFO, "number of VMs available for free at next timestep for tier "+t.getId()+"= "+t.getInitialNumberOfVMs());
				int index=ModelManager.getApplicationTierAtRuntime(t.getId()).getAlgorithmIndex();
				for(int j=1; j<=ModelManager.getOptimizationWindow();j++)
					OptimizationInputWriter.writeFile("initialVM.dat", toAdapt.getId(), "let Nond["+index+","+(1+j)+"]:=\n"+t.getInitialNumberOfVMs()+"\n;");
			}
			
			
			
			journal.log(Level.INFO, "initialVM.dat file created");
			
			
			journal.log(Level.INFO, "Calling MILP solver via ssh");
			//SshAdapter.run(toAdapt);
			////////////////////////////////////////////////////////////////////////////	
			OptimizationOutputParser outputParser= new OptimizationOutputParser();
			int[] algorithmResult=outputParser.parseExecutionOutput("executions/execution_"+toAdapt.getId()+"/IaaS_1/output.out", toAdapt.getApplicationTier().size());
			
			//if the new instances to instantiate for a tier is greater then the exipiring instances leght for the same tier
			//call the scale of command for the tier passing as number of new instances to start (outputOptimization-exipiring
			//if the new instance to instantiate returne are the same exiping do nothing and let the expiring rinnovate
			//if the new instances to instantiate are less then the expiring terminate a number of VMs equal to (expiring -outputOptimization
			
			cloudml=new CloudMLAdapter("ws://"+ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketIP)+":"+
					ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketPort)+"");
			
			for(ApplicationTier tier: toAdapt.getApplicationTier()){
				int tierResult=algorithmResult[ModelManager.getApplicationTierAtRuntime(tier.getId()).getAlgorithmIndex()-1];
				journal.log(Level.INFO, "calculated necessary new VMs for tier "+tier.getId()+"="+tierResult);
				int instanceToScale=tierResult-expiring.get(tier.getId()).size();
				journal.log(Level.INFO, "number of expiring VMs for tier "+tier.getId()+"="+expiring.get(tier.getId()).size());
				journal.log(Level.INFO, "number of new VMs to instantiate considering renewable expiring ones for tier "+tier.getId()+"="+instanceToScale);

				
				if(instanceToScale>0){
					journal.log(Level.INFO, "Scaling out tier "+tier.getId()+" starting "+instanceToScale+" new instances");
					ScaleOut command=new ScaleOut(ModelManager.getApplicationTierAtRuntime(tier.getId()).getInstanceToScale());
					cloudml.scaleOut(command, 2);
					//cloudml.scaleOut(command, instanceToScale);		
				}else if (instanceToScale<0){
					//terminate instances
				}
				
			}
			cloudml.getDeploymentModel();
			
			ModelManager.flushTemporaryMonitoringData(toAdapt);
			
			ModelManager.printModel();

		} catch (TierNotFoudException e) {
			// TODO Auto-generated catch block
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
