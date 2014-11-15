package model;

import it.polimi.modaclouds.qos_models.schema.ResourceContainer;
import it.polimi.modaclouds.qos_models.schema.ResourceModelExtension;
import it.polimi.modaclouds.qos_models.util.XMLHelper;
import it.polimi.modaclouds.resourcemodel.cloud.CloudResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import PCMManaging.PCMManager;
import RHS4CloudExceptions.PalladioHelpingException;

import com.hp.hpl.jena.sparql.algebra.Op;

public class ModelManager {
	
	private List <OptimizerExecution> executions;
	
	public ModelManager(){
		this.executions=new ArrayList<OptimizerExecution>();
	}
	
	public void initializeModel(String pathToS4CResourceModelExtension, String pathToPCMAllocation, String pathToSPCMystem, String pathToPCMResourceEnvironment){
		
		PCMManager palladioManager= new PCMManager(pathToPCMResourceEnvironment, pathToPCMAllocation, pathToSPCMystem);
		XMLHelper space4cloudSolutionParser=new XMLHelper();
		
		try {
			ResourceModelExtension solution = (ResourceModelExtension) space4cloudSolutionParser
					.deserialize(new FileInputStream(pathToS4CResourceModelExtension),
							ResourceModelExtension.class);

			for (ResourceContainer tier : solution.getResourceContainer()) {
				
				ApplicationTier tempContainer = new ApplicationTier();
				
				tempContainer.setContainer(tier);
				tempContainer.setContainerId(tier.getId());
				tempContainer.setContainerName(palladioManager.getResEnvManager().getNameFromId(tier.getId()));
				
				Resource resource = new Resource();
				resource.setName(tier.getCloudResource().getServiceName());
				resource.setProvider(tier.getProvider());
				resource.setServiceType(tier.getCloudResource().getServiceType());
				resource.setSize(tier.getCloudResource().getResourceSizeID());
				resource.setRegion(tier.getCloudResource().getLocation().getRegion());

				OptimizerExecution execution= this.getExecutionFromResource(resource);
				
				if(execution!=null){
					execution.addContainer(tempContainer);
						
				} else {
					execution= new OptimizerExecution();
					execution.setResource(resource);
					execution.addContainer(tempContainer);
					this.addExecution(execution);
				}		
			}
		} catch (FileNotFoundException | JAXBException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PalladioHelpingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List <OptimizerExecution> getExecutions(){
		return this.executions;
	}
	
	public void setExecution(List<OptimizerExecution> toSet){
		this.executions=toSet;
	}
	
	public void addExecution(OptimizerExecution toAdd){
		this.executions.add(toAdd);
	}
	
	public OptimizerExecution getExecutionFromResource(Resource resource) {
		
		
		for(OptimizerExecution ex: this.executions){
			Resource temp= ex.getResource();
			
			if(temp.getName().equals(resource.getName()) &&
					temp.getServiceType().equals(resource.getServiceType()) &&
						temp.getSize().equals(resource.getSize()) &&
							temp.getRegion().equals(resource.getRegion()))
				return ex;
				
		}
		
		return null;
		
	}
	
	public OptimizerExecution getExecutionFromContainer(ResourceContainer container) {
		
		for(OptimizerExecution ex: this.executions){
			Resource temp= ex.getResource();
			
			if(temp.getName().equals(container.getCloudResource().getServiceName()) &&
					temp.getServiceType().equals(container.getCloudResource().getServiceType()) &&
						temp.getSize().equals(container.getCloudResource().getResourceSizeID()) &&
							temp.getRegion().equals(container.getCloudResource().getLocation().getRegion()))
				return ex;
				
		} 
		
		return null;
		
	}

}
