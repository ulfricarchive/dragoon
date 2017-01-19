package com.ulfric.commons.cdi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.ulfric.verify.Verify;

@RunWith(JUnitPlatform.class)
public class InjectorTest {

	private ObjectFactory factory;
	private Injector injector;

	@BeforeEach
	void init()
	{
		this.factory = ObjectFactory.newInstance();
		this.injector = new Injector(this.factory);
	}

	@Test
	void testNew()
	{
		Verify.that(this.injector).isNotNull();
	}

}