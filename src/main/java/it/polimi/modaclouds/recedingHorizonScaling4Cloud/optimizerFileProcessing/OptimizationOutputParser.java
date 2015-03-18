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

public class OptimizationOutputParser {

	private final static Charset ENCODING = StandardCharsets.UTF_8;


	
	public OptimizationOutputParser(){
		
	}
	
	public int[] parseExecutionOutput(String pathToOutput, int nTier){
		
		BufferedReader br;
		
		int[] toReturn=new int[nTier];
		try {
			br = new BufferedReader(new FileReader(pathToOutput));
			String line;
			int cont=0;
			
			for(int i=0; i< nTier;i++){
				boolean finded=false;
				
				while ((line = br.readLine()) != null && finded==false) {
					if(line.startsWith(String.valueOf(i+1))){
						finded=true;
						System.out.println(line);
						String[] splitted= line.split("\\s+");
						System.out.println(splitted[8]);
						toReturn[i]=Integer.parseInt(splitted[8]);
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
