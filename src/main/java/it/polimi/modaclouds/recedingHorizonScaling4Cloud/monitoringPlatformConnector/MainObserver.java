package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
public class MainObserver {
	private static final Logger journal = LoggerFactory
			.getLogger(MainObserver.class);
	@POST
    @Path("/data")
    public Response receiveData(InputStream incomingData) {
		
		journal.info("New monitoring data received");

        StringBuilder dataBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                dataBuilder.append(line);
            }
        } catch (Exception e) {
            journal.error("Error Parsing: - ", e);
        }
        

		try {

			JSONArray data=new JSONArray(dataBuilder.toString());
			
			for (int i=0; i<data.length(); i++) {

				JSONObject datum=data.getJSONObject(i);
				
				String resource = datum.optString("resourceId");
				String metric = datum.optString("metric");
				double value = datum.optDouble("value", 0.0);

				journal.trace("{ resource: {}, metric: {}, value: {} }", resource, metric, value);
				
				if (metric.equals("EstimatedDemand")) {
					ModelManager.updateServiceDemand(resource, value);
				} else if (metric.contains("ForecastedWorkload")) {
					ModelManager.updateServiceWorkloadPrediction(resource, metric, value);
				}
	        }
		} catch (JSONException e) {
			journal.error("Error while parsing the returned data from the request.", e);
		}
 
        return Response.status(204).build();
    }

    public static void startServer(String port) {

        final ResourceConfig rc = new ResourceConfig().packages(MainObserver.class.getPackage().getName());
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:"+port), rc, false);
        NetworkListener listener = httpServer.getListeners().iterator().next(); 
        ThreadPoolConfig thx=listener.getTransport().getWorkerThreadPoolConfig(); 
        thx.setQueueLimit(500);
        thx.setMaxPoolSize(500);
        
		try {
			httpServer.start();
		} catch (IOException e) {
			journal.error("Error while starting the HTTP server.", e);
		}
    }

    public static void main(String[] args) throws IOException {
    	//(String port = (args.length > 0) ? args[0] : "8001";
        //startServer(port);
    	String test="["+
					    "{"+
					        "\"metric\": \"ForecastedWorkload2\","+
					        "\"resourceId\": \"saveAnswers_-571442537\","+
					        "\"timestamp\": 1440757854169,"+
					        "\"value\": 62.737677450401058"+
					    "}"+
					"]";
    	InputStream stream = new ByteArrayInputStream(test.getBytes(StandardCharsets.UTF_8));
    	MainObserver obs=new MainObserver();
    	obs.receiveData(stream);
    }
}