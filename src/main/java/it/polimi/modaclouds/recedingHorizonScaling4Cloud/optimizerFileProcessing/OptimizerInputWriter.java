package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class OptimizerInputWriter {


	public OptimizerInputWriter() {	
	}

	public void writeStaticInput(Containers containers) {

		
		
		for (Container c : containers.getContainer()) {
			
			writeFile("C.dat", c.getId(), "let C:=\n"
					+ c.getCapacity() + "\n;");
			writeFile("W.dat", c.getId(), "let W:=\n"
					+ c.getMaxReserved() + "\n;");
			writeFile("delta.dat", c.getId(),
					"let delta:=\n" + c.getOnDemandCost() + "\n;");
			writeFile("rho.dat", c.getId(), "let rho:=\n"
					+ c.getReservedCost() + "\n;");

			
			int cont=1;
			for(ApplicationTier t: c.getApplicationTier()){
				
				writeFile("Rcross.dat", c.getId(), "let Rcross["+cont+"]:=\n"+Float.toString(t.getResponseTimeThreshold().get(0).getValue())+"\n;");
				writeFile("initialVM.dat", c.getId(), "let Nond["+cont+",1]:=\n"+Integer.toString(t.getInitialNumberOfVMs())+"\n;");

				cont++;
			}
					
		}
	}

	public static void writeFile(String fileName, String execution,
			String fileContent) {
		File file = null;
		file = new File("executions/execution_"+execution+"/IaaS_1/" + fileName);

		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
		    out.println(fileContent);
		}catch (IOException e) {
		e.printStackTrace();
		}
	}
	
}
