package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.jar.Attributes.Name;

import javax.naming.spi.DirStateFactory.Result;


public class MILPInputDatParsed {
	
	
	
	private final Path pathToFile;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private final MILPInputDatParser result;
			
	public static void main(String... args) throws IOException {
	    MILPInputDatParsed parser = new MILPInputDatParsed(args[0]);
	    MILPInputDatParser p=parser.parse();
	    System.out.println("Done.");

	    
	}
	

	
	public MILPInputDatParsed(String pathString){
		this.pathToFile=Paths.get(pathString);
		this.result=new MILPInputDatParser();
	}
	
	public MILPInputDatParser parse() throws IOException{
		
		try (Scanner scanner =  new Scanner(this.pathToFile, ENCODING.name())){
			scanner.useDelimiter(";");
			
		     while (scanner.hasNext()){
		       processParameter(scanner.next());
		     }
		   }
		
		return this.result;
		
		
	}
	
	private void processParameter(String aLine){
	    //use a second Scanner to parse the content of each line 
		
		if(aLine.length()>1){
			Scanner scanner = new Scanner(aLine);
		    scanner.useDelimiter(":=");
		    if (scanner.hasNext()){
		      //assumes the line has a certain structure
		      String [] declaration = scanner.next().split(" ");
	
		      String value = scanner.next();
		      
		      this.addParameter(declaration[1], value);
	
		    }
		    else {
		      System.out.println("Empty or invalid line. Unable to process.");
		    }
		}
	  }

	private void addParameter(String paramName, String paramValue){
				
		String[] tokens;
		String [] lines;
		float[][] tempMatrix;
		float[] tempVector;
		float[][][] tempCube;
		
		switch (paramName) {
		case "CONTAINER":
			paramValue=paramValue.substring(1);
			tokens=paramValue.split(" ");
			this.result.setnTier(tokens.length);
			break;
		case "PROVIDER":
			paramValue=paramValue.substring(1);
			tokens=paramValue.split(" ");
			this.result.setnProvider(tokens.length);
			break;
		case "CLASS_REQUEST":
			paramValue=paramValue.substring(1);
			tokens=paramValue.split(" ");
			this.result.setnClass(tokens.length);
			break;
		case "TYPE_VM":
			paramValue=paramValue.substring(1);
			tokens=paramValue.split(" ");
			this.result.setnTypeVM(tokens.length);
			break;
		case "TIME_INT":
			paramValue=paramValue.substring(1);
			tokens=paramValue.split(" ");
			this.result.setnTimeInt(tokens.length);
			break;
		case "COMPONENT":
			paramValue=paramValue.substring(1);
			tokens=paramValue.split(" ");
			this.result.setnComponent(tokens.length);
			break;
			
		case "ProbabilityToBeInComponent":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempMatrix= new float[this.result.getnClass()][this.result.getnComponent()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempMatrix[Integer.parseInt(tokens[0].substring(1))-1][Integer.parseInt(tokens[1].substring(1))-1]=Float.parseFloat(tokens[2]);
			}
			this.result.setProbabilityToBeInComponent(tempMatrix);
					
			break;
			
		case "ArrRate":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempVector= new float[this.result.getnTimeInt()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempVector[Integer.parseInt(tokens[0].substring(1))-1]=Float.parseFloat(tokens[1]);
			}
			this.result.setArrRate(tempVector);
					
			break;
			
		case "MaximumSR":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempMatrix= new float[this.result.getnClass()][this.result.getnComponent()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempMatrix[Integer.parseInt(tokens[0].substring(1))-1][Integer.parseInt(tokens[1].substring(1))-1]=Float.parseFloat(tokens[2]);
			}
			this.result.setMaximumSR(tempMatrix);
			
			break;
			
		case "PartitionComponents":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempMatrix= new float [this.result.getnComponent()][this.result.getnTier()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempMatrix[Integer.parseInt(tokens[0].substring(1))-1][Integer.parseInt(tokens[1].substring(1))-1]=Float.parseFloat(tokens[2]);
			}
			this.result.setComponentAllocation(tempMatrix);
			
			break;
			
		case "Speed":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempCube= new float [this.result.getnTypeVM()][this.result.getnProvider()][this.result.getnTier()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempCube[Integer.parseInt(tokens[0].substring(1))-1][Integer.parseInt(tokens[1].substring(1))-1][Integer.parseInt(tokens[2].substring(1))-1]=Float.parseFloat(tokens[3]);
			}
			this.result.setSpeedVM(tempCube);
			
			break;
			
		case "Cost":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempCube= new float [this.result.getnTypeVM()][this.result.getnProvider()][this.result.getnTier()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempCube[Integer.parseInt(tokens[0].substring(1))-1][Integer.parseInt(tokens[1].substring(1))-1][Integer.parseInt(tokens[2].substring(1))-1]=Float.parseFloat(tokens[3]);
			}
			this.result.setCost(tempCube);
			
			break;
			
		case "MaxResponseTime":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempMatrix= new float[this.result.getnClass()][this.result.getnComponent()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempMatrix[Integer.parseInt(tokens[0].substring(1))-1][Integer.parseInt(tokens[1].substring(1))-1]=Float.parseFloat(tokens[2]);
			}
			this.result.setMaxResponseTime(tempMatrix);
			
			break;
			
		case "MaxVMPerContainer":
			
			paramValue=paramValue.substring(1);

			this.result.setMaxVMPerContainer(Integer.parseInt(paramValue));
						
			break;
			
		case "Alpha":
			
			paramValue=paramValue.substring(1);
			lines=paramValue.split("\\r?\\n");			
			tempVector= new float[this.result.getnClass()];
			
			for(String line: lines){
				tokens=line.split(" ");
				tempVector[Integer.parseInt(tokens[0].substring(1))-1]=Float.parseFloat(tokens[1]);
			}
			this.result.setAlpha(tempVector);
			
			break;
			
		}
		
	}



	/**
	 * @return the pathToFile
	 */
	public Path getPathToFile() {
		return pathToFile;
	}



	/**
	 * @return the result
	 */
	public MILPInputDatParser getResult() {
		return result;
	}
}
