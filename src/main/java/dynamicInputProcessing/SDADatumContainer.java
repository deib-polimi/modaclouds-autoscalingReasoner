package dynamicInputProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SDADatumContainer {

	private static List<SDADatum> data=new ArrayList<>();
	
	public static void addDatum(String datum){
		
		SDADatum toAdd=parseDatum(datum);
		data.add(toAdd);
	}
	
	public static SDADatum getLastDatum(){
		SDADatum toReturn;
		if(data.size()>0){
			toReturn=data.get(data.size()-1);
			data.remove(data.size()-1);
			return  toReturn;
		}
		else
			return null; //da sostituire con un eccezione?
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
