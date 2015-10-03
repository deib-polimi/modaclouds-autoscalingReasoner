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
package it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.CloudMLReturnedModelException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientEndpoint
public class WSClient implements Runnable, AutoCloseable {
	private static final Logger journal = LoggerFactory
			.getLogger(WSClient.class);
	
	private String serverURI;
	
	private Session session;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
	
	public WSClient(String ip, int port) {
		this(String.format("ws://%s:%d", ip, port));
	}
	
	public WSClient(String serverURI) {
		this.serverURI = serverURI;
	}
	
	public void open() throws Exception {
		if (session != null)
			journal.info("You're already connected!");
		
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		boolean goOn = true;
		while (goOn) {
			try {
				container.connectToServer(this, new URI(serverURI));
				goOn = false;
			} catch (Exception e) {
				journal.info("No CloudML daemon found in {}, retrying in 10 seconds...", serverURI);
				try {
					Thread.sleep(10000);
				} catch (Exception e1) { }
			}
		}
	}
	
	@OnOpen
	public void onOpen(Session session) {
		journal.info("Connected to the CloudML server {}.", serverURI);
		this.session = session;
		this.session.setMaxTextMessageBufferSize(10 * 1024 * 1024);
		
		pcs.firePropertyChange("Connection", false, true);
	}
	
	public void send(String command) {
		if (!isConnected())
			throw new RuntimeException("You're not connected to any server!");
		
		journal.trace(">>> {}", command);
		
		try {
			session.getBasicRemote().sendText("!listenToAny");
			
			Thread.sleep(800);
			
			session.getBasicRemote().sendText(command);
		} catch (Exception e) {
			journal.error("Error while sending the command.", e);
		}
	}

	@OnMessage
	public void onMessage(String s) {
		if (s.trim().length() == 0)
			return;

		journal.trace("<<< {}", s);
		
		pcs.firePropertyChange("Message", false, true);

		if(s.equals("!ack {fromPeer: org.cloudml.facade.commands.ScaleOut, status: completed}")){
			journal.info("Scale out completed! Asking CloudML for the deployment model");
			this.send("!getSnapshot { path : / }");

		}else if(s.contains("return of GetSnapshot")){
			journal.info("Received deployment model from CloudML");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(s.substring(27));
				JSONArray instances=jsonObject.getJSONArray("vmInstances");
				ModelManager.updateDeploymentInfo(instances);
			} catch (JSONException | TierNotFoudException | CloudMLReturnedModelException e) {
				journal.error("Error while parsing the deployment info returned by CloudML.", e);
			}
		}
	}

	@OnClose
	public void onClose(CloseReason closeReason) {
		journal.info("Disconnected from the CloudML server {} ({}: {}).", serverURI, closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
		session = null;
		
		pcs.firePropertyChange("Connection", true, false);
		
		if (closeReason.getCloseCode().getCode() == 1006) {
			journal.info("The connection was killed for timeout. Reconnecting...");
			try {
				open();
			} catch (Exception e) {
				journal.error("Error while reconnecting to the server.", e);
			}
		}
	}

	@OnError
	public void onError(Throwable e) {
		journal.error("Error met.", e);

		pcs.firePropertyChange("Error", false, true);
	}
	
	public boolean isConnected() {
		return session != null && session.isOpen();
	}
	
	@Override
	public void close() {
		try {
			session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Requested explicitly"));
		} catch (Exception e) {
			journal.error("Error while disconnecting.", e);
		}
	}

	@Override
	public void run() {
		try {
			open();
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) { }
			}
		} catch (Exception e) {
			journal.error("Error while opening the connection.", e);
		}
	}

}
