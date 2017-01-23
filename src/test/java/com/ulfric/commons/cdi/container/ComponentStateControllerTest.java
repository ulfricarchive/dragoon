package com.ulfric.commons.cdi.container;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.ulfric.commons.exception.Try;
import com.ulfric.verify.Verify;

@RunWith(JUnitPlatform.class)
public class ComponentStateControllerTest {

	private Component parent;
	private ComponentStateController controller;

	private Component toInstall;
	private Component secondInstall;

	@BeforeEach
	void init()
	{
		this.parent = Mockito.mock(Component.class);

		Mockito.when(this.parent.isLoaded()).thenReturn(true);
		Mockito.when(this.parent.isEnabled()).thenReturn(true);

		this.toInstall = Mockito.mock(Component.class);
		this.secondInstall = Mockito.mock(Component.class);

		this.controller = new ComponentStateController(this.parent);
	}

	@Test
	void testInstall_nullValue()
	{
		Verify.that(() -> this.controller.install(null)).doesThrow(NullPointerException.class);
	}

	@Test
	void testInstall_notLoaded()
	{
		Mockito.when(this.toInstall.isLoaded()).thenReturn(false);
		Mockito.when(this.toInstall.isEnabled()).thenReturn(false);

		Verify.that(() -> this.controller.install(this.toInstall)).runsWithoutExceptions();

		Set<Component> states = this.getControllerStatesField();

		Verify.that(states.contains(this.toInstall));

		this.controller.install(this.toInstall);

		Verify.that(states.size()).isEqualTo(1);
	}

	@SuppressWarnings("unchecked")
	private Set<Component> getControllerStatesField()
	{
		Field statesField = Try.to(() -> ComponentStateController.class.getDeclaredField("states"));
		statesField.setAccessible(true);

		return Try.to(() -> (Set<Component>) statesField.get(this.controller));
	}

	@Test
	void testInstall_multiple()
	{
		this.mockInstallationCandidates();

		this.controller.install(this.toInstall);
		this.controller.install(this.secondInstall);

		this.disableParent();

		boolean[] disabled = this.mockDisable();

		this.controller.refresh();

		Verify.that(disabled[0]).isTrue();
	}

	private void mockInstallationCandidates()
	{
		Mockito.when(this.toInstall.isLoaded()).thenReturn(true);
		Mockito.when(this.toInstall.isEnabled()).thenReturn(true);
		Mockito.when(this.toInstall.isDisabled()).thenReturn(false);

		Mockito.when(this.secondInstall.isLoaded()).thenReturn(false);
		Mockito.when(this.secondInstall.isEnabled()).thenReturn(false);
		Mockito.when(this.secondInstall.isDisabled()).thenReturn(true);
	}

	private boolean[] mockDisable()
	{
		final boolean[] disabled = new boolean[] { false };

		Mockito.doAnswer(ignored ->
		{
			disabled[0] = true;
			return null;
		}).when(this.toInstall).disable();

		return disabled;
	}

	private void disableParent()
	{
		Mockito.when(this.parent.isLoaded()).thenReturn(false);
		Mockito.when(this.parent.isEnabled()).thenReturn(false);
		Mockito.when(this.parent.isDisabled()).thenReturn(true);
	}

}
