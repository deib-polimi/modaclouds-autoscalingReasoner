package it.polimi.modaclouds.recedingHorizonScaling4Cloud;

import it.polimi.modaclouds.qos_models.schema.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptationClock {
	Timer timer;
	private static final Logger journal = LoggerFactory
			.getLogger(AdaptationClock.class);
	
    public AdaptationClock(int timeStepDurationInMinutes) {
        timer = new Timer();
		journal.info("Here is the adaptation clock!");
		

        timer.scheduleAtFixedRate(new ControllerLauncher(), 5000, timeStepDurationInMinutes*60*1000);

    }

    class ControllerLauncher extends TimerTask {
    	
        public void run() {
        	
    		journal.info("Launching the adaptation step at the end of timestep {}", ModelManager.getCurrentTimeStep());
        	
        	List<Container> toLaunch=ModelManager.getModel().getContainer();
        	Thread[] toWait=new Thread[toLaunch.size()];
        	
        	int i=0;
        	for(Container c: toLaunch){
        		Thread t=new Thread(new AdaptationController(c));
        		toWait[i]=t;
        		i++;
        		
        		journal.info("Starting a controller for container {}", c.getId());
        		t.start();
        	}

        	
    		journal.info("Waiting for all the controller to end");

        	for(i = 0; i < toWait.length; i++){
				try {
					toWait[i].join();
				} catch (InterruptedException e) {
					journal.error("Error while waiting the " + i + " thread.", e);
				}
        	}
        	
    		journal.info("Printing the current model");
        	ModelManager.increaseCurrentTimeStep();
    		ModelManager.printCurrentModel();
        }
    }



    public static void main(String args[]) {
        new AdaptationClock(5);
        System.out.format("Task scheduled.%n");
    }
}
