package it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers;

import it.polimi.modaclouds.monitoring.metrics_observer.MetricsObServer;


public class Observer extends MetricsObServer {

    public Observer(int listeningPort) {
		super(listeningPort, "/v1/results", MyResultHandler.class);
    }

}