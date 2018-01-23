package com.qq1833111108.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.qq1833111108.core.interceptor.LocaleInterceptor;
import com.qq1833111108.core.interceptor.MaliciousRequestInterceptor;

@Configuration
public class AddInterceptor extends WebMvcConfigurerAdapter {
	@Autowired
	MaliciousRequestInterceptor maliciousRequestInterceptor;
	@Autowired
	LocaleInterceptor localeInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		String[] excludePathPatterns = { "/*.ico", "/*/api-docs", "/swagger**", "/webjars/**", "/configuration/**" };

		registry.addInterceptor(this.maliciousRequestInterceptor).addPathPatterns(new String[] { "/**" })
				.excludePathPatterns(excludePathPatterns);

		registry.addInterceptor(this.localeInterceptor).addPathPatterns(new String[] { "/**" })
				.excludePathPatterns(excludePathPatterns);

		super.addInterceptors(registry);
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/test1").setViewName("testajax/test1");
		registry.addViewController("/test2").setViewName("testajax/test2");
		registry.addViewController("/test3").setViewName("testajax/test3");
		registry.addViewController("/test4").setViewName("testajax/test4");
		registry.addViewController("/test5").setViewName("testajax/test5");
		registry.addViewController("/test6").setViewName("testajax/test6");
		registry.addViewController("/test7").setViewName("testajax/test7");
		registry.addViewController("/test8").setViewName("testajax/test8");
		super.addViewControllers(registry);
	}
}
