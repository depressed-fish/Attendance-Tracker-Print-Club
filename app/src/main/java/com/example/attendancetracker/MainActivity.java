package com.example.attendancetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    String sheetID = "1fv8KHlvuJu0o_sx26L14YP8rsz0QdQuBOAVB4Ti61LM";
    String apiKEY = "AIzaSyA1oGHZE1EK9giZLMnKdsnNFHYqnEkA1Cs";

    String strID;
    String strName;
    String strCGPA;

    JSONArray jsonArray;
    ListView listView;

    CustomAdapter customAdapter;

    ArrayList<String> listID = new ArrayList<String>();
    ArrayList<String> listNAME = new ArrayList<String>();
    ArrayList<String> listCGPA = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview_id);

        getData();

    }

    private void getData() {
        String urls = "https://sheets.googleapis.com/v4/spreadsheets/"+sheetID+"/values/sheet1?key="+apiKEY;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urls, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonArray = response.getJSONArray("values");
                } catch (Exception e) {}

                IntStream.range(1, jsonArray.length()).forEach(i-> {
                    try {
                        JSONArray json = jsonArray.getJSONArray(i);
                        strID = json.getString(0);
                        strName = json.getString(1);
                        strCGPA = json.getString(2);

                        listID.add(strID);
                        listNAME.add(strName);
                        listCGPA.add(strCGPA);

                        customAdapter = new CustomAdapter(getApplicationContext(), listID, listNAME, listCGPA);
                        listView.setAdapter(customAdapter);

                    } catch (Exception e) {}
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }


}