package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import org.cloudml.facade.CloudML;
import org.cloudml.facade.Factory;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandFactory;
import org.cloudml.facade.commands.ScaleOut;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigDictionary;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class Main {

	public static void main(String[] args) {
		AdaptationInitializer controller=new AdaptationInitializer();
		controller.initialize();
		
		
		
		//CloudMLAdapter cloudml=new CloudMLAdapter("ws://127.0.0.1:9000");
		//ScaleOut command=new ScaleOut("eu-west-1/i-152841f2");
		//cloudml.scaleOut(command, 1);
		
	}

}
