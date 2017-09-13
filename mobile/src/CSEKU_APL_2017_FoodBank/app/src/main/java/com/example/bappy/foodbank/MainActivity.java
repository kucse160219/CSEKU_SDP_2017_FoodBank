package com.example.bappy.foodbank;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Handler;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //setting up a finite time for screen delay
    private static int splash_time_Out=2000;

    SharedPreferences sharedPreferences;
    Boolean save_login,skip;
    SharedPreferences.Editor edit;
    String type,name,resname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getSharedPreferences(getString(R.string.PREF_FILE), 0);
        edit=sharedPreferences.edit();

        type=sharedPreferences.getString(getString(R.string.TYPE),"None");
        name=sharedPreferences.getString(getString(R.string.NAME),"None");
        resname=sharedPreferences.getString(getString(R.string.RESTAURANT_NAME),"None");
        save_login=sharedPreferences.getBoolean(getString(R.string.SAVE_LOGIN),false);
        skip=sharedPreferences.getBoolean(getString(R.string.SKIP),false);

//        if (!isNetworkAvilabe()) {
//            //Creating an Alertdialog
//            AlertDialog.Builder CheckBuild = new AlertDialog.Builder(MainActivity.this);
//            CheckBuild.setIcon(R.drawable.no);
//            CheckBuild.setTitle("Error!");
//            CheckBuild.setMessage("Check Your Internet Connection");
//
//            //Builder Retry Button
//
//            CheckBuild.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int id) {
//                    //Restart The Activity
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                }
//
//            });
//            CheckBuild.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int which) {
//                    //Exit The Activity
//                    finish();
//                }
//
//            });
//            AlertDialog alertDialog = CheckBuild.create();
//            alertDialog.show();
//        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        if(type.equals("User"))
                        {
                            //creating intent and going to the home activity
                            Intent newintent = new Intent(MainActivity.this, HomeActivity.class);
                            //starting the activity
                            startActivity(newintent);
                        }
                        else if(type.equals("Admin"))
                        {
                            //creating intent and going to the home activity
                            Intent intent = new Intent(MainActivity.this, Adminstaff.class);
                            intent.putExtra("username", name);
                            intent.putExtra("resname", resname);
                            intent.putExtra("role", "Admin");
                            //starting the activity
                            startActivity(intent);
                        }
                        else if(type.equals("Staff"))
                        {
                            //creating intent and going to the home activity
                            Intent intent = new Intent(MainActivity.this, OnlyStaff.class);
                            intent.putExtra("username", name);
                            intent.putExtra("resname", resname);
                            intent.putExtra("role", "Staff");
                            //starting the activity
                            startActivity(intent);
                        }
                        else if(type.equals("Superadmin"))
                        {
                            //creating intent and going to the home activity
                            Intent newintent = new Intent(MainActivity.this, SuperAdminRestaurant.class);
                            newintent.putExtra("username", name);
                            newintent.putExtra("role", "SuperAdmin");
                            //starting the activity
                            startActivity(newintent);
                        }
                        else if(type.equals("None") && !skip)
                        {
                            //creating intent and going to the home activity
                            Intent newintent = new Intent(MainActivity.this, staff_login_resistor.class);
                             //starting the activity
                            startActivity(newintent);
                        }
                        else
                        {
                            //creating intent and going to the home activity
                            Intent newintent = new Intent(MainActivity.this, HomeActivity.class);
                            //starting the activity
                            startActivity(newintent);
                        }
                        //when intent is start and go to home class then main activity will finish
                        finish();
                }
            }, splash_time_Out);//a time out for finishing the main activity
       // }
    }
        private boolean isNetworkAvilabe()
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
}