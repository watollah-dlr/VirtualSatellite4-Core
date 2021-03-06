/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.project.resources.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.junit.Test;


import de.dlr.sc.virsat.model.dvlm.roles.Discipline;
import de.dlr.sc.virsat.model.dvlm.roles.RoleManagement;
import de.dlr.sc.virsat.model.dvlm.roles.RolesFactory;
import de.dlr.sc.virsat.model.dvlm.roles.RolesPackage;
import de.dlr.sc.virsat.model.dvlm.roles.UserRegistry;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.project.editingDomain.VirSatEditingDomainRegistry;
import de.dlr.sc.virsat.project.editingDomain.VirSatTransactionalEditingDomain;
import de.dlr.sc.virsat.project.resources.VirSatResourceSet;
import de.dlr.sc.virsat.project.structure.VirSatProjectCommons;
import de.dlr.sc.virsat.project.test.AProjectTestCase;

/**
 * Test case for testing the AssignDisciplineCommand.
 * @author muel_s8
 *
 */
public class AssignDisciplineCommandTest extends AProjectTestCase {

	private VirSatResourceSet rs;
	private VirSatTransactionalEditingDomain rsEd;
	private VirSatProjectCommons projectCommons;
	
	private StructuralElementInstance sei;
	private Discipline discipline;
	
	@Override
	public void setUp() throws CoreException {
		super.setUp();
		
		projectCommons = new VirSatProjectCommons(testProject);
		projectCommons.createProjectStructure(null);
		
		discipline = RolesFactory.eINSTANCE.createDiscipline();
		sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();

		rs = VirSatResourceSet.getResourceSet(testProject, false);
		rsEd = VirSatEditingDomainRegistry.INSTANCE.getEd(testProject);
		
		Command cmd = rs.initializeModelsAndResourceSet(null, rsEd);
		rsEd.getCommandStack().execute(cmd);
		
		RoleManagement rm = rs.getRoleManagement();
		
		cmd = AddCommand.create(rsEd, rm, RolesPackage.Literals.ROLE_MANAGEMENT__DISCIPLINES, discipline);
		rsEd.getCommandStack().execute(cmd);
		
		cmd = new CreateSeiResourceAndFileCommand(rs, sei);
		rsEd.getCommandStack().execute(cmd);
		
		rsEd.saveAll();
	}

	@Override
	public void tearDown() throws CoreException {
		super.tearDown();
		
		VirSatResourceSet.clear();
		VirSatEditingDomainRegistry.INSTANCE.clear();
	}
	
	@Test
	public void testPrepare() {
		AssignDisciplineCommand cmd = new AssignDisciplineCommand(rs, sei, discipline);
		assertTrue("Command can be prepared", cmd.prepare());
	}

	@Test
	public void testCanUndo() {
		AssignDisciplineCommand cmd = new AssignDisciplineCommand(rs, sei, discipline);
		assertFalse("Command cannot be undone", cmd.canUndo());
	}

	@Test
	public void testExecute() {
		Resource seiResource = sei.eResource();
		
		AssignDisciplineCommand cmd = new AssignDisciplineCommand(rs, sei, discipline);
		assertTrue("Command can be executed", cmd.canExecute());
		
		UserRegistry.getInstance().setSuperUser(true);
		rsEd.getCommandStack().execute(cmd);
		assertEquals("Assigned Discipline is correct", discipline, sei.getAssignedDiscipline());
		assertFalse("Since we always save the resource in the AssignDisciplineCommand, it should not be dirty", rsEd.isDirty(seiResource));
		UserRegistry.getInstance().setSuperUser(false);
		
		cmd = new AssignDisciplineCommand(rs, sei, null);
		rsEd.getCommandStack().execute(cmd);
		assertEquals("Assigned Discipline has not changed since we dont have any rights", discipline, sei.getAssignedDiscipline());
	}

}
