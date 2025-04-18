/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.metadata.aggregated;

import org.hibernate.validator.internal.engine.valueextraction.ValueExtractorManager;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation.ConstraintLocationKind;
import org.hibernate.validator.internal.properties.Getter;

/**
 * A {@link Cascadable} backed by a getter of a Java bean.
 *
 * @author Gunnar Morling
 * @author Marko Bekhta
 */
public class GetterCascadable extends AbstractPropertyCascadable<Getter> {

	GetterCascadable(Getter getter, CascadingMetaData cascadingMetaData) {
		super( getter, cascadingMetaData );
	}

	@Override
	public ConstraintLocationKind getConstraintLocationKind() {
		return ConstraintLocationKind.GETTER;
	}

	public static class Builder extends AbstractPropertyCascadable.AbstractBuilder<Getter> {

		protected Builder(ValueExtractorManager valueExtractorManager, Getter getter, CascadingMetaDataBuilder cascadingMetaDataBuilder) {
			super( valueExtractorManager, getter, cascadingMetaDataBuilder );
		}

		@Override
		protected Cascadable create(Getter getter, CascadingMetaData cascadingMetaData) {
			return new GetterCascadable( getter, cascadingMetaData );
		}
	}
}
