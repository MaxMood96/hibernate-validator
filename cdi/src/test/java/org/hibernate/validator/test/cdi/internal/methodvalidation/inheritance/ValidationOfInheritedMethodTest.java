/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.cdi.internal.methodvalidation.inheritance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.fail;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
public class ValidationOfInheritedMethodTest extends Arquillian {

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create( JavaArchive.class )
				.addClass( Greeter.class )
				.addClass( SimpleGreeter.class )
				.addClass( AbstractGreeter.class )
				.addClass( Encryptor.class )
				.addClass( RefusingEncryptor.class )
				.addAsManifestResource( "beans.xml" );
	}

	@Inject
	Greeter greeter;

	@Inject
	Encryptor encryptor;

	@Test
	public void testInheritedMethodGetsValidated() throws Exception {
		try {
			greeter.greet( "how are you" );
			fail( "CDI method interceptor should throw an exception" );
		}
		catch (ConstraintViolationException e) {
			// success
		}
	}

	@Test
	public void testInterfaceMethodWithExecutableTypeNoneDoesNotGetValidated() throws Exception {
		try {
			assertThat( encryptor.encrypt( "top secret" ) ).isNull();
		}
		catch (ConstraintViolationException e) {
			fail( "Encryptor#encrypt should not be validated, because it is explicitly excluded from executable validation" );
		}
	}
}
