package br.com.caelum.vraptor.interceptor;

import static java.util.Arrays.asList;
import static java.util.Collections.disjoint;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.Dependent;

import br.com.caelum.vraptor.controller.ControllerInstance;
import br.com.caelum.vraptor.controller.ControllerMethod;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * Verify if certain annotations are presents in class or method.
 *
 * @author Alberto Souza
 *
 */
@Dependent
public class WithAnnotationAcceptor implements AcceptsValidator<AcceptsWithAnnotations> {

	private List<Class<? extends Annotation>> allowedTypes;

	@Override
	public boolean validate(ControllerMethod controllerMethod, ControllerInstance instance) {
		return containsAllowedTypes(instance.getBeanClass().getAnnotations()) 
				|| containsAllowedTypes(controllerMethod.getAnnotations());
	}
	
	private boolean containsAllowedTypes(Annotation[] annotations) {
		Collection<Class<? extends Annotation>> currentTypes = FluentIterable.from(asList(annotations))
				.transform(new AnnotationInstanceToType())
				.toList();

		return !disjoint(allowedTypes, currentTypes);
	}

	@Override
	public void initialize(AcceptsWithAnnotations annotation) {
		this.allowedTypes = asList(annotation.value());
	}

	private class AnnotationInstanceToType implements Function<Annotation, Class<? extends Annotation>> {

		@Override
		public Class<? extends Annotation> apply(Annotation input) {
			return input.annotationType();
		}
	}
}
