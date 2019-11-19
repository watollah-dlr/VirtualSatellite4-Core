/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

/**
 * This class represents a Jetty Instance to run Virtual Satellite
 * Stand alone as server application. The class runs some Jersey Servelets
 * to provide a REST API to the outside world
 *
 */
public class VirSatJettyServer {

	private VirSatJettyServer() {
	}
	
	private static final int VIRSAT_JETTY_PORT = 8000; 
	
	/**
	 * Main entry point for Virtual Satellite Jetty Server
	 * @param args Not used currently
	 */
	public static void main(String[] args) {
		try {
			new VirSatJettyServer().start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Server server;
	
	/**
	 *  Call this method to start the Virtual Satellite specific Jetty Server.
	 *  This method blocks in a join statement to wait make the cally thread wait
	 *  until the server is shut down.
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void start() throws Exception, InterruptedException {
		server = new Server(VIRSAT_JETTY_PORT);

		ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
		servletContextHandler.setContextPath("/");
		server.setHandler(servletContextHandler);

		ServletHolder servletHolder = servletContextHandler.addServlet(ServletContainer.class, "/rest/*");
		servletHolder.setInitOrder(0);
		servletHolder.setInitParameter(
			"jersey.config.server.provider.packages",
			"de.dlr.sc.virsat.server.resources");

		server.start();
		server.join();
	}
	
	/**
	 * Call this method to stop the server
	 * @throws Exception
	 */
	public void stop() throws Exception {
		if (server != null) {
			server.stop();
		}
	}
}