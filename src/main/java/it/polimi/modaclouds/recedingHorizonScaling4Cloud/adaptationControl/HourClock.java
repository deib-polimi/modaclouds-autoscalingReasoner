package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.util.Timer;
import java.util.TimerTask;

public class HourClock {
	Timer timer;
	
	public static final int HOUR_MILLIS = 3600*1000;

    public HourClock() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new HourUpdater(), 0, HOUR_MILLIS);

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
        new AdaptationClock(5);
        System.out.format("Task scheduled.%n");
    }
}
