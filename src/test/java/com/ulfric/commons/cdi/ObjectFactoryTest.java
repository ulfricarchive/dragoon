package com.ulfric.commons.cdi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.ulfric.verify.Verify;

@RunWith(JUnitPlatform.class)
public class ObjectFactoryTest {

	private ObjectFactory factory;

	@BeforeEach
	void init()
	{
		this.factory = ObjectFactory.newInstance();
	}

	@Test
	void testNewInstance()
	{
		Verify.that(ObjectFactory::newInstance).suppliesUniqueValues();
	}

	@Test
	void testSubfactory()
	{
		Verify.that(this.factory::subfactory).suppliesUniqueValues();
	}

	@Test
	void testHasParent_subfactory_isTrue()
	{
		Verify.that(this.factory.subfactory().hasParent()).isTrue();
	}

	@Test
	void testHasParent_root_isFalse()
	{
		Verify.that(this.factory.hasParent()).isFalse();
	}

	@Test
	void testBind_null_throwsNPE()
	{
		Verify.that(() -> this.factory.bind(null)).doesThrow(NullPointerException.class);
	}

	@Test
	void testBind_nonnull_isNotNull()
	{
		Verify.that(this.factory.bind(Hello.class)).isNotNull();
	}

	@Test
	void testRequest_null_throwsNPE()
	{
		Verify.that(() -> this.factory.request(null)).doesThrow(NullPointerException.class);
	}

	@Test
	void testRequest_nonnullButEmpty_null()
	{
		Verify.that(this.factory.request(Hello.class)).isNull();
	}

	@Test
	void testRequest_nonnull_nonnull()
	{
		this.factory.bind(Hello.class).to(HelloImpl.class);
		Verify.that(this.factory.request(Hello.class)).isNotNull();
	}

	interface Hello
	{
		
	}

	class HelloImpl implements Hello
	{
		
	}

}