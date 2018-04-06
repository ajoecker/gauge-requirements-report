package org.gauge.plugin.requirements.model;

import com.thoughtworks.gauge.Spec;

import java.util.ArrayList;
import java.util.List;

public class RequirementResult {
    private String id = "";
    private String link = "";
    private List<TestResult> testResults = new ArrayList<>();
    private int total = 0;
    private int passed = 0;

    public void setId(String id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void addResult(TestResult result) {
        testResults.add(result);
        total++;
        if (result.isPassed()) {
            passed++;
        }
    }

    public String getId() {
        return id;
    }
}
