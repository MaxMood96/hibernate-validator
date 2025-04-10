/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.internal.util;

/**
 * @author Hardy Ferentschik
 */
public class Node<T> {
	private T data;

	public Node(T data) {
		this.data = data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
