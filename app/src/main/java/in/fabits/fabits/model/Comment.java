package in.fabits.fabits.model;


import java.net.URL;

public class Comment {

    int post_id;
    int comment_id;
    int user_id;
    String user_name;
    String username;
    String comment;
    String comment_time;
    URL user_picture;

    public Comment(int post_id, int comment_id, int user_id, String user_name, String username, String comment, String comment_time, URL user_picture) {
        this.post_id = post_id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.username = username;
        this.comment = comment;
        this.comment_time = comment_time;
        this.user_picture = user_picture;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public URL getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(URL user_picture) {
        this.user_picture = user_picture;
    }
}
