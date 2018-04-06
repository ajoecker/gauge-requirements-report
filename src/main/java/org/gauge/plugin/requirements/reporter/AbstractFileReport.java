package org.gauge.plugin.requirements.reporter;

import org.gauge.plugin.requirements.model.GeneralReport;
import org.gauge.plugin.requirements.reporter.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractFileReport implements Report {
    /**
     * Returns the file extension of the reporter with trailing .
     *
     * @return the file extension with trailing ., like <code>.xml</code>
     */
    protected abstract String fileExtension();

    @Override
    public final void writeResult(GeneralReport generalReport) {
        try {
            writeResultToFile(generalReport, getTargetFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void writeResultToFile(GeneralReport generalReport, Path file);

    private Path getTargetFile() throws IOException {
        String root = System.getenv("GAUGE_PROJECT_ROOT");
        String reportsDir = System.getenv("gauge_reports_dir");
        Path dest = Paths.get(root, reportsDir, "requirements");
        Boolean overwrite;
        try {
            overwrite = Boolean.parseBoolean(System.getenv("overwrite_reports"));
        } catch (Exception e) {
            overwrite = true; //default
        }

        if (!overwrite) {
            // Do not overwrite previous report
            // Gauge convention is that when overwrite=false, generate a report in a timestamped subdirectory
            dest = dest.resolve(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss")));
        }
        Files.createDirectories(dest);
        return dest.resolve("result" + fileExtension()).normalize();
    }
}
