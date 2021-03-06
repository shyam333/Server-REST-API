package helloworld.demo.com.godrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shyamramesh on 25/05/18.
 */

public class SearchedJobs extends AppCompatActivity {

    List<ListItem> listItem = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view2);

        loadRecyclerViewData();

        recyclerView = (RecyclerView)findViewById(R.id.rc2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Search Results");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.backarrow);
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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....!");
        progressDialog.show();

        SimpleMultiPartRequest stringRequest = new SimpleMultiPartRequest(Request.Method.GET,
                Constants.URL_JOBS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            progressDialog.dismiss();

                            Intent intent = getIntent();
                            String category = intent.getStringExtra("key1");
                            String location = intent.getStringExtra("key2");
                            String expfrom = intent.getStringExtra("key3");
                            String expto = intent.getStringExtra("key4");

                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("data");

                            //JSONArray array = new JSONArray(s);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("job_title"),
                                        o.getString("category_name"),
                                        o.getString("experience_from"),
                                        o.getString("experience_to"),
                                        o.getString("location_name"),
                                        o.getString("keyskills"),
                                        o.getString("job_description"),
                                        o.getString("id")
                                );

                                String cat = o.getString("category_name");
                                String loc = o.getString("location_name");
                                String expf = o.getString("experience_from");
                                String expt = o.getString("experience_to");

                                Float value1 = Float.valueOf(expf);
                                Float value2 = Float.valueOf(expt);
                                Float value3 = Float.valueOf(expfrom);
                                Float value4 = Float.valueOf(expto);
                                //if((cat.equals(category) && loc.equals(location)) && (expf.equals(expfrom) && expt.equals(expto)))
                              //  if() {
                                 //   if ((value1 >= value3 && value1 <= value4) && (value2 >= value3 && value2 <= value4))
                                //    if ((value1 >= value4 && value1 <= value3) && (value2 >= value4 && value2 <= value3))
                                if(cat.equals(category) && loc.equals(location)) {
                                    if ((value3 >= value1 && value3 <= value2) || (value4 >= value1 && value4 <= value2) || (value3 <= value1 && value4 >= value2)) {
                                        listItem.add(item);
                                    }
                                }

                            }
                            mAdapter = new MyAdapter6(listItem,getApplicationContext());
                            if(listItem.isEmpty())
                            {
                                Toast.makeText(SearchedJobs.this,"No Jobs Found",Toast.LENGTH_SHORT).show();
                            }
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
