package in.fabits.fabits.model;

import java.net.URL;


public class Profile {

    private int id;
    private String username;
    private String name;
    private String intro;
    private String dob;
    private URL wallBig;
    private URL wallSmall;
    private URL profileBig;
    private URL profileSmall;
    private String college;
    private String branch;
    private String year;
    private int followers;
    private int following;
    private int profile_views;
    private int posts;
    private int isBlocked;
    private int isFollow;
    private String faceMatch;
    private String location;
    private String relationship;
    private long phone;


    public Profile(int id, String username, String name, String intro, String dob, URL wallBig, URL wallSmall, URL profileBig, URL profileSmall, String college, String branch, String year, int followers, int following, int profile_views, int posts, int isBlocked, int isFollow, String faceMatch, String relationship, long phone, String location) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.intro = intro;
        this.dob = dob;
        this.wallBig = wallBig;
        this.wallSmall = wallSmall;
        this.profileBig = profileBig;
        this.profileSmall = profileSmall;
        this.college = college;
        this.branch = branch;
        this.year = year;
        this.followers = followers;
        this.following = following;
        this.profile_views = profile_views;
        this.posts = posts;
        this.isBlocked = isBlocked;
        this.isFollow = isFollow;
        this.faceMatch = faceMatch;
        this.relationship = relationship;
        this.location = location;
        this.phone = phone;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public URL getWallBig() {
        return wallBig;
    }

    public void setWallBig(URL wallBig) {
        this.wallBig = wallBig;
    }

    public URL getWallSmall() {
        return wallSmall;
    }

    public void setWallSmall(URL wallSmall) {
        this.wallSmall = wallSmall;
    }

    public URL getProfileBig() {
        return profileBig;
    }

    public void setProfileBig(URL profileBig) {
        this.profileBig = profileBig;
    }

    public URL getProfileSmall() {
        return profileSmall;
    }

    public void setProfileSmall(URL profileSmall) {
        this.profileSmall = profileSmall;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getProfile_views() {
        return profile_views;
    }

    public void setProfile_views(int profile_views) {
        this.profile_views = profile_views;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public String getFaceMatch() {
        return faceMatch;
    }

    public void setFaceMatch(String faceMatch) {
        this.faceMatch = faceMatch;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
