package resultsMagager;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.util.ArrayList;
import java.util.List;

public class CustomTestListener extends SummaryGeneratingListener implements TestExecutionListener{
    private final List<TestResult> results = new ArrayList<>();


    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        super.executionFinished(testIdentifier, testExecutionResult);
        if(testIdentifier.isTest()){
            results.add(new TestResult(testIdentifier.getDisplayName(),testExecutionResult.toString()));
        }
    }

    public List<TestResult> getResults() {
        return results;
    }

    public static class TestResult {
        private final String testName;
        private final String status;

        public TestResult(String testName, String status) {
            this.testName = testName;
            this.status = status;
        }

        public String getTestName() {
            return testName;
        }

        public String getStatus() {
            return status;
        }
    }
}
