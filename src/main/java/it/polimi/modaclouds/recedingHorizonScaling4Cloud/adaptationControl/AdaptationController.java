package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;

import ssh.SshAdapter;
import staticInputProcessing.StaticInputWriter;
import util.ConfigManager;
import util.ConfigDictionary;
import util.ExecutionOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.OptimizationExecution;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.DynamicInputWriter;
import it.polimi.modaclouds.space4cloud.milp.ssh.*;
import util.InputSupplier;



public class AdaptationController {

	
	
	public static void main(String[] args) {

		
		
		
		
		
		ConfigManager cm=new ConfigManager();
		
		try {
			cm.loadConfiguration();
		} catch (ConfigurationFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		

		if(cm!=null){
			mm.initializeModel();
		
		try {
			cm.inizializeFileSystem(mm.getExecutions());
		} catch (ProjectFileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StaticInputWriter siw= new StaticInputWriter();
		siw.writeStaticInput(mm.getExecutions());
		
		//DynamicInputWriter diw= new DynamicInputWriter();
		
		//diw.writeDynamicInput(mm.getExecutions());
		
		//SUYPPLING THE STILL NOT AVAILABLE INPUT FILES
		
		InputSupplier newSupplier= new InputSupplier();
		
		for(OptimizationExecution ex: mm.getExecutions()){
			newSupplier.supplyInput(ex.toString());

		}
		
		
		
		}
		
		
		//SSH
		
		//SshConnector.run(mm.getExecutions());
		
		
		
		ExecutionOutputParser outputParser= new ExecutionOutputParser();
		
		
		for(OptimizationExecution ex: mm.getExecutions()){
			outputParser.parseExecutionOutput("executions/execution_"+ex+"/IaaS_1/output.out", ex);
		}
		
		
		
		
		
	}

}
