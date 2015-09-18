package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;



import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Functionality;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;
import it.polimi.tower4clouds.common.net.UnexpectedAnswerFromServerException;
import it.polimi.tower4clouds.manager.api.ManagerAPI;
import it.polimi.tower4clouds.manager.api.NotFoundException;
import it.polimi.tower4clouds.rules.Action;
import it.polimi.tower4clouds.rules.Actions;
import it.polimi.tower4clouds.rules.CollectedMetric;
import it.polimi.tower4clouds.rules.MonitoredTarget;
import it.polimi.tower4clouds.rules.MonitoredTargets;
import it.polimi.tower4clouds.rules.MonitoringMetricAggregation;
import it.polimi.tower4clouds.rules.MonitoringRule;
import it.polimi.tower4clouds.rules.MonitoringRules;
import it.polimi.tower4clouds.rules.ObjectFactory;
import it.polimi.tower4clouds.rules.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//need to build one different cpuRule per tier because of the demand rule required parameter?
//need to properly set up (look for the right value if static) some dynamic parameters in the rules possibly received via some configuration
public class MonitoringConnector {
	
	private static final Logger journal = LoggerFactory
			.getLogger(MonitoringConnector.class);
	
	private ManagerAPI monitoring;
	private ObjectFactory factory;
	
	public MonitoringConnector(){
	  monitoring=new ManagerAPI(ConfigManager.MONITORING_PLATFORM_IP, Integer.parseInt(ConfigManager.MONITORING_PLATFORM_PORT));
	  factory=new ObjectFactory();
	
	}
	
	public void installRules(MonitoringRules toInstall) {		 
			try {
				monitoring.installRules(toInstall);
			} catch (IOException e) {
				journal.error("Error while installing the rules.", e);
			}
	}
	
