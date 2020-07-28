package in.specialsoft.premarkets.Model;

public class Posts {
    private String date,description,heading,image,pid,time;


    public Posts()
    {

    }

    public Posts(String date, String description, String heading, String image, String pid, String time) {
        this.date = date;
        this.description = description;
        this.heading = heading;
        this.image = image;
        this.pid = pid;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

