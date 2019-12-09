/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.server.jetty;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jgit.util.FileUtils;

import de.dlr.sc.virsat.server.dao.ServerProjectRepositoryDAO;
import de.dlr.sc.virsat.server.rest.servlet.VirSatModelAccessServlet;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class represents a Jetty Instance to run Virtual Satellite
 * Stand alone as server application. The class runs some Jersey Servelets
 * to provide a REST API to the outside world
 *
 */
public class VirSatJettyServer extends Server {
	
	public static final int VIRSAT_JETTY_PORT = 8000; 
	
	private ServerProjectRepositoryDAO repositoryDao;
	
	public VirSatJettyServer(int virsatJettyPort) {
		super(virsatJettyPort);
	}

	public void init() throws IOException {
		// Try to identify the configuration Area from the Plugin Resources
		File fileConfigArea = getConfigurationArea();
		
		// Setup the config file for storing the Repo Config DAO
		repositoryDao = new ServerProjectRepositoryDAO(new File(fileConfigArea, CONFIG_AREA_REPOSITORIES));
		
		ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
		servletContextHandler.setContextPath("/");
		servletContextHandler.addServlet(VirSatModelAccessServlet.class, VirSatModelAccessServlet.URI_PATH_WILDCARD);
		
		setHandler(servletContextHandler);
	}
	
	private Server server;
	
	public static final String CONFIG_AREA_PATH = "configuration";
	public static final String CONFIG_AREA_REPOSITORIES = "repoconfig.json";
	
	public static final File getConfigurationArea() throws IOException {
		File wsRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();
		File configAreaFile = new File(wsRoot, CONFIG_AREA_PATH);
		FileUtils.mkdirs(configAreaFile, true);
		return configAreaFile;
	}
	
	public ServerProjectRepositoryDAO
}