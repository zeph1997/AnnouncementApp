package com.example.zeph1.announcementapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by zeph1 on 3/4/2018.
 */

public class SavedFragment extends Fragment {

    public AnnouncementAdapter savedAnnouncementAdapter;
    //public ArrayList<>
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.saved_fragment_layout,container,false);

        ListView savedFeed = (ListView) v.findViewById(R.id.lvSaved);

        savedAnnouncementAdapter = new AnnouncementAdapter(getActivity().getApplicationContext(),MainActivity.savedAnnouncements);

        SQLiteDatabase sqLiteDatabase = getActivity().getApplicationContext().openOrCreateDatabase("Articles",Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS savedArticles (id INTEGER PRIMARY KEY, articleID INTEGER, url VARCHAR, title VARCHAR, author VARCHAR, content VARCHAR)");
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM savedArticles",null);
        int numberOfSaved = c.getCount();
        if(numberOfSaved<MainActivity.savedAnnouncements.size()){
            String sql = "INSERT INTO savedArticles (url,title,author) VALUES (?,?,?)";
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
            sqLiteStatement.bindString(1,MainActivity.savedAnnouncementIDandURL.get(MainActivity.savedAnnouncementIDandURL.size()-1));
            sqLiteStatement.bindString(2,MainActivity.savedAnnouncements.get(MainActivity.savedAnnouncements.size()-1).getTitle());
            sqLiteStatement.bindString(3,MainActivity.savedAnnouncements.get(MainActivity.savedAnnouncements.size()-1).getAuthor());
            sqLiteStatement.execute();

        }
        savedFeed.setAdapter(savedAnnouncementAdapter);

        savedFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent nextPage = new Intent(getActivity().getApplicationContext(), WebPageActivity.class);
                nextPage.putExtra("ArticleTitle",MainActivity.savedAnnouncements.get(i).getTitle());
                nextPage.putExtra("ArticleUrl",MainActivity.savedAnnouncementIDandURL.get(i));
                startActivity(nextPage);
            }
        });

        return v;
    }

}
