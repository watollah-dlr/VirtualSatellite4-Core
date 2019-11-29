/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.server.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.dlr.sc.virsat.server.dao.ServerProjectRepositoryDAOTest;
import de.dlr.sc.virsat.server.dao.VirSatGitAccessTest;
import de.dlr.sc.virsat.server.rest.resources.AccessTestResourceTest;
import de.dlr.sc.virsat.server.rest.resources.WorkspaceAccessResourceTest;
import de.dlr.sc.virsat.server.rest.resources.WorkspaceUserContextTest;
import junit.framework.JUnit4TestAdapter;

/**
 * 
 * test Suite for the VirSat Server
 */
@RunWith(Suite.class)

@SuiteClasses({
	ServerProjectRepositoryDAOTest.class,
	VirSatGitAccessTest.class,
	WorkspaceAccessResourceTest.class,
	AccessTestResourceTest.class,
	WorkspaceUserContextTest.class
})

public class AllTests {

	/**
	 * the constructor
	 */
	private AllTests() {
	}
	/**
	 * initializes the test suite
	 * @return a newt JUnit test adapter
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}	
}