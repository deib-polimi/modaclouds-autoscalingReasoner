package dynamicInputProcessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.ApplicationTier;
import model.OptimizerExecution;

import com.hp.hpl.jena.sparql.algebra.optimize.Optimize;

public class DynamicInputWriter {

	public DynamicInputWriter() {
		ExampleObserver observer = new ExampleObserver(8176);
		try {
			observer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// effettuare chiamate per registrare l observer a tutte le metriche
		// necessarie observer
	}

	public void writeDynamicInput(List<OptimizerExecution> executions) {

		SDADataManager sdaManager = SDADataManager.getInstance();

		for (OptimizerExecution ex : executions) {

			System.out.println("Writing mu.dat for execution: "+ex.toString());

			while (!sdaManager.isMetricEmpty("Demand", ex)) {
								
				System.out.println("no data found for mu.dat for execution: "+ex.toString());

				try {
				    Thread.sleep(5000);                 //1000 milliseconds is one second.
				} catch(InterruptedException e) {
				    Thread.currentThread().interrupt();
				}
			}
			
			this.writeFile("mu.dat", ex.toString(), "let mu:=\n"
					+ sdaManager.getLastDatum("Demand", ex).getValue()
					+ "\n;");

		}

	}

	private void writeFile(String fileName, String execution, String fileContent) {

		FileWriter fw = null;
		File file = null;

		try {
			file = new File("executions/execution_" + execution + "/"
					+ fileName);
			file.createNewFile();
			fw = new FileWriter(file);
			fw.append(fileContent);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
