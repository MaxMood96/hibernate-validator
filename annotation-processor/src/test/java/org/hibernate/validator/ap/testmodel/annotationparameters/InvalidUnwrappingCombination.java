/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.ap.testmodel.annotationparameters;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Optional;
import java.util.OptionalInt;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.valueextraction.Unwrapping;

/**
 * @author Marko Bekhta
 */
public class InvalidUnwrappingCombination {

	@OptionalConstraint(payload = { Unwrapping.Unwrap.class, Unwrapping.Skip.class })
	private final OptionalInt number;

	@OptionalConstraint(payload = { Unwrapping.Skip.class })
	private final Optional<Integer> number2;

	@OptionalConstraint(payload = { Unwrapping.Unwrap.class })
	private final Optional<Integer> number3;

	public InvalidUnwrappingCombination(OptionalInt number, Optional<Integer> number2, Optional<Integer> number3) {
		this.number = number;
		this.number2 = number2;
		this.number3 = number3;
	}

	@Target({ FIELD })
	@Retention(RUNTIME)
	@Documented
	@Constraint(validatedBy = { OptionalConstraintValidator.class })
	public @interface OptionalConstraint {

		String message() default "{org.hibernate.validator.ap.testmodel.annotationparameters.OptionalConstraint.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	private static class OptionalConstraintValidator implements ConstraintValidator<OptionalConstraint, Optional<?>> {

		@Override
		public boolean isValid(Optional<?> value, ConstraintValidatorContext context) {
			return true;
		}
	}
}
