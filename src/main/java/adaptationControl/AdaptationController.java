package adaptationControl;


import staticInputProcessing.StaticInputWriter;
import dynamicInputProcessing.DynamicInputWriter;
import dynamicInputProcessing.SDADataManager;
import it.polimi.modaclouds.space4cloud.milp.ssh.*;
import model.ModelManager;

public class AdaptationController {

	
	
	private static final String projectPath="/home/mik/workspace/recedingHorizonScaling4Cloud";

	
	private static String pathToPCMResourceEnvironment=projectPath+"/resource/palladio_model/resource_environment";
	private static String pathToPCMAllocation=projectPath+"/resource/palladio_model/allocation";
	private static String pathToSPCMystem=projectPath+"/resource/palladio_model/system";
	private static final String pathToS4CResourceModelExtension = projectPath+"/resource/SPACE4Clouds_output/EXAMPLE/resource_model_extension.xml";

	private static final String pathToLineResult=projectPath+"/resource/LINE/EXAMPLE/lineResult.xml";
	private static final double speedNorm = 1200.0;
	
	public static void main(String[] args) {

		ModelManager mm=new ModelManager();
		mm.initializeModel(pathToS4CResourceModelExtension, pathToPCMAllocation, pathToSPCMystem, pathToPCMResourceEnvironment);
		
		StaticInputWriter siw= new StaticInputWriter();
		siw.writeStaticInput(pathToLineResult, speedNorm, mm.getExecutions());
		
		DynamicInputWriter diw= new DynamicInputWriter();
		diw.writeDynamicInput(mm.getExecutions());
		
		//SSH
		
		
		
		
	}

}
