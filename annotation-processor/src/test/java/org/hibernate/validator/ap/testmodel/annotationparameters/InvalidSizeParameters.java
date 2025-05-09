/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.ap.testmodel.annotationparameters;

import java.util.Collection;

import jakarta.validation.constraints.Size;

/**
 * @author Marko Bekhta
 */
public class InvalidSizeParameters {

	@Size(min = -1)
	private Collection<String> strings1;

	@Size(max = -10)
	private Collection<String> strings2;

	@Size(min = 15, max = 10)
	private Collection<String> strings3;

	@Size.List({ @Size(min = -1), @Size(max = -15), @Size(min = 15, max = 10) })
	private Collection<String> strings4;

	public InvalidSizeParameters(
			@Size(min = -1) Collection<String> strings1,
			@Size(max = -10) Collection<String> strings2,
			@Size(min = 15, max = 10) Collection<String> strings3
	) {

	}

	public void doSomething(
			@Size(min = -1) Collection<String> strings1,
			@Size(max = -10) Collection<String> strings2,
			@Size(min = 15, max = 10) Collection<String> strings3
	) {

	}

	@Size(min = -10)
	public Collection<String> doSomething() {
		return java.util.Collections.emptyList();
	}
}
