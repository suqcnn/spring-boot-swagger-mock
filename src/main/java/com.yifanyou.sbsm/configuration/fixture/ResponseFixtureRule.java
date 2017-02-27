package com.yifanyou.sbsm.configuration.fixture;

import br.com.six2six.fixturefactory.Rule;
import com.yifanyou.sbsm.configuration.fixture.FixtureRule;
import com.yifanyou.sbsm.domain.Response;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by yifanyou on 2017/2/20.
 */
@Component
public class ResponseFixtureRule implements FixtureRule {

    @Override
    public boolean applyRule(Rule rule, Class clazz, Field field, Set<Class<?>> classes) {
        if (Response.class.isAssignableFrom(clazz)) {
            if ("code".equals(field.getName())) {
                rule.add(field.getName(), rule.random(1, 1, 1, 1, 0));
            } else if ("message".equals(field.getName())) {
                rule.add(field.getName(), rule.random("ok", "success", "处理成功", "处理失败"));
            } else if ("result".equals(field.getName())) {
                rule.add(field.getName(), rule.regex("\\d{10}"));
            }
            return true;
        }
        return false;
    }
}
