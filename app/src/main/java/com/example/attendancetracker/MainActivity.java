package com.example.attendancetracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import org.w3c.dom.Text;

import java.security.Signature;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {

    String sheetID = "1fv8KHlvuJu0o_sx26L14YP8rsz0QdQuBOAVB4Ti61LM";
    String apiKEY = "AIzaSyA1oGHZE1EK9giZLMnKdsnNFHYqnEkA1Cs";

    String strStudentNumber;
    String strStudentName;
    String strSignedIn;
    String strRecentSignIn;
    String strTotalTime;
    JSONArray jsonArray;

    ArrayList<String> listStudentNumbers = new ArrayList<String>();
    ArrayList<String> listStudentName = new ArrayList<String>();
    ArrayList<String> listSignedIn = new ArrayList<String>();
    ArrayList<String> listRecentSignIn = new ArrayList<String>();
    ArrayList<String> listTotalTime = new ArrayList<String>();

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

        Button[] pin = new Button[]{buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine};

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
                String pinInput = text.getText().toString();
                getData(v, pinInput);
                text.setText("");
            }
        });


        //getData();
        //addItemToSheet();
    }

    private void continueEnter(View v, String pinInput) {

        boolean found = false;

            for (int i = 0; i < listStudentNumbers.size(); i++) {

                String checkpin = listStudentNumbers.get(i).trim();

                if (pinInput.trim().equals(checkpin)) {

                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.confirmationpopup, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.setElevation(20);
                    // show the popup window
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    String name, signIn, signOut, newTime, totalTime;

                    name = listStudentName.get(i);
                    totalTime = listTotalTime.get(i);

                    Button cancelButton = (Button) popupView.findViewById(R.id.buttoncancel);
                    Button signButton = (Button) popupView.findViewById(R.id.buttonsign);

                    boolean signedIn = Boolean.parseBoolean(listSignedIn.get(i));
                    if (signedIn) {
                        Date d=new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                        signIn = listRecentSignIn.get(i);
                        signOut = sdf.format(d);
                        newTime = getDifference(signIn, signOut);
                        totalTime = String.valueOf(Double.parseDouble(totalTime) + Double.parseDouble(newTime));

                        signButton.setText("Sign-out");
                    } else {
                        Date d=new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                        signIn = sdf.format(d);
                        signOut = "N/A";
                        newTime = "N/A";
                        signButton.setText("Sign-in");
                    }

                    TextView nameText = (TextView) popupView.findViewById(R.id.name);
                    TextView idText = (TextView) popupView.findViewById(R.id.studentnum);
                    TextView signInText = (TextView) popupView.findViewById(R.id.signintime);
                    TextView signOutText = (TextView) popupView.findViewById(R.id.signouttime);
                    TextView newTimeText = (TextView) popupView.findViewById(R.id.newtime);
                    TextView totalTimeText = (TextView) popupView.findViewById(R.id.totaltime);

                    nameText.setText(name);
                    idText.setText(pinInput);
                    signInText.setText("Sign-in:\n" + signIn);
                    signOutText.setText("Sign-out:\n" + signOut);
                    newTimeText.setText("New Time:\n" + newTime);
                    totalTimeText.setText("Total:\n" + totalTime);

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                    final String finalTotalTime = totalTime;

                    final String newHours = newTime;
                    signButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            boolean newSignIn = !signedIn;
                            addItemToSheet(pinInput, name, Boolean.toString(newSignIn), signIn, signOut, newHours, finalTotalTime);
                            popupWindow.dismiss();
                        }
                    });

                    found = true;
                }

                if (found) {
                    break;
                }
            }


        if (!found) {
            Toast.makeText(MainActivity.this, "Incorrect Pin", Toast.LENGTH_LONG).show();
        }
    }

    private String getDifference(String startTime, String endTime) {

        String startHour = startTime.substring(0, 2);
        String startMinute = startTime.substring(3);
        String endHour = endTime.substring(0, 2);
        String endMinute = endTime.substring(3);

        int startHourNum, startMinuteNum, endHourNum, endMinuteNum;

        if (startHour.substring(0, 1).trim().equals("0")) {
            startHourNum = Integer.parseInt(startHour.substring(1));
        } else {
            startHourNum = Integer.parseInt(startHour.substring(0));
        }

        if (startMinute.substring(0, 1).trim().equals("0")) {
            startMinuteNum = Integer.parseInt(startMinute.substring(1));
        } else {
            startMinuteNum = Integer.parseInt(startMinute.substring(0));
        }

        if (endHour.substring(0, 1).trim().equals("0")) {
            endHourNum = Integer.parseInt(endHour.substring(1));
        } else {
            endHourNum = Integer.parseInt(endHour.substring(0));
        }

        if (endMinute.substring(0, 1).trim().equals("0")) {
            endMinuteNum = Integer.parseInt(endMinute.substring(1));
        } else {
            endMinuteNum = Integer.parseInt(endMinute.substring(0));
        }

        double newHour = endHourNum - startHourNum;
        double newMinute = endMinuteNum - startMinuteNum;

        if (newMinute >= 60) {
            newMinute = newMinute - 60;
            newHour += 1;
        } else if (newMinute < 0) {
            newMinute = newMinute + 60;
            newHour -= 1;
        }

        DecimalFormat df=new DecimalFormat("#.##");
        double newTime = newHour + newMinute / 60;

        if (newTime > 12 || newTime < 0) {
            newTime = 0;
        }

        return df.format(newTime);
    }

    private double getHour(String time) {

        String hour = time.substring(0, 2);
        String minutes = time.substring(3);

        DecimalFormat df=new DecimalFormat("#.##");
        double hourTime = Double.parseDouble(hour) + Double.parseDouble(minutes) / 60;

        return Double.parseDouble(df.format(hourTime));
    }

    private void getData(View v, String pinInput) {
        String urls = "https://sheets.googleapis.com/v4/spreadsheets/" + sheetID + "/values/Item?key=" + apiKEY;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        listStudentNumbers = new ArrayList<String>();
        listStudentName = new ArrayList<String>();
        listSignedIn = new ArrayList<String>();
        listRecentSignIn = new ArrayList<String>();
        listTotalTime = new ArrayList<String>();

        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Getting Data", "Please Wait...");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urls, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonArray = response.getJSONArray("values");
                } catch (Exception e) {
                }

                IntStream.range(1, jsonArray.length()).forEach(i -> {
                    try {
                        JSONArray json = jsonArray.getJSONArray(i);
                        strStudentNumber = json.getString(0);
                        strStudentName = json.getString(1);
                        strSignedIn = json.getString(2);
                        strRecentSignIn = json.getString(3);
                        strTotalTime = json.getString(6);

                        listStudentNumbers.add(strStudentNumber);
                        listStudentName.add(strStudentName);
                        listSignedIn.add(strSignedIn);
                        listRecentSignIn.add(strRecentSignIn);
                        listTotalTime.add(strTotalTime);

                    } catch (Exception e) {
                    }
                    dialog.dismiss();

                    continueEnter(v, pinInput);
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void addItemToSheet(String id, String studentName, String newSignIn, String signInTime, String signOutTime, String recentHours, String newTime) {

        final String ACTION = Boolean.parseBoolean(newSignIn) ? "SIGNIN" : "SIGNOUT";

        String titleString = Boolean.parseBoolean(newSignIn) ? "Signing In" : "Signing Out";

        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, titleString, "Please Wait...");
        final String ID = id;
        final String STUDENTNAME = studentName;
        final String SIGNINTIME = signInTime;
        final String SIGNOUTTIME = signOutTime;
        final String RECENTHOURS = recentHours;
        final String NEWTIME = newTime;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwV5sG6ndUKIdkrRTheNdXV40G9BC-Z-r5gd94s7AuYOa45nTowaBRC8e1KnOLSOZUqqA/exec", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_LONG).show();
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

                params.put("ACTION", ACTION);
                params.put("ID", ID);
                params.put("STUDENTNAME", STUDENTNAME);
                params.put("SIGNINTIME", SIGNINTIME);
                params.put("SIGNOUTTIME", SIGNOUTTIME);
                params.put("RECENTHOURS", RECENTHOURS);
                params.put("NEWTIME", NEWTIME);

                return params;
            }
        };

        int timeOut = 50000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        queue.add(stringRequest);

    }
}