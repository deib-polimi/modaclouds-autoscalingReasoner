package it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers;

import it.polimi.modaclouds.monitoring.metrics_observer.MetricsObServer;


public class Observer extends MetricsObServer implements Runnable {

    public Observer(int listeningPort) {
		super(listeningPort, "/v1/results", MyResultHandler.class);
    }

	@Override
	public void run() {
		try {
			this.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}