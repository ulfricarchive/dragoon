package com.ulfric.dragoon.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Methods {

	private static final Map<Method, Class<?>[]> PARAMETERS = new HashMap<>();

	public static List<Method> getOverridableMethods(Class<?> type)
	{
		Map<String, List<Method>> result = new LinkedHashMap<>();

		Class<?> currentType = type;

		do
		{
			Stream.of(type.getDeclaredMethods())
					.filter(Methods::isOverridable)
					.forEach(method ->
					{
						String name = method.getName();
						List<Method> methods = result.computeIfAbsent(name, ignore -> new ArrayList<>());

						if (methods.stream()
								.noneMatch(existingMethod -> Methods.methodNamesAndParamsEqual(method, existingMethod)))
						{
							methods.add(method);
						}
					});

			currentType = currentType.getSuperclass();
		}
		while (!isRootType(currentType));

		return result.values()
				.stream()
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	private static Class<?>[] retrieveParameters(Method method)
	{
		return Methods.PARAMETERS.computeIfAbsent(method, Method::getParameterTypes);
	}

	private static boolean methodNamesAndParamsEqual(Method method1, Method method2)
	{
		boolean namesEqual = method1.getName().equals(method2.getName());

		boolean paramsEqual = Arrays.equals(
				Methods.retrieveParameters(method1),
				Methods.retrieveParameters(method2)
		);

		return namesEqual && paramsEqual;
	}

	private static boolean isOverridable(Method method)
	{
		if (method.isSynthetic())
		{
			return false;
		}

		int modifiers = method.getModifiers();

		return !Modifier.isStatic(modifiers)
				&& !Modifier.isPrivate(modifiers)
				&& !Modifier.isFinal(modifiers)
				&& !Modifier.isNative(modifiers);
	}

	private static boolean isRootType(Class<?> type)
	{
		return type == Object.class || type == null;
	}

	private Methods() { }

}