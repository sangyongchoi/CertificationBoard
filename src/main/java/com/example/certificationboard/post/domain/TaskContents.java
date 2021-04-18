package com.example.certificationboard.post.domain;

import java.time.LocalDateTime;

public class TaskContents implements Contents{
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TaskContents(String title, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "TaskContents{" +
                "title='" + title + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
