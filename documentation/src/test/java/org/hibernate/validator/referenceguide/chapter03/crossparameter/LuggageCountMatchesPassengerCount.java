/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.referenceguide.chapter03.crossparameter;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintTarget;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

@Target({ METHOD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { LuggageCountMatchesPassengerCount.Validator.class })
@Documented
public @interface LuggageCountMatchesPassengerCount {

	String message() default "{org.hibernate.validator.referenceguide.chapter03.crossparameter.LuggageCountMatchesPassengerCount.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;

	int piecesOfLuggagePerPassenger() default 1;

	@SupportedValidationTarget({
			ValidationTarget.PARAMETERS,
			ValidationTarget.ANNOTATED_ELEMENT
	})
	class Validator
			implements ConstraintValidator<LuggageCountMatchesPassengerCount, Object[]> {

		@Override
		public void initialize(LuggageCountMatchesPassengerCount constraintAnnotation) {
		}

		@Override
		public boolean isValid(Object[] value, ConstraintValidatorContext context) {
			return false;
		}
	}
}
