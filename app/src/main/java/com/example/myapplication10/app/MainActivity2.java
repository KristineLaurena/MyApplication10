package com.example.myapplication10.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;


public class MainActivity2 extends ActionBarActivity {

    private static final String LOG_TAG = "Main Activity 2 LOG: ";
    public MainActivity.Showplace showplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);

        final EditText nameTW = (EditText)findViewById(R.id.editText);
        final EditText shortDescrpitionTW = (EditText)findViewById(R.id.editText2);
        final EditText longDescriptionTW = (EditText)findViewById(R.id.editText3);

        Button backButton  = (Button)findViewById(R.id.button);
        Button saveButton  = (Button)findViewById(R.id.button2);
        Button deleteButton  = (Button)findViewById(R.id.button3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showplace.Name = nameTW.getText().toString();
                showplace.Description = shortDescrpitionTW.getText().toString();
                showplace.LongDescription = longDescriptionTW.getText().toString();

                Toast.makeText(getApplicationContext(), "OK till here", Toast.LENGTH_SHORT);
                Log.d(LOG_TAG, "OK TILL HERE");

                String entity = new Gson().toJson(showplace);

                Toast.makeText(getApplicationContext(), entity, Toast.LENGTH_SHORT);
                Log.d(LOG_TAG,  entity);

                try {
                    HttpResponse response = makeRequest("http://81.198.252.179/AvioTravelling/api/Geo/UpdateShowplace", entity);
                    finish();
                }
                catch(Exception e) {
                    //  Log.d(LOG_TAG, e.getCause());
                    Log.d(LOG_TAG, e.getMessage().toString());
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String entity = new Gson().toJson(showplace);
                Log.d(LOG_TAG, entity);

                try {
                    HttpResponse response = makeRequest("http://81.198.252.179/AvioTravelling/api/Geo/DeleteShowPlace", entity);
                    finish();
                }
                catch(Exception e) {
                    //  Log.d(LOG_TAG, e.getCause());
                    //Log.d(LOG_TAG, e.getMessage().toString());
                    finish();
                }

            }
        });

//        TextView textView = new TextView(this);
          showplace = new Gson().fromJson(getIntent().getExtras().getString("user"), MainActivity.Showplace.class);
//        textView.setText(showplace.Name);
//
//        this.setContentView(textView);

        nameTW.setText(showplace.Name);
        shortDescrpitionTW.setText(showplace.Description);
        longDescriptionTW.setText(showplace.LongDescription);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static HttpResponse makeRequest(String path, String entity) throws Exception
    {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httpost = new HttpPost(path);


        StringEntity se = new StringEntity(entity);

        //sets the post request as the resulting string
        httpost.setEntity(se);
        //sets a request header so the page receving the request
        //will know what to do with it
        httpost.setHeader("Accept", "*/*");
        httpost.setHeader("Content-type", "application/json; UTF-8");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        httpclient.execute(httpost, responseHandler);
        return (HttpResponse)httpclient.execute(httpost, responseHandler);
    }

}
