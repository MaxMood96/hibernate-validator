/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.internal.engine.traversableresolver;

import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;

/**
 * @author Emmanuel Bernard
 */
@GroupSequence({ Suit.class, Cloth.class })
public class Suit {

	@Max(value = 50, groups = { Default.class, Cloth.class })
	@Min(1)
	private Integer size;
	@Valid
	private Trousers trousers;
	private Jacket jacket;

	public Trousers getTrousers() {
		return trousers;
	}

	public void setTrousers(Trousers trousers) {
		this.trousers = trousers;
	}

	@Valid
	public Jacket getJacket() {
		return jacket;
	}

	public void setJacket(Jacket jacket) {
		this.jacket = jacket;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
