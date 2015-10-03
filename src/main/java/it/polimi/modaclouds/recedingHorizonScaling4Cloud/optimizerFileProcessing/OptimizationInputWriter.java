/**
 * Copyright (C) 2014 Politecnico di Milano (michele.guerriero@mail.polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import it.polimi.modaclouds.qos_models.schema.ApplicationTier;
import it.polimi.modaclouds.qos_models.schema.Container;
import it.polimi.modaclouds.qos_models.schema.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizationInputWriter {

	private static final Logger journal = LoggerFactory
			.getLogger(OptimizationInputWriter.class);
	
	public OptimizationInputWriter() {	
	}
	
	public static final String STATIC_INPUT_FILE_NAME = "staticInput";
	public static final String DYNAMIC_INPUT_FILE_NAME = "dynamicInput";
	
	private static DecimalFormat doubleFormatter() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
//		DecimalFormat myFormatter = new DecimalFormat("0.000#######", otherSymbols);
		DecimalFormat myFormatter = new DecimalFormat("0.000", otherSymbols);
		return myFormatter;
	}
	private static DecimalFormat doubleFormatter = doubleFormatter();
	
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
			
			//write service rate file
			sb.append(String.format(
					"let mu[%d]:=\n%s\n;\n\n",
					index,
					doubleFormatter.format(tierDemand == 0 ? 0 : (1/tierDemand))));
			
			//write delay file
			sb.append(String.format(
					"let D[%d]:=\n%s\n;\n\n",
					index,
					doubleFormatter.format(t.getDelay())));
			
			//write workload predictions file
			for (int i=1; i<=ModelManager.getOptimizationWindow(); i++) {
				sb.append(String.format(
						"let Lambda[%d,%d]:=\n%s\n;\n",
						index,
						i,
						doubleFormatter.format(ModelManager.getWorkloadPrediction(t.getId(), i))));
			}
			
			sb.append("\n");
			
			int currentHour = ModelManager.getCurrentHour();
			if (currentHour <= 0) {
				// TODO: check this with Michele
				journal.warn("This will cause a problem, increased the hour to 1.");
				currentHour = 1;
			}
			
			//write response time threshold file
			sb.append(String.format(
					"let Rcross[%d]:=\n%s\n;\n\n",
					index,
					doubleFormatter.format(t.getResponseTimeThreshold().get(currentHour-1).getValue())));
			
			//write file with number of intial VM
			for (int j = 1; j <= ModelManager.getOptimizationWindow(); j++)
				sb.append(String.format(
						"let Nond[%d,%d]:=\n%d\n;\n",
						index,
						(1+j),
						ModelManager.getAvailableInstances(t.getId(), j).size()));
			
			sb.append("\n");

			writeFile(DYNAMIC_INPUT_FILE_NAME + ".dat", toAdapt.getId(), sb.toString());
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
					"let C:=\n%s\n;\n\n",
					doubleFormatter.format(c.getCapacity())));
			
			sb.append(String.format(
					"let W:=\n%d\n;\n\n",
					c.getMaxReserved()));
			
			sb.append(String.format(
					"let delta:=\n%s\n;\n\n",
					doubleFormatter.format(c.getOnDemandCost())));
			
			sb.append(String.format(
					"let rho:=\n%s\n;\n\n",
					doubleFormatter.format(c.getReservedCost())));
			
			writeFile(STATIC_INPUT_FILE_NAME + ".dat", c.getId(), sb.toString());
		}
	}

	private void writeFile(String fileName, String execution,
			String fileContent) {
		File file = Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_"+execution, "IaaS_1", fileName).toFile();
		file.getParentFile().mkdirs();

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
