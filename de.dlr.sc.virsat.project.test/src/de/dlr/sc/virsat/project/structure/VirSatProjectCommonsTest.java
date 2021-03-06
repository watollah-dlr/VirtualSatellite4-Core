/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.project.structure;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import de.dlr.sc.virsat.model.dvlm.DVLMFactory;
import de.dlr.sc.virsat.model.dvlm.Repository;
import de.dlr.sc.virsat.model.dvlm.categories.CategoriesFactory;
import de.dlr.sc.virsat.model.dvlm.categories.Category;
import de.dlr.sc.virsat.model.dvlm.categories.CategoryAssignment;
import de.dlr.sc.virsat.model.dvlm.concepts.Concept;
import de.dlr.sc.virsat.model.dvlm.concepts.ConceptsFactory;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElement;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralElementInstance;
import de.dlr.sc.virsat.model.dvlm.structural.StructuralFactory;
import de.dlr.sc.virsat.project.resources.VirSatResourceSet;
import de.dlr.sc.virsat.project.structure.nature.VirSatProjectNature;
import de.dlr.sc.virsat.project.test.AProjectTestCase;

/**
 * 
 * @author fisc_ph
 *
 */
public class VirSatProjectCommonsTest extends AProjectTestCase {

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testCreateProjectStructure() {
		VirSatProjectCommons virSatProject = new VirSatProjectCommons(testProject); 
		
		assertFalse("Folder does not yet exist", testProject.getFolder(VirSatProjectCommons.FOLDERNAME_DATA).exists());
		assertFalse("Folder does not yet exist", testProject.getFolder(VirSatProjectCommons.FOLDERNAME_UNVERSIONED).exists());
		
		boolean result = virSatProject.createProjectStructure(null);
		
		assertTrue("Method was susccesfully executed", result);
		assertTrue("Folder now exist", testProject.getFolder(VirSatProjectCommons.FOLDERNAME_DATA).exists());
		assertTrue("Folder now exist", testProject.getFolder(VirSatProjectCommons.FOLDERNAME_UNVERSIONED).exists());
	}
	
	@Test
	public void testAttachProjectNature() throws CoreException {
		VirSatProjectCommons virSatProject = new VirSatProjectCommons(testProject); 

		boolean result = virSatProject.attachProjectNature();
		
		assertTrue("Method was susccesfully executed", result);
		
		List<String> natures = Arrays.asList(testProject.getDescription().getNatureIds());
		
		assertThat("Nature is in the list", natures, hasItem(VirSatProjectNature.NATURE_ID));
	}
	
	@Test
	public void testCreateFolderStructure() throws CoreException {
		VirSatProjectCommons virSatProject = new VirSatProjectCommons(testProject);
		virSatProject.createProjectStructure(null);
		
		// create a structural element instance which will be added to the project
		// this creates a folder and a file in the folder
		// we check if they were correctly created.
		
		StructuralElementInstance sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		String seiUuid = sei.getUuid().toString();
		
		String fullFolderNameSei = VirSatProjectCommons.FOLDERNAME_DATA + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_PREFIX + seiUuid;
		String fullFolderNameSeiDocuments = fullFolderNameSei + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_DOCUMENTS;
		
		assertFalse("Folder does not yet exist", testProject.getFolder(fullFolderNameSei).exists());
		assertFalse("Folder does not yet exist", testProject.getFolder(fullFolderNameSeiDocuments).exists());
		assertFalse("File does not yet exist", testProject.getFile(fullFolderNameSei + "/" + VirSatProjectCommons.FILENAME_STRUCTURAL_ELEMENT).exists());
		
		virSatProject.createFolderStructure(sei, null);
		
		assertTrue("Folder does exist now", testProject.getFolder(fullFolderNameSei).exists());
		assertTrue("Folder does exist now", testProject.getFolder(fullFolderNameSeiDocuments).exists());
		// The actual StructuralElement.dvlm file is not created with this method
		// this code magic happens in 
		// Command initAndAddIsteCommand = resourceSet.initializeStructuralElement(iste, ed);
		assertFalse("File does exist now", testProject.getFile(fullFolderNameSei + "/" + VirSatProjectCommons.FILENAME_STRUCTURAL_ELEMENT).exists());
		
	}
	
