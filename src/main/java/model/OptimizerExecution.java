package model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.hp.hpl.jena.vocabulary.DB;

import exceptions.StaticInputBuildingException;
import it.polimi.modaclouds.resourcemodel.cloud.CloudResource;
import it.polimi.modaclouds.resourcemodel.cloud.Cost;
import it.polimi.modaclouds.resourcemodel.cloud.VirtualHWResource;
import it.polimi.modaclouds.resourcemodel.cloud.VirtualHWResourceType;

public class OptimizerExecution {
	
	private Resource resource;

	private double capacity;
	private double onDemandCost=0;
	private double reservedCost=0;
	private int W;

	
	private List <ApplicationTier> containers;
	
	public OptimizerExecution(){
		this.containers=new ArrayList<ApplicationTier>();
		
	}


	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}


	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}


	/**
	 * @return the capacity
	 */
	public double getCapacity() {
		return capacity;
	}


	/**
	 * @param capacity the capacity to set
	 * @throws StaticInputBuildingException 
	 */
	public void setCapacity(double capacity) throws StaticInputBuildingException {
		
		this.capacity=capacity;
		
	}


	/**
	 * @return the ondemandCost
	 */
	public double getOnDemandCost() {
		return onDemandCost;
	}
	
	public void setOnDemandCost(double toSet) {
		this.onDemandCost=toSet;
	}

	/**
	 * @return the reservedCost
	 */
	public double getReservedCost() {
		return reservedCost;
	}
	
	public void setReservedCost(double toSet) {
		this.reservedCost=toSet;
	}

	/**
	 * @return the w
	 */
	public int getW() {
		return W;
	}


	/**
	 * @param w the w to set
	 */
	public void setW(int w) {
		W = w;
	}


	/**
	 * @return the containers
	 */
	public List<ApplicationTier> getContainers() {
		return containers;
	}


	/**
	 * @param containers the containers to set
	 */
	public void setContainers(List<ApplicationTier> containers) {
		this.containers = containers;
	} 
	
	public void addContainer(ApplicationTier container){
		this.containers.add(container);
	}
	
	public ApplicationTier getContainerById(String containerName){
		for(ApplicationTier c: this.containers){
			if(c.getContainerId().equals(containerName));
				return c;
		}
		 return null;
	}
	
	public ApplicationTier getContainerByName( String containerName){
		for(ApplicationTier c: this.containers){
			if(c.getContainerName().equals(containerName));
				return c;
		}
		 return null;	}
	
	public boolean hasContainer(String containerId){
		for(ApplicationTier c: this.containers){
			if(c.getContainerId().equals(containerId))
				return true;
		}
		
		return false;
	}
	
	
}
