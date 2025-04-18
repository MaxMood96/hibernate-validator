/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.engine.resolver;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import jakarta.validation.TraversableResolver;
import jakarta.validation.ValidationException;

import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.actions.GetMethod;
import org.hibernate.validator.internal.util.actions.LoadClass;
import org.hibernate.validator.internal.util.actions.NewInstance;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

public class TraversableResolvers {

	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	/**
	 * Class to load to check whether Jakarta Persistence is on the classpath.
	 */
	private static final String PERSISTENCE_CLASS_NAME = "jakarta.persistence.Persistence";

	/**
	 * Method to check whether the found {@code Persistence} class is of the version 2
	 */
	private static final String PERSISTENCE_UTIL_METHOD = "getPersistenceUtil";

	/**
	 * Class to instantiate in case Jakarta Persistence is on the classpath.
	 */
	private static final String JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME = "org.hibernate.validator.internal.engine.resolver.JPATraversableResolver";

	private TraversableResolvers() {
	}

	/**
	 * Initializes and returns the default {@link TraversableResolver} depending on the environment.
	 * <p>
	 * If Jakarta Persistence is present in the classpath, a {@link JPATraversableResolver} instance is returned.
	 * <p>
	 * Otherwise, it returns an instance of the default {@link TraverseAllTraversableResolver}.
	 */
	public static TraversableResolver getDefault() {
		// check whether we have Persistence on the classpath
		Class<?> persistenceClass;
		try {
			persistenceClass = LoadClass.action( PERSISTENCE_CLASS_NAME, TraversableResolvers.class.getClassLoader() );
		}
		catch (ValidationException e) {
			LOG.debugf(
					"Cannot find %s on classpath. Assuming non Jakarta Persistence environment. All properties will per default be traversable.",
					PERSISTENCE_CLASS_NAME
			);
			return getTraverseAllTraversableResolver();
		}

		// check whether Persistence contains getPersistenceUtil
		Method persistenceUtilGetter = GetMethod.action( persistenceClass, PERSISTENCE_UTIL_METHOD );
		if ( persistenceUtilGetter == null ) {
			LOG.debugf(
					"Found %s on classpath, but no method '%s'. Assuming JPA 1 environment. All properties will per default be traversable.",
					PERSISTENCE_CLASS_NAME,
					PERSISTENCE_UTIL_METHOD
			);
			return getTraverseAllTraversableResolver();
		}

		// try to invoke the method to make sure that we are dealing with a complete JPA2 implementation
		// unfortunately there are several incomplete implementations out there (see HV-374)
		try {
			Object persistence = NewInstance.action( persistenceClass, "persistence provider" );
			ReflectionHelper.getValue( persistenceUtilGetter, persistence );
		}
		catch (Exception e) {
			LOG.debugf(
					"Unable to invoke %s.%s. Inconsistent Jakarta Persistence environment. All properties will per default be traversable.",
					PERSISTENCE_CLASS_NAME,
					PERSISTENCE_UTIL_METHOD
			);
			return getTraverseAllTraversableResolver();
		}

		LOG.debugf(
				"Found %s on classpath containing '%s'. Assuming Jakarta Persistence environment. Trying to instantiate Jakarta Persistence aware TraversableResolver",
				PERSISTENCE_CLASS_NAME,
				PERSISTENCE_UTIL_METHOD
		);

		try {
			@SuppressWarnings("unchecked")
			Class<? extends TraversableResolver> jpaAwareResolverClass =
					(Class<? extends TraversableResolver>) LoadClass.action( JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME, TraversableResolvers.class.getClassLoader() );
			LOG.debugf(
					"Instantiated Jakarta Persistence aware TraversableResolver of type %s.", JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME
			);
			return NewInstance.action( jpaAwareResolverClass, "" );
		}
		catch (ValidationException e) {
			LOG.logUnableToLoadOrInstantiateJPAAwareResolver( JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME );
			return getTraverseAllTraversableResolver();
		}
	}

	/**
	 * Potentially wrap the {@link TraversableResolver} into a caching one.
	 * <p>
	 * If {@code traversableResolver} is {@code TraverseAllTraversableResolver.INSTANCE}, we don't wrap it and it is
	 * returned directly. Same if the caching is explicitly disabled.
	 * <p>
	 * If {@code traversableResolver} is an instance of our {@code JPATraversableResolver}, we wrap it with a caching
	 * wrapper specially tailored for the requirements of the spec. It is a very common case as it is used as soon as we
	 * have a Jakarta Persistence implementation in the classpath so optimizing this case is worth it.
	 * <p>
	 * In all the other cases, we wrap the resolver for caching.
	 * <p>
	 * Note that, in the {@code TraversableResolver} is wrapped, a new instance is returned each time and it should be
	 * used only for the duration of a validation call.
	 *
	 * @return The resolver for the duration of a validation call.
	 */
	public static TraversableResolver wrapWithCachingForSingleValidation(TraversableResolver traversableResolver,
			boolean traversableResolverResultCacheEnabled) {

		if ( TraverseAllTraversableResolver.class.equals( traversableResolver.getClass() ) || !traversableResolverResultCacheEnabled ) {
			return traversableResolver;
		}
		else if ( JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME.equals( traversableResolver.getClass().getName() ) ) {
			return new CachingJPATraversableResolverForSingleValidation( traversableResolver );
		}
		else {
			return new CachingTraversableResolverForSingleValidation( traversableResolver );
		}
	}

	private static TraversableResolver getTraverseAllTraversableResolver() {
		return new TraverseAllTraversableResolver();
	}

}