	@Test
	public void testRemoveFolderStructure() throws CoreException {
		VirSatProjectCommons virSatProject = new VirSatProjectCommons(testProject);
		virSatProject.createProjectStructure(null);
		StructuralElementInstance sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		String seiUuid = sei.getUuid().toString();
		virSatProject.createFolderStructure(sei, null);
		
		String fullFolderNameSei = VirSatProjectCommons.FOLDERNAME_DATA + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_PREFIX + seiUuid;
		String fullFolderNameSeiDocuments = fullFolderNameSei + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_DOCUMENTS;
		
		assertTrue("Folder does exist", testProject.getFolder(fullFolderNameSei).exists());
		assertTrue("Folder does exist", testProject.getFolder(fullFolderNameSeiDocuments).exists());
		
		virSatProject.removeFolderStructure(sei, null);
		
		assertFalse("Folder does not exist after remove operation", testProject.getFolder(fullFolderNameSei).exists());
		assertFalse("Folder does not exist  after remove operation", testProject.getFolder(fullFolderNameSeiDocuments).exists());
	}
	
	@Test
	public void testGetAllVirSatProjects() throws CoreException {
		// Create a workspace with some projects from which some are VirSat ones
		// and others are not. Call the method and see that we retrieve only
		// the projects which are actual VirSat projects
		IProject project1VirSat =  testProject;
		IProject project2Other = createTestProject("testProject2Other");
		IProject project3VirSat = createTestProject("testProject3");

		VirSatProjectCommons projectCommons1 = new VirSatProjectCommons(project1VirSat);
		VirSatProjectCommons projectCommons3 = new VirSatProjectCommons(project3VirSat);
		
		projectCommons1.attachProjectNature();
		projectCommons3.attachProjectNature();
		
		// Now try to get the projects from the workspace
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		List<IProject> virSatProjects = VirSatProjectCommons.getAllVirSatProjects(ws);
		
		assertThat("Project1 and 3 is contained", virSatProjects, hasItems(project1VirSat, project3VirSat));
		assertThat("Project2 is not contained", virSatProjects, not(hasItem(project2Other)));
	}
	
	@Test
	public void testGetRepositoryFile() {
		VirSatProjectCommons projectCommons = new VirSatProjectCommons(testProject);
		projectCommons.createProjectStructure(null);
		IFile file = projectCommons.getRepositoryFile();
		assertEquals("It is the correct file", VirSatProjectCommons.FILENAME_REPOSITORY, file.getName());
	}

	@Test
	public void testGetUnitManagementFile() {
		VirSatProjectCommons projectCommons = new VirSatProjectCommons(testProject);
		projectCommons.createProjectStructure(null);
		IFile file = projectCommons.getUnitManagementFile();
		assertEquals("It is the correct file", VirSatProjectCommons.FILENAME_UNIT_MANAGEMENT, file.getName());
	}

	@Test
	public void testGetRoleManagementFile() {
		VirSatProjectCommons projectCommons = new VirSatProjectCommons(testProject);
		projectCommons.createProjectStructure(null);
		IFile file = projectCommons.getRoleManagementFile();
		assertEquals("It is the correct file", VirSatProjectCommons.FILENAME_ROLE_MANAGEMENT, file.getName());
	}
	
	@Test
	public void testGetStructuralElementFile() {
		VirSatProjectCommons projectCommons = new VirSatProjectCommons(testProject);
		projectCommons.createProjectStructure(null);
		
		// Create two structural elements which we want to store in the project as well
		StructuralElementInstance sei1 = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		StructuralElementInstance sei2 = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		
		// Now add the folder and file structures for the structural elements
		projectCommons.createFolderStructure(sei1, null);
		projectCommons.createFolderStructure(sei2, null);
		
		IFile file1 = projectCommons.getStructuralElementInstanceFile(sei1);
		IFile file2 = projectCommons.getStructuralElementInstanceFile(sei2);
		IFile file22 = projectCommons.getStructuralElementInstanceFile(sei2);
		
		assertNotSame("Make sure both are individual files for different ISE", file1, file2);
		assertEquals("Make sure files are the same for the same ISE", file2, file22);
	}
	
	@Test
	public void testGetDocumentFolder() {
		VirSatProjectCommons projectCommons = new VirSatProjectCommons(testProject);
		projectCommons.createProjectStructure(null);
		
		// Create two structural elements which we want to store in the project as well
		StructuralElement se = StructuralFactory.eINSTANCE.createStructuralElement();
		StructuralElementInstance sei1 = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		sei1.setType(se);
		
		// Setting up the resources factory to deal with the model extension
		Resource.Factory.Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
	    Map<String, Object> m = resourceRegistry.getExtensionToFactoryMap();
	    m.put(VirSatProjectCommons.FILENAME_EXTENSION, new XMIResourceFactoryImpl());

	    // Create a resource set for correctly containing the SEI and subcomponents
	    IFile seiFile = projectCommons.getStructuralElementInstanceFile(sei1);
	    URI seiUri = URI.createPlatformResourceURI(seiFile.getFullPath().toString(), true);
	    ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.createResource(seiUri);
		resource.getContents().add(sei1);
		
		// Create one CategoryAssignemnt for the SEI
		Category c = CategoriesFactory.eINSTANCE.createCategory();
		c.setIsApplicableForAll(true);
		CategoryAssignment ca = CategoriesFactory.eINSTANCE.createCategoryAssignment();
		ca.setType(c);
		sei1.getCategoryAssignments().add(ca);
		
		assertTrue("Make sure that we actually have the CA assigned", sei1.getCategoryAssignments().contains(ca));
		
		// Now add the folder and file structures for the structural elements
		projectCommons.createFolderStructure(sei1, null);
		
		IFolder folderForSei1 = VirSatProjectCommons.getDocumentFolder(sei1);
		IFolder folderForSeiCa = VirSatProjectCommons.getDocumentFolder(ca);
		
		assertTrue("Folder exists", folderForSei1.exists());
		assertTrue("Folder exists", folderForSeiCa.exists());
		
		assertEquals("Folders point to the same location", folderForSei1, folderForSeiCa);
	}

