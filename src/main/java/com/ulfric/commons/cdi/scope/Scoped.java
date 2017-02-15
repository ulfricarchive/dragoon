package com.ulfric.commons.cdi.scope;

import java.util.NoSuchElementException;

public final class Scoped<T> {

	public static <R> Scoped<R> createEmptyScope(Class<R> request)
	{
		return new Scoped<>(request, null);
	}

	private final Class<T> request;
	private final T value;

	private volatile boolean read;

	public Scoped(Class<T> request, T value)
	{
		this.request = request;
		this.value = value;
	}

	public boolean isRead()
	{
		return this.read;
	}

	public T read()
	{
		if (!this.isEmpty())
		{
			this.read = true;
			return this.value;
		}
		else
		{
			throw new NoSuchElementException("Could read scoped for request: " + request.getName());
		}
	}

	public boolean isEmpty()
	{
		return this.value == null;
	}

}