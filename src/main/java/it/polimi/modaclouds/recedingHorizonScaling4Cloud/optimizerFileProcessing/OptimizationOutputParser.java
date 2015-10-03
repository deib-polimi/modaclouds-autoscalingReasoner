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


import it.polimi.modaclouds.qos_models.schema.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizationOutputParser {
	
	private static final Logger journal = LoggerFactory
			.getLogger(OptimizationOutputParser.class);

//	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static final String OUTPUT_FILE_NAME = "output";
	
	public OptimizationOutputParser(){
		
	}
	
	public int[] parseExecutionOutput(Container container){
		switch (ConfigManager.MATH_SOLVER) {
		case AMPL:
			return parseExecutionOutputAMPL(container);
		case CMPL:
//			return parseExecutionOutputCMPL(container);
			journal.error("CMPL not supported at the moment.");
		}
		return new int[] {};
	}
	
	private int[] parseExecutionOutputAMPL(Container container) {
		Path output = Paths.get(ConfigManager.getLocalTmp().toString(), "executions", "execution_"+container.getId(), "IaaS_1", OUTPUT_FILE_NAME + ".out");
		int nTier = container.getApplicationTier().size();
		
		int[] toReturn=new int[nTier];
		try (Scanner sc = new Scanner(output)) {
			for(int i=0; i< nTier;i++){
				boolean found = false;
				
				while (sc.hasNextLine() && found == false) {
					String line = sc.nextLine();
					if (line.startsWith(String.valueOf(i+1))){
						found = true;
						String[] splitted= line.split("\\s+");
						toReturn[i]=Integer.parseInt(splitted[8]);
					}
					
				}
			}
			
			return toReturn;
		} catch (IOException e) {
			journal.error("Error while dealing with the file.", e);
		}
		
		return null;
		
	}
}
