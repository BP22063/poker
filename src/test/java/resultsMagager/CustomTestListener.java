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
            results.add(new TestResult(getClassName(testIdentifier) ,testIdentifier.getDisplayName(),testExecutionResult.getStatus().toString(), testExecutionResult.getThrowable().toString()));
        }
    }

    public List<TestResult> getResults() {
        return results;
    }

    private String getClassName(TestIdentifier testIdentifier){
        return testIdentifier.getParentId().toString().replaceAll(".*\\[class:([^\\]]+)\\].*", "$1");
    }

}
