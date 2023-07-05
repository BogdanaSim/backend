package com.hospital.backend.RulesConfig;

import org.kie.api.runtime.rule.ConsequenceExceptionHandler;
import org.kie.api.runtime.rule.Match;
import org.kie.api.runtime.rule.RuleRuntime;

public class MyConsequenceExceptionHandler implements ConsequenceExceptionHandler {
    @Override
    public void handleException(Match match, RuleRuntime ruleRuntime, Exception e) {
        Throwable rootCause = getRootCause(e);
        if (rootCause instanceof IllegalArgumentException) {
            System.out.println("Error occurred: " + rootCause.getMessage());

        } else {
            e.printStackTrace();
        }
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }


}
