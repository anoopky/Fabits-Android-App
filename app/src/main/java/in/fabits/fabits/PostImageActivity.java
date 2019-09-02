package in.fabits.fabits;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.fabits.fabits.adapters.EmojieSectionsPagerAdapter;
import in.fabits.fabits.adapters.PostImagesAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.EmojiesV1;
import in.fabits.fabits.api.IntentKeys;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.placeholder.EmojiesPlaceholderFragment;
import in.fabits.fabits.placeholder.PlaceholderFragment;

public class PostImageActivity extends AppCompatActivity implements PostImagesAdapter.ListItemClickListener,
        EmojiesPlaceholderFragment.ListItemClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;
    ImageView image;
    ProgressBar loading;
    EditText imageDescription;
    RecyclerView recyclerView;
    private List<String> mPostImages = new ArrayList<>();
    private ImageLoader imageLoader;
    RelativeLayout root_layout;
    private String type;
    private String CID;

    LinearLayout smilesList;
    ImageView smiles;
    private TabLayout tabLayout;
    private boolean isSmilesOpen = false;


    @Override
    public void onListItemClick(int id) {

        String c = mPostImages.get(id);
        imageLoader.displayImage("file:///" + c, image);
        myFile = mPostImages.get(id);
    }

    @Override
    public void onBackPressed() {
        if (isSmilesOpen) {
            hideSmiles();
        } else {
            super.onBackPressed();
        }
    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);
        image = (ImageView) findViewById(R.id.imageView3);
        imageDescription = (EditText) findViewById(R.id.imageDescription);
        loading = (ProgressBar) findViewById(R.id.loading);
        imageLoader = ImageLoader.getInstance();
        type = getIntent().getStringExtra(IntentKeys.CHAT_NAME);
        CID = getIntent().getStringExtra(IntentKeys.CONVERSATION_ID);
        smiles = (ImageView) findViewById(R.id.smiles);

        EmojiesSetup();

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
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);

        switch (type) {
            case "POST":
                FILE_UPLOAD_URL = ApiUtil.getPostImageUrl();
                getSupportActionBar().setTitle("Post");

                break;
            case "CHAT":
                FILE_UPLOAD_URL = ApiUtil.getChatImageUpload();
                getSupportActionBar().setTitle("Send Image");

                break;
            case "PROFILE":

                FILE_UPLOAD_URL = ApiUtil.getProfilePic();
                getSupportActionBar().setTitle("Change Profile Pic");

                break;

            case "S_PROFILE":
                imageDescription.setVisibility(View.GONE);
                smiles.setVisibility(View.GONE);
                FILE_UPLOAD_URL = ApiUtil.getSignUpProfilePic();
                getSupportActionBar().setTitle("Change Profile Pic");

                break;
            case "WALL":

                FILE_UPLOAD_URL = ApiUtil.getProfileWall();
                getSupportActionBar().setTitle("Change Wall");

                break;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            mPostImages = getImagesPath(this);
            recyclerView = (RecyclerView) findViewById(R.id.imageList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
            layoutManager.setReverseLayout(false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            PostImagesAdapter imagesAdapter = new PostImagesAdapter(mPostImages, this);
            recyclerView.setAdapter(imagesAdapter);
            imageLoader.displayImage("file:///" + mPostImages.get(0), image);
            myFile = mPostImages.get(0);

        }

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen || isSmilesOpen)
                    hideImageList();
                else
                    showImageList();
            }
        });
    }


    private void EmojiesSetup() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        EmojieSectionsPagerAdapter emojiSectionsPagerAdapter = new EmojieSectionsPagerAdapter(
                getSupportFragmentManager(), this);
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
                    inputMethodManager.toggleSoftInputFromWindow(imageDescription.getApplicationWindowToken(),
                            InputMethodManager.SHOW_IMPLICIT, 0);
                    imageDescription.requestFocus();
                }

            }
        });
        imageDescription.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                hideSmiles();
                return false;
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
        smilesList.setLayoutParams(params);
        root_layout.setLayoutParams(param1);
        isSmilesOpen = true;
        hideImageList();

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
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param1.weight = 2.0f;
        smilesList.setLayoutParams(param1);
        root_layout.setLayoutParams(params);

        isSmilesOpen = false;
        showImageList();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postText:
                if(Utils.isNetworkAvailable(this)) {
                    new UploadFileToServer(item).execute();
                }
                else{
                    Toast.makeText(getBaseContext(),"No Internet", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }


    ImageView imageview;
    String imagepath;
    File sourceFile;
    int totalSize = 0;
    String myFile = null;
    String FILE_UPLOAD_URL;
    String text;

    @Override
    public void onListItemClick(String id) {
        String htmlText = Html.toHtml(imageDescription.getText());

        htmlText = Utils.EmojiEncoder(htmlText);
        htmlText = Utils.EmojiDecoder(htmlText);
        htmlText += "<img src=\"" + id + "\">";
        htmlText = htmlText.trim();
        imageDescription.setText("");
        imageDescription.append(Html.fromHtml(htmlText, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int resourceId = getResources().getIdentifier(source, "drawable",
                        getPackageName());
                Drawable drawable = getResources().getDrawable(resourceId);
                drawable.setBounds(0, 0, 45, 45);
                return drawable;
            }
        }, null));
        imageDescription.append(" ");
    }


    private class UploadFileToServer extends AsyncTask<String, String, String> {

        MenuItem m;
        public UploadFileToServer(MenuItem m) {
            this.m = m;
        }

        @Override
        protected void onPreExecute() {
            if (myFile != null) {
                loading.setProgress(0);
                sourceFile = new File(myFile);
                totalSize = (int) sourceFile.length();
                text = Utils.EmojiEncoder(Html.toHtml(imageDescription.getText())).trim();
                m.setEnabled(false);
                super.onPreExecute();
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            loading.setProgress(Integer.parseInt(progress[0]));
            if (Integer.parseInt(progress[0]) > 0) {
                getSupportActionBar().setSubtitle(progress[0] + "% completed");
            }
            if (Integer.parseInt(progress[0]) == 100)
                getSupportActionBar().setSubtitle("Posting...");

        }

        @Override
        protected String doInBackground(String... args) {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = sourceFile.getName();

            StringBuilder builder = null;
            try {
                connection = (HttpURLConnection) new URL(FILE_UPLOAD_URL).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + text + "\n";
                String metadataPart1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"conversation_Id\"\r\n\r\n"
                        + CID + "\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = sourceFile.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + metadataPart1 + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(sourceFile));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    out.write(buf, 0, bytesRead);
                    out.flush();
                    progress += bytesRead; // Here progress is total uploaded bytes

                    publishProgress("" + (int) ((progress * 100) / totalSize)); // sending progress percent to publishProgress
                }

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

            } catch (Exception e) {
                m.setEnabled(true);

                // Exception
            } finally {
                if (connection != null) connection.disconnect();
            }
            return String.valueOf(builder);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            isSmilesOpen = false;
            switch (type) {
                case "CHAT":
                    JSONObject obj;
                    try {
                        obj = new JSONObject(result);
                        JSONArray arr = Utils.getJSONArray(obj.getString("message"), obj.getString("id"), obj.getString("time"), String.valueOf(CID));
                        Preferences.saveMessages(CID, arr, getBaseContext());
                        onBackPressed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "POST":

                    PlaceholderFragment.P_INIT = -1;
                    onBackPressed();

                    break;
                case "S_PROFILE":
                    ApiUtil.setProfileSmall(result);
                    SharedPreferences urlsPref = getApplicationContext().getSharedPreferences("Account", 0);
                    SharedPreferences.Editor UrlsEditor = urlsPref.edit();
                    UrlsEditor.putString("profileSmall", result);
                    UrlsEditor.apply();
                    onBackPressed();

                    break;
                case "PROFILE": {

                    SharedPreferences urlsPref1 = getApplicationContext().getSharedPreferences("Account", 0);
                    SharedPreferences.Editor UrlsEditor1 = urlsPref1.edit();
                    UrlsEditor1.putString("profileSmall", result);
                    UrlsEditor1.apply();
                    ApiUtil.setProfileSmall(result);
                    PlaceholderFragment.P_INIT = -1;
                    Intent intent = new Intent(PostImageActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                    break;
                }
                case "WALL": {
                    PlaceholderFragment.P_INIT = -1;

                    Intent intent = new Intent(PostImageActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                    break;
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    mPostImages = getImagesPath(this);
                    recyclerView = (RecyclerView) findViewById(R.id.imageList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
                    layoutManager.setReverseLayout(false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    PostImagesAdapter imagesAdapter = new PostImagesAdapter(mPostImages, this);
                    recyclerView.setAdapter(imagesAdapter);
                    imageLoader.displayImage("file:///" + mPostImages.get(0), image);
                    myFile = mPostImages.get(0);
                }
                break;

            default:
                break;
        }
    }


    public static ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {

            PathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(0, PathOfImage);
        }
        return listOfAllImages;
    }

    Boolean isShown = true;

    void showImageList() {
        recyclerView.setVisibility(View.VISIBLE);
        isShown = false;
    }

    void hideImageList() {
        recyclerView.setVisibility(View.GONE);
    }

}
