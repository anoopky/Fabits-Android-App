package in.fabits.fabits.model;



public class Seen {

    int UserLastDelivered;
    int UserLastRead;
    String UserLastSeen;
    int userID ;
    int ConversationID ;
    String image;


    public Seen(){
    }

    public int getConversationID() {
        return ConversationID;
    }

    public void setConversationID(int conversationID) {
        ConversationID = conversationID;
    }

    public int getUserLastDelivered() {
        return UserLastDelivered;
    }

    public void setUserLastDelivered(int userLastDelivered) {
        UserLastDelivered = userLastDelivered;
    }

    public int getUserLastRead() {
        return UserLastRead;
    }

    public void setUserLastRead(int userLastRead) {
        UserLastRead = userLastRead;
    }

    public String getUserLastSeen() {
        return UserLastSeen;
    }

    public void setUserLastSeen(String userLastSeen) {
        UserLastSeen = userLastSeen;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
