/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.server.rest.resources;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.dlr.sc.virsat.server.dao.ModelAccess;
import de.dlr.sc.virsat.server.repositories.RepositoryConfiguration;

@Path("/admin")
public class ServerAdminResource {
	
	List<RepositoryConfiguration> repos = new ArrayList<>();
	
	public ServerAdminResource() {
		repos.add(new RepositoryConfiguration("test1", "localPath/Test1", "Project1", "f_functional", "ABSHS632626", Arrays.asList("user_one", "user_two")));
		repos.add(new RepositoryConfiguration("test2", "localPath/Test2", "Project2", "f_functional", "ABSHS632626", Arrays.asList("user_one")));
		repos.add(new RepositoryConfiguration("test3", "localPath/Test3", "Project3", "f_functional", "ABSHS632626", Arrays.asList("user_one", "user_two")));
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<RepositoryConfiguration> getTodos() {

		return repos;
	}

}
