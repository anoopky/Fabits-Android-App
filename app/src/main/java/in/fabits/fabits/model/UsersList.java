package in.fabits.fabits.model;

import java.net.URL;

public class UsersList {

    int UserID;
    String Username;
    String Name;
    URL UserPicture;
    String Time;

    public UsersList(int userID, String name, String username, URL userPicture, String time) {
        UserID = userID;
        Username = username;
        Name = name;
        UserPicture = userPicture;
        Time = time;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public URL getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(URL userPicture) {
        UserPicture = userPicture;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
