package com.yifanyou.sbsm.configuration.fixture;

import br.com.six2six.fixturefactory.Rule;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by yifanyou on 2017/2/20.
 */
public interface FixtureRule {
    boolean applyRule(Rule rule, Class clazz, Field field, Set<Class<?>> classes);
}
