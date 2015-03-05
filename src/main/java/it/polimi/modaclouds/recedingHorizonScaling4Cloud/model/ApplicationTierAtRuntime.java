package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplicationTierAtRuntime {
	
	private String tierId;
	private Map<String,Date> instancesStartTimes;
	private String instanceToScale;
	
	public ApplicationTierAtRuntime(){
		instancesStartTimes=new HashMap<String, Date>();
	}
	
	public String getTierId() {
		return tierId;
	}
	public void setTierId(String tierId) {
		this.tierId = tierId;
	}
	public Map<String, Date> getInstancesStartTimes() {
		return instancesStartTimes;
	}
	public void setInstancesStartTimes(Map<String, Date> instancesStartTimes) {
		this.instancesStartTimes = instancesStartTimes;
	}
	public String getInstanceToScale() {
		return instanceToScale;
	}
	public void setInstanceToScale(String instanceToScale) {
		this.instanceToScale = instanceToScale;
	}
	
	
	public void addNewInstance(String instanceId){
		Calendar cal = Calendar.getInstance();
		
		instancesStartTimes.put(instanceId, cal.getTime());
	}

}
