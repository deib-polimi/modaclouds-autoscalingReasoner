package it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatum;
import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatumHandler;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationInitializer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;


public class ObservedMetricHandler extends MonitoringDatumHandler {

	private static final Logger journal = Logger
			.getLogger(ObservedMetricHandler.class.getName());
	
    @Override
    public void getData(List<MonitoringDatum> monitoringData) {
    	String observerTimestamp = Long.toString(new Date().getTime());
		for (MonitoringDatum monitoringDatum : monitoringData) {
			
			System.out.println(observerTimestamp + ","
			+ monitoringDatum.getResourceId() + ","
			+ monitoringDatum.getMetric() + ","
			+ monitoringDatum.getValue() + ","
			+ monitoringDatum.getTimestamp());

        }

	    Random rand = new Random();

		for (MonitoringDatum monitoringDatum : monitoringData) {
			try {

			    double randomNum = rand.nextDouble();

				Thread.sleep((long)(randomNum*3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(monitoringDatum.getMetric().equals("EstimatedDemand")){
				ModelManager.updateDemand(monitoringDatum.getResourceId(),
						new Float(monitoringDatum.getValue().split(":")[1]),monitoringDatum.getValue().split(":")[0]);
			}else if(monitoringDatum.getMetric().contains("ForecastedWorkload")){
				ModelManager.updateWorkloadPrediction(monitoringDatum.getResourceId(), monitoringDatum.getMetric(),
						new Float(monitoringDatum.getValue().split(":")[1]),monitoringDatum.getValue().split(":")[0]);
			}
		

        }
		
		ModelManager.checkContainerReadyForOptimization("MIC");
			
    }

}