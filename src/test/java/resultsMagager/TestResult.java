package resultsMagager;

public class TestResult {
    private final String parent;
    private final String testName;
    private final String status;
    private final String throwable;

    public TestResult(String parent, String testName, String status, String throwable) {
        this.parent = parent;
        this.testName = testName;
        this.status = status;
        this.throwable = throwable;
    }

    public String getParent() {
        return parent;
    }

    public String getTestName() {
        return testName;
    }

    public String getStatus() {
        return status;
    }

    public String getThrowable() {
        return throwable;
    }
}