	public void uninstallPreviousRules(MonitoringRules toInstall){
		MonitoringRules installedRules;
		
		try {
			installedRules = monitoring.getRules();
			
			List<String> rulesIds= new ArrayList<String>();
			
			for(MonitoringRule rule: toInstall.getMonitoringRules()){
				rulesIds.add(rule.getId());
			}
			
			for(MonitoringRule rule: installedRules.getMonitoringRules()){
				for(String toInstallRuleId: rulesIds){
					if(rule.getId().equals(toInstallRuleId)){
						monitoring.uninstallRule(rule.getId());
					}
				}
			}
		} catch (UnexpectedAnswerFromServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void attachObserver(String targetMetric, String observerIP, String observerPort) throws NotFoundException, IOException{
		monitoring.registerHttpObserver(targetMetric, "http://"+observerIP+":"+observerPort+"/v1/results", "TOWER/JSON");
	}
	
	public  MonitoringRules buildRequiredRules(){
		MonitoringRules toReturn=factory.createMonitoringRules();
		
		//building all required rules for demand monitoring if default demand values are not used
		if(ModelManager.getDefaultDemand()==0){
			toReturn.getMonitoringRules().addAll(this.buildCpuRules().getMonitoringRules());
			toReturn.getMonitoringRules().addAll(this.buildDemandRules().getMonitoringRules());
			toReturn.getMonitoringRules().addAll(this.buildRespondeTimeRules().getMonitoringRules());
		}
		
		//building all required rules for workload prediction monitoring
		toReturn.getMonitoringRules().addAll(this.buildWorkloadRules().getMonitoringRules());
		toReturn.getMonitoringRules().addAll(this.buildWorkloadForecastRules().getMonitoringRules());

		return toReturn;
	}
	
	public void attachRequiredObservers(){
		try {
			//attaching demand observer is default demand values are not used
			if(ModelManager.getDefaultDemand()==0){
				this.attachObserver("EstimatedDemandARGenerated", ConfigManager.OWN_IP, ConfigManager.LISTENING_PORT);
			}
			
			//attaching workload forecasts observers
			for(int i=1; i<=ModelManager.getOptimizationWindow();i++){
				this.attachObserver("ForecastedWorkloadARGenerated"+i, ConfigManager.OWN_IP, ConfigManager.LISTENING_PORT);
			}
		} catch (NotFoundException e) {
			journal.error("File not found.", e);
		} catch (IOException e) {
			journal.error("Error while reading the files.", e);
		}

	}
	
	
	private MonitoringRules buildCpuRules(){

				MonitoringRules toReturn=factory.createMonitoringRules();
				MonitoringRule rule;
				MonitoredTargets monitoredTargets;
				MonitoredTarget monitoredTarget;
				CollectedMetric collectedMetric;
				MonitoringMetricAggregation metricAggregation;
				Parameter samplingTime;
				Parameter samplingPorbability;
				Actions actions;
				Action action;
				Parameter metric;
				Parameter value;
				Parameter resourceId;
				
				for(Container c: ModelManager.getModel().getContainer()){
					for(ApplicationTier t: c.getApplicationTier()){
						
						//initialize rule parameter
						rule=factory.createMonitoringRule();
						monitoredTargets=factory.createMonitoredTargets();
						monitoredTarget=factory.createMonitoredTarget();
						collectedMetric=factory.createCollectedMetric();
						metricAggregation=factory.createMonitoringMetricAggregation();
						samplingTime=factory.createParameter();
						samplingPorbability=factory.createParameter();
						actions=factory.createActions();
						action=factory.createAction();
						metric=factory.createParameter();
						value=factory.createParameter();
						resourceId=factory.createParameter();
						
						//setting rule attribute
						rule.setId("cpuRuleARGenerated");
						rule.setTimeStep("10");
						rule.setTimeWindow("10");
								
						//setting the monitored target
						monitoredTarget.setClazz("VM");
						monitoredTarget.setType(t.getId());
						monitoredTargets.getMonitoredTargets().add(monitoredTarget);
						
						//setting the collected metric
						collectedMetric.setMetricName("CPUUtilization");
						samplingPorbability.setName("samplingPorbability");
						samplingPorbability.setValue("1");
						samplingTime.setName("samplingTime");
						samplingTime.setValue("1");
						collectedMetric.getParameters().add(samplingTime);
						collectedMetric.getParameters().add(samplingPorbability);
						
						//setting the aggregation fucntion
						metricAggregation.setAggregateFunction("Average");
						metricAggregation.setGroupingClass("VM");
						
						//setting the output action
						action.setName("OutputMetric");
						metric.setName("metric");
						metric.setValue(t.getId()+"CPUUtilizationARGenerated");
						value.setName("value");
						value.setValue("METRIC");
						resourceId.setName("resourceId");
						resourceId.setValue("ID");
						action.getParameters().add(metric);
						action.getParameters().add(value);
						action.getParameters().add(resourceId);
						actions.getActions().add(action);
						
						//setting rule field and adding the created rule to the returned list
						rule.setMonitoredTargets(monitoredTargets);
						rule.setCollectedMetric(collectedMetric);
						rule.setMetricAggregation(metricAggregation);
						rule.setActions(actions);
						toReturn.getMonitoringRules().add(rule);
						
					}
				}

				return toReturn;
	}
	
	private MonitoringRules buildRespondeTimeRules(){
		
		MonitoringRules toReturn=factory.createMonitoringRules();
		MonitoringRule rule;
		MonitoredTargets monitoredTargets;
		MonitoredTarget monitoredTarget;
		CollectedMetric collectedMetric;
		Parameter samplingPorbability;
		MonitoringMetricAggregation metricAggregation;
		Actions actions;
		Action action;
		Parameter metric;
		Parameter value;
		Parameter resourceId;
		
		//initialize rule parameter
		rule=factory.createMonitoringRule();
		monitoredTargets=factory.createMonitoredTargets();
		monitoredTarget=factory.createMonitoredTarget();
		collectedMetric=factory.createCollectedMetric();
		samplingPorbability=factory.createParameter();
		metricAggregation=factory.createMonitoringMetricAggregation();
		actions=factory.createActions();
		action=factory.createAction();
		metric=factory.createParameter();
		value=factory.createParameter();
		resourceId=factory.createParameter();
		
		//setting rule attribute
		rule.setId("respTimeRuleARGenerated");
		rule.setTimeStep("10");
		rule.setTimeWindow("10");
		
		
		//setting the monitored target
		for(Container c: ModelManager.getModel().getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				for(Functionality f: t.getFunctionality()){
					monitoredTarget=factory.createMonitoredTarget();
					monitoredTarget.setClazz("Method");
					monitoredTarget.setType(f.getId());
					monitoredTargets.getMonitoredTargets().add(monitoredTarget);
				}
			}
		}
				
		//setting the collected metric
		collectedMetric.setMetricName("EffectiveResponseTime");
		samplingPorbability.setName("samplingPorbability");
		samplingPorbability.setValue("1");;
		collectedMetric.getParameters().add(samplingPorbability);
		
		//setting the aggregation fucntion
		metricAggregation.setAggregateFunction("Average");
		metricAggregation.setGroupingClass("Method");
		
		//setting the output action
		action.setName("OutputMetric");
		metric.setName("metric");
		metric.setValue("AvarageEffectiveResponseTimeARGenerated");
		value.setName("value");
		value.setValue("METRIC");
		resourceId.setName("resourceId");
		resourceId.setValue("ID");
		action.getParameters().add(metric);
		action.getParameters().add(value);
		action.getParameters().add(resourceId);
		actions.getActions().add(action);
		
		//setting rule field and adding the created rule to the returned list
		rule.setMonitoredTargets(monitoredTargets);
		rule.setCollectedMetric(collectedMetric);
		rule.setMetricAggregation(metricAggregation);
		rule.setActions(actions);
		toReturn.getMonitoringRules().add(rule);

		return toReturn;
	}
	
	private MonitoringRules buildDemandRules(){
		
		MonitoringRules toReturn=factory.createMonitoringRules();
		MonitoringRule rule;
		MonitoredTargets monitoredTargets;
		MonitoredTarget monitoredTarget;
		CollectedMetric collectedMetric;
		Parameter window;
		Parameter nCPU;
		Parameter CPUUtilTarget;
		Parameter CPUUtilMetric;
		Parameter samplingTime;
		Parameter filePath;
		Actions actions;
		Action action;
		Parameter metric;
		Parameter value;
		Parameter resourceId;

		
		for(Container c: ModelManager.getModel().getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				//initialize rule parameter
				rule=factory.createMonitoringRule();
				monitoredTargets=factory.createMonitoredTargets();
				monitoredTarget=factory.createMonitoredTarget();
				collectedMetric=factory.createCollectedMetric();
				window=factory.createParameter();
				nCPU=factory.createParameter();
				CPUUtilTarget=factory.createParameter();
				CPUUtilMetric=factory.createParameter();
				samplingTime=factory.createParameter();
				filePath=factory.createParameter();
				actions=factory.createActions();
				action=factory.createAction();
				metric=factory.createParameter();
				value=factory.createParameter();
				resourceId=factory.createParameter();
				
				//setting rule attribute
				rule.setId("sdaHaproxyARGenerated");
				rule.setTimeStep("10");
				rule.setTimeWindow("10");	

				//setting the monitored target
				for(Functionality f: t.getFunctionality()){
					monitoredTarget=factory.createMonitoredTarget();
					monitoredTarget.setClazz("Method");
					monitoredTarget.setType(f.getId());
					monitoredTargets.getMonitoredTargets().add(monitoredTarget);
				}
						
				//setting the collected metric
				collectedMetric.setMetricName("EstimationERPS_AvarageEffectiveResponseTimeARGenerated");
				window.setName("window");
				window.setValue("60");
				nCPU.setName("nCPU");
				nCPU.setValue(Integer.toString(c.getNCore()));
				CPUUtilTarget.setName("CPUUtilTarget");
				CPUUtilTarget.setValue(t.getId());
				CPUUtilMetric.setName("CPUUtilMetric");
				CPUUtilMetric.setValue(t.getId()+"CPUUtilizationARGenerated");
				samplingTime.setName("samplingTime");
				samplingTime.setValue("1");
				filePath.setName("filePath");
				filePath.setValue("/home/ubuntu/modaclouds-sda/");		
				collectedMetric.getParameters().add(window);
				collectedMetric.getParameters().add(nCPU);
				collectedMetric.getParameters().add(CPUUtilTarget);
				collectedMetric.getParameters().add(CPUUtilMetric);
				collectedMetric.getParameters().add(samplingTime);
				collectedMetric.getParameters().add(filePath);

				//setting the output action
				action.setName("OutputMetric");
				metric.setName("metric");
				metric.setValue("EstimatedDemandARGenerated");
				value.setName("value");
				value.setValue("METRIC");
				resourceId.setName("resourceId");
				resourceId.setValue("ID");
				action.getParameters().add(metric);
				action.getParameters().add(value);
				action.getParameters().add(resourceId);
				actions.getActions().add(action);
				


				//setting rule field and adding the created rule to the returned list
				rule.setMonitoredTargets(monitoredTargets);
				rule.setCollectedMetric(collectedMetric);
				rule.setActions(actions);
				toReturn.getMonitoringRules().add(rule);
			}
		}
		

		return toReturn;
		
	}
	
