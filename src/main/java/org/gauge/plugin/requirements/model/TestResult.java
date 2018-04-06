package org.gauge.plugin.requirements.model;

import com.thoughtworks.gauge.Spec;

public class TestResult {
    private String id;
    private String heading;
    private long executionTime;
    private Spec.ExecutionStatus status;

    private TestResult(String id) {
        this.id = id;
    }

    public boolean isPassed() {
        return status == Spec.ExecutionStatus.PASSED;
    }

    public static final class TestResultBuilder {
        private TestResult tr;

        public static TestResultBuilder newTestResult(String id) {
            TestResultBuilder testResultBuilder = new TestResultBuilder();
            testResultBuilder.tr = new TestResult(id);
            return testResultBuilder;
        }

        public TestResultBuilder titled(String heading) {
            tr.heading = heading;
            return this;
        }

        public TestResultBuilder time(long executionTime) {
            tr.executionTime = executionTime;
            return this;
        }

        public TestResultBuilder status(Spec.ExecutionStatus status) {
            tr.status = status;
            return this;
        }

        public TestResult done() {
            return tr;
        }
    }
}
