/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
//tag::include[]
package org.hibernate.validator.referenceguide.chapter12.context;

//end::include[]
import java.time.Instant;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Future;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

//tag::include[]
public class MyFutureValidator implements ConstraintValidator<Future, Instant> {

	@Override
	public void initialize(Future constraintAnnotation) {
	}

	@Override
	public boolean isValid(Instant value, ConstraintValidatorContext context) {
		if ( value == null ) {
			return true;
		}

		HibernateConstraintValidatorContext hibernateContext = context.unwrap(
				HibernateConstraintValidatorContext.class
		);

		Instant now = Instant.now( context.getClockProvider().getClock() );

		if ( !value.isAfter( now ) ) {
			hibernateContext.disableDefaultConstraintViolation();
			hibernateContext
					.addExpressionVariable( "now", now )
					.buildConstraintViolationWithTemplate( "Must be after ${now}" )
					.addConstraintViolation();

			return false;
		}

		return true;
	}
}
//end::include[]
