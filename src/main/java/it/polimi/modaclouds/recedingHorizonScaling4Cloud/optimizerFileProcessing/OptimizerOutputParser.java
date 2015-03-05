package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class OptimizerOutputParser {

	private final static Charset ENCODING = StandardCharsets.UTF_8;

	
	public static void main(String[] args) {
		
		OptimizerOutputParser p= new OptimizerOutputParser();
		p.parseExecutionOutput("executions/execution_model.OptimizationExecution@7049cca1/IaaS_1/output.out", null);
	}
	
	
	public OptimizerOutputParser(){
		
	}
	
	public int[] parseExecutionOutput(String pathToOutput, Container toUpdate){
		
		BufferedReader br;
		
		int[] toReturn=new int[toUpdate.getApplicationTier().size()];
		try {
			br = new BufferedReader(new FileReader(pathToOutput));
			String line;
			int cont=0;
			
			for(int i=1; i<= toUpdate.getApplicationTier().size();i++){
				boolean finded=false;
				
				while ((line = br.readLine()) != null && finded==false) {
					if(line.startsWith(String.valueOf(i))){
						finded=true;
						System.out.println(line);
						String[] splitted= line.split("\\s+");
						System.out.println(splitted[8]);
						String tierId;
						toReturn[i]=Integer.parseInt(splitted[8]);

						//toUpdate.updateExecutionResult(i, Integer.parseInt(splitted[8]));
					}
					
				}
			}
			
			br.close();
			
			return toReturn;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}
