package in.fabits.fabits.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.fabits.fabits.model.Block;
import in.fabits.fabits.model.ChatList;
import in.fabits.fabits.model.Comment;
import in.fabits.fabits.model.Following;
import in.fabits.fabits.model.Like;
import in.fabits.fabits.model.Message;
import in.fabits.fabits.model.Notification;
import in.fabits.fabits.model.Online;
import in.fabits.fabits.model.Pool;
import in.fabits.fabits.model.Posts;
import in.fabits.fabits.model.Search;
import in.fabits.fabits.model.Seen;
import in.fabits.fabits.model.UsersList;

public class Utils {

    public static int POST_LIMIT = 5;

    public static String POST_ADDRESS = "A_POST";
    public static String TREND_ADDRESS = "T_POST";
    public static String PROFILE_ADDRESS = "P_POST";
    public static String SEARCH_ADDRESS;
    public static String UserSearch = "user";
    public static String PostSearch = "post";
    public static String PoolSearch = "pool";
    public static String profileSearch = "profile";

    public static int POST_R_L_LIMIT = 1000000;
    public static int POST_R_U_LIMIT = 10000000;

    public static List<Following> following = new ArrayList<>();
    public static List<Block> blocks = new ArrayList<>();

    public static List<Block> getBlocks() {
        return blocks;
    }

    public static void setBlocks(List<Block> blocks) {
        Utils.blocks = blocks;
    }

    public static List<Following> getFollowing() {
        return following;
    }

    public static void setFollowing(List<Following> following) {
        Utils.following = following;
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public static JSONArray concatArray(JSONArray arr1, JSONArray arr2) throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }

    public static Posts postHelper(JSONArray arr, int i, boolean isTrend) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        int id = obj.getInt("post_id");
        int user_id = obj.getInt("user_id");
        String user_name = (String) obj.get("user_name");
        String pic = (String) obj.get("user_picture");
        URI uri = new URI(pic);
        URL user_picture = uri.toURL();
        String post_text = (String) obj.get("post_text");
        String post_time = (String) obj.get("post_time");
        int likes = obj.getInt("likes");
        int dislikes = obj.getInt("dislikes");
        int comments = obj.getInt("comments");
        int isliked = obj.getInt("isliked");
        int isdisliked = obj.getInt("isdisliked");
        int iscommented = obj.getInt("iscommented");
        int isfollow = obj.getInt("isfollow");
        String post_image = null;
        int type = 0;
        JSONArray abc1 = obj.getJSONArray("post_data");


        String data = "";
        if (abc1.length() > 0) {

            JSONObject abc = abc1.getJSONObject(0);
            type = abc.getInt("type");

            if (type == 4 || type == 2 || type == 3) {
                post_image = (String) abc.get("source");
                data = abc.getString("data").equals("null") ? "" : " - " + abc.getString("data");
                if (type == 4) {
                    String youtube = "www.youtube.com/embed/";
                    int x = post_image.indexOf(youtube);
                    post_image = post_image.substring(x + youtube.length(), post_image.length());
                    int x1 = post_image.indexOf("?");
                    post_image = post_image.substring(0, x1);
                    post_image = "http://img.youtube.com/vi/" + post_image + "/0.jpg";
                }
            }

        }

        List<Like> postLikes = new ArrayList<>();
        List<Comment> postComments = new ArrayList<>();
        JSONArray likeList = obj.getJSONArray("like_all");

        for (int j = 0; j < likeList.length(); j++) {

            JSONObject obj1 = new JSONObject(String.valueOf(likeList.get(j)));
            int Luser_id = obj1.getInt("user_id");
            String Luser_name = obj1.getString("user_name");
            String Lusername = obj1.getString("username");
            String Lpic = obj1.getString("user_picture");
            URI uri1 = new URI(Lpic);
            URL Luser_picture = uri1.toURL();
            Like mlike = new Like(Luser_id, Luser_name, Lusername, Luser_picture);
            postLikes.add(mlike);
        }

        JSONArray CommentList = obj.getJSONArray("post_comment");

        for (int j = 0; j < CommentList.length(); j++) {

            JSONObject obj1 = new JSONObject(String.valueOf(CommentList.get(j)));
            int Lpost_id = obj1.getInt("post_id");
            int Lcomment_id = obj1.getInt("comment_id");
            String Lusername = obj1.getString("username");
            String Lcomment = obj1.getString("comment");
            String Lcomment_time = obj1.getString("comment_time");
            int Luser_id = obj1.getInt("user_id");
            String Luser_name = obj1.getString("user_name");
            String Lpic = obj1.getString("user_picture");
            URI uri1 = new URI(Lpic);
            URL Luser_picture = uri1.toURL();
            Comment mlike = new
                    Comment(Lpost_id, Lcomment_id, Luser_id, Luser_name,
                    Lusername, Lcomment, Lcomment_time, Luser_picture);
            postComments.add(mlike);
        }

