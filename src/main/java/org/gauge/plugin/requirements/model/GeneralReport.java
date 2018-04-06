package org.gauge.plugin.requirements.model;

import com.google.common.collect.Sets;

import java.util.*;

public class GeneralReport {
    private List<RequirementResult> requirements = new ArrayList<>();
    private Set<String> missingIds = new HashSet<>();

    public void setRequirements(Collection<RequirementResult> requirements) {
        this.requirements = new ArrayList<>(requirements);
    }

    public void setMissingIds(Set<String> missingIds) {
        this.missingIds = missingIds;
    }
}
