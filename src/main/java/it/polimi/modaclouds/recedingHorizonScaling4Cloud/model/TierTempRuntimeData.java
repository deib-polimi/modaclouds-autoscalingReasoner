package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierTempRuntimeData {
	

	private boolean ready=false;
	private int nFunct;
	private List<Float> demands;
	private Map<Integer,List<Float>> functWorkloadForecast;
	
	public TierTempRuntimeData(int nFunct, int adaptationHorizon){
		this.nFunct=nFunct;
		this.demands=new ArrayList<Float>();
		float[] f=new float[4];
		this.functWorkloadForecast=new HashMap<Integer, List<Float>>();
		
		for(int i=1; i<=adaptationHorizon;i++){
			this.functWorkloadForecast.put(Integer.valueOf(i), new ArrayList<Float>());
		}
	}
	
	public boolean getReady(){
		return this.ready;
	}
	
	public void addDemandValue(Float value){
		this.demands.add(value);
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
	}
	
	public void addWorkloadForecastValue(Float value, int timestepPrediction){

		this.functWorkloadForecast.get(Integer.valueOf(timestepPrediction)).add(value);
		
		if(this.functWorkloadForecast.get(Integer.valueOf(timestepPrediction)).size()==this.nFunct){
			boolean workloadReady=true;

			for(Integer key:this.functWorkloadForecast.keySet()){

				if(key.intValue()!=timestepPrediction){
					if(this.functWorkloadForecast.get(key).size()!=this.nFunct)
						workloadReady=false;
					break;
				}
			}
			
			if(workloadReady==true){
				if(this.demands.size()==this.nFunct){
					this.ready=true;
				}

			}
		}
	}

	public List<Float> getDemands() {
		return demands;
	}

	public void setDemands(List<Float> demands) {
		this.demands = demands;
	}

	public Map<Integer, List<Float>> getFunctWorkloadForecast() {
		return functWorkloadForecast;
	}

	public void setFunctWorkloadForecast(Map<Integer, List<Float>> functWorkloadForecast) {
		this.functWorkloadForecast = functWorkloadForecast;
	}
	
	public float getTierCurrentDemand(){
		float toReturn=0;
		
		for(Float f: this.demands){
			toReturn=toReturn+f.floatValue();
		}
		
		return toReturn/this.demands.size();
	}
	
	public float[] getTierCurrentWorkloadPredictions(){
		
		float[] toReturn= new float[this.functWorkloadForecast.keySet().size()];
		
		for(Integer i:this.functWorkloadForecast.keySet()){
			float temp=0;

			for(Float f: this.functWorkloadForecast.get(i)){
				temp=temp+f.floatValue();
			}
			

			toReturn[i.intValue()-1]=temp/this.functWorkloadForecast.get(i).size();
		}
		
		return toReturn;
	}

}
