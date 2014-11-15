package dynamicInputProcessing;

import RHS4CloudExceptions.NotEnoughDynamicDataException;

public class MainExample {

	public static void main(String[] args) {

		while (true) {
			/*
			 * try{ if(SDADataManager.getLastDatum()!=null){
			 * System.out.println("targer= "
			 * +SDADataManager.getLastDatum().getTarget());
			 * System.out.println("metric= "
			 * +SDADataManager.getLastDatum().getMetric());
			 * System.out.println("value= "
			 * +SDADataManager.getLastDatum().getValue());
			 * System.out.println("class= "
			 * +SDADataManager.getLastDatum().getClassId());
			 * System.out.println("timestamp= "
			 * +SDADataManager.getLastDatum().getTimestamp()); } else{
			 * System.out.println("no data available"); } } catch
			 * (NotEnoughDynamicDataException e){ e.printStackTrace(); }
			 */
			sleep();
		}

	}

	private static void sleep() {
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		while (end - start < 5000) {
			end = System.currentTimeMillis();
		}
	}
}
