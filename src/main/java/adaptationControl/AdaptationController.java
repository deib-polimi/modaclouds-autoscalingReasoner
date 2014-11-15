package adaptationControl;


import RHS4CloudExceptions.ConfigurationFileException;
import RHS4CloudExceptions.ProjectFileSystemException;
import staticInputProcessing.StaticInputWriter;
import util.ConfigManager;
import dynamicInputProcessing.DynamicInputWriter;
import dynamicInputProcessing.SDADataManager;
import it.polimi.modaclouds.space4cloud.milp.ssh.*;
import model.ModelManager;

public class AdaptationController {

	
	
	public static void main(String[] args) {

		
		ModelManager mm=new ModelManager();
		
		ConfigManager cm=null;
		
		try {
			cm = ConfigManager.getInstance();
			cm.initializeConfig();
		
		} catch (ConfigurationFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		if(cm!=null){
		mm.initializeModel(cm.getConfig("pathToS4CResourceModelExtension"), 
				cm.getConfig("pathToPCMAllocation"), 
				cm.getConfig("pathToSPCMystem"), 
				cm.getConfig("pathToPCMResourceEnvironment"));
		
		try {
			cm.inizializeFileSystem(mm.getExecutions());
		} catch (ProjectFileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StaticInputWriter siw= new StaticInputWriter();
		siw.writeStaticInput(cm.getConfig("pathToLineResult"), 
				cm.getConfig("speedNorm"), 
				mm.getExecutions());
		
		DynamicInputWriter diw= new DynamicInputWriter();
		
		diw.writeDynamicInput(mm.getExecutions());
		
		
		
		}
		else{
			System.out.println("ERROR: ConfigManager not initialized");
		}
		
		//SSH
		
		
		
		
	}

}
