package in.fabits.fabits.model;


public class Pool {

    String name;
    String image;
    String search_id;

    public Pool(String name, String image, String search_id) {
        this.name = name;
        this.image = image;
        this.search_id = search_id;
    }

    public String getSearch_id() {

        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
