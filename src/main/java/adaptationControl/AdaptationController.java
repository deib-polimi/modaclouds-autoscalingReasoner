package adaptationControl;


import ssh.SshConnector;
import staticInputProcessing.StaticInputWriter;
import util.ConfigManager;
import util.ConfigDictionary;
import dynamicInputProcessing.DynamicInputWriter;
import exceptions.ConfigurationFileException;
import exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.space4cloud.milp.ssh.*;
import model.ModelManager;

public class AdaptationController {

	
	
	public static void main(String[] args) {

		
		ModelManager mm=new ModelManager();
		
		ConfigManager cm=null;
		
		try {
			cm = ConfigManager.getInstance();
			cm.loadConfiguration();
		
		} catch (ConfigurationFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		if(cm!=null){
		mm.initializeModel(cm.getConfig(ConfigDictionary.pathToS4CResourceModelExtension), 
				cm.getConfig(ConfigDictionary.pathToPCMAllocation), 
				cm.getConfig(ConfigDictionary.pathToSPCMystem), 
				cm.getConfig(ConfigDictionary.pathToPCMResourceEnvironment));
		
		try {
			cm.inizializeFileSystem(mm.getExecutions());
		} catch (ProjectFileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StaticInputWriter siw= new StaticInputWriter();
		siw.writeStaticInput(cm.getConfig(ConfigDictionary.pathToLineResult), 
				cm.getConfig(ConfigDictionary.speedNorm), 
				mm.getExecutions());
		
		//DynamicInputWriter diw= new DynamicInputWriter();
		
		//diw.writeDynamicInput(mm.getExecutions());
		
		
		
		}
		else{
			System.out.println("ERROR: ConfigManager not initialized");
		}
		
		//SSH
		
		SshConnector.run(mm.getExecutions());
		
		
		
		
	}

}
