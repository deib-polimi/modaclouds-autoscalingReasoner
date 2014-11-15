package dynamicInputProcessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.ApplicationTier;
import model.OptimizerExecution;
import RHS4CloudExceptions.NotEnoughDynamicDataException;

import com.hp.hpl.jena.sparql.algebra.optimize.Optimize;

public class DynamicInputWriter {
	
	
	public DynamicInputWriter(){
		ExampleObserver observer = new ExampleObserver(8176);
		try {
		    observer.start();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		//effettuare chiamate per registrare l observer a tutte le metriche necessarie observer
	}
	
	public void writeDynamicInput(List<OptimizerExecution> executions){
		int i=1;
		for (OptimizerExecution ex : executions) {

			try {
				this.writeFile("mu.dat", "Execution "+i, "let mu:=\n"
						+ SDADataManager.getInstance().getLastDatum("Demand").getValue() + "\n;");
			} catch (NotEnoughDynamicDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			i++;
		}
	}
	
	
	private void writeFile(String fileName, String execution,
			String fileContent){
		
		FileWriter fw = null;
		File file = null;
		
		
			try {

				file = new File(execution);
				file.mkdir();
				file = new File(execution + "/" + fileName);
				file.createNewFile();
				fw = new FileWriter(file);
				fw.append(fileContent);
				fw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

}
