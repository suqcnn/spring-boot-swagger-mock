package com.yifanyou.sbsm.configuration.fixture;

import br.com.six2six.fixturefactory.Fixture;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yifanyou on 2017/2/22.
 */
@Component
public class FixtureService {

    public static final String VALID = "valid";

    public static final int MOCK_LIST_SIZE = 10;

    private static volatile FixtureService fixtureService;

    public static FixtureService getInstance() {
        return fixtureService;
    }

    @EventListener
    public void saveInstance(ContextRefreshedEvent event) {
        fixtureService = event.getApplicationContext().getBean(FixtureService.class);
    }

    public <T> T get(Class<T> modelType, Class apiType, String methodName, Object... params) {
        return Fixture.from(modelType).gimme(VALID);
    }

    public <T> List<T> getCollection(Class<T> modelType, Class apiType, String methodName, Object... params) {
        return Fixture.from(modelType).gimme(MOCK_LIST_SIZE, VALID);
    }
}
