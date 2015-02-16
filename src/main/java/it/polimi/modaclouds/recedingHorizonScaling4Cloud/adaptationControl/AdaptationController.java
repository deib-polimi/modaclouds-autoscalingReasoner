package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;




import it.polimi.modaclouds.qos_models.schema.MonitoringRule;
import it.polimi.modaclouds.qos_models.schema.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.SimpleEchoSocket;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringPlatformAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.Observer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.staticInputProcessing.StaticInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ExecutionOutputParser;
import it.polimi.modaclouds.space4cloud.milp.ssh.SshConnector;



public class AdaptationController {

	
	public static void main(String[] args) {

			ModelManager mm=ModelManager.getInstance();
		
			MonitoringPlatformAdapter mp=new MonitoringPlatformAdapter("127.0.0.1");
			
			Observer tempObs;
		

			mm.loadModel("/home/mik/workspace/recedingHorizonScaling4Cloud/resource/outputModelExample.xml");
			
			MonitoringRules demandRules=mp.buildDemandRule(mm.getModel());
			
			
			//costruisce ed installa le regole necessari per monitorare la demand a livello di tier applicativo
			for(MonitoringRule rule: demandRules.getMonitoringRules()){
				mp.installRule(rule);
			}
			
			//installa gli observer necessari per ricevere la demand stimaTA e lancia lobserver relativo
			mp.attachObserver("EstimatedDemand", "127.0.0.1", "8179");
			tempObs=new Observer(8179);

			
			
			//costruisce ed installa le regole per il monitoraggio del workload a livello di tier applicativo
			for(int i=0; i<5;i++){
				MonitoringRules workloadRules=mp.buildWorkloadForecastRule(mm.getModel(), i);
				for(MonitoringRule rule: workloadRules.getMonitoringRules()){
					mp.installRule(rule);
				}
				
				//installa gli observer necessari per ricevere il workload monitoraton e lancia l'observer relativo
				mp.attachObserver("ForecastedWorkload"+i, "127.0.0.1", Integer.toString(8180+i));
				tempObs=new Observer(8180+i);

			}

			//costruisce i file di tipo statico per molinari
			StaticInputWriter siw= new StaticInputWriter();
			siw.writeStaticInput(mm.getModel());
			
			
			
	
		}
	
	
		public static void applyAdaptation(){
			//scrive file di tipo dinamico
			
			//richiama molinari per ogni container per effettuare l ottimizzazione
			
			SshAdapter ssh=new SshAdapter();
			
			ssh.run(ModelManager.getInstance().getModel());
			
			
			//ExecutionOutputParser outputParser= new ExecutionOutputParser();
			
			
			/*
			for(OptimizationExecution ex: mm.getExecutions()){
				outputParser.parseExecutionOutput("executions/execution_"+ex+"/IaaS_1/output.out", ex);
			}
			*/
			
			
			//recupera per ogni ottimizzazione i risultati ed aggiorna il modello richiamando model manager.upodatemodel
			
			//DynamicInputWriter diw= new DynamicInputWriter();
			
			//diw.writeDynamicInput(mm.getExecutions());
			
			//SUYPPLING THE STILL NOT AVAILABLE INPUT FILES
			
			//InputSupplier newSupplier= new InputSupplier();
			
			//for(OptimizationExecution ex: mm.getExecutions()){
			//	newSupplier.supplyInput(ex.toString());
			
			//invoca il connettore con cloudml passandogli il modello e questo tramite il suo modello interno 
			//effettua tutte le verifiche (temporizzazioni) necessarie per decidere quante macchine effettivamente vanno inizializzate da zero ecc
			
			SimpleEchoSocket cloudMLconn=new SimpleEchoSocket();
			
		}
		
		
		
		
		
		
		
	

}
