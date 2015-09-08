package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizationInputWriter {

	private static final Logger journal = LoggerFactory
			.getLogger(OptimizationInputWriter.class);
	
	public OptimizationInputWriter() {	
	}
	
	public void writeDynamicInput(Container toAdapt){
		
		for(ApplicationTier t:toAdapt.getApplicationTier()){
			int index=t.getClassIndex();
			
			//write service rate file
			double tierDemand=ModelManager.getDemand(t.getId());
			
			if(tierDemand!=0){
				writeFile("mu.dat", toAdapt.getId(), "let mu["+index+"]:=\n"+(1/tierDemand)+"\n;");
			}else{
				writeFile("mu.dat", toAdapt.getId(), "let mu["+index+"]:=\n"+0+"\n;");
			}
			
			//write delay file 
			writeFile("Delay.dat", toAdapt.getId(), "let D["+index+"]:=\n"+t.getDelay()+"\n;");

			//write workload predictions file
			for(int i=1; i<=ModelManager.getOptimizationWindow(); i++){
				writeFile("workload_class"+index+".dat", toAdapt.getId(), "let Lambda["+index+","+i+"]:=\n"+ModelManager.getWorkloadPrediction(t.getId(), i)+"\n;");
			}
			
			//write response time threshold file
			writeFile("Rcross.dat", toAdapt.getId(), "let Rcross["+index+"]:=\n"+Double.toString(t.getResponseTimeThreshold().get(ModelManager.getCurrentHour()-1).getValue())+"\n;");

			
			//write file with number of intial VM
			for(int j=1; j<=ModelManager.getOptimizationWindow();j++)
				writeFile("initialVM.dat", toAdapt.getId(), "let Nond["+index+","+(1+j)+"]:=\n"+ModelManager.getAvailableInstances(t.getId(), j).size()+"\n;");
		}	
		
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

					
		}
	}

	private void writeFile(String fileName, String execution,
			String fileContent) {
		File file = null;
		
		file = new File("executions/execution_"+execution+"/IaaS_1/" + fileName);

		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
		    out.println(fileContent);
		}catch (IOException e) {
			journal.error("Error while writing to the file.", e);
		}
	}
	
	public static void deleteFile(String fileName, String execution){
		try {
			Files.deleteIfExists(Paths.get("executions/execution_"+execution+"/IaaS_1/" + fileName));
		} catch (IOException e1) {
			journal.error("Error while deleting the file.", e1);
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
					journal.error("Error while moving the file.", e);
				}
			}
		}
	}
	
	
	
}
