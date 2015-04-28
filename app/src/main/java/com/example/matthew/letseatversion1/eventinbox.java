package com.example.matthew.letseatversion1;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jake on 4/23/15.
 */
public class eventinbox extends ActionBarActivity
{
    String username="";
    String[] list;
    JSONArray arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventscreen);
        setTitle("Event Inbox");
        //getActionBar().setHomeButtonEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);



        if(getIntent().getExtras() != null && getIntent().getExtras().getString("passingUsername") != null)
        {
            username = getIntent().getExtras().getString("passingUsername");
            new CheckEvents().execute("http://www.csce.uark.edu/~mrs018/CheckEventInvites.php");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
        //CreateEvent.php
    }






    class CheckEvents extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from LoginScreen class to HttpRequest class
        String Username;


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try
            {
                //result = "[{\"inviter\":\"ll\",\"dateandtime\":\"fjdjdj\",\"location\":\"house\"}]";
                arr = new JSONArray(result);
                //JSONArray arr = OBJ.getJSONArray("");//new JSONArray(result);
                //String[] inviters = new String[1];
                if(arr != null)
                {
                    list = new String[arr.length()];
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject invite = arr.getJSONObject(i);
                        String inviter = invite.getString("inviter");
                        list[i] = "Event Creator: " + inviter + "\n";
                        list[i] += "Location: " + invite.getString("location") + "\n";
                        list[i] += "Date and Time: " + invite.getString("dateandtime");

                    }
                }
                //list = new String[1];
                //list[0] = arr.getString("inviter");

                //Toast toast = Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ListView listView = (ListView) findViewById(R.id.list);
            String[] locations = {result, "amp", "le"};
            ArrayAdapter<String> listadapter;
            if(list != null)
                listadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listentry, list);
            else
                listadapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listentry, locations);
            listView.setAdapter(listadapter);


            /*if(list.length == 0)
            {
                TextView empty = (TextView) findViewById(R.id.empty);
                empty.setVisibility(View.VISIBLE);
            }*/
            //important^^

            /*checkInviteServerResponse = result;
            if(!checkInviteServerResponse.equalsIgnoreCase("[]")) {
                eventTokens = checkInviteServerResponse.split("\"");
                Inviter = eventTokens[3];
                dateandtime = eventTokens[7];
                location = eventTokens[11];
                invitation = true;

                /*new AlertDialog.Builder(context).setTitle("response from server")
                        .setMessage(result)
                        .setIcon(android.R.drawable.ic_dialog_alert).show();*/
            //}
            //eventCheckFinished()

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            //username = newUserID;
            Username = username;


            super.onPreExecute();
        }

        @Override
        //this passing info to and from the app and server
        //it is done on a separate thread so that it does not interfere with other task while the info is transferred
        protected String doInBackground(String... params) {
            try
            {
                //makes name value pairs to be passed to server
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", Username));


                //makes a httpclient and sends the info to the server
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost method = new HttpPost(params[0]);
                method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(method);
                HttpEntity entity = response.getEntity();

                //keep the stream open until all the data has been passed back
                if(entity != null){
                    return EntityUtils.toString(entity);
                }
                else{
                    return "No string.";
                }
            }
            catch(Exception e){
                return "Network problem";
            }

        }
    }

}