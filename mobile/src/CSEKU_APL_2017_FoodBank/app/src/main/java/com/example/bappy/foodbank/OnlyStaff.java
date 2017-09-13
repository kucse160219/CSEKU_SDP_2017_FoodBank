package com.example.bappy.foodbank;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class OnlyStaff extends AppCompatActivity {

    String name,resname,role;
    String datetime;
    TextView txt;

    private TextView startDateDisplay;
    private Button startPickDate;
    private Calendar startDate;
    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.only_staff_layout);

        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        editor=sharedPreferences.edit();

        name=getIntent().getExtras().getString("username");
        resname=getIntent().getExtras().getString("resname");
        role=getIntent().getExtras().getString("role");
        txt=(TextView)findViewById(R.id.name);
        txt.setText(name+" ("+role+")");

        /*  capture our View elements for the start date function   */
        startDateDisplay = (TextView) findViewById(R.id.date);
        startPickDate = (Button) findViewById(R.id.getdate);

        startDate = Calendar.getInstance();

        /* add a click listener to the button   */
        startPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(OnlyStaff.this, datetime, Toast.LENGTH_SHORT).show();
                showDateDialog(startDateDisplay, startDate);
            }
        });
        updateDisplay(startDateDisplay, startDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_staff_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, staff_login_resistor.class));
                finish();
                return true;
            case R.id.my_profile:
                startActivity(new Intent(this, ShowProfile.class));
                return true;
            case R.id.new_restaurant:
                startActivity(new Intent(this, CreateNewRestaurant.class));
                return true;
            case R.id.edit_profile:
                Intent intent=new Intent(this, EditChangeProfile.class);
                intent.putExtra("op_type","Edit");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        datetime=date.get(Calendar.YEAR)+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DAY_OF_MONTH);
        dateDisplay.setText(datetime);
    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override

        public void onDateSet(android.widget.DatePicker view, int year,int monthOfYear, int dayOfMonth)
        {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
    public void orderlist(View view){
        new BackgroundtaskOrderlist().execute("O",name,resname,role,datetime);
    }

    public void deliverylist(View view){
        new BackgroundtaskOrderlist().execute("D",name,resname,role,datetime);
    }

    public void allorder(View view){
        new BackgroundtaskOrderlist().execute("A",name,resname,role,datetime);
    }
    public class BackgroundtaskOrderlist extends AsyncTask<String,Void,Boolean>
    {

        String json_url;
        String JSON_STRING;
        String name2,role,datet,type,res,resu;

        @Override
        protected void onPreExecute() {
            json_url="http://"+getString(R.string.ip_address)+"/FoodBank/dateorder.php";
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                type=params[0];
                name2 = params[1];
                res=params[2];
                role=params[3];
                datet=params[4];
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputstream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedwritter = new BufferedWriter(new OutputStreamWriter(outputstream, "UTF-8"));
                String postdata = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(name2, "UTF-8")+ "&" +
                        URLEncoder.encode("resname", "UTF-8") + "=" + URLEncoder.encode(res, "UTF-8") + "&" +
                        URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datet, "UTF-8");
                bufferedwritter.write(postdata);
                bufferedwritter.flush();
                bufferedwritter.close();
                outputstream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING=bufferedReader.readLine())!=null){
                    stringBuilder.append(JSON_STRING+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                resu= stringBuilder.toString().trim();
                return true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result)
                Toast.makeText(OnlyStaff.this, "can't connect to the database", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(OnlyStaff.this, StaffFoodOrder.class);
                intent.putExtra("type", type);
                intent.putExtra("username", name2);
                intent.putExtra("res", res);
                intent.putExtra("role", role);
                intent.putExtra("datet", datet);
                intent.putExtra("order_details", resu);
                intent.putExtra("staff_admin", "2");
                startActivity(intent);
                finish();
            }
        }
    }
    //creating activity for back pressing from phone
    public void onBackPressed() {
        //creating a alert dialog(for exit)
        final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(OnlyStaff.this);
        //setting the alertdialog title
        exitbuilder.setTitle("Attention");
        //setting the body message
        exitbuilder.setMessage("Do You Want To Exit?");
        //setting the icon
        exitbuilder.setIcon(R.drawable.exit);
        //set state for cancelling state
        exitbuilder.setCancelable(true);

        //setting activity for positive state button
        exitbuilder.setPositiveButton("YES, Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //setting activity for negative state button
        exitbuilder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //alertdialog create
        AlertDialog mydialog=exitbuilder.create();
        //for working the alertdialog state
        mydialog.show();
    }
}
//changed
