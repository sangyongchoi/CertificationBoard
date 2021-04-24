package com.example.certificationboard.post.domain;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskContents implements Contents{

    private String title;
    @Enumerated(EnumType.STRING)
    private Status taskStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> managers;
    private Priority priority;
    private Integer progress;

    public TaskContents(String title, Status taskStatus, LocalDateTime startDate, LocalDateTime endDate, List<String> managers, Priority priority, Integer progress) {
        this.title = title;
        this.taskStatus = taskStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managers = managers == null ? new ArrayList<>() : managers;
        this.priority = priority;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public List<String> getManagers() {
        return managers;
    }

    public Priority getPriority() {
        return priority;
    }

    public Integer getProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return "TaskContents{" +
                "title='" + title + '\'' +
                ", taskStatus=" + taskStatus +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", managers=" + managers +
                ", priority=" + priority +
                ", progress=" + progress +
                '}';
    }

    public enum Status{
        REQUEST
        , GOING
        , FEEDBACK
        , COMPLETE
        , HOLD
    }

    public enum Priority{
        NORMAL
        , ROW
        , USUALLY
        , HIGH
        , EMERGENCY
    }
}
