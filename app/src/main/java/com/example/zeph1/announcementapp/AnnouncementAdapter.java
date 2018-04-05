package com.example.zeph1.announcementapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zeph1 on 4/4/2018.
 */

public class AnnouncementAdapter extends ArrayAdapter<Announcements> {

    private Context mContext;
    private ArrayList<Announcements> announcementsArrayList = new ArrayList<>();

    public AnnouncementAdapter(@NonNull Context context, ArrayList<Announcements> announcements) {
        super(context, R.layout.home_frag_item_layout,announcements);
        mContext = context;
        announcementsArrayList = announcements;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem = convertView;

        listitem = LayoutInflater.from(mContext).inflate(R.layout.home_frag_item_layout,parent,false);


        Announcements currentAnnouncement = announcementsArrayList.get(position);

        TextView title = (TextView) listitem.findViewById(R.id.tvTitle);
        title.setText(currentAnnouncement.getTitle());

        TextView author = (TextView) listitem.findViewById(R.id.tvAuthor);
        String authorDisplayText = "By: "+currentAnnouncement.getAuthor();
        author.setText(authorDisplayText);

        return listitem;
    }
}
