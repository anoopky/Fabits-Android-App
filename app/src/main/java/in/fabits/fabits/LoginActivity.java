package in.fabits.fabits;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.dialogs.DialogMaster;

public class LoginActivity extends AppCompatActivity {

    Button mButton;
    Button mForget;
    EditText mUsername;
    EditText mPassword;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mButton = (Button) findViewById(R.id.button);
        mForget = (Button) findViewById(R.id.forget);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);

        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://fabits.in";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
        String mToken = pref.getString("token", null);
        int status = pref.getInt("status", -1);

        if (mToken != null) {

            SharedPreferences urlsPref = getApplicationContext().getSharedPreferences("Urls", 0);
            String name = pref.getString("name", null);
            String id = pref.getString("ID", null);
            String username = pref.getString("username", null);
            String profileSmall = pref.getString("profileSmall", null);
            String baseUrl = pref.getString("baseUrl", null);
            String posts = urlsPref.getString("posts", null);
            String chatList = urlsPref.getString("chat_list", null);
            String like = urlsPref.getString("like", null);
            String trend = urlsPref.getString("trend", null);
            String online = urlsPref.getString("online", null);
            String likes = urlsPref.getString("likes", null);
            String comments = urlsPref.getString("comments", null);
            String following = urlsPref.getString("following", null);
            String newMessage = urlsPref.getString("newMessage", null);
            String simpleMessage = urlsPref.getString("simple_message", null);
            String complexMessage = urlsPref.getString("complex_message", null);
            String postText = urlsPref.getString("postText", null);
            String postImage = urlsPref.getString("postImage", null);
            String readConversation = urlsPref.getString("readConversation", null);
            String notification = urlsPref.getString("notification", null);
            String randomProfiles = urlsPref.getString("randomProfiles", null);
            String settings = urlsPref.getString("settings", null);
            String conversationInit = urlsPref.getString("conversationInit", null);
            String online_conversation = urlsPref.getString("online_conversation", null);
            String postSingle = urlsPref.getString("postSingle", null);
            String search = urlsPref.getString("search", null);
            String postsPool = urlsPref.getString("postsPool", null);
            String forceReadConversation = urlsPref.getString("forceReadConversation", null);
            String typing = urlsPref.getString("typing", null);
            String conversationDelete = urlsPref.getString("conversationDelete", null);
            String chatImageUpload = urlsPref.getString("chatImageUpload", null);
            String deletePost = urlsPref.getString("deletePost", null);
            String unFollowPost = urlsPref.getString("unFollowPost", null);
            String deleteComment = urlsPref.getString("deleteComment", null);
            String follow = urlsPref.getString("follow", null);
            String checkFaceMatch = urlsPref.getString("checkFaceMatch", null);
            String changePassword = urlsPref.getString("changePassword", null);
            String contactUs = urlsPref.getString("contactUs", null);
            String logout = urlsPref.getString("logout", null);
            String changePhoneNumber = urlsPref.getString("changePhoneNumber", null);
            String Otp = urlsPref.getString("Otp", null);
            String chatAllow = urlsPref.getString("chatAllow", null);
            String Block = urlsPref.getString("Block", null);
            String my_blocks = urlsPref.getString("my_blocks", null);
            String my_block_list = urlsPref.getString("my_block_list", null);
            String chatBlock = urlsPref.getString("chatBlock", null);
            String bigImage = urlsPref.getString("bigImage", null);
            String profileCountLists = urlsPref.getString("profileCountLists", null);
            String newNotification = urlsPref.getString("newNotification", null);
            String profilePic = urlsPref.getString("profilePic", null);
            String profileWall = urlsPref.getString("profileWall", null);
            String suggestion = urlsPref.getString("suggestion", null);
            String signUpPassword = urlsPref.getString("signUpPassword", null);
            String signUpPasswordSkip = urlsPref.getString("signUpPasswordSkip", null);
            String signUpGenderDob = urlsPref.getString("signUpGenderDob", null);
            String signUpProfilePicSkip = urlsPref.getString("signUpProfilePicSkip", null);
            String signUpProfilePic = urlsPref.getString("signUpProfilePic", null);

            initialize(id, name, username, profileSmall, baseUrl, mToken, posts, chatList, like, trend,
                    online, likes, comments, following, newMessage, simpleMessage, complexMessage, postText, postImage,
                    readConversation, notification, randomProfiles, settings, conversationInit, online_conversation
                    , postSingle, search, postsPool, forceReadConversation, typing, conversationDelete, chatImageUpload,
                    deletePost, unFollowPost, deleteComment, follow, checkFaceMatch, changePassword, contactUs, logout,
                    changePhoneNumber, Otp, chatAllow, Block, my_blocks, my_block_list, chatBlock,
                    bigImage, profileCountLists, newNotification, profilePic, profileWall, suggestion, signUpPassword,
                    signUpPasswordSkip, signUpGenderDob, signUpProfilePicSkip, signUpProfilePic
            );
//            getProfile();
            following();
            block();
            if (status == 0) {
                Intent intent = new Intent(LoginActivity.this, Signup_1.class);
                startActivity(intent);
            } else if (status == 1) {
                Intent intent = new Intent(LoginActivity.this, Signup_2.class);
                startActivity(intent);
            } else if (status == 2) {
                Intent intent = new Intent(LoginActivity.this, Signup_3.class);
                startActivity(intent);
            } else if (status == 3) {
                Intent intent = new Intent(LoginActivity.this, Signup_4.class);
                startActivity(intent);
            } else if (status >= 4) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick();
            }
        });

    }


    private void loginClick() {

        loading();
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();
        String LoginURL = "http://fabits.in/api/token";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.has("success")) {

                                JSONObject result = new JSONObject((String) obj.get("success"));
                                JSONObject setting = result.getJSONObject("settings");

                                Iterator<?> keys = setting.keys();
                                while (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    String value = setting.getString(key) == null ? "0" : setting.getString(key);
                                    Preferences.saveSetting(key, value, getBaseContext());
                                }

                                int status = Integer.parseInt((String) result.get("status"));
                                String token = result.getString("token");
                                String name = result.getString("name");
                                String id = result.getString("ID");
                                String username = result.getString("username");
                                String profileSmall = result.getString("profile_picture_small");
                                String baseUrl = result.getString("baseUrl");

                                JSONObject urls = result.getJSONObject("urls");
                                String posts = urls.getString("_0");
                                String chatList = urls.getString("_1");
                                String like = urls.getString("_2");
                                String trend = urls.getString("_3");
                                String online = urls.getString("_4");
                                String likes = urls.getString("_5");
                                String comments = urls.getString("_6");
                                String following = urls.getString("_7");
                                String newMessage = urls.getString("_8");
                                String SimpleMessage = urls.getString("_9");
                                String ComplexMessage = urls.getString("_10");
                                String postText = urls.getString("_11");
                                String postImage = urls.getString("_12");
                                String readConversation = urls.getString("_13");
                                String notification = urls.getString("_14");
                                String randomProfiles = urls.getString("_15");
                                String settings = urls.getString("_16");
                                String conversationInit = urls.getString("_17");
                                String online_conversation = urls.getString("_18");
                                String postSingle = urls.getString("_19");
                                String search = urls.getString("_20");
                                String postsPool = urls.getString("_21");
                                String forceReadConversation = urls.getString("_22");
                                String typing = urls.getString("_23");
                                String conversationDelete = urls.getString("_24");
                                String chatImageUpload = urls.getString("_25");
                                String deletePost = urls.getString("_26");
                                String unFollowPost = urls.getString("_27");
                                String deleteComment = urls.getString("_28");
                                String follow = urls.getString("_29");
                                String checkFaceMatch = urls.getString("_30");
                                String changePassword = urls.getString("_31");
                                String contactUs = urls.getString("_32");
                                String logout = urls.getString("_33");
                                String changePhoneNumber = urls.getString("_34");
                                String Otp = urls.getString("_35");
                                String chatAllow = urls.getString("_36");
                                String Block = urls.getString("_37");
                                String my_blocks = urls.getString("_38");
                                String my_block_list = urls.getString("_39");
                                String chatBlock = urls.getString("_40");
                                String bigImage = urls.getString("_41");
                                String profileCountLists = urls.getString("_42");
                                String newNotification = urls.getString("_43");
                                String profilePic = urls.getString("_44");
                                String profileWall = urls.getString("_45");
                                String suggestion = urls.getString("_46");
                                String SignUpPassword = urls.getString("_47");
                                String signUpPasswordSkip = urls.getString("_48");
                                String signUpGenderDob = urls.getString("_49");
                                String signUpProfilePicSkip = urls.getString("_50");
                                String signUpProfilePic = urls.getString("_51");

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("Account", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("token", token);
                                editor.putString("name", name);
                                editor.putString("ID", id);
                                editor.putString("username", username);
                                editor.putString("profileSmall", profileSmall);
                                editor.putString("baseUrl", baseUrl);
                                editor.putInt("status", status);
                                editor.apply();
                                SharedPreferences urlsPref = getApplicationContext().getSharedPreferences("Urls", 0);
                                SharedPreferences.Editor UrlsEditor = urlsPref.edit();
                                UrlsEditor.putString("posts", posts);
                                UrlsEditor.putString("chat_list", chatList);
                                UrlsEditor.putString("like", like);
                                UrlsEditor.putString("trend", trend);
                                UrlsEditor.putString("online", online);
                                UrlsEditor.putString("likes", likes);
                                UrlsEditor.putString("comments", comments);
                                UrlsEditor.putString("following", following);
                                UrlsEditor.putString("newMessage", newMessage);
                                UrlsEditor.putString("simple_message", SimpleMessage);
                                UrlsEditor.putString("complex_message", ComplexMessage);
                                UrlsEditor.putString("postText", postText);
                                UrlsEditor.putString("postImage", postImage);
                                UrlsEditor.putString("readConversation", readConversation);
                                UrlsEditor.putString("notification", notification);
                                UrlsEditor.putString("randomProfiles", randomProfiles);
                                UrlsEditor.putString("settings", settings);
                                UrlsEditor.putString("conversationInit", conversationInit);
                                UrlsEditor.putString("online_conversation", online_conversation);
                                UrlsEditor.putString("postSingle", postSingle);
                                UrlsEditor.putString("search", search);
                                UrlsEditor.putString("postsPool", postsPool);
                                UrlsEditor.putString("forceReadConversation", forceReadConversation);
                                UrlsEditor.putString("typing", typing);
                                UrlsEditor.putString("conversationDelete", conversationDelete);
                                UrlsEditor.putString("chatImageUpload", chatImageUpload);
                                UrlsEditor.putString("deletePost", deletePost);
                                UrlsEditor.putString("unFollowPost", unFollowPost);
                                UrlsEditor.putString("deleteComment", deleteComment);
                                UrlsEditor.putString("follow", follow);
                                UrlsEditor.putString("checkFaceMatch", checkFaceMatch);
                                UrlsEditor.putString("changePassword", changePassword);
                                UrlsEditor.putString("contactUs", contactUs);
                                UrlsEditor.putString("logout", logout);
                                UrlsEditor.putString("changePhoneNumber", changePhoneNumber);
                                UrlsEditor.putString("Otp", Otp);
                                UrlsEditor.putString("chatAllow", chatAllow);
                                UrlsEditor.putString("Block", Block);
                                UrlsEditor.putString("my_blocks", my_blocks);
                                UrlsEditor.putString("my_block_list", my_block_list);
                                UrlsEditor.putString("chatBlock", chatBlock);
                                UrlsEditor.putString("bigImage", bigImage);
                                UrlsEditor.putString("profileCountLists", profileCountLists);
                                UrlsEditor.putString("newNotification", newNotification);
                                UrlsEditor.putString("profilePic", profilePic);
                                UrlsEditor.putString("profileWall", profileWall);
                                UrlsEditor.putString("suggestion", suggestion);
                                UrlsEditor.putString("signUpPassword", SignUpPassword);
                                UrlsEditor.putString("signUpPasswordSkip", signUpPasswordSkip);
                                UrlsEditor.putString("signUpGenderDob", signUpGenderDob);
                                UrlsEditor.putString("signUpProfilePicSkip", signUpProfilePicSkip);
                                UrlsEditor.putString("signUpProfilePic", signUpProfilePic);
                                UrlsEditor.apply();

                                initialize(id, name, username, profileSmall, baseUrl, token, posts,
                                        chatList, like, trend, online, likes, comments, following,
                                        newMessage, SimpleMessage, ComplexMessage, postText, postImage,
                                        readConversation, notification
                                        , randomProfiles, settings, conversationInit, online_conversation,
                                        postSingle, search, postsPool, forceReadConversation, typing, conversationDelete,
                                        chatImageUpload, deletePost, unFollowPost, deleteComment, follow, checkFaceMatch,
                                        changePassword, contactUs, logout, changePhoneNumber, Otp, chatAllow, Block, my_blocks,
                                        my_block_list, chatBlock, bigImage, profileCountLists, newNotification,
                                        profilePic, profileWall, suggestion, SignUpPassword, signUpPasswordSkip,
                                        signUpGenderDob, signUpProfilePicSkip, signUpProfilePic
                                );
                                following();
                                block();

                                if (status == 0) {
                                    Intent intent = new Intent(LoginActivity.this, Signup_1.class);
                                    startActivity(intent);
                                } else if (status == 1) {
                                    Intent intent = new Intent(LoginActivity.this, Signup_2.class);
                                    startActivity(intent);
                                } else if (status == 2) {
                                    Intent intent = new Intent(LoginActivity.this, Signup_3.class);
                                    startActivity(intent);
                                } else if (status == 3) {
                                    Intent intent = new Intent(LoginActivity.this, Signup_4.class);
                                    startActivity(intent);
                                } else if (status >= 4) {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }


                            } else {
                                loading();
                                DialogMaster alert = new DialogMaster();
                                JSONObject error = new JSONObject(response);
                                String msg = "";

                                Iterator<?> keys = error.keys();
                                while (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    msg = error.getString(key).replaceAll("\\[", "");
                                    msg = msg.replaceAll("]", "");
                                    break;
                                }


                                alert.showDialog(LoginActivity.this, msg);
                            }
                        } catch (JSONException e) {
                            loading();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading();
                DialogMaster alert = new DialogMaster();
                alert.showDialog(LoginActivity.this, "Can't connect to server!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void block() {

        String url = ApiUtil.getMy_blocks();

        String response = Preferences.getSavedBlock(this);

        try {
            JSONArray arr = new JSONArray(response);
            Utils.blocks.clear();
            for (int i = 0; i < arr.length(); i++) {
                Utils.blocks.add(Utils.BlockHelper(arr, i));
            }
        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray arr = new JSONArray(response);
                            Utils.blocks.clear();
                            for (int i = 0; i < arr.length(); i++) {
                                Utils.blocks.add(Utils.BlockHelper(arr, i));
                            }
                            Preferences.saveBlock(response, getBaseContext());
                        } catch (JSONException | URISyntaxException | MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);


    }

    void initialize(String id, String name, String username, String profileSmall, String baseUrl,
                    String token, String posts, String chatList, String like, String trend,
                    String online, String likes, String comments, String following, String newMessage,
                    String simpleMessage, String complexMessage, String postText, String postImage,
                    String readConversation,
                    String notification, String randomProfiles, String settings, String conversationInit,
                    String online_conversation, String postSingle, String search, String postsPool,
                    String forceReadConversation, String typing, String conversationDelete, String chatImageUpload,
                    String deletePost, String unFollowPost, String deleteComment, String follow, String checkFaceMatch,
                    String changePassword, String contactUs, String logout, String changePhoneNumber, String Otp,
                    String chatAllow, String Block, String my_blocks, String my_block_list, String chatBlock,
                    String bigImage, String profileCountLists, String newNotification, String profilePic,
                    String profileWall, String suggestion, String signUpPassword, String signUpPasswordSkip,
                    String signUpGenderDob, String signUpProfilePicSkip, String signUpProfilePic

    ) {

        ApiUtil.setProfileSmall(profileSmall);
        ApiUtil.setUserId(id);
        ApiUtil.setBaseUrl(baseUrl);
        ApiUtil.setToken(token);
        ApiUtil.setName(name);
        ApiUtil.setUserName(username);
        ApiUtil.setPostUrl(posts);
        ApiUtil.setChatListUrl(chatList);
        ApiUtil.setLikeUrl(like);
        ApiUtil.setTrendUrl(trend);
        ApiUtil.setOnlineListUrl(online);
        ApiUtil.setLikesUrl(likes);
        ApiUtil.setCommentUrl(comments);
        ApiUtil.setfollowing(following);
        ApiUtil.setNewMessage(newMessage);
        ApiUtil.setSimpleMessage(simpleMessage);
        ApiUtil.setComplexMessage(complexMessage);
        ApiUtil.setPostTextUrl(postText);
        ApiUtil.setPostImageUrl(postImage);
        ApiUtil.setReadConversationUrl(readConversation);
        ApiUtil.setNotificationUrl(notification);
        ApiUtil.setRandomProfiles(randomProfiles);
        ApiUtil.setSettings(settings);
        ApiUtil.setConversationInit(conversationInit);
        ApiUtil.setOnline_conversation(online_conversation);
        ApiUtil.setPostSingle(postSingle);
        ApiUtil.setSearch(search);
        ApiUtil.setPostsPool(postsPool);
        ApiUtil.setForceReadConversation(forceReadConversation);
        ApiUtil.setTyping(typing);
        ApiUtil.setConversationDelete(conversationDelete);
        ApiUtil.setChatImageUpload(chatImageUpload);
        ApiUtil.setPostDelete(deletePost);
        ApiUtil.setCommentDelete(deleteComment);
        ApiUtil.setUnFollowPost(unFollowPost);
        ApiUtil.setFollow(follow);
        ApiUtil.setCheckFaceMatch(checkFaceMatch);
        ApiUtil.setChangePassword(changePassword);
        ApiUtil.setChangePhoneNumber(changePhoneNumber);
        ApiUtil.setContactUs(contactUs);
        ApiUtil.setLogout(logout);
        ApiUtil.setChatAllow(chatAllow);
        ApiUtil.setChatBlock(chatBlock);
        ApiUtil.setBlock(Block);
        ApiUtil.setOtp(Otp);
        ApiUtil.setMy_blocks(my_blocks);
        ApiUtil.setMy_blocks_list(my_block_list);
        ApiUtil.setBigImage(bigImage);
        ApiUtil.setProfileCountLists(profileCountLists);
        ApiUtil.setNewNotification(newNotification);
        ApiUtil.setProfilePic(profilePic);
        ApiUtil.setProfileWall(profileWall);
        ApiUtil.setSuggestion(suggestion);
        ApiUtil.setSignUpGenderDob(signUpGenderDob);
        ApiUtil.setSignUpPassword(signUpPassword);
        ApiUtil.setSignUpPasswordSkip(signUpPasswordSkip);
        ApiUtil.setSignUpProfilePic(signUpProfilePic);
        ApiUtil.setSignUpProfilePicSkip(signUpProfilePicSkip);

    }

    void loading() {
        if (mButton.getVisibility() == View.VISIBLE) {
            mButton.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mButton.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    void following() {


        String url = ApiUtil.getfollowing();

        String response = Preferences.getSavedFollowing(this);

        try {
            JSONArray arr = new JSONArray(response);
            Utils.following.clear();
            for (int i = 0; i < arr.length(); i++) {
                Utils.following.add(Utils.FollowingHelper(arr, i));
            }
        } catch (JSONException | URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray arr = new JSONArray(response);
                            Utils.following.clear();
                            for (int i = 0; i < arr.length(); i++) {
                                Utils.following.add(Utils.FollowingHelper(arr, i));
                            }
                            Preferences.saveFollowing(response, getBaseContext());
                        } catch (JSONException | URISyntaxException | MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);

    }
}
