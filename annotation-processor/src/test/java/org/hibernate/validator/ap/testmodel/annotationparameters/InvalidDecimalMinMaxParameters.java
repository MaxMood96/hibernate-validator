/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.ap.testmodel.annotationparameters;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

/**
 * @author Marko Bekhta
 */
public class InvalidDecimalMinMaxParameters {

	@DecimalMax(value = "a")
	@DecimalMin(value = "a")
	private BigDecimal decimal1;

	@DecimalMax(value = "123.12.00")
	@DecimalMin(value = "123.12.00")
	private BigDecimal decimal2;

	@DecimalMax(value = "123E-3-3")
	@DecimalMin(value = "123E-3-3")
	private BigDecimal decimal3;

	@DecimalMax.List({ @DecimalMax(value = "123E-3-3") })
	@DecimalMin.List({ @DecimalMin(value = "123E-3-3") })
	private BigDecimal decimal4;

	public InvalidDecimalMinMaxParameters(
			@DecimalMax(value = "a") BigDecimal decimal1,
			@DecimalMax(value = "0.0.0E+7") BigDecimal decimal2,
			@DecimalMax(value = "1234.5E-4-4") BigDecimal decimal3
	) {

	}

	public void doSomething(
			@DecimalMax(value = "a") BigDecimal decimal1,
			@DecimalMax(value = "0.0.0E+7") BigDecimal decimal2,
			@DecimalMax(value = "1234.5E-4-4") BigDecimal decimal3
	) {

	}

	@DecimalMax(value = "1234.5E-4-4")
	public BigDecimal doSomething() {
		return BigDecimal.ONE;
	}
}
