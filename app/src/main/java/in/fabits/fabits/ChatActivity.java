package in.fabits.fabits;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.john.waveview.WaveView;
import com.pusher.client.channel.SubscriptionEventListener;

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
import java.util.concurrent.ThreadFactory;

import in.fabits.fabits.adapters.EmojieSectionsPagerAdapter;
import in.fabits.fabits.adapters.MessageAdapter;
import in.fabits.fabits.adapters.PostImagesAdapter;
import in.fabits.fabits.adapters.SectionsPagerAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.EmojiesV1;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Socket;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.Message;
import in.fabits.fabits.model.Seen;
import in.fabits.fabits.placeholder.EmojiesPlaceholderFragment;
import in.fabits.fabits.placeholder.PlaceholderFragment;


public class ChatActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        EmojiesPlaceholderFragment.ListItemClickListener,
        MessageAdapter.ListItemLongClickListener {

    String name;
    String id;
    public static int Conversation_ID;
    Seen mSeen = new Seen();
    URL profileUrl;
    String lastSeen;
    Button send;
    int auth;
    int type;
    EditText message;
    RecyclerView recyclerView;
    List<Message> mMessages = new ArrayList<>();
    MessageAdapter messageAdapter;
    LinearLayout smilesList;
    SharedPreferences shared;
    ImageView smiles;
    private TabLayout tabLayout;
    private boolean isSmilesOpen = false;
    WaveView isSeeing;
    private ImageView imageUpload;
    Button allow;
    Button block;
    LinearLayout chatting;
    LinearLayout authenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Conversation_ID = -1;
        Preferences.saveCount(Preferences.CCOUNT, "0", this);
        shared = getSharedPreferences(Preferences.LIST, 0);
        message = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);
        smiles = (ImageView) findViewById(R.id.smiles);
        imageUpload = (ImageView) findViewById(R.id.imageUpload);
        initializeData();
        Toolbar mToolbar;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(name);
        if (lastSeen.equals("online"))
            getSupportActionBar().setSubtitle(lastSeen);

        else
            getSupportActionBar().setSubtitle("Last seen " + lastSeen + " ago");


        initialize();
        offlineMessages(1);
        isSeeing = (WaveView) findViewById(R.id.isSeeing);

        shared.registerOnSharedPreferenceChangeListener(this);
        EmojiesSetup();

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, PostImageActivity.class);
                intent.putExtra(IntentKeys.CHAT_NAME, "CHAT");
                intent.putExtra(IntentKeys.CONVERSATION_ID, String.valueOf(Conversation_ID)
                );
                startActivity(intent);
            }
        });

        message.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int key, KeyEvent event) {


                if (event.getKeyCode() == KeyEvent.KEYCODE_SPACE) {
                    typing();
                }
                return false;
            }
        });


        try {
            conversationInit();
        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

        authenticate = (LinearLayout) findViewById(R.id.authenticate);
        chatting = (LinearLayout) findViewById(R.id.chatting);

        if (auth == 1) {
            authenticate.setVisibility(View.VISIBLE);
            chatting.setVisibility(View.GONE);
        } else if (auth == 2) {
            authenticate.setVisibility(View.GONE);
            chatting.setVisibility(View.VISIBLE);
        } else {
            onBackPressed();
        }
        allow = (Button) findViewById(R.id.allow);
        block = (Button) findViewById(R.id.block);

        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowChat();
                authenticate.setVisibility(View.GONE);
                chatting.setVisibility(View.VISIBLE);
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockChat();
                onBackPressed();
            }
        });

        bindTyping();
        offlineGreen();
        offlineSeen();
    }


    void isNewMessage() {
        String url = ApiUtil.getNewMessage();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                                String conversation_ID = (String) obj.get("conversation_id");
                                JSONArray msg = new JSONArray();
                                msg.put(arr.get(i));
                                Preferences.saveMessages(conversation_ID, msg, getBaseContext());
                            }
                        } catch (JSONException e) {
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


    private void blockChat() {

        final String url = ApiUtil.getChatBlock();
        final int convId = Conversation_ID;
        if (convId != -1) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Utils.updateConversationListMessage("", Conversation_ID, 4, getBaseContext());
                            } catch (JSONException e) {
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
                    params.put("conversation_Id", String.valueOf(convId));
                    return params;
                }
            };
            RequestManager.getInstance(this).addToRequestQueue(stringRequest);

        }
    }

    private void allowChat() {

        final String url = ApiUtil.getChatAllow();
        final int convId = Conversation_ID;
        if (convId != -1) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            isNewMessage();
                            try {
                                Utils.updateConversationListMessage("", Conversation_ID, 5, getBaseContext());
                            } catch (JSONException e) {
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
                    params.put("conversation_Id", String.valueOf(convId));
                    return params;
                }
            };
            RequestManager.getInstance(this).addToRequestQueue(stringRequest);


        }
    }

    private void bindTyping() {
        if (Conversation_ID != -1) {

            Socket.myChannel.bind("typingV2" + Conversation_ID, new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, final String data) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONArray arr = new JSONArray(data);
                                JSONObject obb = new JSONObject(String.valueOf(arr.get(0)));
                                String status = obb.getString("typing");
                                getSupportActionBar().setSubtitle(status);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        if (lastSeen.equals("online"))
                                            getSupportActionBar().setSubtitle(lastSeen);

                                        else
                                            getSupportActionBar().setSubtitle("Last seen " + lastSeen + " ago");

                                    }
                                }, 1000);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        }
    }

    private void typing() {
        final String url = ApiUtil.getTyping();
        final int convId = Conversation_ID;
        if (convId != -1) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("conversationId", String.valueOf(convId));
                    return params;
                }
            };
            RequestManager.getInstance(this).addToRequestQueue(stringRequest);
        }

    }

    private void offlineMessages(int init) {
        String chats = Preferences.getSavedMessages(String.valueOf(Conversation_ID), this);

        try {
            int MaxLimit = 20 * init;
            JSONArray arrn = new JSONArray(chats);
            if (arrn.length() == 0)
                forceReadAll();
            int limit;
            if (arrn.length() > MaxLimit)
                limit = MaxLimit;
            else
                limit = arrn.length();

            mMessages.clear();
            for (int i = 0; i < limit; i++) {
                mMessages.add(Utils.helperMessage(arrn, i));
            }

            JSONObject obj = new JSONObject(String.valueOf(arrn.get(0)));
            int id = obj.getInt("id");
            // if newMessage > 0
            Readed(id);
            offlineSeen();


            displayMessages();

        } catch (JSONException | URISyntaxException | MalformedURLException e1) {
            e1.printStackTrace();
        }

    }

    private void forceReadAll() {
        try {
            String chats = Preferences.getSavedConversationList(this);
            JSONArray chatList = new JSONArray(chats);
            for (int i = 0; i < chatList.length(); i++) {
                JSONObject obj1 = new JSONObject(String.valueOf(chatList.get(i)));
                if (Conversation_ID == obj1.getInt("conversation_id") && obj1.getInt("count") > 0) {

                    final String url = ApiUtil.getForceReadConversation();
                    final int convId = Conversation_ID;
                    if (convId != -1) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            Utils.updateConversationListMessage("", Conversation_ID, 2, getBaseContext());
                                        } catch (JSONException e) {
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
                                params.put("conversationId", String.valueOf(convId));
                                return params;
                            }
                        };
                        RequestManager.getInstance(this).addToRequestQueue(stringRequest);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayMessages() {

        if (recyclerView.getAdapter() == null) {

            messageAdapter = new MessageAdapter(mMessages, mSeen, this, name);
            recyclerView.setAdapter(messageAdapter);
        } else {
            messageAdapter.setMessage(mMessages, mSeen);
            messageAdapter.notifyDataSetChanged();
        }

    }

    private void Readed(final int id) {

        if (id != -1) {
            final String url = ApiUtil.getReadConversationUrl();
            final int convId = Conversation_ID;
            if (convId != -1) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    Utils.updateConversationListMessage("", Conversation_ID, 2, getBaseContext());
                                } catch (JSONException e) {
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
                        params.put("conversationId", String.valueOf(convId));
                        params.put("lastRead", String.valueOf(id));
                        return params;
                    }
                };
                RequestManager.getInstance(this).addToRequestQueue(stringRequest);
            }
        }
    }

    private void sendMessage(final String messageData) throws JSONException, MalformedURLException, URISyntaxException {

        final String url;
        String conversation = String.valueOf(Conversation_ID);

        if (Conversation_ID == -1) {
            conversation = id;
            url = ApiUtil.getComplexMessage();
        } else
            url = ApiUtil.getSimpleMessage();

        JSONArray arr = getJSONArray(messageData, Conversation_ID);
        mMessages.add(0, Utils.helperMessage(arr, 0));
        displayMessages();

        final String FinalConversation = conversation;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray arr;
                        PlaceholderFragment.C_INIT = -1;

                        if (url.equals(ApiUtil.getComplexMessage())) {
                            try {
                                arr = new JSONArray(response);
                                JSONObject obj = new JSONObject(String.valueOf(arr.get(0)));
                                Conversation_ID = obj.getInt("conversation_id");
                                Preferences.saveMessages(String.valueOf(Conversation_ID), arr, getBaseContext());
                                Preferences.addConversationList(arr, getBaseContext());

                                bindTyping();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject obj = new JSONObject(response);
                                arr = Utils.getJSONArray(messageData, obj.getString("id"), obj.getString("time"), String.valueOf(Conversation_ID));
                                Preferences.saveMessages(String.valueOf(Conversation_ID), arr, getBaseContext());
                                Utils.updateConversationListMessage(messageData, Conversation_ID, 1, getBaseContext());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                params.put("message", messageData);
                params.put("conversation_Id", FinalConversation);
                params.put("type", String.valueOf(type));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void conversationInit() throws JSONException, MalformedURLException, URISyntaxException {

        final String url;
        String conversation = String.valueOf(Conversation_ID);
        if (conversation.equals("-1"))
            conversation = id;
        url = ApiUtil.getConversationInit();
        final String FinalConversation = conversation;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = new JSONObject(String.valueOf(arr.get(0)));
                            int UserLastDelivered = obj.getInt("lastDelivered");
                            int conversation_id = obj.getInt("conversation_id");
                            int UserLastRead = obj.getInt("lastRead");
                            String UserLastSeen = obj.getString("lastSeen");
                            int userId = obj.getInt("userID");
                            if (UserLastSeen.equals("online"))
                                getSupportActionBar().setSubtitle(UserLastSeen);

                            else
                                getSupportActionBar().setSubtitle("Last seen " + UserLastSeen + " ago");

                            lastSeen = UserLastSeen;
                            String image = obj.getString("image");
                            Preferences.saveSeen(String.valueOf(FinalConversation), arr.toString(), getBaseContext());
                            mSeen.setUserLastDelivered(UserLastDelivered);
                            mSeen.setUserLastRead(UserLastRead);
                            mSeen.setUserLastSeen(UserLastSeen);
                            mSeen.setUserID(userId);
                            mSeen.setImage(image);
                            mSeen.setConversationID(conversation_id);

                            displayMessages();
                        } catch (JSONException e) {
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
                params.put("conversationId", FinalConversation);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getInstance(this).addToRequestQueue(stringRequest);

    }

    private JSONArray getJSONArray(String messageData, int ConversationID) throws JSONException {
        JSONArray arr;
        JSONObject json = new JSONObject();
        json.put("userIDS", ApiUtil.getUserId());
        json.put("conversation_id", ConversationID);
        json.put("id", -1);
        json.put("image", ApiUtil.getProfileSmall());
        json.put("time", "Sending...");
        json.put("message", messageData);
        arr = new JSONArray();
        arr.put(json);
        return arr;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Preferences.MESSAGES + Conversation_ID)) {
            offlineMessages(1);
        }

        if (key.equals(Preferences.SEEN + Conversation_ID)) {
            offlineSeen();
        }

        if (key.equals(Preferences.GREEN + Conversation_ID)) {
            offlineGreen();
        }

    }

    private void offlineGreen() {
        try {
            String data = Preferences.getSavedGreenBarStatus(String.valueOf(Conversation_ID), this);
            JSONArray arr = new JSONArray(data);
            if (arr.length() > 0) {
                JSONObject obb = new JSONObject(String.valueOf(arr.get(0)));
                int type = obb.getInt("type");
                if (type == 1 && lastSeen.equals("online"))
                    isSeeing.setVisibility(View.VISIBLE);
                else
                    isSeeing.setVisibility(View.GONE);
            } else
                isSeeing.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void offlineSeen() {
        try {
            String response = Preferences.getSavedSeen(String.valueOf(Conversation_ID), this);
            JSONArray arr = new JSONArray(response);
            JSONObject obj = new JSONObject(String.valueOf(arr.get(0)));
            int UserLastDelivered = obj.getInt("lastDelivered");
            int conversation_id = obj.getInt("conversation_id");
            int UserLastRead = obj.getInt("lastRead");
            lastSeen = obj.getString("lastSeen");

            if (lastSeen.equals("online"))
                getSupportActionBar().setSubtitle(lastSeen);

            else
                getSupportActionBar().setSubtitle("Last seen " + lastSeen + " ago");

            int userId = obj.getInt("userID");
            String image = obj.getString("image");
            mSeen.setUserLastDelivered(UserLastDelivered);
            mSeen.setUserLastRead(UserLastRead);
            mSeen.setUserLastSeen(lastSeen);
            mSeen.setUserID(userId);
            mSeen.setImage(image);
            mSeen.setConversationID(conversation_id);

            displayMessages();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shared.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        final String url = ApiUtil.getOnline_conversation();
        if (Conversation_ID != -1) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("conversationId", String.valueOf(Conversation_ID));
                    params.put("type", String.valueOf(0));
                    return params;
                }
            };
            RequestManager.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String url = ApiUtil.getOnline_conversation();
        if (Conversation_ID != -1) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("conversationId", String.valueOf(Conversation_ID));
                    params.put("type", String.valueOf(1));
                    return params;
                }
            };
            RequestManager.getInstance(this).addToRequestQueue(stringRequest);
        }

    }

    @Override
    public void onListItemClick(String id) {


        String htmlText = Html.toHtml(message.getText());

        htmlText = Utils.EmojiEncoder(htmlText);
        htmlText = Utils.EmojiDecoder(htmlText);
        htmlText += "<img src=\"" + id + "\">";
        htmlText = htmlText.trim();
        message.setText("");
        message.append(Html.fromHtml(htmlText, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int resourceId = getResources().getIdentifier(source, "drawable",
                        getPackageName());
                Drawable drawable = getResources().getDrawable(resourceId);
                drawable.setBounds(0, 0, 45, 45);
                return drawable;
            }
        }, null));
        message.append(" ");
    }

    @Override
    public void onBackPressed() {
        if (isSmilesOpen) {
            hideSmiles();
        } else {
            super.onBackPressed();
            final String url = ApiUtil.getOnline_conversation();
            if (Conversation_ID != -1) {
                final int cid = Conversation_ID;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("conversationId", String.valueOf(cid));
                        params.put("type", String.valueOf(0));
                        return params;
                    }
                };
                RequestManager.getInstance(this).addToRequestQueue(stringRequest);
            }
            Conversation_ID = -1;
        }
    }

    private int visibleThreshold = 2;
    private int PlastVisibleItem, PtotalItemCount;
    private boolean Ploading;

    void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.chats);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                PtotalItemCount = layoutManager.getItemCount();
                PlastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!Ploading && PtotalItemCount <= (PlastVisibleItem + visibleThreshold)) {
                    Ploading = true;
                    offlineMessages(++cPointer);
                }
            }

        });


    }

    int cPointer = 1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id1 = item.getItemId();
        if (id1 == R.id.action_profile) {
            Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
            intent.putExtra(IntentKeys.PROFILE_ID, id);
            startActivity(intent);
            return true;
        } else if (id1 == R.id.action_block) {
            blockChat();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void EmojiesSetup() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        EmojieSectionsPagerAdapter emojiSectionsPagerAdapter = new EmojieSectionsPagerAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(emojiSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        int pic1 = getResources().getIdentifier(EmojiesV1.people[0], "drawable", getPackageName());
        int pic2 = getResources().getIdentifier(EmojiesV1.nature[0], "drawable", getPackageName());
        int pic3 = getResources().getIdentifier(EmojiesV1.food[0], "drawable", getPackageName());
        int pic4 = getResources().getIdentifier(EmojiesV1.activity[0], "drawable", getPackageName());
        int pic5 = getResources().getIdentifier(EmojiesV1.travel[0], "drawable", getPackageName());
        int pic6 = getResources().getIdentifier(EmojiesV1.objects[0], "drawable", getPackageName());
        int pic7 = getResources().getIdentifier(EmojiesV1.symbols[0], "drawable", getPackageName());
        int pic8 = getResources().getIdentifier(EmojiesV1.flags[0], "drawable", getPackageName());
        int pic9 = getResources().getIdentifier(EmojiesV1.diversity[0], "drawable", getPackageName());
        tabLayout.getTabAt(0).setIcon(pic1);
        tabLayout.getTabAt(1).setIcon(pic2);
        tabLayout.getTabAt(2).setIcon(pic3);
        tabLayout.getTabAt(3).setIcon(pic4);
        tabLayout.getTabAt(4).setIcon(pic5);
        tabLayout.getTabAt(5).setIcon(pic6);
        tabLayout.getTabAt(6).setIcon(pic7);
        tabLayout.getTabAt(7).setIcon(pic8);
        tabLayout.getTabAt(8).setIcon(pic9);

        smiles = (ImageView) findViewById(R.id.smiles);
        smilesList = (LinearLayout) findViewById(R.id.smilesList);

        smiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSmilesOpen)
                    showSmiles();
                else {
                    hideSmiles();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(message.getApplicationWindowToken(),
                            InputMethodManager.SHOW_IMPLICIT, 0);
                    message.requestFocus();
                }

            }
        });
        message.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                hideSmiles();
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (Utils.isNetworkAvailable(getBaseContext())) {
                        String messageData = Html.toHtml(message.getText());
                        messageData = messageData.trim();
                        if (messageData.length() > 0) {
                            YoYo.with(Techniques.BounceIn)
                                    .duration(150)
                                    .repeat(0)
                                    .playOn(send);
                            sendMessage(Utils.EmojiEncoder(messageData));
                            message.setText("");
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showSmiles() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard, getApplicationContext().getTheme()));

        } else {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard));
        }


        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.weight = 1.0f;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60,
                r.getDisplayMetrics()
        );

        param1.setMargins(0, 0, 0, px);
        smilesList.setLayoutParams(params);
        recyclerView.setLayoutParams(param1);
        isSmilesOpen = true;

    }

    private void hideSmiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood, getApplicationContext().getTheme()));

        } else {
            smiles.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 0.0f;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60,
                r.getDisplayMetrics()
        );

        params.setMargins(0, 0, 0, px);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.weight = 2.0f;
        smilesList.setLayoutParams(param1);
        recyclerView.setLayoutParams(params);

        isSmilesOpen = false;

    }

    void initializeData() {

        String IntentConversation = getIntent().getStringExtra(IntentKeys.CONVERSATION_ID);
        auth = getIntent().getIntExtra(IntentKeys.AUTH, 2);
        type = getIntent().getIntExtra(IntentKeys.TYPE, 1);

        String chatList = Preferences.getSavedConversationList(this);

        try {
            JSONArray arr = new JSONArray(chatList);

            if (IntentConversation != null) {
                Conversation_ID = Integer.parseInt(IntentConversation);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                    int conversation_id = obj.getInt("conversation_id");
                    if (conversation_id == Conversation_ID) {
                        name = (String) obj.get("name");
                        id = (String) obj.get("userID");
                        String img = (String) obj.get("image");
                        URI uri = new URI(img);
                        profileUrl = uri.toURL();
//                    int count = obj.getInt("count");
                        auth = obj.getInt("auth");
                        type = obj.getInt("type");
                    }

                }


            } else {

                id = getIntent().getStringExtra(IntentKeys.CHAT);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = new JSONObject(String.valueOf(arr.get(i)));
                    String userID = obj.getString("userID");
                    if (userID.equals(id)) {
                        Conversation_ID = obj.getInt("conversation_id");
                        auth = obj.getInt("auth");
                        type = obj.getInt("type");
                        break;
                    }
                }
                name = getIntent().getStringExtra(IntentKeys.CHAT_NAME);
                String img = getIntent().getStringExtra(IntentKeys.CHAT_IMAGE);
                URI uri = new URI(img);
                profileUrl = uri.toURL();

                lastSeen = getIntent().getStringExtra(IntentKeys.CHAT_SEEN);

            }

            if (lastSeen == null)
                lastSeen = "";

        } catch (JSONException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onListItemClick(final int id) {
        final CharSequence[] items = {"Copy", "Delete",};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setTitle("Select The Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (item == 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", mMessages.get(id).getMessage());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getBaseContext(), "Text Copied.", Toast.LENGTH_SHORT).show();
                } else if (item == 1) {
                    mMessages.remove(id);
                    String msgs = Preferences.getSavedMessages(String.valueOf(Conversation_ID), getBaseContext());
                    try {
                        JSONArray arr = new JSONArray(msgs);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            arr.remove(id);
                        }
                        Preferences.forceSaveMessages(String.valueOf(Conversation_ID), arr, getBaseContext());
                        if (arr.length() == 0)
                            displayMessages();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        builder.show();
    }
}
