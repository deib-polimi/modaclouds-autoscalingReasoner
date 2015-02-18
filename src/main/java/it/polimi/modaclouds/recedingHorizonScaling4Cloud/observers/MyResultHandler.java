package it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatum;
import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatumHandler;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationController;


public class MyResultHandler extends MonitoringDatumHandler {

    @Override
    public void getData(List<MonitoringDatum> monitoringData) {
    	String observerTimestamp = Long.toString(new Date().getTime());
		for (MonitoringDatum monitoringDatum : monitoringData) {
		AdaptationController.printObserved(observerTimestamp + ","
		+ monitoringDatum.getResourceId() + ","
		+ monitoringDatum.getMetric() + ","
		+ monitoringDatum.getValue() + ","
		+ monitoringDatum.getTimestamp());

		/*
		SDADatum toAdd= new SDADatum();
		toAdd.setValue(Float.parseFloat(monitoringDatum.getValue()));
		toAdd.setMetric(monitoringDatum.getMetric());
		
        SDADataManager.getInstance().addDatum(toAdd);
        */
        }
    }

}