/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.engine.valueextraction;

import java.util.Optional;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

import org.hibernate.validator.internal.engine.path.NodeImpl;

class ObjectArrayValueExtractor implements ValueExtractor<Object @ExtractedValue []> {

	static final ValueExtractorDescriptor DESCRIPTOR = new ValueExtractorDescriptor( new ObjectArrayValueExtractor(), Object[].class,
			new ArrayElement( Object[].class ), false, Optional.empty() );

	private ObjectArrayValueExtractor() {
	}

	@Override
	public void extractValues(Object[] originalValue, ValueReceiver receiver) {
		for ( int i = 0; i < originalValue.length; i++ ) {
			receiver.indexedValue( NodeImpl.ITERABLE_ELEMENT_NODE_NAME, i, originalValue[i] );
		}
	}
}
