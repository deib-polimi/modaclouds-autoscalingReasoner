package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;

import java.util.Timer;
import java.util.TimerTask;

public class HourClock {
	Timer timer;

    public HourClock() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new HourUpdater(), 0, 3600*1000);

    }

    class HourUpdater extends TimerTask {
        public void run() {
        	
        	ModelManager.increaseCurrentHour();
        	
        	MonitoringConnector monitor=new MonitoringConnector();
        	
        	
        	for(Container c:ModelManager.getModel().getContainer()){
        		for(ApplicationTier t:c.getApplicationTier()){
        			monitor.changeRuleThreshold(t.getResponseTimeThresholdRuleID(), 
        					t.getResponseTimeThreshold().get(ModelManager.getCurrentHour()-1).getValue());
        		}
        	}
        	
        	
        }
    }



    public static void main(String args[]) {
        new AdapatationClock(5);
        System.out.format("Task scheduled.%n");
    }
}
