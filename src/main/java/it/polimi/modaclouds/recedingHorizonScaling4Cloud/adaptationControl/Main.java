package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cloudml.facade.CloudML;
import org.cloudml.facade.Factory;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandFactory;
import org.cloudml.facade.commands.ScaleOut;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class Main {

	public static void main(String[] args) {


		AdaptationInitializer initializer=new AdaptationInitializer();
		initializer.initialize();
		
	}

}
