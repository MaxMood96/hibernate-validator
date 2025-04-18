/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.ap.testmodel.annotationparameters;

import org.hibernate.validator.constraints.Length;

/**
 * @author Marko Bekhta
 */
public class InvalidLengthParameters {

	@Length(min = -1)
	private String string1;

	@Length(max = -10)
	private String string2;

	@Length(min = 15, max = 10)
	private String string3;

	@Length.List({ @Length(min = 15, max = 10), @Length(max = -10) })
	private String string4;

	public InvalidLengthParameters(
			@Length(min = -1) String strings1,
			@Length(max = -10) String strings2,
			@Length(min = 15, max = 10) String strings3
	) {

	}

	public void doSomething(
			@Length(min = -1) String strings1,
			@Length(max = -10) String strings2,
			@Length(min = 15, max = 10) String strings3
	) {

	}

	@Length(min = -10)
	public String doSomething() {
		return "";
	}

}
