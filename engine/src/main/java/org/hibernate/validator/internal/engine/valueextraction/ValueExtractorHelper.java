/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.engine.valueextraction;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ValidationException;
import jakarta.validation.valueextraction.ValueExtractor;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/**
 * Various utilities to manipulate {@link ValueExtractor}s and {@link ValueExtractorDescriptor}s.
 *
 * @author Guillaume Smet
 */
public class ValueExtractorHelper {

	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	private ValueExtractorHelper() {
	}

	@SuppressWarnings("rawtypes")
	public static Set<Class<? extends ValueExtractor>> toValueExtractorClasses(Set<ValueExtractorDescriptor> valueExtractorDescriptors) {
		return valueExtractorDescriptors.stream()
				.map( valueExtractorDescriptor -> valueExtractorDescriptor.getValueExtractor().getClass() )
				.collect( Collectors.toSet() );
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void extractValues(ValueExtractorDescriptor valueExtractorDescriptor, Object containerValue, ValueExtractor.ValueReceiver valueReceiver) {
		ValueExtractor valueExtractor = valueExtractorDescriptor.getValueExtractor();
		try {
			valueExtractor.extractValues( containerValue, valueReceiver );
		}
		catch (ValidationException e) {
			throw e;
		}
		catch (Exception e) {
			throw LOG.getErrorWhileExtractingValuesInValueExtractorException( valueExtractor.getClass(), e );
		}
	}
}
