package com.farhan.haque.doctor;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private  List<String> listData= new ArrayList<>();
    private  List<String> listData2= new ArrayList<>();
    //private  Map<String, List<String>> mapData= new HashMap<>();
    ArrayAdapter adapter;
    ListView listView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listView);
        textView= (TextView) findViewById(R.id.textView2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        Background background= new Background();
        background.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
               // String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
               // Toast.makeText(MainActivity.this,listData2.get(position),Toast.LENGTH_LONG).show();
                Uri number = Uri.parse("tel:"+listData2.get(position));
                Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                startActivity(callIntent);
                v.setVisibility(View.INVISIBLE);
               /* Intent localIntent4 = new Intent("com.viber.voip.action.CALL");
                localIntent4.setType("vnd.android.cursor.item/vnd.com.viber.voip.call");
                localIntent4.setData(Uri.parse("tel:" + listData2.get(position)));
                localIntent4.putExtra("external_call", true);
                localIntent4.putExtra("contact_id", -1L);
                startActivity(localIntent4);*/
            }
        });

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public class Background extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            InputStream is = null;
            try {
                is = new URL("http://1-dot-restfulservice-1246.appspot.com/database/users").openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONArray jsonArray = new JSONArray(jsonText);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                    if(jsonobject.getString("event").matches("Doctor")) {
                        String name = jsonobject.getString("name");
                        //String event = jsonobject.getString("event");
                       // String latitude = jsonobject.getString("latitude");
                       // String longitude = jsonobject.getString("longitude");
                        String phone = jsonobject.getString("phone");
                        listData.add(name);
                        listData2.add(phone);
                       // listData.add(event);
                       // listData.add(latitude);
                       // listData.add(longitude);
                       // mapData.put(phone, listData);
                    }
                }
                is.close();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          //  Toast.makeText(MainActivity.this,"test", Toast.LENGTH_LONG).show();
            adapter= new ArrayAdapter<>(MainActivity.this,R.layout.item, listData);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    }
}
