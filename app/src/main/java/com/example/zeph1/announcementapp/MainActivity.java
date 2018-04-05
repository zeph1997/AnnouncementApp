package com.example.zeph1.announcementapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference dbRef;
    BottomNavigationView bottomNavigationView;
    int fragStateIdentifier;
    private ViewPager viewPager;
    ArrayList<Announcements> announcementsArrayList = new ArrayList<>();
    AnnouncementAdapter announcementAdapter;
    SQLiteDatabase sqlDb;
    ArrayList<String> articleURLs = new ArrayList<String>();

    //Public static arraylist for saved articles
    public static ArrayList<Announcements> savedAnnouncements = new ArrayList<>();
    public static ArrayList<String>  savedAnnouncementIDandURL = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise firebase reference for app
        //dbRef = FirebaseDatabase.getInstance().getReference();

        //Initiate ViewPager
        viewPager = (ViewPager) findViewById(R.id.fragment_main);
        SectionStatePagerAdapter adapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(),"Home");
        adapter.addFragment(new MessageFragment(),"Message");
        adapter.addFragment(new SavedFragment(),"Saved");
        adapter.addFragment(new ProfileFragment(),"Profile");
        viewPager.setAdapter(adapter);

        //get Action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Hacker News API");

        //Intitialise stuffs in main activity first
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavBar);
        android.support.v4.app.FragmentManager fragmentManager = this.getSupportFragmentManager();
        //Reset the identifier to 0 and fragement to home fragement, enabling for easy reference to current fragement
        fragStateIdentifier = 0;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Create Fragement Transaction and prepare for transaction commit
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()){
                    case R.id.action_home:
                        if (fragStateIdentifier !=0){
                            android.support.v4.app.Fragment newFragment = new HomeFragment();
                            //Replace whatever is in the fragment_container view with this fragment,
                            //and add the transaction to the back stack
                            //transaction.replace(R.id.fragment_main, newFragment);
                            //transaction.addToBackStack(null);
                            //transaction.commit();
                            fragStateIdentifier = 0;
                            viewPager.setCurrentItem(fragStateIdentifier);

                        }
                        break;
                    case R.id.action_message:
                        if (fragStateIdentifier != 1){
                            android.support.v4.app.Fragment newFragment = new MessageFragment();
                            fragStateIdentifier = 1;
                            viewPager.setCurrentItem(fragStateIdentifier);
                        }
                        break;
                    case R.id.action_saved:
                        if (fragStateIdentifier != 2){
                            android.support.v4.app.Fragment newFragment = new SavedFragment();
                            fragStateIdentifier = 2;
                            viewPager.setCurrentItem(fragStateIdentifier);
                        }
                        break;
                    case R.id.action_profile:
                        if (fragStateIdentifier != 3){
                            android.support.v4.app.Fragment newFragment = new ProfileFragment();
                            fragStateIdentifier = 3;
                            viewPager.setCurrentItem(fragStateIdentifier);
                        }
                        break;
                }
                return true;
            }
        });

        //Check if the user swipes page to the side and changes tab
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int selectedFragmentIconId = 0;
                switch(position){
                    case 0:
                        selectedFragmentIconId = R.id.action_home;
                        actionBar.setTitle("Hacker News API");
                        break;
                    case 1:
                        selectedFragmentIconId = R.id.action_message;
                        actionBar.setTitle("Message");
                        break;
                    case 2:
                        selectedFragmentIconId = R.id.action_saved;
                        actionBar.setTitle("Saved Articles");
                        break;
                    case 3:
                        selectedFragmentIconId = R.id.action_profile;
                        actionBar.setTitle("Your Profile");
                        break;
                }
                bottomNavigationView.setSelectedItemId(selectedFragmentIconId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        //if (homeFeed != null){
        //    Toast.makeText(this,"homefeed not empty",Toast.LENGTH_SHORT).show();
        //}else{
        //    Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        //}

        //homeFeed.setAdapter(announcementAdapter);



        //Initialise announcementAdapter
        //homeFeed.setAdapter(HomeFragment.finalAdapter);

    }




    public static class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
    }
}
