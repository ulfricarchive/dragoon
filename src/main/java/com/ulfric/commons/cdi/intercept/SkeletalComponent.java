package com.ulfric.commons.cdi.intercept;

import com.ulfric.commons.cdi.container.Component;

abstract class SkeletalComponent implements Component {

	public SkeletalComponent(Component parent)
	{
		this.parent = parent;
	}

	protected final Component parent;

	@Override
	public final boolean isLoaded()
	{
		return this.parent.isLoaded();
	}

	@Override
	public final boolean isEnabled()
	{
		return this.parent.isEnabled();
	}

	@Override
	public final boolean isDisabled()
	{
		return Component.super.isDisabled();
	}

	@Override
	public void load() { }

}