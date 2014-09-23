package PCMManaging;

import java.util.HashMap;
import java.util.Map;

import RHS4CloudExceptions.PalladioHelpingException;

public class PCMManager {
	

	
	private  ResEnvManager resEnv=null;
	private  SystemManager system=null;
	private  AllocationManager allocation=null;
	
	
	public PCMManager(String pathToPCMResourceEnvironment, String pathToPCMAllocation, String pathToSPCMystem){
		
		
		resEnv= new ResEnvManager(pathToPCMResourceEnvironment);
		allocation = new AllocationManager(pathToPCMAllocation);	
		system= new SystemManager(pathToSPCMystem);

	}
	
	
	public   Map<String,String> getAllocationByNames(){
	
		
		Map<String, String> containerIdName= resEnv.getContainerCredentials();
		Map<String, String> assemblyMapping= system.getAssemblyMapping();
		Map<String, String> componentAllocation= allocation.getComponentAllocation();
		
		Map<String, String> toReturn=new HashMap<String, String>();
		
		for(String compId: assemblyMapping.keySet()){
			toReturn.put(assemblyMapping.get(compId), containerIdName.get(componentAllocation.get(compId)));
		}
		
		return toReturn;
	}
	
	public  ResEnvManager getResEnvManager() throws PalladioHelpingException{
		if(resEnv!=null)
			return resEnv;
		else
			throw new PalladioHelpingException("ResourceEnvironmenteManager not initialized.");
	}
	
	public  AllocationManager getAllocationManager() throws PalladioHelpingException{
		if(allocation!=null)
			return allocation;
		else
			throw new PalladioHelpingException("AllocationManager not initialized.");
	}
	
	public  SystemManager getSystemManager() throws PalladioHelpingException{
		if(system!=null)
			return system;
		else
			throw new PalladioHelpingException("SystemManager not initialized.");
	}

}
