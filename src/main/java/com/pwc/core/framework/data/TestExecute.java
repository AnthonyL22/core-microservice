package com.pwc.core.framework.data;

import lombok.Data;

@Data
public class TestExecute {

    private String executionId;
    private String cycleId;
    private int status;
    private String issueId;
    private String projectId;
    private String versionId;

    public static class Builder {

        private String executionId;
        private String cycleId;
        private int status;
        private String issueId;
        private String projectId;
        private String versionId;

        public Builder setExecutionId(String executionId) {
            this.executionId = executionId;
            return this;
        }

        public Builder setCycleId(String cycleId) {
            this.cycleId = cycleId;
            return this;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setIssueId(String issueId) {
            this.issueId = issueId;
            return this;
        }

        public Builder setProjectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder setVersionId(String versionId) {
            this.versionId = versionId;
            return this;
        }

        public TestExecute build() {
            TestExecute testExecute = new TestExecute();
            testExecute.executionId = this.executionId;
            testExecute.cycleId = this.cycleId;
            testExecute.status = this.status;
            testExecute.issueId = this.issueId;
            testExecute.projectId = this.projectId;
            testExecute.versionId = this.versionId;
            return testExecute;
        }

    }

}
