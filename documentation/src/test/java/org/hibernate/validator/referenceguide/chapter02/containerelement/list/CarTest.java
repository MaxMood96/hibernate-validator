/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.referenceguide.chapter02.containerelement.list;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

import org.junit.BeforeClass;
import org.junit.Test;

public class CarTest {

	private static Validator validator;

	@BeforeClass
	public static void setUpValidator() {
		ValidatorFactory factory = Validation.byProvider( HibernateValidator.class )
				.configure()
				.buildValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void validateListContainerElementConstraint() {
		//tag::validateListContainerElementConstraint[]
		Car car = new Car();
		car.addPart( "Wheel" );
		car.addPart( null );

		Set<ConstraintViolation<Car>> constraintViolations = validator.validate( car );

		assertEquals( 1, constraintViolations.size() );

		ConstraintViolation<Car> constraintViolation =
				constraintViolations.iterator().next();
		assertEquals(
				"'null' is not a valid car part.",
				constraintViolation.getMessage()
		);
		assertEquals( "parts[1].<list element>",
				constraintViolation.getPropertyPath().toString() );
		//end::validateListContainerElementConstraint[]
	}

}
