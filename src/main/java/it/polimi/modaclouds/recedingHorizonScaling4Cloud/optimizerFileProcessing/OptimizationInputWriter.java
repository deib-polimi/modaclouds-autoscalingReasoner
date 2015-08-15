package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OptimizationInputWriter {


	public OptimizationInputWriter() {	
	}

	public void writeStaticInput(Containers containers){

		
		
		for (Container c : containers.getContainer()) {
			
			writeFile("C.dat", c.getId(), "let C:=\n"
					+ c.getCapacity() + "\n;");
			writeFile("W.dat", c.getId(), "let W:=\n"
					+ c.getMaxReserved() + "\n;");
			writeFile("delta.dat", c.getId(),
					"let delta:=\n" + c.getOnDemandCost() + "\n;");
			writeFile("rho.dat", c.getId(), "let rho:=\n"
					+ c.getReservedCost() + "\n;");

			int index;
			for(ApplicationTier t: c.getApplicationTier()){
					index=ModelManager.getClassIndex(t.getId());
					writeFile("Rcross.dat", c.getId(), "let Rcross["+index+"]:=\n"+Double.toString(t.getResponseTimeThreshold().get(0).getValue())+"\n;");

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
	
	public static void deleteFile(String fileName, String execution){
		try {
			Files.deleteIfExists(Paths.get("executions/execution_"+execution+"/IaaS_1/" + fileName));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void storeFile(List<String> files, String execution) throws ProjectFileSystemException{
		File file = null;

		file = new File("executions/execution_"+execution+"/IaaS_1/"+System.currentTimeMillis()+"_output/");
		boolean success=file.mkdirs();
		
		if(!success)
			throw new ProjectFileSystemException("Error initializing the project file system: the following folder cannot be created: "
					+ "executions/execution_"+execution+"/IaaS_1/"+System.currentTimeMillis()+"_output/");
		else{
			for(String f: files){
				try {
					Files.move(Paths.get("executions/execution_"+execution+"/IaaS_1/"+f), 
							Paths.get(file.getAbsolutePath()+"/"+f),
							REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