        return new Posts(id, user_id, user_name, user_picture, post_text, post_time, likes, dislikes, comments,
                isliked, isdisliked, iscommented, isfollow, post_image, type, postLikes, postComments, data
        );
    }

    public static ChatList helperChatList(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        String name = (String) obj.get("name");
        String username = (String) obj.get("userID");
        String img = (String) obj.get("image");
        URI uri = new URI(img);
        URL image = uri.toURL();
        int count = obj.getInt("count");
        int auth = obj.getInt("auth");
        int type = obj.getInt("type");
        int conversation_id = obj.getInt("conversation_id");
        String time = obj.getString("time");
        String time_tag = obj.getString("time_tag");
        String message = obj.getString("message");
        return new ChatList(name, username, image, count, auth, conversation_id, time, time_tag, message, type);

    }

    public static int helperChatCount(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        int count = obj.getInt("count");
        if (count > 0)
            return 1;
        else
            return 0;

    }

    public static Message helperMessage(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        String username = obj.getString("userIDS");
        int id = obj.getInt("id");
        int conversation_id = obj.getInt("conversation_id");
        String img = (String) obj.get("image");
        URI uri = new URI(img);
        URL image = uri.toURL();
        String time = obj.getString("time");
        String message = obj.getString("message");
        boolean isMe = false;
        if (ApiUtil.getUserId().equals(username))
            isMe = true;
        return new Message(id, conversation_id, username, image, message, time, isMe);
    }

    public static Online helperOnlineList(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {
        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        String name = (String) obj.get("user_name");
        String intro = (String) obj.get("intro");
        String img = (String) obj.get("user_picture");
        URI uri = new URI(img);
        URL image = uri.toURL();
        int id = obj.getInt("id");
        String last_seen = (String) obj.get("last_seen");
        return new Online(id, name, intro, last_seen, image);
    }

    public static Seen helperSeen(JSONObject obj) throws JSONException, URISyntaxException, MalformedURLException {
        int UserLastDelivered = obj.getInt("lastDelivered");
        int conversation_id = obj.getInt("conversation_id");
        int UserLastRead = obj.getInt("lastRead");
        String UserLastSeen = obj.getString("lastSeen");
        int userId = obj.getInt("userID");
        String image = obj.getString("image");
        Seen mSeen = new Seen();
        mSeen.setUserLastDelivered(UserLastDelivered);
        mSeen.setUserLastRead(UserLastRead);
        mSeen.setUserLastSeen(UserLastSeen);
        mSeen.setUserID(userId);
        mSeen.setImage(image);
        mSeen.setConversationID(conversation_id);
        return mSeen;
    }

    public static Notification helperNotificationList(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {
        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        String message = (String) obj.get("message");
        int id = 0;
//        int id = obj.getInt("id");
        int type = obj.getInt("type");
        String source_id = obj.getString("source_id");
        int activity_type = obj.getInt("activity_type");
        String img = (String) obj.get("image");
        URI uri = new URI(img);
        URL image = uri.toURL();

        return new Notification(id, message, image, type, source_id, activity_type);
    }

    public static UsersList UserListHelper(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        int id = obj.getInt("user_id");
        String name = obj.getString("user_name");
        String username = obj.getString("username");
        String img = obj.getString("user_picture");
        URI uri = new URI(img);
        URL image = uri.toURL();
        String time = obj.getString("time");
        return new UsersList(id, name, username, image, time);

    }


    public static Following FollowingHelper(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {
        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        int id = obj.getInt("user_id2");
        return new Following(id);

    }

    public static Block BlockHelper(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {
        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
        int id = obj.getInt("user_id2");
        return new Block(id);

    }

    public static void updateConversationListMessage(String messageData, int Conversation_ID, int type, Context context) throws JSONException {
        String chats = Preferences.getSavedConversationList(context);
        JSONArray chatList = new JSONArray(chats);
        for (int i = 0; i < chatList.length(); i++) {
            JSONObject obj1 = new JSONObject(String.valueOf(chatList.get(i)));
            if (Conversation_ID == obj1.getInt("conversation_id")) {

                switch (type) {
                    case 1:
                        obj1.put("message", messageData);
                        chatList.put(i, obj1);
                        break;
                    case 2:
                        obj1.put("count", 0);
                        chatList.put(i, obj1);
                        break;
                    case 3:
                        int count = obj1.getInt("count");
                        obj1.put("count", ++count);
                        chatList.put(i, obj1);
                        break;
                    case 4:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            chatList.remove(i);
                        }
                        break;

                    case 5:
                        obj1.put("auth", 2);
                        chatList.put(i, obj1);
                        break;
                }
            }
        }
        Preferences.saveConversationList(chatList, context);
    }

    public static void updateSetting(final String value, final String settingName, final Context context) {
        if (Utils.isNetworkAvailable(context)) {

            String url = ApiUtil.getSettings();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("1")) {
                                Preferences.saveSetting(settingName, value, context);
                                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(settingName, value);
                    params.put("setting_name", settingName);
                    return params;
                }
            };

            RequestManager.getInstance(context).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
//            Preferences.saveSetting(settingName, prev, context);
        }
    }


    public static void updateSetting(final String value, final String settingName, final Context context, int status) {
        if (Utils.isNetworkAvailable(context)) {

            String url = ApiUtil.getSettings();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("1")) {
                                Preferences.saveSetting(settingName, value, context);
                                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(settingName, value);
                    params.put("setting_name", settingName);
                    params.put("status", "s");
                    return params;
                }
            };

            RequestManager.getInstance(context).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
//            Preferences.saveSetting(settingName, prev, context);
        }
    }

    public static boolean seetingItsME = false;

    public static void updateSetting(final String value, final String settingName, final Context context, final String prev) {
        if (Utils.isNetworkAvailable(context)) {

            String url = ApiUtil.getSettings();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equals("1")) {
                                Preferences.saveSetting(settingName, value, context);
                                Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();

                                if(settingName.equals(Preferences.S_USERNAME)){
                                    SharedPreferences urlsPref1 = context.getApplicationContext().getSharedPreferences("Account", 0);
                                    SharedPreferences.Editor UrlsEditor1 = urlsPref1.edit();
                                    UrlsEditor1.putString("username", value);
                                    UrlsEditor1.apply();
                                    ApiUtil.setUserName(value);
                                }
                            } else {
                                seetingItsME = true;
                                Preferences.saveSetting(settingName, prev, context);
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(settingName, value);
                    params.put("setting_name", settingName);
                    return params;
                }
            };

            RequestManager.getInstance(context).addToRequestQueue(stringRequest);
        } else {
            seetingItsME = true;
            Preferences.saveSetting(settingName, prev, context);
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();


//            Preferences.saveSetting(settingName, prev, context);
        }
    }

    public static Search searchHelper(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));

        String name = obj.getString("name");
        int id = obj.getInt("id");
        String username = obj.getString("username");
        String intro = obj.getString("intro");
        String image = obj.getString("image");

        return new Search(name, id, username, intro, image);


    }

    public static Pool poolHelper(JSONArray arr, int i) throws JSONException, URISyntaxException, MalformedURLException {

        JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));

        String name = obj.getString("name");
        String image = obj.getString("image");
        String search_id = obj.getString("search_id");

        return new Pool(name, image, search_id);


    }

    public static JSONArray getJSONArray(String messageData, String id, String time, String Conversation_ID) throws JSONException {
        JSONArray arr;
        JSONObject json = new JSONObject();
        json.put("userIDS", ApiUtil.getUserId());
        json.put("conversation_id", Conversation_ID);
        json.put("id", id);
        json.put("image", ApiUtil.getProfileSmall());
        json.put("time", time);
        json.put("message", messageData);
        arr = new JSONArray();
        arr.put(json);
        return arr;
    }


    public static String HtmlEraser(String msg) {
        msg = android.text.Html.fromHtml(msg).toString();
        return msg.length() > 2 ? msg.substring(0, msg.length() - 2) : msg;

    }

    public static String EmojiEncoder(String msg) {
        msg = msg.replaceAll("<img src=\"e_(.*?)\">", "**$1.png**");
        msg = HtmlEraser(msg);
        return msg;
    }

    public static String EmojiDecoder(String msg) {
        msg = msg.replaceAll("\\*\\*(.*?)\\.png\\*\\*", "<img src=\"e_$1\">");
        msg = msg.replaceAll("\n", "<br>");
        return msg;
    }


    public static void block(final int userID, final Context context) {

        String url = ApiUtil.getBlock();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray arr1 = new JSONArray();
                        String myBlocks = Preferences.getSavedBlock(context);
                        try {
                            JSONArray arr = new JSONArray(myBlocks);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                                int id = obj.getInt("user_id2");
                                if (id == userID) continue;
                                arr1.put(obj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            Utils.blocks.clear();
                            for (int i = 0; i < arr1.length(); i++) {
                                Utils.blocks.add(Utils.BlockHelper(arr1, i));
                            }
                            if (response.equals("1")) {
                                Utils.blocks.add(new Block(userID));
                                arr1.put(new JSONObject().put("user_id2", String.valueOf(userID)));
                            }

                            Preferences.saveBlock(arr1.toString(), context);

                        } catch (JSONException | URISyntaxException | MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(userID));
                return params;
            }
        };

        RequestManager.getInstance(context).addToRequestQueue(stringRequest);

    }

}
