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
		
		ConfigManager cm=new ConfigManager();
		
		try {
			cm.loadConfiguration();
		} catch (ConfigurationFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		

		if(cm!=null){
		mm.initializeModel();
		
		try {
			cm.inizializeFileSystem(mm.getExecutions());
		} catch (ProjectFileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StaticInputWriter siw= new StaticInputWriter();
		siw.writeStaticInput(mm.getExecutions());
		
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
