/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.constraintvalidators.bv.number.bound.decimal;

/**
 * Check that the number being validated is less than or equal to the maximum
 * value specified.
 *
 * @author Marko Bekhta
 */
public class DecimalMaxValidatorForLong extends AbstractDecimalMaxValidator<Long> {

	@Override
	protected int compare(Long number) {
		return DecimalNumberComparatorHelper.compare( number, maxValue );
	}
}
