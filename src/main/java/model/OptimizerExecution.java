package model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.hp.hpl.jena.vocabulary.DB;

import RHS4CloudExceptions.StaticInputBuildingException;
import it.polimi.modaclouds.resourcemodel.cloud.CloudResource;
import it.polimi.modaclouds.resourcemodel.cloud.Cost;
import it.polimi.modaclouds.resourcemodel.cloud.VirtualHWResource;
import it.polimi.modaclouds.resourcemodel.cloud.VirtualHWResourceType;

public class OptimizerExecution {
	
	private RHSResource resource;

	private double capacity;
	private double onDemandCost=0;
	private double reservedCost=0;
	private int W;

	
	private List <RHSContainer> containers;
	
	public OptimizerExecution(){
		this.containers=new ArrayList<RHSContainer>();
		
	}


	/**
	 * @return the resource
	 */
	public RHSResource getResource() {
		return resource;
	}


	/**
	 * @param resource the resource to set
	 */
	public void setResource(RHSResource resource) {
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
	public List<RHSContainer> getContainers() {
		return containers;
	}


	/**
	 * @param containers the containers to set
	 */
	public void setContainers(List<RHSContainer> containers) {
		this.containers = containers;
	} 
	
	public void addContainer(RHSContainer container){
		this.containers.add(container);
	}
	
	public RHSContainer getContainerById(String containerName){
		for(RHSContainer c: this.containers){
			if(c.getContainerId().equals(containerName));
				return c;
		}
		 return null;
	}
	
	public RHSContainer getContainerByName( String containerName){
		for(RHSContainer c: this.containers){
			if(c.getContainerName().equals(containerName));
				return c;
		}
		 return null;	}
	
	public boolean hasContainer(String containerId){
		for(RHSContainer c: this.containers){
			if(c.getContainerId().equals(containerId))
				return true;
		}
		
		return false;
	}
	
}
