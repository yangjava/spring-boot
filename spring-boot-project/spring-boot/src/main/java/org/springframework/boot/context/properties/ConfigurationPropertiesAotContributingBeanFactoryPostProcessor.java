/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.generator.AotContributingBeanFactoryPostProcessor;
import org.springframework.beans.factory.generator.BeanFactoryContribution;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/**
 * {@link AotContributingBeanFactoryPostProcessor} that contributes runtime hints for
 * configuration properties-annotated types.
 *
 * @author Stephane Nicoll
 */
class ConfigurationPropertiesAotContributingBeanFactoryPostProcessor
		implements AotContributingBeanFactoryPostProcessor {

	@Override
	public BeanFactoryContribution contribute(ConfigurableListableBeanFactory beanFactory) {
		String[] beanNames = beanFactory.getBeanNamesForAnnotation(ConfigurationProperties.class);
		List<Class<?>> types = new ArrayList<>();
		for (String beanName : beanNames) {
			BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
			types.add(ClassUtils.getUserClass(beanDefinition.getResolvableType().toClass()));
		}
		if (!CollectionUtils.isEmpty(types)) {
			return new ConfigurationPropertiesReflectionHintsContribution(types);
		}
		return null;
	}

}
