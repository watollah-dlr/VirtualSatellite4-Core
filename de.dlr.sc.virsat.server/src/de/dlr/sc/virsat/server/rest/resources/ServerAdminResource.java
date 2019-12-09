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
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<RepositoryConfiguration> getTodos() {

		return repos;
	}

}
