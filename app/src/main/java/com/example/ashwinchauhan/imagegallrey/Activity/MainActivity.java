package com.example.ashwinchauhan.imagegallrey.Activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ashwinchauhan.imagegallrey.Adapter.GalleryAdapter;
import com.example.ashwinchauhan.imagegallrey.R;
import com.example.ashwinchauhan.imagegallrey.app.AppController;
import com.example.ashwinchauhan.imagegallrey.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "https://api.androidhive.info/json/glide.json";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        recyclerView=findViewById(R.id.recycler_view);

        pDialog=new ProgressDialog(this);
        images=new ArrayList<>();
        mAdapter=new GalleryAdapter(getApplicationContext(),images);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Do some stuff
            RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getApplicationContext(),3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        }
        else
        {
            RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getApplicationContext(),2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        }


        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecycleTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchImages();
    }

   private void fetchImages() {
       pDialog.setMessage("Downloading json...");
       pDialog.show();

        JsonArrayRequest req=new JsonArrayRequest(endpoint, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

               // Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG,response.toString());
                pDialog.hide();

                images.clear();

                for(int i=0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);

                        Image image=new Image();
                        image.setName(jsonObject.getString("name"));

                        JSONObject url=jsonObject.getJSONObject("url");
                        image.setSmall(url.getString("small"));
                        image.setMedium(url.getString("medium"));
                        image.setLarge(url.getString("large"));

                        image.setTimestamp(jsonObject.getString("timestamp"));

                        images.add(image);
                    } catch (JSONException e) {

                        Log.e(TAG, "Json parsing error: " + e.getMessage());

                        e.printStackTrace();
                    }

                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
       AppController.getInstance().addToRequestQueue(req);
    }
}
