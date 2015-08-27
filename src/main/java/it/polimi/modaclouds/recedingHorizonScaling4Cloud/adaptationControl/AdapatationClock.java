package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdapatationClock {
	Timer timer;
	private static final Logger journal = Logger
			.getLogger(AdapatationClock.class.getName());
	
    public AdapatationClock(int timeStepDurationInMinutes) {
        timer = new Timer();
		journal.log(Level.INFO, "Here is the adaptation clock!");
		

        timer.scheduleAtFixedRate(new ControllerLauncher(), 5000, timeStepDurationInMinutes*60*1000);

    }

    class ControllerLauncher extends TimerTask {
    	
        public void run() {
        	
    		journal.log(Level.INFO, "Launching the adaptation step at the end of timestep "+ModelManager.getCurrentTimeStep());
        	
        	List<Container> toLaunch=ModelManager.getModel().getContainer();
        	Thread[] toWait=new Thread[toLaunch.size()];
        	
        	int i=0;
        	for(Container c: toLaunch){
        		Thread t=new Thread(new AdaptationController(c));
        		toWait[i]=t;
        		i++;
        		
        		journal.log(Level.INFO, "Starting a controller for container "+c.getId());
        		t.start();
        	}

        	
    		journal.log(Level.INFO, "Waiting for all the controller to end");

        	for(i = 0; i < toWait.length; i++){
				try {
					toWait[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        	
    		journal.log(Level.INFO, "Printing the current model");
        	ModelManager.increaseCurrentTimeStep();
    		ModelManager.printCurrentModel();
        }
    }



    public static void main(String args[]) {
        new AdapatationClock(5);
        System.out.format("Task scheduled.%n");
    }
}
