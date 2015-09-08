package it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimizationOutputParser {
	
	private static final Logger journal = LoggerFactory
			.getLogger(OptimizationOutputParser.class);

	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public OptimizationOutputParser(){
		
	}
	
	public int[] parseExecutionOutput(String pathToOutput, int nTier){
		
		int[] toReturn=new int[nTier];
		try (BufferedReader br = new BufferedReader(new FileReader(pathToOutput))) {
			String line;
			int cont=0;
			
			for(int i=0; i< nTier;i++){
				boolean finded=false;
				
				while ((line = br.readLine()) != null && finded==false) {
					if(line.startsWith(String.valueOf(i+1))){
						finded=true;
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
