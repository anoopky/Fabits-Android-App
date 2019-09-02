package in.fabits.fabits.placeholder;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import in.fabits.fabits.R;
import in.fabits.fabits.adapters.ChatListAdapter;
import in.fabits.fabits.adapters.CustomGrid;
import in.fabits.fabits.adapters.HomeAdapter;
import in.fabits.fabits.adapters.OnlineListAdapter;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.EmojiesV1;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.RequestManager;
import in.fabits.fabits.api.Utils;
import in.fabits.fabits.model.ChatList;
import in.fabits.fabits.model.Online;
import in.fabits.fabits.model.Posts;


public class EmojiesPlaceholderFragment extends Fragment

{

    private static final String ARG_SECTION_NUMBER = "section_number";

    public interface ListItemClickListener{

        void onListItemClick(String id);
    }

    private static ListItemClickListener mOnClickListener;

    public static EmojiesPlaceholderFragment newInstance(int sectionNumber, ListItemClickListener mOnClickListener1 ) {
        EmojiesPlaceholderFragment fragment = new EmojiesPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        mOnClickListener = mOnClickListener1;
        return fragment;
    }


    private void onClicked(String s) {

        mOnClickListener.onListItemClick(s);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.emojies_grid, container, false);
        Bundle args = new Bundle(getArguments());
        int TabID = args.getInt(ARG_SECTION_NUMBER);

        if (TabID == 1) {

            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.people);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.people[position]);
                }
            });

        } else if (TabID == 2) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.nature);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.nature[position]);
//                    Toast.makeText(getContext(), EmojiesV1.nature[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 3) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.food);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.food[position]);
//                    Toast.makeText(getContext(), EmojiesV1.food[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 4) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.activity);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.activity[position]);

//                    Toast.makeText(getContext(), EmojiesV1.activity[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 5) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.travel);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.travel[position]);

//                    Toast.makeText(getContext(), EmojiesV1.travel[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 6) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.objects);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.objects[position]);

//                    Toast.makeText(getContext(), EmojiesV1.objects[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 7) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.symbols);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
           grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    onClicked(EmojiesV1.symbols[position]);
//                    Toast.makeText(getContext(), EmojiesV1.symbols[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 8) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.flags);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    onClicked(EmojiesV1.flags[position]);
//                    Toast.makeText(getContext(), EmojiesV1.flags[position] , Toast.LENGTH_SHORT).show();
                }
            });

        } else if (TabID == 9) {
            CustomGrid adapter = new CustomGrid(getContext(), EmojiesV1.diversity);
            GridView grid = (GridView) rootView.findViewById(R.id.grid);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    onClicked(EmojiesV1.diversity[position]);

//                    Toast.makeText(getContext(), EmojiesV1.diversity[position] , Toast.LENGTH_SHORT).show();
                }
            });


        }
        return rootView;
    }


}