package in.fabits.fabits.api;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Preferences {

    public static String POST = "posts";
    public static String POST_TRENDING = "trending";
    public static String POST_LATEST = "feeds";
    public static String PROFILE = "profiles";
    public static String PROFILE_POSTS = "profiles_posts";

    public static String LIST = "lists";
    public static String HCOUNT = "HCOUNT";
    public static String NCOUNT = "NCOUNT";
    public static String CCOUNT = "CCOUNT";
    public static String CHAT_LIST = "chat_lists2";
    public static String MESSAGES = "message";
    public static String COMMENT = "comment";
    public static String LIKES = "likes";
    public static String SUGGESTION = "Suggestions";
    public static String ONLINE = "online_list";
    public static String NOTIFICATION = "notification";

    public static String POSTS = "posts";

    public static String SEARCH = "SEARCH";
    public static String S_USERNAME = "S_USERNAME";
    public static String S_STATUS = "S_STATUS";
    public static String S_EMAIL = "S_EMAIL";
    public static String S_FACEBOOK = "S_FACEBOOK";
    public static String S_WHATSAPP = "S_WHATSAPP";
    public static String S_LOCATION = "S_LOCATION";
    public static String S_RELATIONSHIP = "S_RELATIONSHIP";
    public static String S_PHONE = "S_PHONE";
    public static String S_OTP = "S_OTP";
    public static String S_P_PHONE = "S_P_PHONE";
    public static String S_P_FOLLOWERS = "S_P_FOLLOWERS";
    public static String S_P_FOLLOWING = "S_P_FOLLOWING";
    public static String S_P_FACEMATCH = "S_P_FACEMATCH";
    public static String S_A_LOGIN = "S_A_LOGIN";
    public static String S_A_MESSAGE = "S_A_MESSAGE";
    public static String S_A_NOTIFICATION = "S_A_NOTIFICATION";
    public static String S_A_ANONY_MESSAGE = "S_A_ANONY_MESSAGE";
    public static String S_BLOCKLIST = "S_BLOCKLIST";

    public static String SEEN = "SEEN";
    public static String FOLLOWING = "FOLLOWING";
    public static String GREEN = "GREEN";
    private static String BLOCK = "BLOCK";


    public static void saveSearchPosts(JSONArray data, String search, Context context) {
        saveOffline(SEARCH, search, data.toString(), context);
    }


    public static void addSearchPosts(JSONArray data, String search, Context context) throws JSONException {
        String dataPrev = getSavedSearchPosts(search, context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), data);
        saveOffline(SEARCH, search, arr.toString(), context);
    }

    public static String getSavedSearchPosts(String search, Context context) {
        return getSavedOffline(SEARCH, search, context);

    }


    public static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    public static void saveSetting(String Key, String data, Context context) {
        saveOffline(getDefaultSharedPreferencesName(context), Key, data, context);
    }

    public static String getSavedSeen(String CID, Context context) {
        return getSavedOffline(LIST, SEEN + CID, context);
    }

    public static void saveCount(String Key, String data, Context context) {
        saveOffline(LIST, Key, data, context);
    }

    public static String getCount(String key, Context context) {
        return getSavedOffline(LIST, key, context);
    }


    public static void saveSeen(String CID, String data, Context context) {
        saveOffline(LIST, SEEN + CID, data, context);
    }


    public static String getSavedFollowing(Context context) {
        return getSavedOffline(LIST, FOLLOWING, context);
    }

    public static void saveFollowing(String data, Context context) {
        saveOffline(LIST, FOLLOWING, data, context);
    }

    public static void saveBlock(String data, Context context) {
        saveOffline(LIST, BLOCK, data, context);
    }
    public static String getSavedBlock(Context context) {
        return getSavedOffline(LIST, BLOCK, context);
    }






    public static String getSavedSetting(String Key, Context context) {
        return getSavedOffline(getDefaultSharedPreferencesName(context), Key, context);
    }


    public static void savePosts(int postId, String data, Context context) {
        saveOffline(POST, String.valueOf(postId), data, context);
    }

    public static String getSavedPosts(int postId, Context context) {
        return getSavedOffline(POST, String.valueOf(postId), context);
    }

    public static void saveHomePosts(JSONArray data, Context context) {
        saveOffline(POST, POST_LATEST, data.toString(), context);
    }


    public static String getSavedGreenBarStatus(String CID, Context context) {
        return getSavedOffline(LIST, GREEN + CID, context);
    }

    public static void saveGreenBarStatus(String CID, JSONArray data, Context context) {
        saveOffline(LIST, GREEN + CID, data.toString(), context);
    }


    public static void saveTrendingPosts(JSONArray data, Context context) {
        saveOffline(POST, POST_TRENDING, data.toString(), context);
    }

    public static void addHomePosts(JSONArray data, Context context) throws JSONException {
        String dataPrev = getSavedHomePosts(context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), data);
        saveOffline(POST, POST_LATEST, arr.toString(), context);
    }


    public static void addTrendingPosts(JSONArray data, Context context) throws JSONException {
        String dataPrev = getSavedTrendingPosts(context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), data);
        saveOffline(POST, POST_TRENDING, arr.toString(), context);
    }

    public static void saveConversationList(JSONArray conversations, Context context) {
        saveOffline(LIST, CHAT_LIST, conversations.toString(), context);
    }

    public static void addConversationList(JSONArray conversations, Context context) throws JSONException {
        String data = getSavedOffline(LIST, CHAT_LIST, context);
        JSONArray arr = Utils.concatArray(conversations, new JSONArray(data));
        saveConversationList(arr, context);
    }

    public static void saveOnlineList() {
    }

    public static void saveMessages(String conversation_ID, JSONArray message, Context context) throws JSONException {
        String key = MESSAGES + conversation_ID;
        String data = getSavedOffline(LIST, key, context);
        JSONArray abc = new JSONArray(data);
        JSONArray arr = Utils.concatArray(message, abc);
        saveOffline(LIST, key, arr.toString(), context);
    }

    public static void forceSaveMessages(String conversation_ID, JSONArray message, Context context) throws JSONException {
        String key = MESSAGES + conversation_ID;
        saveOffline(LIST, key, message.toString(), context);
    }

    public static void saveProfiles(String username, String data, Context context) {
        String key = PROFILE + username;
        saveOffline(PROFILE, key, data, context);
    }

    public static void saveProfilesPosts(String username, JSONArray data, Context context) {
        String key = PROFILE_POSTS + username;
        saveOffline(PROFILE, key, data.toString(), context);
    }

    public static void addProfilesPosts(String username, JSONArray data, Context context) throws JSONException {
        String key = PROFILE_POSTS + username;
        String dataPrev = getSavedProfilesPosts(username, context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), data);
        saveOffline(PROFILE, key, arr.toString(), context);
    }

    public static void saveLikeList(JSONArray likesList, String likesId, int type, Context context) throws JSONException {
        saveOffline(LIST, LIKES + likesId+type, likesList.toString(), context);
    }


    public static void addLikeList(JSONArray likesList, String likesId, int type, Context context) throws JSONException {

        String dataPrev = getSavedLikeList(likesId, type, context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), likesList);

        saveOffline(LIST, LIKES + likesId+type, arr.toString(), context);
    }



    public static void saveSuggestions(JSONArray likesList, Context context) throws JSONException {
        saveOffline(LIST, SUGGESTION, likesList.toString(), context);
    }


    public static void addSuggestions(JSONArray likesList, Context context) throws JSONException {

        String dataPrev = getSavedSuggestions(context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), likesList);

        saveOffline(LIST, SUGGESTION, arr.toString(), context);
    }

    public static String getSavedSuggestions(Context context) {
        return getSavedOffline(LIST, SUGGESTION, context);
    }


    public static void addCommentList(JSONArray commentList, String postId, Context context) throws JSONException {

        String dataPrev = getSavedCommentList(postId, context);
        JSONArray arr = Utils.concatArray(new JSONArray(dataPrev), commentList);
        saveOffline(LIST, COMMENT + postId, arr.toString(), context);
    }

    public static void saveCommentList(JSONArray commentList, String postId, Context context) {
        saveOffline(LIST, COMMENT + postId, commentList.toString(), context);
    }


    public static void addNotification(JSONArray notifications, Context context) throws JSONException {

        String dataPrev = getSavedNotification(context);
        JSONArray arr = Utils.concatArray( notifications, new JSONArray(dataPrev));
        saveOffline(LIST, NOTIFICATION, arr.toString(), context);
    }

    public static void saveNotification(JSONArray notifications, Context context) {
        saveOffline(LIST, NOTIFICATION, notifications.toString(), context);
    }

    public static String getSavedHomePosts(Context context) {
        return getSavedOffline(POST, POST_LATEST, context);

    }

    public static String getSavedTrendingPosts(Context context) {
        return getSavedOffline(POST, POST_TRENDING, context);

    }

    public static String getSavedConversationList(Context context) {
        return getSavedOffline(LIST, CHAT_LIST, context);
    }

    public static void getSavedOnlineList() {
    }

    public static String getSavedMessages(String conversation_ID, Context context) {
        String key = MESSAGES + conversation_ID;
        return getSavedOffline(LIST, key, context);
    }

    public static String getSavedProfiles(String username, Context context) {
        String key = PROFILE + username;
        return getSavedOffline(PROFILE, key, context);
    }

    public static String getSavedProfilesPosts(String username, Context context) {
        String key = PROFILE_POSTS + username;
        return getSavedOffline(PROFILE, key, context);
    }

    public static String getSavedLikeList(String likesId,int type, Context context) {
        return getSavedOffline(LIST, LIKES + likesId+type, context);
    }

    public static String getSavedCommentList(String postId, Context context) {
        return getSavedOffline(LIST, COMMENT + postId, context);
    }


    public static String getSavedNotification(Context context) {
        return getSavedOffline(LIST, NOTIFICATION, context);

    }

    private static void saveOffline(String PrefName, String key, String data, Context context) {
        SharedPreferences list = context.getSharedPreferences(PrefName, 0);
        SharedPreferences.Editor editor = list.edit();
        editor.putString(key, data);
        editor.apply();
    }

    private static String getSavedOffline(String PrefName, String key, Context context) {
        SharedPreferences list = context.getSharedPreferences(PrefName, 0);
        return list.getString(key, "[]");
    }


}
