/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.constraintvalidators.bv.size;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class SizeValidatorForArraysOfByte extends SizeValidatorForArraysOfPrimitives
		implements ConstraintValidator<Size, byte[]> {

	/**
	 * Checks the number of entries in an array.
	 *
	 * @param array The array to validate.
	 * @param constraintValidatorContext context in which the constraint is evaluated.
	 *
	 * @return Returns {@code true} if the array is {@code null} or the number of entries in
	 *         {@code array} is between the specified {@code min} and {@code max} values (inclusive),
	 *         {@code false} otherwise.
	 */
	@Override
	public boolean isValid(byte[] array, ConstraintValidatorContext constraintValidatorContext) {
		if ( array == null ) {
			return true;
		}
		return array.length >= min && array.length <= max;
	}
}
