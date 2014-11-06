package adaptationControl;


import staticInputProcessing.StaticInputWriter;

public class AdaptationHandler {

	private static final String projectPath="/Users/MIK/Documents/workspace kepler/DesignToRuntimeConnector";

	
	private static String pathToPCMResourceEnvironment=projectPath+"/INPUT/palladio_model/resource_environment";
	private static String pathToPCMAllocation=projectPath+"/INPUT/palladio_model/allocation";
	private static String pathToSPCMystem=projectPath+"/INPUT/palladio_model/system";
	
	private static final String pathToS4CResourceModelExtension = projectPath+"/INPUT/SPACE4Clouds_output/EXAMPLE/resource_model_extension.xml";
	private static final String pathToLineResult=projectPath+"/INPUT/LINE/EXAMPLE/lineResult.xml";
	private static final double speedNorm = 1200.0;
	
	public static void main(String[] args) {

		


		StaticInputWriter siw= new StaticInputWriter(pathToS4CResourceModelExtension, pathToLineResult, speedNorm, pathToPCMResourceEnvironment, pathToPCMAllocation, pathToSPCMystem);
		siw.writeStaticInput();
		
	}

}
