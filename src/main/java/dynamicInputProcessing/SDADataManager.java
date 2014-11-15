package dynamicInputProcessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import RHS4CloudExceptions.NotEnoughDynamicDataException;

public class SDADataManager {

	private static SDADataManager _instance=null;
	private  List<SDADatum> data=new ArrayList<>();
	
	public static SDADataManager getInstance() {
		if (_instance == null)
			_instance = new SDADataManager();
		return _instance;
	}
	
	public  void addDatum(String datum){
		
		SDADatum toAdd=parseDatum(datum);
		data.add(toAdd);
	}
	
	public  SDADatum getLastDatum(String metric) throws NotEnoughDynamicDataException{
		
		SDADatum toReturn=null;

		if(metric.equals("Demand")){
			toReturn=new SDADatum();
			toReturn.setValue(1);
		}
				
		
		if(data.size()>0){
			
			Iterator  i= data.iterator();
			int mostRecentTimestamp=Integer.MIN_VALUE;
			
			
			while(i.hasNext()){
				SDADatum temp=(SDADatum) i.next();
				if(temp.getMetric().equals(metric) && temp.getTimestamp()> mostRecentTimestamp){
					mostRecentTimestamp=temp.getTimestamp();
					toReturn=temp;
				}				
			}
			
			if(toReturn!= null){
				clearOldestDatum(metric);
				return toReturn;
			}
			else
				throw new NotEnoughDynamicDataException("Not enough dynamic data available yet. They could require some time to be available...");
		
		}
		else
			throw new NotEnoughDynamicDataException("Not enough dynamic data available yet. They could require some time to be available...");
	
	}
	
	private  void clearOldestDatum(String metric){
		
		Iterator  i= data.iterator();
		int oldestTimestamp=Integer.MAX_VALUE;
		SDADatum toRemove=null;
		
		while(i.hasNext()){
			SDADatum temp=(SDADatum) i.next();
			if(temp.getMetric().equals(metric) && temp.getTimestamp()< oldestTimestamp){
				oldestTimestamp=temp.getTimestamp();
				toRemove=temp;
			}				
		}
		
		if(toRemove!=null)
			data.remove(toRemove);
		
	}
	
	private static SDADatum parseDatum(String toParse){
		SDADatum toReturn=new SDADatum();
		String[] parsedDatum=toParse.split(" ");
		for(String s: parsedDatum)
			System.out.println(s);
		
		
		toReturn.setTarget(parsedDatum[0]);
		toReturn.setMetric(parsedDatum[1]);
		toReturn.setValue(Float.parseFloat(parsedDatum[2]));
		toReturn.setClassId(Integer.parseInt(parsedDatum[3]));
		toReturn.setTimestamp(Integer.parseInt(parsedDatum[4]));

		return null;
	}
}
