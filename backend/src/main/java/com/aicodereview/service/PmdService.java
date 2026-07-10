package com.aicodereview.service;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.rule.RulePriority;
import net.sourceforge.pmd.reporting.Report;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PmdService {

    public List<PmdIssue> analyze(String filePath) {
        List<PmdIssue> issues = new ArrayList<>();

        PMDConfiguration config = new PMDConfiguration();
        config.addInputPath(java.nio.file.Path.of(filePath));
        config.setDefaultLanguageVersion(
                config.getLanguageRegistry()
                        .getLanguageById("java")
                        .getDefaultVersion()
        );
        config.setMinimumPriority(RulePriority.LOW);
        config.addRuleSet("category/java/bestpractices.xml");
        config.addRuleSet("category/java/codestyle.xml");
        config.addRuleSet("category/java/design.xml");
        config.addRuleSet("category/java/errorprone.xml");
        config.addRuleSet("category/java/performance.xml");
        config.addRuleSet("category/java/security.xml");

        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {
            Report report = pmd.performAnalysisAndCollectReport();
            for (RuleViolation violation : report.getViolations()) {
                issues.add(new PmdIssue(
                        violation.getRule().getName(),
                        violation.getDescription(),
                        violation.getBeginLine(),
                        violation.getFileId().getFileName(),
                        violation.getRule().getPriority().toString()
                ));
            }
        }

        return issues;
    }

    public record PmdIssue(
            String ruleName,
            String message,
            int line,
            String fileName,
            String priority
    ) {}
}