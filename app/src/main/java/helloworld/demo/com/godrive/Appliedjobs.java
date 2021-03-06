package helloworld.demo.com.godrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shyamramesh on 24/05/18.
 */

public class Appliedjobs extends AppCompatActivity {

    List<ListItem> listItem = new ArrayList<>();
    MyAdapter3 mAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
   // String strJobtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view2);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        loadRecyclerViewData();

        recyclerView = (RecyclerView) findViewById(R.id.rc2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Applied Jobs");
        Drawable drawable = ContextCompat.getDrawable(Appliedjobs.this,R.drawable.backarrow);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private void loadRecyclerViewData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Appliedjobs.this);
        final String s = preferences.getString("candidateid","n/a");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....!");
        progressDialog.show();


        SimpleMultiPartRequest stringRequest = new SimpleMultiPartRequest(Request.Method.POST,
                Constants.URL_APPLIED_JOBS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("applied response",response);
                            String status = jsonObject.getString("status");
                            Log.d("STATUS",status);
                            String statusMessage = jsonObject.getString("status_message");
                            Log.d("STATUS MESSAGE",statusMessage);
                            if(statusMessage.equals("No Jobs Applied"))
                            {

                                Toast.makeText(Appliedjobs.this, "No Jobs Applied", Toast.LENGTH_LONG).show();

                               // startActivity(new Intent(Appliedjobs.this, Main2Activity.class));
                            }

                            JSONArray array = jsonObject.getJSONArray("data");
                            //JSONArray array = new JSONArray(s);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                               // strJobtitle = o.getString("job_title");

                                ListItem item = new ListItem(

                                        o.getString("job_title"),
                                        o.getString("category_name"),
                                        o.getString("experience_from"),
                                        o.getString("experience_to"),
                                        o.getString("location_name"),
                                        o.getString("keyskills"),
                                        o.getString("job_description"),
                                        o.getString("id"),
                                        o.getString("applied_date")

                                );
                                        listItem.add(item);

                            }
                            mAdapter = new MyAdapter3(listItem, getApplicationContext());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        stringRequest.addStringParam("candidateid",s);


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


}

