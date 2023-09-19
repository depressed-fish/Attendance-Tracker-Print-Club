package com.example.attendancetracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    String sheetID = "1fv8KHlvuJu0o_sx26L14YP8rsz0QdQuBOAVB4Ti61LM";
    String apiKEY = "AIzaSyA1oGHZE1EK9giZLMnKdsnNFHYqnEkA1Cs";

    String strDate;
    String strID;
    String strName;
    String strCGPA;

    JSONArray jsonArray;
    ListView listView;

    CustomAdapter customAdapter;

    ArrayList<String> listDate = new ArrayList<String>();
    ArrayList<String> listID = new ArrayList<String>();
    ArrayList<String> listNAME = new ArrayList<String>();
    ArrayList<String> listCGPA = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.pininput);

        //Masterclass level code right here:
        Button buttonOne = (Button) findViewById(R.id.button1);
        Button buttonTwo = (Button) findViewById(R.id.button2);
        Button buttonThree = (Button) findViewById(R.id.button3);
        Button buttonFour = (Button) findViewById(R.id.button4);
        Button buttonFive = (Button) findViewById(R.id.button5);
        Button buttonSix = (Button) findViewById(R.id.button6);
        Button buttonSeven = (Button) findViewById(R.id.button7);
        Button buttonEight = (Button) findViewById(R.id.button8);
        Button buttonNine = (Button) findViewById(R.id.button9);
        Button buttonDelete = (Button) findViewById(R.id.buttondelete);
        Button buttonZero = (Button) findViewById(R.id.button0);
        Button buttonEnter = (Button) findViewById(R.id.buttonenter);

        Button[] pin = new Button[] {buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine};

        for (int i = 0; i < pin.length; i++) {
            Button button = pin[i];
            int num = i;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String newText = text.getText().toString() + "" + num;
                    text.setText(newText);
                }
            });
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldText = text.getText().toString();
                try {
                    String newText = oldText.toString().substring(0, oldText.length() - 1);

                    text.setText(newText);
                } catch (Exception e) {

                }
            }
        });

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });


        getData();
        addItemToSheet();
    }

    private void getData() {
        String urls = "https://sheets.googleapis.com/v4/spreadsheets/"+sheetID+"/values/Item?key="+apiKEY;

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
                        strDate = json.getString(0);
                        strID = json.getString(1);
                        strName = json.getString(2);
                        strCGPA = json.getString(3);

                        listDate.add(strDate);
                        listID.add(strID);
                        listNAME.add(strName);
                        listCGPA.add(strCGPA);

                        customAdapter = new CustomAdapter(getApplicationContext(), listDate, listID, listNAME, listCGPA);
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

    private void addItemToSheet() {

        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Adding Item", "Please Wait...");
        final String ID = "3";
        final String NAME = "Grant";
        final String CGPA = "3.5";

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwV5sG6ndUKIdkrRTheNdXV40G9BC-Z-r5gd94s7AuYOa45nTowaBRC8e1KnOLSOZUqqA/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("ID", ID);
                params.put("NAME", NAME);
                params.put("CGPA", CGPA);

                return params;
            }
        };

        int timeOut = 50000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        queue.add(stringRequest);

    }
}