package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;




import it.polimi.modaclouds.qos_models.schema.MonitoringRule;
import it.polimi.modaclouds.qos_models.schema.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.SimpleEchoSocket;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringPlatformAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.FakeObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.Observer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.staticInputProcessing.OptimizerInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ExecutionOutputParser;
import it.polimi.modaclouds.space4cloud.milp.ssh.SshConnector;



public class AdaptationController {

	
	public static void main(String[] args) {
			Thread thread;
			ModelManager mm=ModelManager.getInstance();
			//path al modello di design dato in input come argomento/prelevato dall'object store
			mm.setOptimizationHorizon(1);

			mm.loadModel("/home/mik/workspace/recedingHorizonScaling4Cloud/resource/outputModelExample.xml");
			
			//orizzonta predittivo da considerare dato in inpout come argomento
			
			//IP della MP dato in input come argomento
			MonitoringPlatformAdapter mp=new MonitoringPlatformAdapter("54.154.45.180");
			
			Observer tempObs;
			
			
			ConfigManager cm=new ConfigManager();
			
			try {
				
				cm.loadConfiguration();
				cm.inizializeFileSystem(mm.getModel());
			} catch (ConfigurationFileException | ProjectFileSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

			
			MonitoringRules demandRules=mp.buildDemandRule(mm.getModel());
			
			
			//costruisce ed installa le regole necessari per monitorare la demand a livello di tier applicativo
			//mp.installRules(demandRules);
	
			
			//installa gli observer necessari per ricevere la demand stimaTA e lancia lobserver relativo
			//mp.attachObserver("EstimatedDemand", "131.175.135.243", "8179");
			tempObs=new Observer(8179);
			thread = new Thread(tempObs);
			thread.start();

			
			
			//costruisce ed installa le regole per il monitoraggio del workload a livello di tier applicativo
			for(int i=1; i<=mm.getOptimizationHorizon();i++){
				//MonitoringRules workloadRules=mp.buildWorkloadForecastRule(mm.getModel(), i);
					//mp.installRules(workloadRules);
		
				
				//installa gli observer necessari per ricevere il workload monitoraton e lancia l'observer relativo
				//mp.attachObserver("ForecastedWorkload"+i, "131.175.135.243", Integer.toString(8180+i-1));
				tempObs=new Observer(8180+i);
				thread = new Thread(tempObs);
				thread.start();

			}

			//costruisce i file di tipo statico per molinari
			OptimizerInputWriter siw= new OptimizerInputWriter();
			siw.writeStaticInput(mm.getModel());
			
			
			//test observer
			//va tutto parametrizzato compresi i nomi delle metriche scelti, l'ip della macchia su cui si trova il componente e òle porte da utilizzare
			//mp.attachObserver("FrontendCPUUtilization", "131.175.135.243", "8177");
			tempObs=new Observer(8177);
			thread = new Thread(tempObs);
			thread.start();
			
			System.out.println("sto per lanciare fake");
			FakeObserver fake=new FakeObserver();
			fake.startCollectData(mm.getModel(), mm.getOptimizationHorizon());

	
		}
	
	
		public static void applyAdaptation(String containerToAdapt){
			//scrive file di tipo dinamico
			
			//richiama molinari per ogni container per effettuare l ottimizzazione
			
			//SshAdapter ssh=new SshAdapter();
			
			//ssh.run(ModelManager.getInstance().getModel());
			
			
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
			
			//SimpleEchoSocket cloudMLconn=new SimpleEchoSocket();
			
		}
		
		
		public static void printObserved(String toPrint){
			System.out.println(toPrint);
		}
		
		
		
		
		
		
		
	

}