	@Test
	public void testIsDvlmFile() {
		IFile correctFile = testProject.getFile(new Path("correctFVile.dvlm"));
		IFile incorrectFile = testProject.getFile(new Path("incorrectFVile.other"));
		
		assertTrue("Is DVLM file", VirSatProjectCommons.isDvlmFile(correctFile));
		assertFalse("Is No DVLM file", VirSatProjectCommons.isDvlmFile(incorrectFile));
	}
	

	@Test
	public void testGetStructuralElementInstanceFullPath() {
		StructuralElementInstance sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		String seiUuid = sei.getUuid().toString();
		
		String fullFolderNameSei = VirSatProjectCommons.FOLDERNAME_DATA + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_PREFIX + seiUuid + "/" + VirSatProjectCommons.FILENAME_STRUCTURAL_ELEMENT;
		
		String path = VirSatProjectCommons.getStructuralElementInstanceFullPath(sei);
		
		assertEquals("path is correct", fullFolderNameSei, path);
	}
	
	@Test
	public void testGetStructuralElementInstancePath() {
		StructuralElementInstance sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		String seiUuid = sei.getUuid().toString();
		
		String fullFolderNameSei = VirSatProjectCommons.FOLDERNAME_DATA + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_PREFIX + seiUuid;
		
		String path = VirSatProjectCommons.getStructuralElementInstancePath(sei);
		
		assertEquals("path is correct", fullFolderNameSei, path);
	}

	@Test
	public void testGetStructuralElementInstanceDocumentPath() {
		StructuralElementInstance sei = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		String seiUuid = sei.getUuid().toString();
		
		String fullFolderNameSei = VirSatProjectCommons.FOLDERNAME_DATA + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_PREFIX + seiUuid + "/" + VirSatProjectCommons.FOLDERNAME_STRUCTURAL_ELEMENT_DOCUMENTS;
		
		String path = VirSatProjectCommons.getStructuralElementInstanceDocumentPath(sei);
		
		assertEquals("path is correct", fullFolderNameSei, path);
	}
	
	@Test
	public void testGetWorkspaceResource() throws IOException {
		VirSatResourceSet resSet = VirSatResourceSet.createUnmanagedResourceSet(testProject);
		resSet.getResources().clear();
		VirSatProjectCommons projectCommons = new VirSatProjectCommons(testProject); 
		
		// Build StructuralElement concept for this test. will consist of Definitions and Configurations
		StructuralElement seEd = StructuralFactory.eINSTANCE.createStructuralElement();
		seEd.setIsApplicableForAll(true);
		seEd.setName("Definition");
		StructuralElementInstance seiEdSc = StructuralFactory.eINSTANCE.createStructuralElementInstance();
		seiEdSc.setType(seEd);
		
		Concept concept = ConceptsFactory.eINSTANCE.createConcept();
		concept.getStructuralElements().add(seEd);
		
		Repository repo = DVLMFactory.eINSTANCE.createRepository();
		repo.getActiveConcepts().add(concept);
		repo.getRootEntities().add(seiEdSc);
		
		Resource resRepo = resSet.getRepositoryResource();
		Resource resSc = resSet.getStructuralElementInstanceResource(seiEdSc);
		
		resRepo.getContents().add(repo);
		resSc.getContents().add(seiEdSc);
		
		resRepo.save(Collections.EMPTY_MAP);
		resSc.save(Collections.EMPTY_MAP);
		
		IFile fileRepo = projectCommons.getRepositoryFile();
		IFile fileSc = projectCommons.getStructuralElementInstanceFile(seiEdSc);
		
		assertEquals("Got correct Resource", fileRepo, VirSatProjectCommons.getWorkspaceResource(repo));
		assertEquals("Got correct Resource", fileSc, VirSatProjectCommons.getWorkspaceResource(seiEdSc));
	}
	
}
