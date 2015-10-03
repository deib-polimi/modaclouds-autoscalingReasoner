/**
 * Copyright (C) 2014 Politecnico di Milano (michele.guerriero@mail.polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeObserver {
	Timer timer;
	private static final Logger journal = LoggerFactory
			.getLogger(FakeObserver.class);
	
	private static List<Double> worklaodPrediction;
	
	private static final String DEFAULT_FILE = "/home/ubuntu/autoscaling-reasoner/tests.txt";
	
    public FakeObserver(int timeStepDurationInMinutes) {
        timer = new Timer();
		journal.info("Here is the fake observer!");
		worklaodPrediction=new ArrayList<Double>();
		
		//read from file the workload prediction and load the array
		try (BufferedReader br = new BufferedReader(new FileReader(DEFAULT_FILE))) {
			try {
			    String line = br.readLine();
			    while (line != null) {
			    	worklaodPrediction.add(Double.parseDouble(line));
			    	line = br.readLine();
			    }
			} catch (IOException e) {
				journal.error("Error while reading the file.", e);
			}

		} catch (FileNotFoundException e) {
			journal.error("File not found.", e);
		} catch (IOException e) {
			journal.error("Error while closing the stream.", e);
		}

        timer.scheduleAtFixedRate(new FakeObserverLauncher(), 0, timeStepDurationInMinutes*60*1000);

    }

    class FakeObserverLauncher extends TimerTask {
    	
        public void run() {
        	
        	for(int i=1; i<=ModelManager.getOptimizationWindow(); i++){
            	ModelManager.updateServiceWorkloadPrediction("register_123", "ForecastedWorkload"+i, worklaodPrediction.get(i-1));
        	}
        	worklaodPrediction.remove(0);
    		journal.info("Launching the adaptation step at the end of timestep {}", ModelManager.getCurrentTimeStep());

        }
    }



    public static void main(String args[]) {
    	FakeObserver test=new FakeObserver(10);
    }
}
