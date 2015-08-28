package it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationInitializer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

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

@Path("/")
public class MainObserver {
	private static final Logger journal = Logger
			.getLogger(MainObserver.class.getName());
	@POST
    @Path("/v1/results")
    public Response receiveData(InputStream incomingData) {
		
		journal.log(Level.INFO,"New monitoring data received");

        StringBuilder dataBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                dataBuilder.append(line);
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: - ");
        }
        

		try {
			
			journal.log(Level.INFO,"Reading the received data");

			JSONArray data=new JSONArray(dataBuilder.toString());
			
			for (int i=0; i<data.length(); i++) {

				JSONObject datum=data.getJSONObject(i);
				
				if(datum.get("metric").equals("EstimatedDemand")){
					journal.log(Level.INFO,"Received a datum for metric:"+datum.get("metric"));
					ModelManager.updateServiceDemand(datum.getString("resourceId"),
							datum.getDouble("value"));
				}else if(datum.get("metric").toString().contains("ForecastedWorkload")){
					ModelManager.updateServiceWorkloadPrediction(datum.getString("resourceId"),datum.getString("metric"),
							datum.getDouble("value"));
				}
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			e.printStackTrace();
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