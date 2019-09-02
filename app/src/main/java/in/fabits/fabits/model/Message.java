package in.fabits.fabits.model;


import java.net.URL;

public class Message {

    int id;
    int ConversationID;
    URL image;
    String UserId;
    String Message;
    String Time;
    boolean isMe;

    public Message( int id,int ConversationID, String UserId, URL image, String message,String Time, boolean isMe) {
        this.id=id;
        this.ConversationID=ConversationID;
        this.image = image;
        this.UserId = UserId;
        Message = message;
        this.Time =Time;
        this.isMe=isMe;
    }

    public int getConversationID() {
        return ConversationID;
    }

    public void setConversationID(int conversationID) {
        ConversationID = conversationID;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public boolean IsMe() {
        return isMe;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
