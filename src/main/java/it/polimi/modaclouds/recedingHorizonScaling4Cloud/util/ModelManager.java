/**
 * Copyright (C) 2014 Politecnico di Milano (michele.guerriero@mail.polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This product currently only contains code developed by authors
 * of specific components, as identified by the source code files.
 *
 * Since product implements StAX API, it has dependencies to StAX API
 * classes.
 *
 * For additional credits (generally to people who reported problems)
 * see CREDITS file.
 */
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import it.polimi.modaclouds.qos_models.schema.ApplicationTier;
import it.polimi.modaclouds.qos_models.schema.Container;
import it.polimi.modaclouds.qos_models.schema.Containers;
import it.polimi.modaclouds.qos_models.schema.Functionality;
import it.polimi.modaclouds.qos_models.schema.Instance;
import it.polimi.modaclouds.qos_models.schema.WorkloadForecast;
import it.polimi.modaclouds.qos_models.util.XMLHelper;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.CloudMLReturnedModelException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelManager {
	private static final Logger journal = LoggerFactory.getLogger(ModelManager.class);
	private static Containers model;
	private static double defaultDemand;
	private static int currentHour = 0;
	private static int currentTimeStep = 0;

	public static int getOptimizationWindow() {
		return model.getOptimizationWindowsLenght();
	}

	public static Containers getModel() {
		if (model == null)
			loadModel();
		
		return model;
	}

	public static void loadModel() {

		journal.info("Getting the design time model.");

		//if a path to the initial adaptation model is received as argument this is used as a first choise
		if (ConfigManager.PATH_TO_DESIGN_TIME_MODEL != null) {
			
			journal.info("A path to the initial adaptation model has been specified; loading the initial model from it.");
			try {
				File xml = ConfigManager.getPathToFile(ConfigManager.PATH_TO_DESIGN_TIME_MODEL).toFile();
				try (FileInputStream in = new FileInputStream(xml)) {
					model = (Containers) XMLHelper.deserialize(in, Containers.class);
				}
			} catch (Exception e) {
				journal.error("Error while loading the model.", e);
			}
		}
		// if no path to the initial adaptation model is received as argument the default choice is to retrieve it from the object store
		//is no object store parameters are provided default one are used
		else {
			
			journal.info("No path specified for the initial adaptation model; trying to retrieve the model from the object store.");

			CloseableHttpResponse response1 = null;

			try {

				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpGet httpGet = new HttpGet(
						String.format("http://%s:%s%s",
								ConfigManager.OBJECT_STORE_IP,
								ConfigManager.OBJECT_STORE_PORT,
								ConfigManager.OBJECT_STORE_MODEL_PATH));
				httpGet.setHeader("accept", "*/*");
				response1 = httpclient.execute(httpGet);

				journal.info("{}", response1.getStatusLine());
				HttpEntity entity1 = response1.getEntity();

				File targetFile = Paths.get(ConfigManager.getLocalTmp().toString(), "s4cOpsInitialModel.xml").toFile();
				OutputStream outStream = new FileOutputStream(targetFile);

				byte[] buffer = new byte[8 * 1024];
				int bytesRead;
				while ((bytesRead = entity1.getContent().read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
				outStream.close();

				try (FileInputStream in = new FileInputStream(targetFile)) {
					model = (Containers) XMLHelper.deserialize(in, Containers.class);
				}
			} catch (ClientProtocolException e) {
				journal.error("Error in the client protocol.", e);
			} catch (Exception e) {
				journal.error("Error while accessing the file.", e);
			} finally {
				try {
					response1.close();
				} catch (IOException e) {
					journal.error("Error while closing the response object.", e);
				}
			}

		}

		// setting the class indexes
		journal.info(
				"Setting the class indexes for application tiers.");

		for (Container c : model.getContainer()) {
			int index = 1;
			for (ApplicationTier t : c.getApplicationTier()) {
				t.setClassIndex(index);
				index++;
			}
		}

		// check if default demands value have to be used
		defaultDemand = Double.parseDouble(ConfigManager.DEFAULT_DEMAND);

		// add runtime deployment model information
		{ // TODO: Enable me!
//		journal.info("Initializing cloudml connection.");
//		CloudMLAdapter cloudml = new CloudMLAdapter();
//
//		journal.info("Asking CloudML for the current deployment model");
//		cloudml.getDeploymentModel();
		}
	}

	public static String printCurrentModel() {
		try {
			File currentModel = Paths.get(ConfigManager.getLocalTmp().toString(), "model_" + currentTimeStep + ".xml")
					.toFile();
			OutputStream out = new FileOutputStream(currentModel);
			ByteArrayOutputStream outString = new ByteArrayOutputStream();
			
			final String schemaLocation = "http://www.modaclouds.eu/xsd/2015/9/s4c_ops https://raw.githubusercontent.com/deib-polimi/modaclouds-qos-models/master/metamodels/s4cextension/s4c_ops.xsd";
			
			XMLHelper.serialize(model, out, schemaLocation);
			
			XMLHelper.serialize(model, outString, schemaLocation);
			String res = outString.toString();
			
			journal.trace(res);
			return res;
		} catch (JAXBException e) {
			journal.error("Error while writing the XML file.", e);
		} catch (FileNotFoundException e) {
			journal.error("File not found!", e);
		}

		return null;
	}

	public static void updateServiceDemand(String monitoredResource,
			Double monitoredValue) {

		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				for (Functionality f : t.getFunctionality()) {
					if (monitoredResource.contains(f.getId())) {
						f.setDemand(monitoredValue);
					}
				}
			}
		}
	}

	public static void updateServiceWorkloadPrediction(
			String monitoredResource, String monitoredMetric,
			Double monitoredValue) {

		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				for (Functionality f : t.getFunctionality()) {
					if (monitoredResource.contains(f.getId())) {

						WorkloadForecast toUpdate = null;
						for (WorkloadForecast wf : f.getWorkloadForecast()) {
							if (wf.getTimeStepAhead() == Integer
									.parseInt(monitoredMetric
											.substring(monitoredMetric.length() - 1))) {
								toUpdate = wf;
							}
						}
						if (toUpdate != null) {
							toUpdate.setValue(monitoredValue);
						} else {
							toUpdate = new WorkloadForecast();
							toUpdate.setTimeStepAhead(Integer.parseInt(monitoredMetric
									.substring(monitoredMetric.length() - 1)));
							toUpdate.setValue(monitoredValue);
							f.getWorkloadForecast().add(toUpdate);
						}
					}
				}
			}
		}

	}

	public static String getNewestRunningInstance(String tierId) {

		List<Date> startTimes = new ArrayList<Date>();
		Instance toReturn = null;

		ApplicationTier tier = getTier(tierId);

		for (Instance i : tier.getInstances()) {
			if (i.getStatus().equals("RUNNING")) {
				startTimes
						.add(i.getStartTime().toGregorianCalendar().getTime());
			}
		}

		Calendar max = Calendar.getInstance();

		max.setTime(Collections.max(startTimes));

		Calendar temp = Calendar.getInstance();
		for (Instance i : tier.getInstances()) {
			if (i.getStatus().equals("RUNNING")) {
				temp.setTime(i.getStartTime().toGregorianCalendar().getTime());
				if (max.equals(temp)) {
					toReturn = i;
				}
			}
		}

		return toReturn.getId();

	}

	public static String getOldestRunningInstance(String tierId) {
		List<Date> startTimes = new ArrayList<Date>();
		Instance toReturn = null;

		ApplicationTier tier = getTier(tierId);

		for (Instance i : tier.getInstances()) {
			if (i.getStatus().equals("RUNNING")) {
				startTimes
						.add(i.getStartTime().toGregorianCalendar().getTime());
			}
		}

		Calendar min = Calendar.getInstance();
		min.setTime(Collections.max(startTimes));

		Calendar temp = Calendar.getInstance();
		for (Instance i : tier.getInstances()) {
			temp.setTime(i.getStartTime().toGregorianCalendar().getTime());
			if (min.equals(temp)) {
				toReturn = i;
			}
		}

		return toReturn.getId();

	}
	
	public static void main(String[] args) throws Exception {
		ConfigManager.loadConfiguration();
		ConfigManager.PATH_TO_DESIGN_TIME_MODEL = Paths.get("/Users/ft/Desktop/autoscalingReasoner/httpAgentInitialModel.xml").toString();
		ModelManager.loadModel();
		
		String body = new String(Files.readAllBytes(Paths.get("/Users/ft/Desktop/autoscalingReasoner/model.json")));
		
		try {
			JSONObject jsonObject = new JSONObject(body.substring(27));
			JSONArray instances=jsonObject.getJSONArray("vmInstances");
			ModelManager.updateDeploymentInfo(instances);
			
			journal.debug("{}", getWorkloadPrediction("HTTPAgent", 1)*model.getTimestepDuration()*1000);
			journal.debug("{}", getWorkloadPrediction("HTTPAgent", 2)*model.getTimestepDuration()*1000);
			journal.debug("{}", getWorkloadPrediction("HTTPAgent", 3)*model.getTimestepDuration()*1000);
			journal.debug("{}", getWorkloadPrediction("HTTPAgent", 4)*model.getTimestepDuration()*1000);
			journal.debug("{}", getWorkloadPrediction("HTTPAgent", 5)*model.getTimestepDuration()*1000);
			
			ModelManager.printCurrentModel();
		} catch (JSONException | TierNotFoudException | CloudMLReturnedModelException e) {
			journal.error("Error while parsing the deployment info returned by CloudML.", e);
		}
	}

	public static void updateDeploymentInfo(JSONArray instances)
			throws JSONException, TierNotFoudException,
			CloudMLReturnedModelException {

		for (int i = 0; i < instances.length(); i++) {
			JSONObject instance = instances.getJSONObject(i);
			String instanceId = instance.optString("id");
			String instanceType = instance.optString("type");
			String instanceStatus = instance.optString("status");
			
			if (instanceId.length() == 0)
				continue;

			// if cloudml returns valid information (no field can be null)
			if (!instanceId.equals("null") && !instanceType.equals("null")
					&& !instanceStatus.equals("null")) {

				instanceType = getTierIdFromInstanceType(instanceType);
				
				journal.trace("{ id: {}, tier: {}, status: {} }", instanceId, instanceType, instanceStatus);

				if (getInstance(instanceId) != null) {
					String tierId = getHostedTier(instanceId).getId();

					if (instanceType.equals(tierId)) {
						// CHECK IF THE STATUS OF THE INSTANCE AS REPORTED BY
						// CLOUDML CORRESPONDS TO THE INTERNAL STORED STATUS
						if (!instanceStatus.equals(getInstance(instanceId)
								.getStatus())) {
							// RAISE ERROR SINCE INSTANCE STATUS AS REPORTED BY
							// CLOUDML
							// DOES NOT CORRESPOND TO THE INTERNALLY STORED
							// STATUS
							throw new CloudMLReturnedModelException(
									"CloudML reported status does not correspond "
											+ "to the internally stored status for instance: "
											+ instanceId);
						}
					} else {
						// RAISE ERROR SINCE THE VM INSTANCE TYPE HAS REPORTED
						// FROM CLOUDML IS NOT EQUAL TO
						// THE TIER ID TO WHICH THE INSTANCE BELONGS TO WITHIN
						// THE INTERNAL MODEL
						throw new CloudMLReturnedModelException(
								"CloudML reported instance type does not correspond "
										+ "to the internally stored instance type for instance: "
										+ instanceId);
					}
				} else {
					// THE INSTANCE IS NOT ASSOCIATED TO ANY MANAGED APPLICATION
					// TIER WITHIN THE INTERNAL MODEL AND NEEDS TO BE ADDED
					addInstance(instanceId, instanceType, instanceStatus);
				}
			} else {
				// RAISE ERROR SINCE A VM INSTANCE FROM CLOUDML RESPONSE HAS A
				// NULL ID OR A NULL TYPE OR A NULL STATUS
				throw new CloudMLReturnedModelException(
						"CloudML reported instance with null type or status or Id for instance: "
								+ instanceId);
			}

		}
		
		ModelManager.printCurrentModel();
	}

	private static String getTierIdFromInstanceType(String instanceType)
			throws CloudMLReturnedModelException {
		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				if (instanceType.contains(t.getId())) {
					return t.getId();
				}
			}
		}

		throw new CloudMLReturnedModelException(
				"CloudML returned an instance type which not correspond to any internally managed application tier id");
	}

	public static Instance getInstance(String instanceId) {
		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				for (Instance i : t.getInstances()) {
					if (i.getId().equals(instanceId)) {
						return i;
					}
				}
			}
		}

		return null;
	}

	public static ApplicationTier getTier(String tierId) {
		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				if (t.getId().equals(tierId)) {
					return t;
				}
			}
		}

		return null;
	}

	public static Container getContainerByTierId(String tierId) {
		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				if (t.getId().equals(tierId)) {
					return c;
				}
			}
		}

		return null;
	}

	public Container getContainerById(String containerId) {
		for (Container c : model.getContainer()) {
			if (c.getId().equals(containerId)) {
				return c;
			}
		}

		return null;
	}

	public static List<String> getExpiringInstances(String tierId, int lookAhead) {
		List<String> toReturn = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();

		ApplicationTier tier = getTier(tierId);

		cal.add(Calendar.MINUTE,
				-(60 - model.getTimestepDuration() * lookAhead));

		for (Instance instance : tier.getInstances()) {
			// if the 'instance' is not stopped
			if (instance.getStatus().equals("RUNNING")) {
				// and if the instance will be automatically recharged within
				// the next 'lookAhead' timesteps
				if (instance.getStartTime().toGregorianCalendar().getTime()
						.before(cal.getTime())) {
					toReturn.add(instance.getId());
				}
			}
		}

		return toReturn;
	}

	public static List<String> getAvailableInstances(String tierId,
			int lookAhead) {
		List<String> toReturn = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();

		ApplicationTier tier = getTier(tierId);

		cal.add(Calendar.MINUTE,
				-(60 - model.getTimestepDuration() * lookAhead));

		for (Instance instance : tier.getInstances()) {
			// if the 'instance' is not stopped
			if (instance.getStatus().equals("RUNNING")) {
				// and if the instance will be automatically recharged within
				// the next 'lookAhead' timesteps
				if (instance.getStartTime().toGregorianCalendar().getTime()
						.after(cal.getTime())) {
					toReturn.add(instance.getId());
				}
			}
		}

		return toReturn;
	}

	public static List<String> getRunningInstances(String tierId) {
		List<String> toReturn = new ArrayList<String>();

		ApplicationTier tier = getTier(tierId);

		for (Instance instance : tier.getInstances()) {
			if (instance.getStatus().equals("RUNNING")) {
				toReturn.add(instance.getId());

			}
		}

		return toReturn;
	}

	public static List<String> getStoppedInstances(String tierId) {
		List<String> toReturn = new ArrayList<String>();

		ApplicationTier tier = getTier(tierId);

		for (Instance instance : tier.getInstances()) {
			if (instance.getStatus().equals("STOPPED")) {
				toReturn.add(instance.getId());

			}
		}

		return toReturn;
	}

	public static int getTimestepDuration() {
		return model.getTimestepDuration();
	}

	public static void stopInstances(List<String> instances)
			throws TierNotFoudException {

		CloudMLAdapter cloudml = new CloudMLAdapter();
		String toSend = "";

		for (String instanceId : instances) {

			if (toSend.equals("")) {
				toSend += instanceId;
			} else {
				toSend += "," + instanceId;
			}
		}

		cloudml.stopInstances(toSend);
		for (String instanceId : instances) {

			Instance stopped = getInstance(instanceId);
			stopped.setStatus("STOPPED");
			stopped.setStartTime(null);

			journal.info("Instance {} successfully stopped", instanceId);

		}
	}

	public static void startInstance(List<String> instances)
			throws TierNotFoudException {

		CloudMLAdapter cloudml = new CloudMLAdapter();
		String toSend = "";

		for (String instanceId : instances) {

			if (toSend.equals("")) {
				toSend += instanceId;
			} else {
				toSend += "," + instanceId;
			}
		}

		cloudml.startInstances(toSend);

		for (String instanceId : instances) {

			Instance started = getInstance(instanceId);
			started.setStatus("RUNNING");
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date());
			XMLGregorianCalendar date2;
			try {
				date2 = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(c);
				started.setStartTime(date2);
			} catch (DatatypeConfigurationException e) {
				journal.error("Error while dealing with the datatype.", e);
			}
			journal.info("Instance {} successfully restarted", instanceId);
		}

	}

	public static void renewRunningInstance(String toRenew) {
		Instance instance = getInstance(toRenew);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar date2;
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			instance.setStartTime(date2);
		} catch (DatatypeConfigurationException e) {
			journal.error("Error while dealing with the datatype.", e);
		}
		journal.info("Instance {} successfully restarted", toRenew);
	}

	public static void addInstance(String instanceId, String tierId,
			String status) throws TierNotFoudException {
		Instance toAdd = new Instance();
		toAdd.setId(instanceId);
		toAdd.setStatus(status);
		toAdd.setUsedForScale(false);

		if (status.equals("RUNNING")) {
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date());
			XMLGregorianCalendar date2;
			try {
				date2 = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(c);
				toAdd.setStartTime(date2);
			} catch (DatatypeConfigurationException e) {
				journal.error("Error while dealing with the datatype.", e);
			}
		} else {
			toAdd.setStartTime(null);
		}

		getTier(tierId).getInstances().add(toAdd);
	}

	public static ApplicationTier getHostedTier(String instanceId) {
		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				for (Instance i : t.getInstances()) {
					if (i.getId().equals(instanceId)) {
						return t;
					}
				}
			}
		}

		return null;
	}

	public static ApplicationTier getContainingTier(String functionalityId) {
		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {
				for (Functionality f : t.getFunctionality()) {
					if (f.getId().equals(functionalityId)) {
						return t;
					}
				}
			}
		}

		return null;
	}

	public static List<String> getExposedFunctionalities(String tierId) {
		List<String> toReturn = new ArrayList<String>();

		for (Functionality f : getTier(tierId).getFunctionality()) {
			toReturn.add(f.getId());
		}

		return toReturn;
	}

	public static List<String> getHostingInstances(String tierId) {

		List<String> toReturn = new ArrayList<String>();

		for (Instance i : getTier(tierId).getInstances()) {
			toReturn.add(i.getId());
		}

		return toReturn;
	}

	public static double getDemand(String tierId) {
		ApplicationTier tier = getTier(tierId);

		if (defaultDemand == 0) {
			double toReturn = 0;
			for (Functionality f : tier.getFunctionality()) {
				if (f.getDemand() != null) {
					toReturn = toReturn + f.getDemand();
				}
			}
			tier.setDemand(toReturn);
			return toReturn;
		} else {
			return defaultDemand;
		}

	}

	public static double getWorkloadPrediction(String tierId, int lookAhead) {

		ApplicationTier tier = getTier(tierId);
		double toReturn = 0;

		
		
		for (Functionality f : tier.getFunctionality()) {
			for (WorkloadForecast wf : f.getWorkloadForecast()) {
				if (wf.getTimeStepAhead() == lookAhead) {
					toReturn = toReturn + wf.getValue();
				}
			}
		}
		
		if(toReturn==0){
			
			double sum = 0;
			int count = 0;
			
			for (Functionality f : tier.getFunctionality()) {
				for (WorkloadForecast wf : f.getWorkloadForecast()) {
					if (wf.getTimeStepAhead() != lookAhead & wf.getValue()!=null && !wf.getValue().isNaN()) {
						sum = sum + wf.getValue().doubleValue();
						count++;
					}
				}
			}
			if (count == 0)
				count = 1;
			toReturn=sum/count;
			
		}

		WorkloadForecast toUpdate = null;
		
		for (WorkloadForecast wf : tier.getWorkloadForecast()) {
			if (wf.getTimeStepAhead() == lookAhead) {
				toUpdate = wf;
			}
		}
		
		if (toUpdate != null) {
			toUpdate.setValue(toReturn);
		} else {
			toUpdate = new WorkloadForecast();
			toUpdate.setTimeStepAhead(lookAhead);
			toUpdate.setValue(toReturn);
			tier.getWorkloadForecast().add(toUpdate);
		}

		return toReturn/(model.getTimestepDuration()*1000);
	}

	public static String getInstanceToScale(String tierId) {

		for (Instance i : getTier(tierId).getInstances()) {
			if (i.isUsedForScale()) {
				return i.getId();
			}
		}

		return null;
	}

	public static void setInstanceToScale(String instanceId) {
		getInstance(instanceId).setUsedForScale(true);
	}

	public static void initializeUsedForScale() {

		for (Container c : model.getContainer()) {
			for (ApplicationTier t : c.getApplicationTier()) {

				if (getRunningInstances(t.getId()).size() > 0) {
					String toSet = getNewestRunningInstance(t.getId());
					for (Instance i : t.getInstances()) {
						if (i.getId().equals(toSet)) {
							i.setUsedForScale(true);
						} else {
							i.setUsedForScale(false);
						}
					}
				}

			}
		}
	}

	public static void initializeUsedForScale(String tierId) {

		ApplicationTier t = getTier(tierId);
		String toSet = getNewestRunningInstance(t.getId());
		for (Instance i : t.getInstances()) {
			if (i.getId().equals(toSet)) {
				i.setUsedForScale(true);
			} else {
				i.setUsedForScale(false);
			}
		}

	}

	public static double getDefaultDemand() {
		return defaultDemand;
	}

	public static void setDefaultDemand(double defaultDemand) {
		ModelManager.defaultDemand = defaultDemand;
	}

	public static int getCurrentHour() {
		return currentHour;
	}

	public static void setCurrentHour(int currentHour) {
		ModelManager.currentHour = currentHour;
	}

	public static int getCurrentTimeStep() {
		return currentTimeStep;
	}

	public static void setCurrentTimeStep(int currentTimeStep) {
		ModelManager.currentTimeStep = currentTimeStep;
	}

	public static void increaseCurrentTimeStep() {
		currentTimeStep++;
	}

	public static void increaseCurrentHour() {
		currentHour++;
	}
}