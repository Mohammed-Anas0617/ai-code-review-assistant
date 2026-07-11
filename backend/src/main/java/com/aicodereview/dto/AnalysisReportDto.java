package com.aicodereview.dto;

import java.util.List;

public class AnalysisReportDto {

    private String fileName;
    private List<String> checkstyleViolations;
    private List<String> pmdViolations;
    private List<String> spotbugsViolations;

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getCheckstyleViolations() {
        return checkstyleViolations;
    }

    public void setCheckstyleViolations(List<String> checkstyleViolations) {
        this.checkstyleViolations = checkstyleViolations;
    }

    public List<String> getPmdViolations() {
        return pmdViolations;
    }

    public void setPmdViolations(List<String> pmdViolations) {
        this.pmdViolations = pmdViolations;
    }

    public List<String> getSpotbugsViolations() {
        return spotbugsViolations;
    }

    public void setSpotbugsViolations(List<String> spotbugsViolations) {
        this.spotbugsViolations = spotbugsViolations;
    }
}