package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class InputSupplier {
	
	public void supplyInput(String execution){
		  String cpMissingFiles = "cp /home/mik/workspace/recedingHorizonScaling4Cloud/missingInputFiles/* /home/mik/workspace/recedingHorizonScaling4Cloud/executions/execution_"+execution+"/IaaS_1";
		  String scriptPath="/home/mik/inputSupplier.sh";

		    try {
		        FileWriter fw = new FileWriter(scriptPath);

		        PrintWriter pw = new PrintWriter(fw);

		        pw.println("#!/bin/bash");
		        pw.println(cpMissingFiles);

		        pw.close();
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        e1.printStackTrace();
		    }

		    Process proc = null;

		    try {
		        proc = Runtime.getRuntime().exec("/bin/bash "+scriptPath);
		        proc.waitFor();

		    } catch (IOException | InterruptedException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
	}

}
