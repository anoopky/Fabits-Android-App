
package in.fabits.fabits.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.fabits.fabits.LoginActivity;
import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.contactUs;
import in.fabits.fabits.settings.AccountSettingActivity;
import in.fabits.fabits.settings.AlertActivity;
import in.fabits.fabits.settings.BlockedUsersActivity;
import in.fabits.fabits.settings.ChangePassword;
import in.fabits.fabits.settings.PrivacyActivity;
import in.fabits.fabits.settings.changePhone;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.NumberViewHolder> {

    private static final String TAG = SettingsAdapter.class.getSimpleName();

    private List<String> mSettings = new ArrayList<>();

    private ImageLoader imageLoader = null;


    public SettingsAdapter(List<String> Settings) {

        this.mSettings = Settings;
    }


    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.settings_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions opts = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(opts).build();
        ImageLoader.getInstance().init(config);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSettings.size();
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView message;
        Context helperContext;

        NumberViewHolder(View itemView) {

            super(itemView);
            helperContext = itemView.getContext();
            image = (CircleImageView) itemView.findViewById(R.id.image);
            message = (TextView) itemView.findViewById(R.id.message);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            String p = mSettings.get(listIndex);

            if (listIndex == 0) {
                image.setVisibility(View.VISIBLE);
                message.setVisibility(View.GONE);
                imageLoader.displayImage(String.valueOf(ApiUtil.getProfileSmall()), image);
            } else {
                image.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText(p);
            }

        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Intent i = null;

            if (pos == 1) {
                i = new Intent(helperContext,
                        AccountSettingActivity.class);
            } else if (pos == 2) {
                i = new Intent(helperContext,
                        changePhone.class);

            } else if (pos == 3) {
                i = new Intent(helperContext,
                        ChangePassword.class);

            } else if (pos == 4) {
                i = new Intent(helperContext,
                        PrivacyActivity.class);

            } else if (pos == 5) {
                i = new Intent(helperContext,
                        AlertActivity.class);

            } else if (pos == 6) {
                i = new Intent(helperContext,
                        BlockedUsersActivity.class);
            } else if (pos == 7) {
                i = new Intent(helperContext,
                        contactUs.class);
            } else if (pos == 8) {
                if (Utils.isNetworkAvailable(helperContext)) {
                    logout();
                    helperContext.getApplicationContext().getSharedPreferences("Account", 0).edit().clear().apply();
                    helperContext.getApplicationContext().getSharedPreferences("Urls", 0).edit().clear().apply();
                    helperContext.getApplicationContext().getSharedPreferences(Preferences.LIST, 0).edit().clear().apply();
                    helperContext.getApplicationContext().getSharedPreferences(Preferences.POST, 0).edit().clear().apply();
                    helperContext.getApplicationContext().getSharedPreferences(Preferences.SEARCH, 0).edit().clear().apply();
                    i = new Intent(helperContext,
                            LoginActivity.class);

                } else {
                    Toast.makeText(helperContext, "No Internet", Toast.LENGTH_SHORT).show();
                }
            } else
                return;


            helperContext.startActivity(i);


        }

        private void logout() {


            String url = ApiUtil.getLogout();
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(helperContext));
            dispatcher.cancelAll();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            RequestManager.getInstance(helperContext).addToRequestQueue(stringRequest);


        }

    }
}
