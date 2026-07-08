package com.aicodereview.service;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class CheckstyleService {

    public List<String> analyze(File javaFile) throws Exception {
        List<String> violations = new ArrayList<>();

        // Load the rules file from resources
        ClassPathResource resource = new ClassPathResource("checkstyle-rules.xml");
        Configuration config = ConfigurationLoader.loadConfiguration(
                resource.getURL().toString(),
                new PropertiesExpander(new Properties())
        );

        Checker checker = new Checker();
        checker.setModuleClassLoader(Checker.class.getClassLoader());
        checker.configure(config);

        // Listener collects each violation Checkstyle finds
        AuditListener listener = new AuditListener() {
            @Override
            public void auditStarted(AuditEvent event) {}

            @Override
            public void auditFinished(AuditEvent event) {}

            @Override
            public void fileStarted(AuditEvent event) {}

            @Override
            public void fileFinished(AuditEvent event) {}

            @Override
            public void addError(AuditEvent event) {
                violations.add(String.format("Line %d: %s",
                        event.getLine(), event.getMessage()));
            }

            @Override
            public void addException(AuditEvent event, Throwable throwable) {
                violations.add("Exception during check: " + throwable.getMessage());
            }
        };

        checker.addListener(listener);
        checker.process(List.of(javaFile));
        checker.destroy();

        return violations;
    }
}