package in.fabits.fabits.model;

import java.net.URL;

public class Notification {
    int id;
    String message;
    URL image;
    int type;
    String source_id;
    int activity_type;

    public Notification(int id, String message, URL image, int type, String source_id, int activity_type) {
        this.id = id;
        this.message = message;
        this.image = image;
        this.type = type;
        this.source_id = source_id;
        this.activity_type = activity_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public int getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(int activity_type) {
        this.activity_type = activity_type;
    }
}
