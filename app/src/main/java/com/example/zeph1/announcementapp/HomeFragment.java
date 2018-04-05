package com.example.zeph1.announcementapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zeph1 on 3/4/2018.
 */

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment_layout,container,false);


        ListView homeFeed = (ListView) v.findViewById(R.id.lvHomeFeed);


        //Gather Feed data
        sqlDb = getActivity().getBaseContext().openOrCreateDatabase("Articles",MODE_PRIVATE,null);
        sqlDb.execSQL("DELETE FROM articles");
        sqlDb.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, url VARCHAR, title VARCHAR, author VARCHAR, content VARCHAR)");

        MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask();

        try {
            String result = downloadTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
            JSONArray jsonArray = new JSONArray(result);

            for(int i = 0; i < 40 ;i++){
                String articleID = jsonArray.getString(i);

                MainActivity.DownloadTask loadArticle = new MainActivity.DownloadTask();
                String articleLoad = loadArticle.execute("https://hacker-news.firebaseio.com/v0/item/"+articleID+".json?print=pretty").get();
                JSONObject jsonObject = new JSONObject(articleLoad);

                String articleTitle = jsonObject.getString("title");
                String articleURL;
                if (articleID.equals("16745042")){
                    articleURL = " ";
                }else{
                    articleURL = jsonObject.getString("url");
                }
                String articleAuthor = jsonObject.getString("by");

                String sql = "INSERT INTO articles (articleID, url, title, author) VALUES (?,?,?,?)";
                SQLiteStatement sqLiteStatement = sqlDb.compileStatement(sql);
                sqLiteStatement.bindString(1,articleID);
                sqLiteStatement.bindString(2,articleURL);
                sqLiteStatement.bindString(3,articleTitle);
                sqLiteStatement.bindString(4,articleAuthor);
                sqLiteStatement.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Cursor c = sqlDb.rawQuery("SELECT * FROM articles ORDER BY id DESC",null);
        int articleTitleIndex = c.getColumnIndex("title");
        int articleAuthorIndex = c.getColumnIndex("author");
        int articleUrlIndex = c.getColumnIndex("url");

        c.moveToFirst();

        announcementsArrayList.clear();
        articleURLs.clear();

        while(!c.isLast()){
            Announcements tempAnnounce = new Announcements(c.getString(articleTitleIndex),c.getString(articleAuthorIndex));
            announcementsArrayList.add(tempAnnounce);
            articleURLs.add(c.getString(articleUrlIndex));
            c.moveToNext();
        }

        //Toast.makeText(getActivity().getApplicationContext(), announcementsArrayList.size()+ " ", Toast.LENGTH_SHORT).show();
        Announcements tempAnnounce = new Announcements(c.getString(articleTitleIndex),c.getString(articleAuthorIndex));
        announcementsArrayList.add(tempAnnounce);
        articleURLs.add(c.getString(articleUrlIndex));

        announcementAdapter = new AnnouncementAdapter(getActivity().getBaseContext(),announcementsArrayList);
        announcementAdapter.notifyDataSetChanged();
        homeFeed.setAdapter(announcementAdapter);

        homeFeed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity().getApplicationContext(), "Saved announcement: "+announcementsArrayList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                MainActivity.savedAnnouncements.add(announcementsArrayList.get(i));
                MainActivity.savedAnnouncementIDandURL.add(articleURLs.get(i));
                return true;
            }
        });

        homeFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent nextPage = new Intent(getActivity().getApplicationContext(), WebPageActivity.class);
                nextPage.putExtra("ArticleTitle",announcementsArrayList.get(i).getTitle());
                nextPage.putExtra("ArticleUrl",articleURLs.get(i));
                startActivity(nextPage);
            }
        });

        return v;
    }

    ArrayList<Announcements> announcementsArrayList = new ArrayList<>();
    AnnouncementAdapter announcementAdapter;
    SQLiteDatabase sqlDb;
    ArrayList<String> articleURLs = new ArrayList<String>();
    public static AnnouncementAdapter finalAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
