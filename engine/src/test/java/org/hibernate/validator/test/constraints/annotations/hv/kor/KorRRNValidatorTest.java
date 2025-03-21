/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.constraints.annotations.hv.kor;

import static org.hibernate.validator.testutil.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.validator.testutil.ConstraintViolationAssert.assertThat;
import static org.hibernate.validator.testutil.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;

import org.hibernate.validator.constraints.kor.KorRRN;
import org.hibernate.validator.test.constraints.annotations.AbstractConstrainedTest;

import org.testng.annotations.Test;

public class KorRRNValidatorTest extends AbstractConstrainedTest {

	@Test
	void testKorRRN() {
		final KorRegistrationCard korRegistrationCard = new KorRegistrationCard( "030205-1000000" );
		final Set<ConstraintViolation<KorRegistrationCard>> violations = validator.validate( korRegistrationCard );
		assertNoViolations( violations );
	}

	@Test
	public void testKorRRNInvalid() {
		final KorRegistrationCard korRegistrationCard = new KorRegistrationCard( "999999-1063015" );
		final Set<ConstraintViolation<KorRegistrationCard>> violations = validator.validate( korRegistrationCard );
		assertThat( violations )
				.containsOnlyViolations( violationOf( KorRRN.class )
						.withMessage( "invalid Korean resident registration number (KorRRN)" ) );
	}

	private static class KorRegistrationCard {

		@KorRRN
		private final String rrn;

		public KorRegistrationCard(String rrn) {
			this.rrn = rrn;
		}
	}
}
