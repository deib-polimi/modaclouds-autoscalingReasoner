package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

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
	
	public static final String STATIC_INPUT_FILE_NAME = "staticInput";
	public static final String DYNAMIC_INPUT_FILE_NAME = "dynamicInput";
	
	public void writeDynamicInput(Container toAdapt){
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			writeDynamicInputAMPL(toAdapt);
			break;
		case CMPL:
//			writeDynamicInputCMPL(toAdapt);
			journal.error("CMPL not supported at the moment.");
			break;
		}
	}
	
	private void writeDynamicInputAMPL(Container toAdapt){
		
		for(ApplicationTier t:toAdapt.getApplicationTier()){
			int index=t.getClassIndex();
			
			StringBuilder sb = new StringBuilder();
			
			double tierDemand=ModelManager.getDemand(t.getId());
			
			sb.append(String.format(
					"let mu[%d]:=\n%f\n;",
					index,
					tierDemand == 0 ? 0 : (1/tierDemand)));
			
			sb.append(String.format(
					"let D[%d]:=\n%f\n;",
					index,
					t.getDelay()));
			
			for (int i=1; i<=ModelManager.getOptimizationWindow(); i++) {
				sb.append(String.format(
						"let Lambda[%d,%d]:=\n%f\n;",
						index,
						i,
						ModelManager.getWorkloadPrediction(t.getId(), i)));
			}
			
			sb.append(String.format(
					"let Rcross[%d]:=\n%s\n;",
					index,
					Double.toString(t.getResponseTimeThreshold().get(ModelManager.getCurrentHour()-1).getValue())));
			
			for (int j = 1; j <= ModelManager.getOptimizationWindow(); j++)
				sb.append(String.format(
						"let Nond[%d,%d]:=\n%d\n;",
						index,
						(1+j),
						ModelManager.getAvailableInstances(t.getId(), j).size()));

			writeFile(DYNAMIC_INPUT_FILE_NAME + ".dat", toAdapt.getId(), sb.toString());
			
			{ // FIXME: previous version
				
				//write service rate file
				writeFile("mu.dat", toAdapt.getId(),
						String.format("let mu[%d]:=\n%f\n;", index, tierDemand == 0 ? 0 : (1/tierDemand)));
				
				//write delay file 
				writeFile("Delay.dat", toAdapt.getId(),
						String.format("let D[%d]:=\n%f\n;", index, t.getDelay()));
	
				//write workload predictions file
				for(int i=1; i<=ModelManager.getOptimizationWindow(); i++){
					writeFile("workload_class"+index+".dat", toAdapt.getId(),
							String.format("let Lambda[%d,%d]:=\n%f\n;", index, i, ModelManager.getWorkloadPrediction(t.getId(), i)));
				}
				
				//write response time threshold file
				writeFile("Rcross.dat", toAdapt.getId(),
						String.format("let Rcross[%d]:=\n%s\n;", index, Double.toString(t.getResponseTimeThreshold().get(ModelManager.getCurrentHour()-1).getValue())));
	
				
				//write file with number of intial VM
				for(int j=1; j<=ModelManager.getOptimizationWindow();j++)
					writeFile("initialVM.dat", toAdapt.getId(),
							String.format("let Nond[%d,%d]:=\n%d\n;", index, (1+j), ModelManager.getAvailableInstances(t.getId(), j).size()));
			}
		}	
		
	}
	
	public void writeStaticInput(Containers containers){
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			writeStaticInputAMPL(containers);
			break;
		case CMPL:
//			writeStaticInputCMPL(containers);
			journal.error("CMPL not supported at the moment.");
			break;
		}
	}

	private void writeStaticInputAMPL(Containers containers){
		for (Container c : containers.getContainer()) {
			StringBuilder sb = new StringBuilder();
			
			sb.append(String.format(
					"let C:=\n%f\n;",
					c.getCapacity()));
			
			sb.append(String.format(
					"let W:=\n%d\n;",
					c.getMaxReserved()));
			
			sb.append(String.format(
					"let delta:=\n%f\n;",
					c.getOnDemandCost()));
			
			sb.append(String.format(
					"let rho:=\n%f\n;",
					c.getReservedCost()));
			
			writeFile(STATIC_INPUT_FILE_NAME + ".dat", c.getId(), sb.toString());
			
			{ // FIXME: previous version
				
				writeFile("C.dat", c.getId(),
						String.format("let C:=\n%f\n;", c.getCapacity()));
				writeFile("W.dat", c.getId(),
						String.format("let W:=\n%d\n;", c.getMaxReserved()));
				writeFile("delta.dat", c.getId(),
						String.format("let delta:=\n%f\n;", c.getOnDemandCost()));
				writeFile("rho.dat", c.getId(),
						String.format("let rho:=\n%f\n;", c.getReservedCost()));
			}
		}
	}

	private void writeFile(String fileName, String execution,
			String fileContent) {
		File file = Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_"+execution, "IaaS_1", fileName).toFile();
		file.mkdirs();

		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
		    out.println(fileContent);
		    out.flush();
		}catch (IOException e) {
			journal.error("Error while writing to the file.", e);
		}
	}
	
//	public static void deleteFile(String fileName, String execution){
//		try {
//			Files.deleteIfExists(Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_"+execution, "IaaS_1", fileName));
//		} catch (IOException e1) {
//			journal.error("Error while deleting the file.", e1);
//		}
//	}
	
	public static void storeFiles(List<String> files, String execution) throws ProjectFileSystemException{
		File file = null;

		long millis = System.currentTimeMillis();
		
		file = Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_"+execution, "IaaS_1", millis+"_output").toFile();
		boolean success=file.mkdirs();
		
		if(!success)
			throw new ProjectFileSystemException("Error initializing the project file system: the following folder cannot be created: "
					+ "executions/execution_"+execution+"/IaaS_1/"+millis+"_output/");
		else{
			for(String f: files){
				try {
					Files.move(Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_"+execution, "IaaS_1", f), 
							Paths.get(file.getAbsolutePath(), f),
							REPLACE_EXISTING);
				} catch (IOException e) {
					journal.error("Error while moving the file.", e);
				}
			}
		}
	}
	
	
	
}
