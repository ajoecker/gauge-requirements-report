package org.gauge.plugin.requirements.reporter;

import org.gauge.plugin.requirements.model.GeneralReport;

public interface Report {
    void writeResult(GeneralReport generalReport);
}
