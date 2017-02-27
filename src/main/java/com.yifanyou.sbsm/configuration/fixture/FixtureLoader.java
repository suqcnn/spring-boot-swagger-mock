package com.yifanyou.sbsm.configuration.fixture;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import com.yifanyou.sbsm.configuration.fixture.FixtureRule;
import com.yifanyou.sbsm.configuration.fixture.FixtureSettings;
import com.yifanyou.sbsm.domain.Response;
import com.yifanyou.sbsm.domain.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yifanyou.sbsm.configuration.fixture.FixtureService.MOCK_LIST_SIZE;
import static com.yifanyou.sbsm.configuration.fixture.FixtureService.VALID;
import static java.lang.String.format;

/**
 * Created by yifanyou on 2017/2/25.
 */
@Component
@EnableConfigurationProperties(FixtureSettings.class)
public class FixtureLoader {
    private static final Logger logger = LoggerFactory.getLogger(FixtureLoader.class);


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private List<FixtureRule> fixtureRules;

    @Autowired
    private FixtureSettings fixtureSettings;

//    private String resolvedPath = resolvePackageToScan("com.xforceplus.imsc.common.model");
//    private Fairy fairy = Fairy.create();//Locale.SIMPLIFIED_CHINESE);

    @PostConstruct
    public void load() {
        SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(applicationContext);
        try {
            Set<Class<?>> classes = fixtureSettings.getModelPackages().stream()
                    .flatMap(path -> {
                        try {
                            return Arrays.stream(applicationContext.getResources(path));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).filter(Resource::isReadable)
                    .map(resource -> {
                        MetadataReader metadataReader = null;
                        try {
                            metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return Tuple2.of(metadataReader.getClassMetadata(), metadataReader.getAnnotationMetadata());
                    })
//                    .peek(tuple -> logger.info("resource: " + tuple))
                    .filter(tuple2 -> tuple2._1.isConcrete())
//                            && tuple2._2.hasAnnotation("io.swagger.annotations.ApiModel"))
//                            && tuple2._2.hasAnnotation("javax.annotation.Generated"))
//                    .peek(tuple -> logger.info("filtered: " + tuple))
                    .map(tuple2 -> {
                        try {
                            return ClassUtils.forName(tuple2._1.getClassName(), null);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toSet());

            classes.add(Response.class);
            classes.add(File.class);

            classes.stream().forEach(clazz -> {
                Rule rule = new Rule();
                ReflectionUtils.doWithFields(clazz, f -> {
                    this.createRuleByField(rule, clazz, f, classes);
                });
                Fixture.of(clazz).addTemplate("valid", rule);
            });
//            Rule rule = new Rule();
//            createRuleByField(rule, Response.class, Response.class.getDeclaredField("code"), classes);
//            createRuleByField(rule, Response.class, Response.class.getDeclaredField("message"), classes);
//            createRuleByField(rule, Response.class, Response.class.getDeclaredField("result"), classes);
//            Fixture.of(Response.class).addTemplate("valid", rule);
        } catch (Exception e) {
            throw new RuntimeException(format("Cannot scan package '%s' for classes.", fixtureSettings.getModelPackages()), e);
        }
//        catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }

//        Rule rule = new Rule();
//        rule.add("code", rule.random(1));
//        rule.add("message", rule.name());
//        rule.add("result", rule.name());
//        Fixture.of(Response.class).addTemplate("valid", rule);
    }

    private void createRuleByField(Rule rule, Class clazz, Field field, Set<Class<?>> classes) {
        if (fixtureRules != null) {
            for (FixtureRule fixtureRule : fixtureRules) {
                if (fixtureRule.applyRule(rule, clazz, field, classes)) {
                    return;
                }
            }
        }
        Class<?> fieldType = field.getType();
        if (Number.class.isAssignableFrom(fieldType)) {
            if (Integer.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                rule.add(field.getName(), rule.random(fieldType, rule.range(1, 1000)));
            } else {
                rule.add(field.getName(), rule.random(fieldType, rule.range(0, 1000000)));
            }
        } else if (CharSequence.class.isAssignableFrom(fieldType)) {
            rule.add(field.getName(), rule.name());
        } else if (classes.contains(fieldType)) {
            rule.add(field.getName(), rule.one(fieldType, VALID));
        } else if (Collection.class.isAssignableFrom(fieldType)) {
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Arrays.stream(parameterizedType.getActualTypeArguments()).findFirst().ifPresent(typeArgument -> {
                    Class<?> elementType = (Class<?>) typeArgument;
                    if (Number.class.isAssignableFrom(elementType)) {
                        if (Integer.class.isAssignableFrom(elementType) || Long.class.isAssignableFrom(elementType)) {
                            rule.add(field.getName(), rule.random(fieldType, rule.range(1, 1000)));
                        } else {
                            rule.add(field.getName(), rule.random(fieldType, rule.range(0, 1000000)));
                        }
                    } else if (CharSequence.class.isAssignableFrom(elementType)) {
                        rule.add(field.getName(), rule.name());
                    } else if (classes.contains(elementType)) {
                        rule.add(field.getName(), rule.has(MOCK_LIST_SIZE).of(elementType, "valid"));
                    }
                });
            }
        }
    }

//    /**
//     * Converts the scanPackage to something that the resource loader can handleRequest.
//     *
//     * @param scanPackage
//     */
//    private String resolvePackageToScan(String scanPackage) {
//        return "classpath*:" + convertClassNameToResourcePath(scanPackage) + "/**/*.class";
//    }
}
