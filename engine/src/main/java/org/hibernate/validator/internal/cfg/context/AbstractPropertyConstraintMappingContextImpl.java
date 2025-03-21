/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.cfg.context;

import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
import org.hibernate.validator.cfg.context.ContainerElementConstraintMappingContext;
import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
import org.hibernate.validator.internal.engine.ConstraintCreationContext;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.properties.Property;

/**
 * Constraint mapping creational context which allows to configure the constraints for one bean property.
 *
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 * @author Kevin Pollet &lt;kevin.pollet@serli.com&gt; (C) 2011 SERLI
 * @author Marko Bekhta
 */
abstract class AbstractPropertyConstraintMappingContextImpl<T extends Property>
		extends CascadableConstraintMappingContextImplBase<PropertyConstraintMappingContext>
		implements PropertyConstraintMappingContext {

	private final TypeConstraintMappingContextImpl<?> typeContext;

	// either Field or Method
	private final T property;
	private final ConstraintLocation location;

	protected AbstractPropertyConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, T property, ConstraintLocation location) {
		super( typeContext.getConstraintMapping(), property.getType() );
		this.typeContext = typeContext;
		this.property = property;
		this.location = location;
	}

	@Override
	protected AbstractPropertyConstraintMappingContextImpl<?> getThis() {
		return this;
	}

	@Override
	public PropertyConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations) {
		mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnMember( property, ignoreAnnotations );
		return this;
	}

	@Override
	public PropertyConstraintMappingContext field(String property) {
		return typeContext.field( property );
	}

	@Override
	public PropertyConstraintMappingContext getter(String property) {
		return typeContext.getter( property );
	}

	@Override
	public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes) {
		return typeContext.constructor( parameterTypes );
	}

	@Override
	public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes) {
		return typeContext.method( name, parameterTypes );
	}

	@Override
	public ContainerElementConstraintMappingContext containerElementType() {
		return super.containerElement( this, typeContext, location );
	}

	@Override
	public ContainerElementConstraintMappingContext containerElementType(int index, int... nestedIndexes) {
		return super.containerElement( this, typeContext, location, index, nestedIndexes );
	}

	abstract ConstrainedElement build(ConstraintCreationContext constraintCreationContext);

	@Override
	protected ConstraintType getConstraintType() {
		return ConstraintType.GENERIC;
	}

	protected T getProperty() {
		return property;
	}
}
