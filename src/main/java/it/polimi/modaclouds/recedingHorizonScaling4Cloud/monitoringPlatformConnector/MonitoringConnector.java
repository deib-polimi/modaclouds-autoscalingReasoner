package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;



import java.io.IOException;
import it.polimi.tower4clouds.rules.Action;
import it.polimi.tower4clouds.rules.Actions;
import it.polimi.tower4clouds.rules.CollectedMetric;
import it.polimi.tower4clouds.rules.MonitoredTarget;
import it.polimi.tower4clouds.rules.MonitoredTargets;
import it.polimi.tower4clouds.rules.MonitoringRule;
import it.polimi.tower4clouds.rules.MonitoringRules;
import it.polimi.tower4clouds.rules.ObjectFactory;
import it.polimi.tower4clouds.rules.Parameter;
import it.polimi.tower4clouds.rules.MonitoringMetricAggregation;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Functionality;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.tower4clouds.manager.api.ManagerAPI;
import it.polimi.tower4clouds.manager.api.NotFoundException;


//need to build one different cpuRule per tier because of the demand rule required parameter?
//need to properly set up (look for the right value if static) some dynamic parameters in the rules possibly received via some configuration
public class MonitoringConnector {
	
	private ManagerAPI monitoring;
	private ObjectFactory factory;
	
	
	public static void main(String[] args) {
		
		
		
	}
	
	public MonitoringConnector(){
	  monitoring=new ManagerAPI(ConfigManager.MONITORING_PLATFORM_IP, Integer.parseInt(ConfigManager.MONITORING_PLATFORM_PORT));
	  factory=new ObjectFactory();
	
	}
	
	public void installRules(MonitoringRules toInstall) throws IOException{		 
			monitoring.installRules(toInstall);
	}
	
	public void attachObserver(String targetMetric, String observerIP, String observerPort) throws NotFoundException, IOException{
		monitoring.registerHttpObserver(targetMetric, "http://"+observerIP+":"+observerPort+"/v1/results", "RDF/JSON");
	}
	
	public  MonitoringRules buildRequiredRules(){
		MonitoringRules toReturn=factory.createMonitoringRules();
		
		toReturn.getMonitoringRules().addAll(this.buildCpuRules().getMonitoringRules());
		toReturn.getMonitoringRules().addAll(this.buildDemandRules().getMonitoringRules());
		toReturn.getMonitoringRules().addAll(this.buildRespondeTimeRules().getMonitoringRules());
		toReturn.getMonitoringRules().addAll(this.buildWorkloadRules().getMonitoringRules());
		toReturn.getMonitoringRules().addAll(this.buildWorkloadForecastRules().getMonitoringRules());

		return toReturn;
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
						rule.setId("cpuRule");
						rule.setTimeStep("2");
						rule.setTimeWindow("2");
								
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
						metric.setValue(t.getId()+"CPUUtilization");
						value.setName("value");
						value.setValue("METRIC");
						resourceId.setName("resourceId");
						resourceId.setValue("ID");
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
		rule.setId("respTimeRule");
		rule.setTimeStep("2");
		rule.setTimeWindow("2");
		
		
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
		metric.setValue("AvarageEffectiveResponseTime");
		value.setName("value");
		value.setValue("METRIC");
		resourceId.setName("resourceId");
		resourceId.setValue("ID");
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
		MonitoringMetricAggregation metricAggregation;
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
				metricAggregation=factory.createMonitoringMetricAggregation();
				actions=factory.createActions();
				action=factory.createAction();
				metric=factory.createParameter();
				value=factory.createParameter();
				resourceId=factory.createParameter();
				
				//setting rule attribute
				rule.setId("sdaHaproxy");
				rule.setTimeStep("2");
				rule.setTimeWindow("2");	

				//setting the monitored target
				for(Functionality f: t.getFunctionality()){
					monitoredTarget=factory.createMonitoredTarget();
					monitoredTarget.setClazz("Method");
					monitoredTarget.setType(f.getId());
					monitoredTargets.getMonitoredTargets().add(monitoredTarget);
				}
						
				//setting the collected metric
				collectedMetric.setMetricName("EstimationERPS_AvarageEffectiveResponseTime");
				window.setName("window");
				window.setValue("60");
				nCPU.setName("nCPU");
				nCPU.setValue(Integer.toString(c.getNCore()));
				CPUUtilTarget.setName("CPUUtilTarget");
				CPUUtilTarget.setValue(t.getId());
				CPUUtilMetric.setName("CPUUtilMetric");
				CPUUtilMetric.setValue(t.getId()+"CPUUtilization");
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
				metric.setValue("EstimatedDemand");
				value.setName("value");
				value.setValue("METRIC");
				resourceId.setName("resourceId");
				resourceId.setValue("ID");
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
		rule.setId("workloadRule");
		rule.setTimeStep("2");
		rule.setTimeWindow("2");
				
		//setting the collected metric
		collectedMetric.setMetricName("EffectiveResponseTime");
		samplingPorbability.setName("samplingPorbability");
		samplingPorbability.setValue("1");;
		collectedMetric.getParameters().add(samplingPorbability);
		
		//setting the aggregation fucntion
		metricAggregation.setAggregateFunction("Count");
		metricAggregation.setGroupingClass("Method");
		
		//setting the output action
		action.setName("OutputMetric");
		metric.setName("metric");
		metric.setValue("Workload");
		value.setName("value");
		value.setValue("METRIC");
		resourceId.setName("resourceId");
		resourceId.setValue("ID");
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
			rule.setId("sdaForecast"+timestep);
			rule.setTimeStep("2");
			rule.setTimeWindow("2");
			
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
			collectedMetric.setMetricName("ForecastingTimeseriesARIMA_Workload_"+timestep);
			order.setName("order");
			order.setValue("1");
			forecastPeriod.setName("forecastPeriod");
			forecastPeriod.setValue(Integer.toString(timestep*ModelManager.getTimestepDuration()));
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
			metric.setValue("ForecastedWorkload"+timestep);
			value.setName("value");
			value.setValue("METRIC");
			resourceId.setName("resourceId");
			resourceId.setValue("ID");
			actions.getActions().add(action);
					
			//setting rule field and adding the created rule to the returned list
			rule.setMonitoredTargets(monitoredTargets);
			rule.setCollectedMetric(collectedMetric);
			rule.setActions(actions);
			toReturn.getMonitoringRules().add(rule);
		}
		


		return toReturn;
	}
	
}
