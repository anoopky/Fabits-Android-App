package in.fabits.fabits.model;

import java.net.URL;

/**
 * Created by pi on 3/13/17.
 */

public class ChatList {

    String name;
    String userID;
    URL image;
    int count;
    int auth;
    int type;
    int conversation_id;
    String time;
    String time_tag;
    String message;

    public ChatList(String name, String userID, URL image, int count, int auth,
                    int conversation_id, String time, String time_tag, String message, int type) {
        this.name = name;
        this.userID = userID;
        this.image = image;
        this.count = count;

        this.auth = auth;
        this.conversation_id = conversation_id;
        this.time = time;
        this.time_tag = time_tag;
        this.message = message;
        this.type =type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_tag() {
        return time_tag;
    }

    public void setTime_tag(String time_tag) {
        this.time_tag = time_tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
