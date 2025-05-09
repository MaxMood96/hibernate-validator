/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.cdi.internal;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.actions.GetMethods;
import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;

/**
 * Deals with methods of types in inheritance hierarchies.
 *
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 *
 */
public class InheritedMethodsHelper {

	private InheritedMethodsHelper() {
		// Not allowed
	}

	/**
	 * Get a list of all methods which the given class declares, implements,
	 * overrides or inherits. Methods are added by adding first all methods of
	 * the class itself and its implemented interfaces, then the super class and
	 * its interfaces, etc.
	 *
	 * @param clazz the class for which to retrieve the methods
	 *
	 * @return A list of all methods of the given class
	 */
	public static List<Method> getAllMethods(Class<?> clazz) {
		Contracts.assertNotNull( clazz );

		List<Method> methods = newArrayList();

		for ( Class<?> hierarchyClass : ClassHierarchyHelper.getHierarchy( clazz ) ) {
			Collections.addAll( methods, GetMethods.action( hierarchyClass ) );
		}

		return methods;
	}
}
