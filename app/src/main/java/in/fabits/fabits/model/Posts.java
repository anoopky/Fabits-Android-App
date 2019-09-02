package in.fabits.fabits.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Posts {

    private int id;
    private int user_id;
    private String user_name;
    private URL user_picture;
    private String post_text;
    private String post_time;
    private int likes;
    private int post_type;
    private int dislikes;
    private int comments;
    private int isliked;
    private int isdisliked;
    private int iscommented;
    private int isfollow;
    private String post_image;
    private String data;

    private  List<Like> like = new ArrayList<>();

    private  List<Comment> comment = new ArrayList<>();

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }


    public Posts(int id, int user_id, String user_name,
                 URL user_picture, String post_text, String post_time, int likes,
                 int dislikes, int comments, int isliked, int isdisliked, int iscommented,
                 int isfollow, String post_image, int post_type, List<Like> like, List<Comment> comment, String data
    ) {
        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_picture = user_picture;
        this.post_text = post_text;
        this.post_time = post_time;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.isliked = isliked;
        this.isdisliked = isdisliked;
        this.iscommented = iscommented;
        this.isfollow = isfollow;
        this.post_image = post_image;
        this.like = like;
        this.comment = comment;
        this.post_type = post_type;
        this.data = data;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }


    public  List<Like> getLike() {

        return like;
    }

    public  void setLike(List<Like> like) {
        this.like = like;
    }


    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public URL getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(URL user_picture) {
        this.user_picture = user_picture;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getIsliked() {
        return isliked;
    }

    public void setIsliked(int isliked) {
        this.isliked = isliked;
    }

    public int getIsdisliked() {
        return isdisliked;
    }

    public void setIsdisliked(int isdisliked) {
        this.isdisliked = isdisliked;
    }

    public int getIscommented() {
        return iscommented;
    }

    public void setIscommented(int iscommented) {
        this.iscommented = iscommented;
    }

    public int getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(int isfollow) {
        this.isfollow = isfollow;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Posts other = (Posts) obj;
        return id == other.id;
    }

}
