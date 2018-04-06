package org.gauge.plugin.requirements.reporter;

import com.google.gson.Gson;
import org.gauge.plugin.requirements.model.GeneralReport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONReport extends AbstractFileReport {
    @Override
    protected String fileExtension() {
        return ".json";
    }

    @Override
    protected void writeResultToFile(GeneralReport generalReport, Path file) {
        try {
            Files.write(file, new Gson().toJson(generalReport).getBytes());
            System.out.println("Writing done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

