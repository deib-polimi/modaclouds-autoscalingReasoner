package model;

import it.polimi.modaclouds.qos_models.schema.ResourceContainer;
import it.polimi.modaclouds.resourcemodel.cloud.CloudResource;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.sparql.algebra.Op;

public class ExecutionContainer {
	
	private List <OptimizerExecution> executions;
	
	public ExecutionContainer(){
		this.executions=new ArrayList<OptimizerExecution>();
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
	
	public OptimizerExecution getExecutionFromResource(RHSResource resource) {
		
		
		for(OptimizerExecution ex: this.executions){
			RHSResource temp= ex.getResource();
			
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
			RHSResource temp= ex.getResource();
			
			if(temp.getName().equals(container.getCloudResource().getServiceName()) &&
					temp.getServiceType().equals(container.getCloudResource().getServiceType()) &&
						temp.getSize().equals(container.getCloudResource().getResourceSizeID()) &&
							temp.getRegion().equals(container.getCloudResource().getLocation().getRegion()))
				return ex;
				
		} 
		
		return null;
		
	}

}
