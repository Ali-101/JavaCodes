package utils;

public class Tasks {

    private String taskTitle;
    private String desc;
    private String date;
    private int priority;
    private boolean status;
    private String projectTitle;

    public Tasks(String taskTitle, String desc, String date, int priority, boolean status, String projectTitle) {
        this.taskTitle = taskTitle;
        this.desc = desc;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.projectTitle = projectTitle;
    }

    public Tasks() {

    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getStatus() {
        return status;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPriority(int priority) {
        if (priority == 1 || priority == 2 || priority == 3 || priority == 4 || priority == 5) {
            this.priority = priority;
        } else {
            this.priority = 0;
        }
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
