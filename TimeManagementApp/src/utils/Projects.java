package utils;

public class Projects {

    private String title;
    private String password;

    public Projects(String title, String password) {
        this.title = title;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return title;
    }
    
}
