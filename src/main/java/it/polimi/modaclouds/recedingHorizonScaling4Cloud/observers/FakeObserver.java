package it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Containers;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FakeObserver implements Runnable {
	
	public Containers model;
	public int optimizationHorizon;
	
	public void run() throws NumberFormatException{
		
		Scanner input;

		List<Float> method1Demand=new ArrayList<Float>();
		List<Float> method2Demand=new ArrayList<Float>();
		List<Float> method3Demand=new ArrayList<Float>();
		Map<Integer,List<Float>> method1WorkloadForecast=new HashMap<Integer,List<Float>>();
		Map<Integer,List<Float>> method2WorkloadForecast=new HashMap<Integer,List<Float>>();
		Map<Integer,List<Float>> method3WorkloadForecast=new HashMap<Integer,List<Float>>();
				
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method1/demand.out"));
			
			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   method1Demand.add(value);

			   
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method1/forecasted_WL_1.out"));
			List<Float> toAdd=new ArrayList<Float>();
			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);
			}
			
			method1WorkloadForecast.put(new Integer(1), toAdd);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method1/forecasted_WL_2.out"));
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method1WorkloadForecast.put(new Integer(2), toAdd);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method1/forecasted_WL_3.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method1WorkloadForecast.put(new Integer(3), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method1/forecasted_WL_4.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method1WorkloadForecast.put(new Integer(4), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method1/forecasted_WL_5.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method1WorkloadForecast.put(new Integer(5), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method2/demand.out"));
			
			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   method2Demand.add(value);
			  
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method2/forecasted_WL_1.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method2WorkloadForecast.put(new Integer(1), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method2/forecasted_WL_2.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method2WorkloadForecast.put(new Integer(2), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method2/forecasted_WL_3.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method2WorkloadForecast.put(new Integer(3), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method2/forecasted_WL_4.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method2WorkloadForecast.put(new Integer(4), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method2/forecasted_WL_5.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method2WorkloadForecast.put(new Integer(5), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				
		
		
		///////////////////////////////////////////////////////////////////////////////////////////
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method3/demand.out"));
			
			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   method3Demand.add(value);
			  
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method3/forecasted_WL_1.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method3WorkloadForecast.put(new Integer(1), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method3/forecasted_WL_2.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method3WorkloadForecast.put(new Integer(2), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method3/forecasted_WL_3.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method3WorkloadForecast.put(new Integer(3), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method3/forecasted_WL_4.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method3WorkloadForecast.put(new Integer(4), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			input = new Scanner(new File("resource/observe_ramp_data/method3/forecasted_WL_5.out"));
			
			List<Float> toAdd=new ArrayList<Float>();

			while(input.hasNextLine())
			{
			   String line =input.nextLine();
			   Float value=Float.parseFloat(line.split(",")[4]);
			   toAdd.add(value);

			}
			method3WorkloadForecast.put(new Integer(5), toAdd);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		int cont=1;
		
		try {
			
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		while(cont<=34){
			
			
			
			ModelManager.updateDemand("MIC", method1Demand.get(cont), "register");
			ModelManager.updateDemand("MIC", method2Demand.get(cont), "saveAnswers ");
			ModelManager.updateDemand("MIC", method3Demand.get(cont), "answerQuestions");
			
			ModelManager.updateWorkloadPrediction("MIC", "1", method1WorkloadForecast.get(1).get(cont), "register");
			ModelManager.updateWorkloadPrediction("MIC", "1", method2WorkloadForecast.get(1).get(cont), "saveAnswers");
			ModelManager.updateWorkloadPrediction("MIC", "1", method3WorkloadForecast.get(1).get(cont), "answerQuestions");

			ModelManager.updateWorkloadPrediction("MIC", "2", method1WorkloadForecast.get(2).get(cont), "register");
			ModelManager.updateWorkloadPrediction("MIC", "2", method2WorkloadForecast.get(2).get(cont), "saveAnswers");
			ModelManager.updateWorkloadPrediction("MIC", "2", method3WorkloadForecast.get(2).get(cont), "answerQuestions");
			
			ModelManager.updateWorkloadPrediction("MIC", "3", method1WorkloadForecast.get(3).get(cont), "register");
			ModelManager.updateWorkloadPrediction("MIC", "3", method2WorkloadForecast.get(3).get(cont), "saveAnswers");
			ModelManager.updateWorkloadPrediction("MIC", "3", method3WorkloadForecast.get(3).get(cont), "answerQuestions");
			
			ModelManager.updateWorkloadPrediction("MIC", "4", method1WorkloadForecast.get(4).get(cont), "register");
			ModelManager.updateWorkloadPrediction("MIC", "4", method2WorkloadForecast.get(4).get(cont), "saveAnswers");
			ModelManager.updateWorkloadPrediction("MIC", "4", method3WorkloadForecast.get(4).get(cont), "answerQuestions");
			
			ModelManager.updateWorkloadPrediction("MIC", "5", method1WorkloadForecast.get(5).get(cont), "register");
			ModelManager.updateWorkloadPrediction("MIC", "5", method2WorkloadForecast.get(5).get(cont), "saveAnswers");
			ModelManager.updateWorkloadPrediction("MIC", "5", method3WorkloadForecast.get(5).get(cont), "answerQuestions");
			
			cont++;
			
			try {
				System.out.println("observer va in waiting");
				Thread.sleep(360000);
			} catch (InterruptedException e) {
				System.out.println("INTERRUPT EXCEPTION");
				e.printStackTrace();
			}
		}
		

				
				
		
		
	}


}
