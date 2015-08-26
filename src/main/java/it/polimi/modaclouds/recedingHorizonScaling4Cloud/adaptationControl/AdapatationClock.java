package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

import java.util.Timer;
import java.util.TimerTask;

public class AdapatationClock {
	Timer timer;

    public AdapatationClock(int timeStepDurationInMinutes) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new ControllerLauncher(), timeStepDurationInMinutes*60*1000, timeStepDurationInMinutes*60*1000);

    }

    class ControllerLauncher extends TimerTask {
        public void run() {
        	for(Container c: ModelManager.getModel().getContainer()){
        		Thread t=new Thread(new AdaptationController(c));
        		t.start();
        	}
        }
    }



    public static void main(String args[]) {
        new AdapatationClock(5);
        System.out.format("Task scheduled.%n");
    }
}