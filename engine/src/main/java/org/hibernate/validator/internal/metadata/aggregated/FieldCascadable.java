/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.metadata.aggregated;

import org.hibernate.validator.internal.engine.valueextraction.ValueExtractorManager;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation.ConstraintLocationKind;
import org.hibernate.validator.internal.properties.Field;

/**
 * A {@link Cascadable} backed by a field of a Java bean.
 *
 * @author Gunnar Morling
 * @author Marko Bekhta
 */
public class FieldCascadable extends AbstractPropertyCascadable<Field> {

	FieldCascadable(Field field, CascadingMetaData cascadingMetaData) {
		super( field, cascadingMetaData );
	}

	@Override
	public ConstraintLocationKind getConstraintLocationKind() {
		return ConstraintLocationKind.FIELD;
	}

	public static class Builder extends AbstractPropertyCascadable.AbstractBuilder<Field> {

		protected Builder(ValueExtractorManager valueExtractorManager, Field field, CascadingMetaDataBuilder cascadingMetaDataBuilder) {
			super( valueExtractorManager, field, cascadingMetaDataBuilder );
		}

		@Override
		protected Cascadable create(Field field, CascadingMetaData cascadingMetaData) {
			return new FieldCascadable( field, cascadingMetaData );
		}
	}
}
