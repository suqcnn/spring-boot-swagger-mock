package com.yifanyou.sbsm.configuration.fixture;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yifanyou on 2017/2/25.
 */
@ConfigurationProperties("xplat.fixture")
public class FixtureSettings {

    private List<String> modelPackages = new ArrayList<>();

    public List<String> getModelPackages() {
        return modelPackages;
    }

    public void setModelPackages(List<String> modelPackages) {
        this.modelPackages = modelPackages;
    }
}
