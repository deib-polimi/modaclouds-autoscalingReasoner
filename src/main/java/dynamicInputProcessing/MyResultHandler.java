package dynamicInputProcessing;

import java.util.List;
import java.util.Map;
import it.polimi.modaclouds.monitoring.metrics_observer.ResultsHandler;
import it.polimi.modaclouds.monitoring.metrics_observer.model.Variable;

public class MyResultHandler extends ResultsHandler {

    @Override
    public void getData(List<String> varNames,
            List<Map<String, Variable>> bindings) {
        String value;
        for (Map<String, Variable> m : bindings) {
            String datum = "";
            int last = varNames.size();
            for (int i = 0; i < last; i++) {
                Variable var = m.get(varNames.get(i));
                if (var != null) {
                    value = var.getValue();
                    datum += value + (i == last - 1 ? "" : " ");
                }
            }
            SDADatumContainer.addDatum(datum);
        }
    }

}