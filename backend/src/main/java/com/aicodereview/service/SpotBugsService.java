package com.aicodereview.service;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.SortedBugCollection;
import edu.umd.cs.findbugs.config.UserPreferences;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotBugsService {

    public List<SpotBugsIssue> analyze(String classFilePath) throws Exception {
        List<SpotBugsIssue> issues = new ArrayList<>();

        Project project = new Project();
        project.addFile(classFilePath);
        String javaHome = System.getProperty("java.home");
        project.addAuxClasspathEntry(javaHome + "/lib/jrt-fs.jar");

        FindBugs2 findBugs = new FindBugs2();
        edu.umd.cs.findbugs.BugCollectionBugReporter bugReporter = new edu.umd.cs.findbugs.BugCollectionBugReporter(project);
        bugReporter.setPriorityThreshold(edu.umd.cs.findbugs.Priorities.LOW_PRIORITY);

        findBugs.setBugReporter(bugReporter);
        findBugs.setProject(project);
        findBugs.setUserPreferences(UserPreferences.createDefaultUserPreferences());
        findBugs.setDetectorFactoryCollection(edu.umd.cs.findbugs.DetectorFactoryCollection.instance());

        findBugs.execute();

        for (BugInstance bug : bugReporter.getBugCollection().getCollection()) {
            issues.add(new SpotBugsIssue(
                    bug.getType(),
                    bug.getMessageWithoutPrefix(),
                    bug.getPriorityString(),
                    bug.getPrimarySourceLineAnnotation() != null
                            ? bug.getPrimarySourceLineAnnotation().getStartLine()
                            : -1
            ));
        }

        return issues;
    }

    public record SpotBugsIssue(
            String type,
            String message,
            String priority,
            int line
    ) {}
}