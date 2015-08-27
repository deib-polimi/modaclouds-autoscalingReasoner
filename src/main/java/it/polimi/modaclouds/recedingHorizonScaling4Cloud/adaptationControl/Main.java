package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final Logger journal = Logger
			.getLogger(Main.class.getName());
	public static void main(String[] args) {

		journal.log(Level.INFO, "Autoscaling Reasoner started");
		journal.log(Level.INFO, "Starting the initialization phase");
		AdaptationInitializer initializer=new AdaptationInitializer();
		initializer.initialize();
		
	}

}
