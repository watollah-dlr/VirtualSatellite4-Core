/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.server.repositories;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RepositoryConfiguration {

	private String name;
	private String repositoryLocation;
	private String projectLocation;
	private String accountName;
	private String accountPassword;
	private List<String> users;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public RepositoryConfiguration(
			String name,
			String repositoryLocation,
			String projectLocation,
			String accountName,
			String accountPassword,
			List<String> users) {
		super();
		this.name = name;
		this.repositoryLocation = repositoryLocation;
		this.projectLocation = projectLocation;
		this.accountName = accountName;
		this.accountPassword = accountPassword;
		this.users = users;
	}

	public String getRepositoryLocation() {
		return repositoryLocation;
	}
	
	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}
	
	public String getProjectLocation() {
		return projectLocation;
	}
	
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountPassword() {
		return accountPassword;
	}
	
	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}
	
	public List<String> getUsers() {
		return users;
	}
	
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	public void update(RepositoryConfiguration serverProjectRepository) {
		this.name = serverProjectRepository.name;
		this.repositoryLocation = serverProjectRepository.repositoryLocation;
		this.projectLocation = serverProjectRepository.projectLocation;
		this.accountName = serverProjectRepository.accountName;
		this.accountPassword = serverProjectRepository.accountPassword;
		this.users = serverProjectRepository.users;
	}
	
	
}
