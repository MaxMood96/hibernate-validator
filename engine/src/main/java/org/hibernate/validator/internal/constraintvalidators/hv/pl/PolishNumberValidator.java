/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.constraintvalidators.hv.pl;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import jakarta.validation.ConstraintValidator;

import org.hibernate.validator.internal.constraintvalidators.hv.ModCheckBase;
import org.hibernate.validator.internal.util.ModUtil;

/**
 * A base class validator for different Polish identification numbers. They differ in the lengths and weights used to calculate the mod sum.
 * In order to implement one you need to provide a method that gives an array of weights
 * and {@link ConstraintValidator#initialize(Annotation)} methods.
 *
 * @author Marko Bekhta
 */
public abstract class PolishNumberValidator<T extends Annotation> extends ModCheckBase implements ConstraintValidator<T, CharSequence> {

	@Override
	public boolean isCheckDigitValid(List<Integer> digits, char checkDigit) {
		Collections.reverse( digits );

		int[] weights = getWeights( digits );

		// if the length of the number is incorrect we can return fast
		if ( weights.length != digits.size() ) {
			return false;
		}

		// as we need sum % 11 rather than 11 - (sum % 11) returned by Mod11 algorithm:
		int modResult = 11 - ModUtil.calculateModXCheckWithWeights( digits, 11, Integer.MAX_VALUE, weights );
		switch ( modResult ) {
			case 10:
			case 11:
				return checkTwoDigitModuloResult( checkDigit );
			default:
				return Character.isDigit( checkDigit ) && modResult == extractDigit( checkDigit );
		}
	}

	protected boolean checkTwoDigitModuloResult(char checkDigit) {
		return checkDigit == '0';
	}

	protected abstract int[] getWeights(List<Integer> digits);
}
