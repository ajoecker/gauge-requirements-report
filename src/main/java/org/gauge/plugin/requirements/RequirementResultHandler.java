package org.gauge.plugin.requirements;

import com.google.common.base.Splitter;
import com.thoughtworks.gauge.Messages;
import com.thoughtworks.gauge.Spec;
import org.gauge.plugin.requirements.model.GeneralReport;
import org.gauge.plugin.requirements.model.RequirementResult;
import org.gauge.plugin.requirements.model.TestResult;
import org.gauge.plugin.requirements.reporter.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.newHashSet;
import static org.gauge.plugin.requirements.model.TestResult.TestResultBuilder.newTestResult;

final class RequirementResultHandler {
    private Map<String, RequirementResult> id2Result = new HashMap<>();

    void handleResult(Messages.SuiteExecutionResult suiteExecutionResult, Report reportCreator) {
        Spec.ProtoSuiteResult suiteResult = suiteExecutionResult.getSuiteResult();

        List<Spec.ProtoScenario> scenarios = retrieveScenarios(suiteResult);

        groupResultsByTag(scenarios);

        writeReport(id2Result, reportCreator);
    }

    private void groupResultsByTag(List<Spec.ProtoScenario> scenarios) {
        scenarios.stream().forEach(protoScenario -> {
            List<String> tags = protoScenario.getTagsList().stream().filter(tag -> isRequirement(tag)).collect(Collectors.toList());
            tags.stream().forEach(tag -> createRequirementResult(tag, createTestResult(protoScenario)));
        });
    }

    private List<Spec.ProtoScenario> retrieveScenarios(Spec.ProtoSuiteResult suiteResult) {
        return suiteResult.getSpecResultsList().stream()
                    .map(Spec.ProtoSpecResult::getProtoSpec)
                    .flatMap(ps -> ps.getItemsList().stream())
                    .filter(protoItem -> protoItem.getItemType() == Spec.ProtoItem.ItemType.Scenario)
                    .map(Spec.ProtoItem::getScenario).collect(Collectors.toList());
    }

    private boolean isRequirement(String tag) {
        return tag.matches(System.getenv("gauge.requirements.pattern"));
    }

    private void createRequirementResult(String tag, TestResult testResult) {
        RequirementResult res = id2Result.getOrDefault(tag, new RequirementResult());
        res.setId(tag);
        String requirementsLink = System.getenv("gauge.requirements.link");
        if (!"".equals(requirementsLink)) {
            requirementsLink = requirementsLink.endsWith("/") ? requirementsLink : requirementsLink + "/";
            res.setLink(requirementsLink + tag);
        }
        res.addResult(testResult);
        id2Result.put(tag, res);
    }

    private TestResult createTestResult(Spec.ProtoScenario protoScenario) {
        return newTestResult(protoScenario.getID())
                .titled(protoScenario.getScenarioHeading())
                .time(protoScenario.getExecutionTime())
                .status(protoScenario.getExecutionStatus())
                .done();
    }

    private void writeReport(Map<String, RequirementResult> id2Result, Report reportCreator) {
        GeneralReport generalReport = new GeneralReport();
        Set<String> allTagsInScenarios = id2Result.keySet();

        String allRelevantTags = System.getenv("gauge.requirements.ids");
        if (!"".equals(allRelevantTags)) {
            Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(allRelevantTags);
            generalReport.setMissingIds(difference(newHashSet(split), allTagsInScenarios));
        }

        generalReport.setRequirements(id2Result.values());
        reportCreator.writeResult(generalReport);
    }
}
