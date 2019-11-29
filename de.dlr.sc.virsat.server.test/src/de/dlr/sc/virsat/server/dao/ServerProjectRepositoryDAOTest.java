/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.server.dao;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import javax.json.stream.JsonParsingException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.dlr.sc.virsat.server.repositories.RepositoryConfiguration;

/**
 * Test cases for the Repository Configuration DAO
 *
 */
public class ServerProjectRepositoryDAOTest {

	private File testPersistenceFile;
	private ServerProjectRepositoryDAO repoDao;
	
	RepositoryConfiguration repo1 = new RepositoryConfiguration("test1", "repoLoc1", "projLoc1", "func_one", "secret1", Arrays.asList("user_1", "user_2")); 
	RepositoryConfiguration repo2 = new RepositoryConfiguration("test2", "repoLoc2", "projLoc2", "func_two", "secret2", Arrays.asList("user_1", "user_2")); 
	
	
	@Before
	public void setUp() throws Exception {
		// Create a config file for the test
		File workspaceRoot = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();
		testPersistenceFile = new File(workspaceRoot, "testRepositories.json");
		FileUtils.deleteQuietly(testPersistenceFile);
		assertFalse("There is no config file yet", testPersistenceFile.exists());
		
		// Create the Server Project Repository DAO
		repoDao = new ServerProjectRepositoryDAO(testPersistenceFile);
	}

	@After
	public void tearDown() throws Exception {
		FileUtils.deleteQuietly(testPersistenceFile);
	}
	
	@Test
	public void testGet() {
		assertTrue("No Config yet", repoDao.get().isEmpty());
		
		repoDao.post(repo1);
		assertThat("First Repo exists", repoDao.get().values(), allOf(hasItem(repo1), not(hasItem(repo2))));
	
		repoDao.post(repo2);
		assertThat("Both Repo exists", repoDao.get().values(), allOf(hasItem(repo1), hasItem(repo2)));
	}

	@Test
	public void testGetString() {
		UUID repo1Uuid = repoDao.post(repo1);
		UUID repo2Uuid = repoDao.post(repo2);
		
		assertEquals("Got correct config", repo1, repoDao.get(repo1Uuid));
		assertEquals("Got correct config", repo2, repoDao.get(repo2Uuid));
		assertNull("Got no object", repoDao.get(UUID.randomUUID()));
	}

	@Test
	public void testPost() {
		UUID repo1Uuid = repoDao.post(repo1);
		UUID repo2Uuid = repoDao.post(repo2);

		assertThat("All configs present", repoDao.get().keySet(), hasItems(repo1Uuid, repo2Uuid));

		// Check that reposts add new elements as required by the REST Contract
		UUID repo1Uuid2 = repoDao.post(repo1);
		UUID repo2Uuid2 = repoDao.post(repo2);

		assertThat("All configs present", repoDao.get().keySet(), hasItems(repo1Uuid, repo2Uuid, repo1Uuid2, repo2Uuid2));
	}

	@Test
	public void testPut() {
		UUID repo1Uuid = repoDao.post(repo1);
		UUID repo2Uuid = repoDao.post(repo2);
		
		assertThat("All configs present", repoDao.get().keySet(), hasItems(repo1Uuid, repo2Uuid));
		
		// Reput the object, which should not change anything
		UUID repo1UuidReply1 = repoDao.put(repo1Uuid, repo1);
		
		assertEquals("UUID has not changed", repo1UuidReply1, repo1Uuid);
		assertEquals("Amount of configs did not change", 2, repoDao.get().size());
		
		// Now put repo2 with the id of one, this should update repo one with values of 2
		UUID repo1UuidReply2 = repoDao.put(repo1Uuid, repo2);
		
		assertEquals("UUID has not changed", repo1UuidReply2, repo1Uuid);
		assertEquals("Amount of configs did not change", 2, repoDao.get().size());
		assertSame("Still the old object", repo1, repoDao.get().get(repo1Uuid));
		assertEquals("The content has changed", repo2.getName(), repoDao.get().get(repo1Uuid).getName());
		
		// now try to put and object under an unknown id, nothing should happen
		UUID randomUuid = UUID.randomUUID();
		UUID randomUuidReply = repoDao.put(randomUuid, repo2);
		assertEquals("UUID has not changed", randomUuidReply, randomUuid);
		assertEquals("Amount of configs did not change", 2, repoDao.get().size());
	}

	@Test
	public void testDelete() {
		UUID repo1Uuid = repoDao.post(repo1);
		UUID repo2Uuid = repoDao.post(repo2);
		
		assertThat("All configs present", repoDao.get().keySet(), hasItems(repo1Uuid, repo2Uuid));
		
		repoDao.delete(repo1Uuid);
	
		assertThat("Config got deleted", repoDao.get().keySet(), hasItems(repo2Uuid));
	}

	@Test
	public void testSaveLoad() throws IOException {
		UUID repo1Uuid = repoDao.post(repo1);

		repoDao.save(); 
		assertTrue("Config File exsits", testPersistenceFile.exists());

		// Create a second repo DAO and read from the configuration
		ServerProjectRepositoryDAO repo2Dao = new ServerProjectRepositoryDAO(testPersistenceFile);
		assertTrue("No Config yet", repo2Dao.get().isEmpty());
		repo2Dao.load();
		assertThat("All configs present", repoDao.get().keySet(), hasItems(repo1Uuid));

		// Add a second repo config and see if it gets correctly serialized with the file
		UUID repo2Uuid = repoDao.post(repo2);
		repoDao.save(); 

		repo2Dao.load();
		assertThat("All configs present", repoDao.get().keySet(), hasItems(repo1Uuid, repo2Uuid));
	}
	
	@Test
	public void testLoadWithNoConfig() throws IOException {
		repoDao.load(); 
		
		assertTrue("Now the configuration exists", testPersistenceFile.exists());
	}

	@Test(expected = JsonParsingException.class)
	public void testLoadWithEmptyConfig() throws IOException {
		testPersistenceFile.createNewFile();
		assertTrue("Config file now exsists", testPersistenceFile.exists());
		repoDao.load(); 
	}
}
