package com.ulfric.commons.cdi.scope;

import java.util.NoSuchElementException;

public final class Scoped<T> {

	public static <R> Scoped<R> createEmptyScope()
	{
		return new Scoped<>(null);
	}
	
	private final T value;

	private volatile boolean read;

	public Scoped(T value)
	{
		this.value = value;
	}

	public boolean isRead()
	{
		return this.read;
	}

	public T read() throws NoSuchElementException
	{
		if (!this.isEmpty())
		{
			this.read = true;
			return this.value;
		}
		else
		{
			throw new NoSuchElementException();
		}
	}

	public boolean isEmpty()
	{
		return this.value == null;
	}

}