package com.example.myapplication10.app;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = "DEBUG";
    public ArrayList<Showplace> showplaces = new ArrayList<Showplace>();


    public class Showplace{
        public String Id;
        public String Name;
        public String Description;
        public String LongDescription;
        public String PictureLink;

        public Showplace(String name, String description, String longdescription)  {
            this.Name = name;
            this.Description = description;
            this.LongDescription = longdescription;
        }
    }

    public class ShowplaceAdapter extends ArrayAdapter<Showplace> {
        public ShowplaceAdapter(Context context, ArrayList<Showplace> showplaces) {
            super(context, 0, showplaces);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Showplace showplace = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                //   convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item_1, parent, false);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewitem, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.textView);
            TextView tvHome = (TextView) convertView.findViewById(R.id.textView2);
            // Populate the data into the template view using the data object
            tvName.setText(showplace.Name);
            tvHome.setText(showplace.Description);
            // Return the completed view to render on screen
            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new HttpAsyncTask().execute("http://81.198.252.179/AvioTravelling/api/Geo/Showplaces");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new HttpAsyncTask().execute("http://81.198.252.179/AvioTravelling/api/Geo/Showplaces");

        ListView listView = (ListView) findViewById(R.id.listView);

        final ArrayList<Showplace> showplaces = new ArrayList<Showplace>();
        ShowplaceAdapter showplaceAdapter = new ShowplaceAdapter(this, showplaces);
        listView.setAdapter(showplaceAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + id);

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("user", new Gson().toJson(showplaces.get(position)));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            new HttpAsyncTask().execute("http://81.198.252.179/AvioTravelling/api/Geo/Showplaces");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
      //      Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            Gson gson = new Gson();
            Showplace[] myTypes = gson.fromJson(result, Showplace[].class);

            ListView listView = (ListView) findViewById(R.id.listView);

            showplaces.clear();

            showplaces.addAll(Arrays.asList(myTypes));
            ShowplaceAdapter adapter = (ShowplaceAdapter)listView.getAdapter();
            adapter.clear();
            adapter.addAll(myTypes);
        }
    }

}