	private MonitoringRules buildWorkloadRules(){
		
		MonitoringRules toReturn=factory.createMonitoringRules();
		MonitoringRule rule;
		MonitoredTargets monitoredTargets;
		MonitoredTarget monitoredTarget;
		CollectedMetric collectedMetric;
		Parameter samplingPorbability;
		MonitoringMetricAggregation metricAggregation;
		Actions actions;
		Action action;
		Parameter metric;
		Parameter value;
		Parameter resourceId;
		
		//initialize rule parameter
		rule=factory.createMonitoringRule();
		monitoredTargets=factory.createMonitoredTargets();
		monitoredTarget=factory.createMonitoredTarget();
		collectedMetric=factory.createCollectedMetric();
		samplingPorbability=factory.createParameter();
		metricAggregation=factory.createMonitoringMetricAggregation();
		actions=factory.createActions();
		action=factory.createAction();
		metric=factory.createParameter();
		value=factory.createParameter();
		resourceId=factory.createParameter();
		
		//setting rule attribute
		rule.setId("workloadRuleARGenerated");
		rule.setTimeStep("10");
		rule.setTimeWindow("10");
				
		//setting the collected metric
		collectedMetric.setMetricName("ResponseTime");
		samplingPorbability.setName("samplingPorbability");
		samplingPorbability.setValue("1");;
		collectedMetric.getParameters().add(samplingPorbability);
		
		//setting the aggregation fucntion
		metricAggregation.setAggregateFunction("Count");
		metricAggregation.setGroupingClass("Method");
		
		//setting the output action
		action.setName("OutputMetric");
		metric.setName("metric");
		metric.setValue("WorkloadARGenerated");
		value.setName("value");
		value.setValue("METRIC");
		resourceId.setName("resourceId");
		resourceId.setValue("ID");
		action.getParameters().add(metric);
		action.getParameters().add(value);
		action.getParameters().add(resourceId);
		actions.getActions().add(action);
		

		
		
		for(Container c: ModelManager.getModel().getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				//setting the monitored target
				for(Functionality f: t.getFunctionality()){
					monitoredTarget=factory.createMonitoredTarget();
					monitoredTarget.setClazz("Method");
					monitoredTarget.setType(f.getId());
					monitoredTargets.getMonitoredTargets().add(monitoredTarget);
				}
			}
		}
		
