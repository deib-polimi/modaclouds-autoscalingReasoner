package dynamicInputProcessing;

public class MainExample {

	public static void main(String[] args) {
		ExampleObserver observer = new ExampleObserver(8123);
		try {
		    observer.start();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	
	
	while(true){
		if(SDADatumContainer.getLastDatum()!=null){
		System.out.println("targer= "+SDADatumContainer.getLastDatum().getTarget());
		System.out.println("metric= "+SDADatumContainer.getLastDatum().getMetric());
		System.out.println("value= "+SDADatumContainer.getLastDatum().getValue());
		System.out.println("class= "+SDADatumContainer.getLastDatum().getClassId());
		System.out.println("timestamp= "+SDADatumContainer.getLastDatum().getTimestamp());
		}
		else{
			System.out.println("no data available");
		}
		sleep();
	}

	}
	
	private static void sleep(){
		long start= System.currentTimeMillis();
		long end=System.currentTimeMillis();
		while(end-start<5000){
			end=System.currentTimeMillis();

		}
	}
}
