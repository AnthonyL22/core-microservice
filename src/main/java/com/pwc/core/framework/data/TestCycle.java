package com.pwc.core.framework.data;

import lombok.Data;

@Data
public class TestCycle {

    private String issueId;
    private String cycleId;
    private String assignee;
    private String assigneeType;
    private String projectId;
    private String versionId;

    public static class Builder {

        private String issueId;
        private String cycleId;
        private String assignee;
        private String assigneeType;
        private String projectId;
        private String versionId;

        public Builder setIssueId(String issueId) {
            this.issueId = issueId;
            return this;
        }

        public Builder setCycleId(String cycleId) {
            this.cycleId = cycleId;
            return this;
        }

        public Builder setAssignee(String assignee) {
            this.assignee = assignee;
            return this;
        }

        public Builder setAssigneeType(String assigneeType) {
            this.assigneeType = assigneeType;
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

        public TestCycle build() {
            TestCycle testExecute = new TestCycle();
            testExecute.issueId = this.issueId;
            testExecute.cycleId = this.cycleId;
            testExecute.assignee = this.assignee;
            testExecute.assigneeType = this.assigneeType;
            testExecute.projectId = this.projectId;
            testExecute.versionId = this.versionId;
            return testExecute;
        }

    }

}