		//setting rule field and adding the created rule to the returned list
		rule.setMonitoredTargets(monitoredTargets);
		rule.setCollectedMetric(collectedMetric);
		rule.setMetricAggregation(metricAggregation);
		rule.setActions(actions);
		toReturn.getMonitoringRules().add(rule);

		return toReturn;
		
	}
	
	private  MonitoringRules buildWorkloadForecastRules(){
		MonitoringRules toReturn=factory.createMonitoringRules();
		MonitoringRule rule;
		MonitoredTargets monitoredTargets;
		MonitoredTarget monitoredTarget;
		CollectedMetric collectedMetric;
		Parameter order;
		Parameter forecastPeriod;
		Parameter autoregressive;
		Parameter movingAverage;
		Parameter integrated;
		Parameter samplingTime;
		Actions actions;
		Action action;
		Parameter metric;
		Parameter value;
		Parameter resourceId;
		
		for(int timestep=1; timestep<=ModelManager.getOptimizationWindow(); timestep++){
			
			//initialize rule parameter
			rule=factory.createMonitoringRule();
			monitoredTargets=factory.createMonitoredTargets();
			monitoredTarget=factory.createMonitoredTarget();
			collectedMetric=factory.createCollectedMetric();
			order=factory.createParameter();
			forecastPeriod=factory.createParameter();
			autoregressive=factory.createParameter();
			movingAverage=factory.createParameter();
			integrated=factory.createParameter();
			samplingTime=factory.createParameter();
			actions=factory.createActions();
			action=factory.createAction();
			metric=factory.createParameter();
			value=factory.createParameter();
			resourceId=factory.createParameter();
			
			//setting rule attribute
			rule.setId("sdaForecastARGenerated"+timestep);
			rule.setTimeStep("300");
			rule.setTimeWindow("300");
			
			//setting the monitored target
			for(Container c: ModelManager.getModel().getContainer()){
				for(ApplicationTier t: c.getApplicationTier()){
					for(Functionality f: t.getFunctionality()){
						monitoredTarget=factory.createMonitoredTarget();
						monitoredTarget.setClazz("Method");
						monitoredTarget.setType(f.getId());
						monitoredTargets.getMonitoredTargets().add(monitoredTarget);
					}
				}
			}
					
			//setting the collected metric
			collectedMetric.setMetricName("ForecastingTimeseriesARIMA_WorkloadARGenerated_"+timestep);
			order.setName("order");
			order.setValue("1");
			forecastPeriod.setName("forecastPeriod");
			forecastPeriod.setValue(Integer.toString(6*timestep*ModelManager.getTimestepDuration()));
			autoregressive.setName("autoregressive");
			autoregressive.setValue("1");
			movingAverage.setName("movingAverage");
			movingAverage.setValue("1");
			integrated.setName("integrated");
			integrated.setValue("1");
			samplingTime.setName("samplingTime");
			samplingTime.setValue("1");
			collectedMetric.getParameters().add(order);
			collectedMetric.getParameters().add(forecastPeriod);
			collectedMetric.getParameters().add(autoregressive);
			collectedMetric.getParameters().add(movingAverage);
			collectedMetric.getParameters().add(integrated);
			collectedMetric.getParameters().add(samplingTime);

			//setting the output action
			action.setName("OutputMetric");
			metric.setName("metric");
			metric.setValue("ForecastedWorkloadARGenerated"+timestep);
			value.setName("value");
			value.setValue("METRIC");
			resourceId.setName("resourceId");
			resourceId.setValue("ID");
			action.getParameters().add(metric);
			action.getParameters().add(value);
			action.getParameters().add(resourceId);
			actions.getActions().add(action);
					
			//setting rule field and adding the created rule to the returned list
			rule.setMonitoredTargets(monitoredTargets);
			rule.setCollectedMetric(collectedMetric);
			rule.setActions(actions);
			toReturn.getMonitoringRules().add(rule);
		}
		


		return toReturn;
	}
	
	
	public void changeRuleThreshold(String ruleId, Double threshold){
		MonitoringRules installedRules;
			try {
				installedRules = monitoring.getRules();
				
				for(MonitoringRule rule: installedRules.getMonitoringRules()){
					if(rule.getId().equals(ruleId)){
						rule.getCondition().setValue(threshold.toString());
						monitoring.disableRule(ruleId);
						MonitoringRules toInstall=factory.createMonitoringRules();
						toInstall.getMonitoringRules().add(rule);
						monitoring.installRules(toInstall);
						break;
					}
				}
			} catch (UnexpectedAnswerFromServerException e) {
				journal.error("Unexpected answer from the server.", e);
			} catch (IOException e) {
				journal.error("Error while dealing with the files.", e);
			} catch (NotFoundException e) {
				journal.error("File not found.", e);
			}			
		
	}
	
}
