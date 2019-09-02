package in.fabits.fabits.model;

import java.net.URL;

/**
 * Created by pi on 3/13/17.
 */

public class Like {


    int user_id;
    String user_name;
    String username;
    URL user_picture;

    public Like(int user_id, String user_name, String username, URL user_picture) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.username = username;
        this.user_picture = user_picture;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URL getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(URL user_picture) {
        this.user_picture = user_picture;
    }
}
