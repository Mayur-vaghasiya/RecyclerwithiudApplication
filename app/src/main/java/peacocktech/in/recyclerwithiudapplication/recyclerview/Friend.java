package peacocktech.in.recyclerwithiudapplication.recyclerview;

/**
 * Created by peacock on 5/13/16.
 */
public class Friend {
    private String name;
    private String job;
    private int gender;
    private int id;

    public Friend(String name, int gender, String job) {
        this.name = name;
        this.gender = gender;
        this.job = job;
    }

    public Friend(int id, String name, int gender, String job) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.job = job;
    }

    public Friend() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setGende(int gender) {
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }
}
