package model;

import it.polimi.modaclouds.resourcemodel.cloud.CloudResource;
import it.polimi.modaclouds.qos_models.schema.ResourceContainer;


public class RHSContainer {

	
	private ResourceContainer container;

	private float Rcross;
	
	private String containerName;
	private String containerId;
	
	public RHSContainer() {

	}
	
	/**
	 * @return the rcross
	 */
	public float getRcross() {
		return Rcross;
	}

	/**
	 * @param rcross
	 *            the rcross to set
	 */
	public void setRcross(float rcross) {
		Rcross = rcross;
	}

	/**
	 * @return the container
	 */
	public ResourceContainer getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(ResourceContainer container) {
		this.container = container;
	}

	/**
	 * @return the containerName
	 */
	public String getContainerName() {
		return containerName;
	}

	/**
	 * @param containerName the containerName to set
	 */
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	/**
	 * @return the containerId
	 */
	public String getContainerId() {
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

}
