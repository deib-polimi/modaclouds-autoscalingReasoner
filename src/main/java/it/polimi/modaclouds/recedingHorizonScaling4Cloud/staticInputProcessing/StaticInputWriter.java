package it.polimi.modaclouds.recedingHorizonScaling4Cloud.staticInputProcessing;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class StaticInputWriter {


	public StaticInputWriter() {	
	}

	public void writeStaticInput(Containers containers) {

		
		int i=1;
		
		for (Container c : containers.getContainer()) {
			
			this.writeFile("C.dat", c.toString(), "let C:=\n"
					+ c.getCapacity() + "\n;");
			this.writeFile("W.dat", c.toString(), "let W:=\n"
					+ c.getMaxReserved() + "\n;");
			this.writeFile("delta.dat", c.toString(),
					"let delta:=\n" + c.getOnDemandCost() + "\n;");
			this.writeFile("rho.dat", c.toString(), "let rho:=\n"
					+ c.getReservedCost() + "\n;");
			
			int cont=1;
			for(ApplicationTier t: c.getApplicationTier()){
				
				this.writeFile("Rcross.dat", c.toString(), "let Rcross["+cont+"]:=\n"+Float.toString(t.getResponseTimeThreshold().get(0).getValue())+"\n;");
				cont++;
			}
					
			i++;
		}
	}

	private void writeFile(String fileName, String execution,
			String fileContent) {
		FileWriter fw = null;
		File file = null;
		file = new File("executions/execution_"+execution+"/IaaS_1/" + fileName);

		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
		    out.println(fileContent);
		}catch (IOException e) {
		e.printStackTrace();
		}
	}
	
}
