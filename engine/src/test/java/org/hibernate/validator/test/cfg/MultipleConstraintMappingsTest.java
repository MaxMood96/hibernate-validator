/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.cfg;

import static org.hibernate.validator.testutils.ValidatorUtil.getConfiguration;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jakarta.validation.GroupDefinitionException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.metadata.BeanDescriptor;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.GenericConstraintDef;
import org.hibernate.validator.cfg.defs.AssertFalseDef;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
import org.hibernate.validator.testutil.TestForIssue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link org.hibernate.validator.cfg.ConstraintMapping} et al.
 *
 * @author Hardy Ferentschik
 */
public class MultipleConstraintMappingsTest {
	HibernateValidatorConfiguration config;

	@BeforeMethod
	public void setUp() {
		config = getConfiguration( HibernateValidator.class );
	}

	@Test
	@TestForIssue(jiraKey = "HV-500")
	public void testMultipleConstraintMappings() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.getter( "name" )
				.constraint( new NotNullDef() );

		ConstraintMapping runnerMapping = config.createConstraintMapping();
		runnerMapping.type( Runner.class )
				.getter( "name" )
				.constraint( new NotNullDef() );

		config.addMapping( marathonMapping );
		config.addMapping( runnerMapping );
		Validator validator = config.buildValidatorFactory().getValidator();

		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Marathon.class );
		assertTrue( beanDescriptor.isBeanConstrained(), "There should be constraints defined on the Marathon class" );
		assertEquals(
				beanDescriptor.getConstrainedProperties().iterator().next().getPropertyName(),
				"name",
				"The property name should be constrained"
		);

		beanDescriptor = validator.getConstraintsForClass( Runner.class );
		assertTrue( beanDescriptor.isBeanConstrained(), "There should be constraints defined on the Runner class" );
		assertEquals(
				beanDescriptor.getConstrainedProperties().iterator().next().getPropertyName(),
				"name",
				"The property name should be constrained"
		);
	}

	@TestForIssue(jiraKey = "HV-500")
	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000171.*")
	public void testSameTypeConfiguredSeveralTimesInSameConstraintMappingCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping
				.type( Marathon.class )
				.defaultGroupSequence( Foo.class, Marathon.class )
				.type( Marathon.class );
	}

	@TestForIssue(jiraKey = "HV-500")
	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000171.*")
	public void testSameTypeConfiguredSeveralTimesInDifferentConstraintMappingsCausesException() {
		ConstraintMapping marathonMapping1 = config.createConstraintMapping();
		marathonMapping1.type( Marathon.class )
				.defaultGroupSequenceProviderClass( MarathonDefaultGroupSequenceProvider.class );

		ConstraintMapping marathonMapping2 = config.createConstraintMapping();
		marathonMapping2.type( Marathon.class )
				.defaultGroupSequenceProviderClass( MarathonDefaultGroupSequenceProvider.class );

		config.addMapping( marathonMapping1 );
		config.addMapping( marathonMapping2 );

		config.buildValidatorFactory().getValidator();
	}

	@TestForIssue(jiraKey = "HV-500")
	@Test(expectedExceptions = GroupDefinitionException.class, expectedExceptionsMessageRegExp = "HV000052.*")
	public void testConfigurationOfSequenceProviderAndGroupSequenceCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.defaultGroupSequence( Foo.class, Marathon.class )
				.defaultGroupSequenceProviderClass( MarathonDefaultGroupSequenceProvider.class );

		config.addMapping( marathonMapping );
		Validator validator = config.buildValidatorFactory().getValidator();
		validator.validate( new Marathon() );
	}

	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000172.*")
	public void testSamePropertyConfiguredSeveralTimesCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.getter( "name" )
				.constraint( new NotNullDef() )
				.getter( "name" );
	}

	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000173.*")
	public void testSameMethodConfiguredSeveralTimesCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.method( "setTournamentDate", Date.class )
				.parameter( 0 )
				.constraint( new NotNullDef() )
				.method( "setTournamentDate", Date.class );
	}

	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000174.*")
	public void testSameParameterConfiguredSeveralTimesCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.method( "setTournamentDate", Date.class )
				.parameter( 0 )
				.constraint( new NotNullDef() )
				.parameter( 0 );
	}

	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000175.*")
	public void testReturnValueConfiguredSeveralTimesCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.method( "addRunner", Runner.class )
				.returnValue()
				.constraint( new AssertFalseDef() )
				.parameter( 0 )
				.returnValue();
	}

	@Test(expectedExceptions = ValidationException.class, expectedExceptionsMessageRegExp = "HV000177.*")
	public void testCrossParameterConfiguredSeveralTimesCausesException() {
		ConstraintMapping marathonMapping = config.createConstraintMapping();
		marathonMapping.type( Marathon.class )
				.method( "addRunner", Runner.class )
				.crossParameter()
				.constraint(
						new GenericConstraintDef<GenericAndCrossParameterConstraint>(
								GenericAndCrossParameterConstraint.class
						)
				)
				.parameter( 0 )
				.crossParameter();
	}

	private interface Foo {
	}

	public static class MarathonDefaultGroupSequenceProvider implements DefaultGroupSequenceProvider<Marathon> {
		@Override
		public List<Class<?>> getValidationGroups(Class<?> klass, Marathon object) {
			return Arrays.<Class<?>>asList( Foo.class, Marathon.class );
		}
	}
}
