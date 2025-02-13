package com.hospital.backend.RulesConfig;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.conf.ConsequenceExceptionHandlerOption;
import org.kie.internal.io.ResourceFactory;

import java.util.List;

public class DroolsBeanFactory {
    private static final String RULES_PATH = "com/hospital/backend/rules/";
    private final KieServices kieServices;

    public DroolsBeanFactory() {
        kieServices = KieServices.Factory.get();
    }

    private KieFileSystem getKieFileSystem() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        List<String> rules = List.of("ScheduleRules_12h.drl");
        for(String rule:rules) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(rule));
        }
        return kieFileSystem;

    }

    public KieContainer getKieContainer() {
        getKieRepository();

        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();

        KieModule kieModule = kb.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());

    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
    }

    public KieSession getKieSession() {
        getKieRepository();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        kieFileSystem.write(ResourceFactory.newClassPathResource("com.hospital.backend.rules/ScheduleRules_12h.drl"));

        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();

        KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        KieBaseConfiguration kieBaseConfig = kieServices.newKieBaseConfiguration();

        kieBaseConfig.setProperty(ConsequenceExceptionHandlerOption.PROPERTY_NAME, "com.hospital.backend.RulesConfig.MyConsequenceExceptionHandler");

        return kContainer.newKieBase(kieBaseConfig).newKieSession();
    }

    public KieSession getKieSession(Resource dt) {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem()
                .write(dt);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem)
                .buildAll();

        KieRepository kieRepository = kieServices.getRepository();

        ReleaseId krDefaultReleaseId = kieRepository.getDefaultReleaseId();

        KieContainer kieContainer = kieServices.newKieContainer(krDefaultReleaseId);
        KieBaseConfiguration kieBaseConfig = kieServices.newKieBaseConfiguration();

        kieBaseConfig.setProperty(ConsequenceExceptionHandlerOption.PROPERTY_NAME, "com.hospital.backend.RulesConfig.MyConsequenceExceptionHandler");


        return kieContainer.newKieBase(kieBaseConfig).newKieSession();
//        return kieContainer.newKieSession();

    }
}
