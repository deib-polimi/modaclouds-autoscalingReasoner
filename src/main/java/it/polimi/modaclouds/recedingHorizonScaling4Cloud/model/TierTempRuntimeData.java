package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierTempRuntimeData {
	

	private boolean ready=false;
	private int nFunct;
	//private List<Float> demands;
	//private Map<Integer,List<Float>> functWorkloadForecast;
	
	private Map<String, Float> demands;
	private Map<String, Map<Integer, Float>> functWorkloadForecast;
	
	
	public TierTempRuntimeData(List<Functionality> funct, int optimizationWindow){
		/*
		this.nFunct=funct.size();
		this.demands=new ArrayList<Float>();
		this.functWorkloadForecast=new HashMap<Integer, List<Float>>();
		
		for(int i=1; i<=optimizationWindow;i++){
			this.functWorkloadForecast.put(Integer.valueOf(i), new ArrayList<Float>());
		}
		*/
		
		this.demands=new HashMap<String,Float>();
		this.functWorkloadForecast=new HashMap<String, Map<Integer,Float>>();
		for(Functionality f: funct){
			this.demands.put(f.getId(), new Float(0));
			
			this.functWorkloadForecast.put(f.getId(), new HashMap<Integer, Float>());
			
			for(int i=1; i<=optimizationWindow;i++){
				this.functWorkloadForecast.get(f.getId()).put(Integer.valueOf(i), new Float(0));
			}
		}
		

	}
	
	public boolean getReady(){
		return this.ready;
	}
	
	public void refreshBuffers(List<Functionality> funct,int optimizationWindow){
		this.demands=new HashMap<String,Float>();
		this.functWorkloadForecast=new HashMap<String, Map<Integer,Float>>();
		for(Functionality f: funct){
			this.demands.put(f.getId(), new Float(0));
			
			this.functWorkloadForecast.put(f.getId(), new HashMap<Integer, Float>());
			
			for(int i=1; i<=optimizationWindow;i++){
				this.functWorkloadForecast.get(f.getId()).put(Integer.valueOf(i), new Float(0));
			}
		}
		this.ready=false;
	}
	
	
	public void addDemandValue(Float value, String functionality){

		this.demands.put(functionality, value);
		
		boolean demandReady=true;
		
		for(String f:demands.keySet()){
			if(demands.get(f)==0){
				demandReady=false;
			}
		}
		
		if(demandReady){
			boolean workloadReady=true;
			
			for(String f: this.functWorkloadForecast.keySet()){
				Map<Integer, Float> forecasts=functWorkloadForecast.get(f);
				
				for(Integer i: forecasts.keySet()){
					if(forecasts.get(i)==0){
						workloadReady=false;
					}
				}
			}
			
			if(workloadReady){
				this.ready=true;
			}
			
		}
		
		/*
		if(this.demands.size()==this.nFunct){
			boolean workloadReady=true;
			
			for(Integer key:this.functWorkloadForecast.keySet()){
				if(this.functWorkloadForecast.get(key).size()!=this.nFunct){
					workloadReady=false;
					break;
				}
			}

			
			if(workloadReady==true)
				this.ready=true;
		}
		*/
		
		System.out.println("actual demand temporary data");
		
		for(String f:this.demands.keySet()){
			System.out.println(f+":"+this.demands.get(f));
		}
	}
	
	public void addWorkloadForecastValue(Float value, int timestepPrediction, String functionality){

		this.functWorkloadForecast.get(functionality).put(timestepPrediction, value);
		
		boolean workloadReady=true;
		
		for(String f: this.functWorkloadForecast.keySet()){
			Map<Integer, Float> forecasts=functWorkloadForecast.get(f);
			
			for(Integer i: forecasts.keySet()){
				if(forecasts.get(i)==0){
					workloadReady=false;
				}
			}
		}
		
		if(workloadReady){
			boolean demandReady=true;

			
			for(String f:demands.keySet()){
				if(demands.get(f)==0){
					demandReady=false;
				}
			}	
			
			if(demandReady){
				this.ready=true;
			}
		}		
		/*
		this.functWorkloadForecast.get(Integer.valueOf(timestepPrediction)).add(value);
		
		if(this.functWorkloadForecast.get(Integer.valueOf(timestepPrediction)).size()==this.nFunct){
			boolean workloadReady=true;

			for(Integer key:this.functWorkloadForecast.keySet()){

				if(key.intValue()!=timestepPrediction){
					if(this.functWorkloadForecast.get(key).size()!=this.nFunct){
						workloadReady=false;
						break;
					}
				}
			}
			
			if(workloadReady==true){
				if(this.demands.size()==this.nFunct){
					this.ready=true;
				}

			}
		}
		*/
		
		System.out.println("actual workload predition temporary data");
		
		for(String f:this.functWorkloadForecast.keySet()){
			System.out.println("functionality "+f);
			for(Integer i: this.functWorkloadForecast.get(f).keySet()){
				System.out.println("timestep "+i+"="+this.functWorkloadForecast.get(f).get(i));
			}
		}
	}

	public Map<String, Float> getDemands() {
		return demands;
	}

	public void setDemands(Map<String, Float> demands) {
		this.demands = demands;
	}

	public Map<String, Map<Integer, Float>> getFunctWorkloadForecast() {
		return functWorkloadForecast;
	}

	public void setFunctWorkloadForecast(Map<String, Map<Integer, Float>> functWorkloadForecast) {
		this.functWorkloadForecast = functWorkloadForecast;
	}
	
	public float getTierCurrentDemand(){
		float toReturn=0;
		
		
		for(String s: demands.keySet()){
			toReturn=toReturn+demands.get(s);
		}
		
		/*
		for(Float f: this.demands){
			toReturn=toReturn+f.floatValue();
		}
		*/
		
		return toReturn;
	}
	
	public float[] getTierCurrentWorkloadPredictions(){
		
		float[] toReturn= new float[ModelManager.getOptimizationWindow()];
		
		for(int i=0; i<ModelManager.getOptimizationWindow();i++){
			toReturn[i]=0;
			for(String f: this.functWorkloadForecast.keySet()){
				toReturn[i]=toReturn[i]+this.functWorkloadForecast.get(f).get(i+1);
			}
			toReturn[i]=toReturn[i]/(ModelManager.getTimestepDuration()*60);
		}

		/*
		for(Integer i:this.functWorkloadForecast.keySet()){
			float temp=0;

			for(Float f: this.functWorkloadForecast.get(i)){
				temp=temp+f.floatValue();
			}
			

			toReturn[i.intValue()-1]=temp/(ModelManager.getTimestepDuration()*60);
		}
		*/
		
		return toReturn;
	}

	public int getnFunct() {
		return nFunct;
	}

	public void setnFunct(int nFunct) {
		this.nFunct = nFunct;
	}

}
