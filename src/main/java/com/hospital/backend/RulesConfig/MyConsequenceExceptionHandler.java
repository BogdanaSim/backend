package com.hospital.backend.RulesConfig;

import com.hospital.backend.Exceptions.InvalidSolutionException;
import org.kie.api.runtime.rule.ConsequenceExceptionHandler;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;
import org.kie.api.runtime.rule.RuleRuntime;

public class MyConsequenceExceptionHandler implements ConsequenceExceptionHandler {
    @Override
    public void handleException(Match match, RuleRuntime ruleRuntime, Exception e) {
        Throwable rootCause = getRootCause(e);
        if (rootCause instanceof IllegalArgumentException) {
            System.out.println("Error occurred: " + rootCause.getMessage());
//            // Retry the rules by reasserting the fact(s) associated with the match
//            for (FactHandle factHandle : match.getFactHandles()) {
//                Object fact = ruleRuntime.getObject(factHandle);
//                ruleRuntime.insert(fact);
         //   }
        } else {
            // For other exceptions, print the stack trace
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
