package in.fabits.fabits.model;


public class Search {

    String name;
    int id;
    String username;
    String intro;
    String image;


    public Search(String name, int id, String username, String intro, String image) {
        this.name = name;
        this.id = id;
        this.username = username;
        this.intro = intro;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
