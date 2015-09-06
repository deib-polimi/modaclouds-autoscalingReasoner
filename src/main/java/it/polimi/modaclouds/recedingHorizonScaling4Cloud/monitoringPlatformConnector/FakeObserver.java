package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdapatationClock;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationController;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FakeObserver {
	Timer timer;
	private static final Logger journal = Logger
			.getLogger(AdapatationClock.class.getName());
	
	private static List<Double> worklaodPrediction;
	
    public FakeObserver(int timeStepDurationInMinutes) {
        timer = new Timer();
		journal.log(Level.INFO, "Here is the fake observer!");
		worklaodPrediction=new ArrayList<Double>();
		
		//read from file the workload prediction and load the array
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("/home/ubuntu/autoscaling-reasoner/tests.txt"));
			try {
			    String line = br.readLine();
			    while (line != null) {
			    	worklaodPrediction.add(Double.parseDouble(line));
			    	line = br.readLine();
			    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			    try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        timer.scheduleAtFixedRate(new FakeObserverLauncher(), 0, timeStepDurationInMinutes*60*1000);

    }

    class FakeObserverLauncher extends TimerTask {
    	
        public void run() {
        	
        	for(int i=1; i<=ModelManager.getOptimizationWindow(); i++){
            	ModelManager.updateServiceWorkloadPrediction("register_123", "ForecastedWorkload"+i, worklaodPrediction.get(i-1));
        	}
        	worklaodPrediction.remove(0);
    		journal.log(Level.INFO, "Launching the adaptation step at the end of timestep "+ModelManager.getCurrentTimeStep());

        }
    }



    public static void main(String args[]) {
    	FakeObserver test=new FakeObserver(10);
    }
}
