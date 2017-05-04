package com.ulfric.dragoon;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ulfric.commons.exception.Try;

abstract class Child<T> {

	private static final Map<Class<?>, MethodHandle> CHILD_CONSTRUCTORS = new ConcurrentHashMap<>();

	private final T parent;

	Child()
	{
		this(null);
	}

	Child(T parent)
	{
		this.parent = parent;
	}

	final boolean hasParent()
	{
		return this.parent != null;
	}

	final T getParent()
	{
		return this.parent;
	}

	T createChild()
	{
		@SuppressWarnings("unchecked")
		T child = (T) Try.to(() -> this.getOrCreateConstructorHandle().invokeExact(this));

		return child;
	}

	private MethodHandle getOrCreateConstructorHandle()
	{
		return Child.CHILD_CONSTRUCTORS
				.computeIfAbsent(this.getClass(), ignore -> this.createConstructorHandle());
	}

	private MethodHandle createConstructorHandle()
	{
		Constructor<?> constructor = this.getConstructor();
		constructor.setAccessible(true);
		MethodHandle handle = Try.to(() -> MethodHandles.lookup().unreflectConstructor(constructor));
		handle = this.castHandle(handle);
		return handle;
	}

	private Constructor<?> getConstructor()
	{
		Class<?> thiz = this.getClass();
		return Try.to(() -> thiz.getDeclaredConstructor(thiz));
	}

	private MethodHandle castHandle(MethodHandle handle)
	{
		return handle.asType(MethodType.methodType(Object.class, Child.class));
	}

}