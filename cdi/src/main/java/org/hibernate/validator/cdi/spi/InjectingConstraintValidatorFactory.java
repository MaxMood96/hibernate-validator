/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.cdi.spi;

import static org.hibernate.validator.cdi.internal.util.BuiltInConstraintValidatorUtils.isBuiltInConstraintValidator;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;

import org.hibernate.validator.cdi.internal.DestructibleBeanInstance;
import org.hibernate.validator.constraintvalidation.spi.DefaultConstraintValidatorFactory;
import org.hibernate.validator.internal.util.Contracts;

/**
 * A {@link ConstraintValidatorFactory} which enables CDI based dependency injection for the created
 * {@link ConstraintValidator}s.
 *
 * @author Gunnar Morling
 * @author Hardy Ferentschik
 */
public class InjectingConstraintValidatorFactory extends DefaultConstraintValidatorFactory {

	// TODO look for something with better performance (HF)
	private final Map<Object, DestructibleBeanInstance<?>> constraintValidatorMap =
			Collections.synchronizedMap( new IdentityHashMap<Object, DestructibleBeanInstance<?>>() );

	private final BeanManager beanManager;

	@Inject
	public InjectingConstraintValidatorFactory(BeanManager beanManager) {
		Contracts.assertNotNull( beanManager, "The BeanManager cannot be null" );
		this.beanManager = beanManager;
	}

	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		if ( isBuiltInConstraintValidator( key ) ) {
			return super.getInstance( key );
		}
		DestructibleBeanInstance<T> destructibleBeanInstance = new DestructibleBeanInstance<T>( beanManager, key );
		constraintValidatorMap.put( destructibleBeanInstance.getInstance(), destructibleBeanInstance );
		return destructibleBeanInstance.getInstance();
	}

	@Override
	public void releaseInstance(ConstraintValidator<?, ?> instance) {
		DestructibleBeanInstance<?> destructibleBeanInstance = constraintValidatorMap.remove( instance );
		// HV-865 (Cleanup is multi threaded and instances can be removed by multiple threads. Explicit null check is needed)
		if ( destructibleBeanInstance != null ) {
			destructibleBeanInstance.destroy();
		}
	}
}
