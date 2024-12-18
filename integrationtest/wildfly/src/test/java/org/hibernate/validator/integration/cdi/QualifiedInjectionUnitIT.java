/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.integration.cdi;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.hibernate.validator.cdi.HibernateValidator;
import org.hibernate.validator.integration.AbstractArquillianIT;
import org.hibernate.validator.testutil.TestForIssue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
public class QualifiedInjectionUnitIT extends AbstractArquillianIT {
	private static final String WAR_FILE_NAME = QualifiedInjectionUnitIT.class.getSimpleName() + ".war";

	@Inject
	@HibernateValidator
	private ValidatorFactory validatorFactory;

	@Inject
	@HibernateValidator
	private Validator validator;

	@Inject
	TestEntity testEntity;

	@Deployment
	public static WebArchive createTestArchive() throws Exception {
		return buildTestArchive( WAR_FILE_NAME )
				.addAsWebInfResource( BEANS_XML, "beans.xml" );
	}

	@Test
	public void testQualifiedValidatorFactoryAndValidatorInjectable() {
		assertThat( validatorFactory ).as( "The validator factory should have been injected" ).isNotNull();
		assertThat( validator ).as( "The validator should have been injected" ).isNotNull();
	}

	@Test
	@TestForIssue(jiraKey = "HV-787")
	public void testInjectionIntoBeanWithPassivatingScope() throws Exception {
		assertThat( testEntity ).isNotNull();
		assertThat( testEntity.getValidatorFactory() ).isNotNull();
		assertThat( testEntity.getValidator() ).isNotNull();
	}

	@SessionScoped
	@SuppressWarnings("serial")
	public static class TestEntity implements Serializable {

		@Inject
		@HibernateValidator
		private ValidatorFactory validatorFactory;

		@Inject
		@HibernateValidator
		private Validator validator;


		public ValidatorFactory getValidatorFactory() {
			return validatorFactory;
		}

		public Validator getValidator() {
			return validator;
		}
	}
}
