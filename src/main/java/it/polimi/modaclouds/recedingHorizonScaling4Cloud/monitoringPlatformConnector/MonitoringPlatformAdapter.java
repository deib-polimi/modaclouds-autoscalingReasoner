package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;


import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import it.polimi.modaclouds.qos_models.schema.Action;
import it.polimi.modaclouds.qos_models.schema.CollectedMetric;
import it.polimi.modaclouds.qos_models.schema.MonitoredTarget;
import it.polimi.modaclouds.qos_models.schema.MonitoringRule;
import it.polimi.modaclouds.qos_models.schema.MonitoringRules;
import it.polimi.modaclouds.qos_models.schema.ObjectFactory;
import it.polimi.modaclouds.qos_models.schema.Parameter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

public class MonitoringPlatformAdapter {
	
	
	private String monitoringPlatformIP;
	private ObjectFactory factory=new ObjectFactory();
	
	public MonitoringPlatformAdapter(String monitoringPlatformIP){
		this.monitoringPlatformIP=monitoringPlatformIP;
	}
	
	public void installRules(MonitoringRules toInstall){
		
		try {
			 
			
			JAXBContext context = JAXBContext.newInstance("it.polimi.modaclouds.qos_models.schema");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
			StringWriter sw = new StringWriter();
			
			marshaller.marshal(toInstall,sw);
			
			System.out.println(sw.toString());
			
			Client client = Client.create();
	 
			WebResource webResource = client
			   .resource("http://"+this.monitoringPlatformIP+":8170/v1/monitoring-rules");
	 
	 
			ClientResponse response = webResource.type("application/json")
			   .post(ClientResponse.class, sw.toString());
	 
			if (response.getStatus() != 204) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
	 
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
	 
		  } catch (Exception e) {
	 
			e.printStackTrace();
	 
		  }
	}
	
	public void attachObserver(String targetMetric, String observerIP, String observerPort){
		try {
			 
			Client client = Client.create();
	 
			WebResource webResource = client
			   .resource("http://"+this.monitoringPlatformIP+":8170/v1/metrics/"+targetMetric+"/");
	 
			String input = "http://"+observerIP+":"+observerPort+"/v1/results";
			
			ClientResponse response = webResource.type("application/json")
			   .post(ClientResponse.class, input);
	 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
	 
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
	 
		  } catch (Exception e) {
	 
			e.printStackTrace();
	 
		  }
	}
	
	public  MonitoringRules buildDemandRule(Containers containers){
		
		MonitoringRules toReturn= factory.createMonitoringRules();
		MonitoringRule rule;
		MonitoredTarget target;
		Action action;
		CollectedMetric collectedMetric;
		Parameter tempParam;
		
		rule=factory.createMonitoringRule();

		rule.setId("sdaHaproxy");
		rule.setTimeStep("10");
		rule.setTimeWindow("10");
		rule.setMonitoredTargets(factory.createMonitoredTargets());

		for(Container c: containers.getContainer()){
			

			for(ApplicationTier t: c.getApplicationTier()){
				

					target=factory.createMonitoredTarget();
					target.setClazz("VM");
					target.setType(t.getId());
					rule.getMonitoredTargets().getMonitoredTargets().add(target);

			}
		
		}
		
		collectedMetric=factory.createCollectedMetric();
		collectedMetric.setMetricName("EstimationUBR");

		tempParam=factory.createParameter();
		tempParam.setName("window");
		tempParam.setValue("60000");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("nCPU");
		tempParam.setValue("4");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("CPUUtilTarget");
		tempParam.setValue("MIC");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("CPUUtilMetric");
		tempParam.setValue("FrontendCPUUtilization");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("targetMetric");
		tempParam.setValue("AvarageResponseTime");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("samplingTime");
		tempParam.setValue("300");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("filePath");
		tempParam.setValue("/home/ubuntu/modaclouds-sda-1.2.2/");
		collectedMetric.getParameters().add(tempParam);
		
		rule.setCollectedMetric(collectedMetric);
		
		action=factory.createAction();
		action.setName("OutputMetric");
	
		tempParam=factory.createParameter();
		tempParam.setName("metric");
		tempParam.setValue("EstimatedDemand");
		action.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("value");
		tempParam.setValue("METRIC");
		action.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("resourceId");
		tempParam.setValue("ID");
		action.getParameters().add(tempParam);
		
		rule.setActions(factory.createActions());
		
		rule.getActions().getActions().add(action);

		toReturn.getMonitoringRules().add(rule);

		
		return toReturn;

	}

	
	public  MonitoringRules buildWorkloadForecastRule(	Containers containers, int timestepAhead){
		MonitoringRules toReturn= factory.createMonitoringRules();
		MonitoringRule rule;
		MonitoredTarget target;
		Action action;
		CollectedMetric collectedMetric;
		Parameter tempParam;
		
		rule=factory.createMonitoringRule();

		rule.setId("sdaForecast"+timestepAhead);
		rule.setTimeStep("10");
		rule.setTimeWindow("10");
		rule.setMonitoredTargets(factory.createMonitoredTargets());
		

		for(Container c: containers.getContainer()){
			

			for(ApplicationTier t: c.getApplicationTier()){
				
					

					target=factory.createMonitoredTarget();
					target.setClazz("VM");
					target.setType(t.getId());
					rule.getMonitoredTargets().getMonitoredTargets().add(target);

			}
		
		}
		collectedMetric=factory.createCollectedMetric();
		collectedMetric.setMetricName("ForecastingTimeSeriesARIMA5Min");

		tempParam=factory.createParameter();
		tempParam.setName("targetMetric");
		tempParam.setValue("Workload");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("forecastPeriod");
		tempParam.setValue(Integer.toString(30*timestepAhead));
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("autoregressive");
		tempParam.setValue("1");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("movingAverage");
		tempParam.setValue("1");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("integrated");
		tempParam.setValue("1");
		collectedMetric.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("samplingTime");
		tempParam.setValue("300");
		collectedMetric.getParameters().add(tempParam);
		
		rule.setCollectedMetric(collectedMetric);

		
		action=factory.createAction();
		action.setName("OutputMetric");
	
		tempParam=factory.createParameter();
		tempParam.setName("metric");
		tempParam.setValue("ForecastedWorkload"+timestepAhead);
		action.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("value");
		tempParam.setValue("METRIC");
		action.getParameters().add(tempParam);
		
		tempParam=factory.createParameter();
		tempParam.setName("resourceId");
		tempParam.setValue("ID");
		action.getParameters().add(tempParam);
		
		rule.setActions(factory.createActions());
		
		rule.getActions().getActions().add(action);

		toReturn.getMonitoringRules().add(rule);
		
		return toReturn;
	}
}
