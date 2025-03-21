/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent;

import java.time.Clock;
import java.time.MonthDay;

/**
 * Check that the {@code java.time.MonthDay} passed is in the future.
 *
 * @author Guillaume Smet
 */
public class FutureOrPresentValidatorForMonthDay extends AbstractFutureOrPresentJavaTimeValidator<MonthDay> {

	@Override
	protected MonthDay getReferenceValue(Clock reference) {
		return MonthDay.now( reference );
	}

}
