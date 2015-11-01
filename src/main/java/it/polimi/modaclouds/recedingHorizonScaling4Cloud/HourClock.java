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
/**
 * This product currently only contains code developed by authors
 * of specific components, as identified by the source code files.
 *
 * Since product implements StAX API, it has dependencies to StAX API
 * classes.
 *
 * For additional credits (generally to people who reported problems)
 * see CREDITS file.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud;

import it.polimi.modaclouds.qos_models.schema.ApplicationTier;
import it.polimi.modaclouds.qos_models.schema.Container;
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
    }
}
