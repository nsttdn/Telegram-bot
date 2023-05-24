package com.example.demo.models;

public class Task {
    private int id;
    private String content;
    private String deadline;

    public Task(String content, String deadline) {
        this.content = content;
        this.deadline = deadline;
    }
    public Task(int id, String content, String deadline){
        this.id = id;
        this.content = content;
        this.deadline = deadline;
    }
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", deadline='" + deadline + '\'' +
                '}';
    }
}