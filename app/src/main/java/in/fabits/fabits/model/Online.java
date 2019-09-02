package in.fabits.fabits.model;

import java.net.URL;

/**
 * Created by pi on 3/15/17.
 */

public class Online {

    int id;
    String user_name;
    String intro;
    String lastseen;
    URL user_picture;

    public Online(int id, String user_name, String intro, String lastseen, URL user_picture) {
        this.id = id;
        this.user_name = user_name;
        this.intro = intro;
        this.lastseen = lastseen;
        this.user_picture = user_picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public URL getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(URL user_picture) {
        this.user_picture = user_picture;
    }
}
