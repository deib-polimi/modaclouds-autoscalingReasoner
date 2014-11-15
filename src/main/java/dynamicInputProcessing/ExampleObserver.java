package dynamicInputProcessing;

import it.polimi.modaclouds.monitoring.metrics_observer.MetricsObServer;


public class ExampleObserver extends MetricsObServer {

    public ExampleObserver(int listeningPort) {
		super(listeningPort, "/v1/results", MyResultHandler.class);
    }

}