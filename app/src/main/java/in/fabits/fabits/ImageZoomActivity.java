package in.fabits.fabits;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.UUID;

import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.IntentKeys;


public class ImageZoomActivity extends AppCompatActivity {

    ImageView imageDetail;
    private ImageLoader imageLoader;
    Bitmap finalBitmap;
    String title;
    private String url;
    private int isME;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isME = getIntent().getIntExtra(IntentKeys.AUTH, -1);

        setContentView(R.layout.activity_image_zoom);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        imageDetail = (ImageView) findViewById(R.id.image);
        url = getIntent().getStringExtra(IntentKeys.IMAGE_URL);
        title = getIntent().getStringExtra(IntentKeys.IMAGE_TITLE);
//        imageDetail.setImage(ImageSource.bitmap(d));

        imageLoader.displayImage(url, imageDetail);
        finalBitmap = imageLoader.loadImageSync(url);
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
        getSupportActionBar().setTitle(title);
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                SaveImage();
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            SaveImage();

            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SaveImage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isME == 1 || isME == 2)
            getMenuInflater().inflate(R.menu.zoom_menu_user, menu);
        else
            getMenuInflater().inflate(R.menu.zoom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id1 = item.getItemId();
        if (id1 == R.id.action_download) {
            isStoragePermissionGranted();
            return true;
        } else if (id1 == R.id.action_upload) {

            Intent intent = new Intent(ImageZoomActivity.this, PostImageActivity.class);
            if (isME == 1)
                intent.putExtra(IntentKeys.CHAT_NAME, "PROFILE");
            if (isME == 2)
                intent.putExtra(IntentKeys.CHAT_NAME, "WALL");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void SaveImage() {
        String name = UUID.randomUUID().toString();
        String fname = "Image-" + name + ".jpg";
        MediaStore.Images.Media.insertImage(getContentResolver(), finalBitmap, fname, title);
        Toast.makeText(this, "Image Saved in Gallery.", Toast.LENGTH_SHORT).show();

//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File();
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);

    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

}
