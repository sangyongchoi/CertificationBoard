package com.example.certificationboard.post.domain;

import com.example.certificationboard.post.exception.NotAllowedValueException;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskContents implements Contents{

    private final String title;
    @Enumerated(EnumType.STRING)
    private final Status taskStatus;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<String> managers;
    private final Priority priority;
    private final Integer progress;
    private final Long taskNumber;
    private final String context;

    public TaskContents(String title, Status taskStatus, LocalDateTime startDate
            , LocalDateTime endDate, List<String> managers, Priority priority
            , Integer progress, Long taskNumber, String context) {
        if (progress < 0 || progress > 100) {
            throw new NotAllowedValueException("진척도는 0이상 100이하의 값만 가능합니다.");
        }

        if (startDate != null && endDate != null) {
            if (startDate.compareTo(endDate) > 0) {
                throw new NotAllowedValueException("시작일자는 종료일자 이후 날짜로 설정할 수 없습니다.");
            }
        }

        this.title = title;
        this.taskStatus = taskStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managers = managers == null ? new ArrayList<>() : managers;
        this.priority = priority;
        this.progress = progress;
        this.taskNumber = taskNumber;
        this.context = context;
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

    public Long getTaskNumber() {
        return taskNumber;
    }

    public String getContext() {
        return context;
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
                ", taskNumber=" + taskNumber +
                ", context='" + context + '\'' +
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
