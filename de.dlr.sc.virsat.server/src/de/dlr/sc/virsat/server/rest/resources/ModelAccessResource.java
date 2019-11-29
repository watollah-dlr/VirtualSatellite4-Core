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


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import de.dlr.sc.virsat.server.dao.ModelAccess;

@Path("/model")
public class ModelAccessResource {
	
	@GET
	@Path("/repository")
	public String showRepository(@QueryParam("projectdirectory") String projectdirectory) {
		return ModelAccess.getInstance().showRepository(projectdirectory);
	}
	
	@GET
	@Path("/sei")
	public String showStructuralElementImplementation(@QueryParam("projectdirectory") String projectdirectory,
													  @QueryParam("uuid") String uuid) {
		return ModelAccess.getInstance().showBeanSeiFromUuid(projectdirectory, uuid);
	}
	
	@GET
	@Path("/ca")
	public String showCategoryAssignment(@QueryParam("projectdirectory") String projectdirectory,
										 @QueryParam("uuid") String uuid) {
		return ModelAccess.getInstance().showBeanCAFromUuid(projectdirectory, uuid);
	}
	
	@GET
	@Path("/property")
	public String showBeanProperty(@QueryParam("projectdirectory") String projectdirectory,
										 @QueryParam("uuid") String uuid) {
		return ModelAccess.getInstance().showBeanPropertyFromUuid(projectdirectory, uuid);
	}
}
