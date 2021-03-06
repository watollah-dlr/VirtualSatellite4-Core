/*******************************************************************************
 * Copyright (c) 2008-2019 German Aerospace Center (DLR), Simulation and Software Technology, Germany.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions;


import de.dlr.sc.virsat.model.dvlm.calculation.IEquationDefinitionInput;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>AQudv Type Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.AQudvTypeProperty#getQuantityKindName <em>Quantity Kind Name</em>}</li>
 *   <li>{@link de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.AQudvTypeProperty#getUnitName <em>Unit Name</em>}</li>
 * </ul>
 *
 * @see de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.PropertydefinitionsPackage#getAQudvTypeProperty()
 * @model abstract="true"
 * @generated
 */
public interface AQudvTypeProperty extends IEquationDefinitionInput, AProperty {
	/**
	 * Returns the value of the '<em><b>Quantity Kind Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity Kind Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity Kind Name</em>' attribute.
	 * @see #setQuantityKindName(String)
	 * @see de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.PropertydefinitionsPackage#getAQudvTypeProperty_QuantityKindName()
	 * @model
	 * @generated
	 */
	String getQuantityKindName();

	/**
	 * Sets the value of the '{@link de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.AQudvTypeProperty#getQuantityKindName <em>Quantity Kind Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity Kind Name</em>' attribute.
	 * @see #getQuantityKindName()
	 * @generated
	 */
	void setQuantityKindName(String value);

	/**
	 * Returns the value of the '<em><b>Unit Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Name</em>' attribute.
	 * @see #setUnitName(String)
	 * @see de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.PropertydefinitionsPackage#getAQudvTypeProperty_UnitName()
	 * @model
	 * @generated
	 */
	String getUnitName();

	/**
	 * Sets the value of the '{@link de.dlr.sc.virsat.model.dvlm.categories.propertydefinitions.AQudvTypeProperty#getUnitName <em>Unit Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Name</em>' attribute.
	 * @see #getUnitName()
	 * @generated
	 */
	void setUnitName(String value);

} // AQudvTypeProperty
