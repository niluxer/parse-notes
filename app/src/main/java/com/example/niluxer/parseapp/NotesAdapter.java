package com.example.niluxer.parseapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by niluxer on 5/5/17.
 */

public class NotesAdapter extends ArrayAdapter<ParseObject> {

    Context context;
    List<ParseObject> listNotes = new ArrayList<ParseObject>();
    NotesAdapter adapter;

    public NotesAdapter(@NonNull Context context, List<ParseObject> objects) {
        super(context, 0, objects);
        this.context = context;
        this.listNotes = objects;
        this.adapter = this;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if( convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.note_row, parent, false);
        }

        final ParseObject object = listNotes.get(position);

        TextView tvNote = (TextView) convertView.findViewById(R.id.tvNote);
        TextView tvNoteCreatedAt = (TextView) convertView.findViewById(R.id.tvNoteCreatedAt);
        ImageButton imgNoteDelete = (ImageButton) convertView.findViewById(R.id.imgNoteDelete);
        imgNoteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Object -> " + object.getObjectId() + " Note-> " + object.getString("note"), Toast.LENGTH_SHORT).show();
                deleteNote(object);
            }
        });

        tvNote.setText(object.getString("note"));
        Date createdat = object.getCreatedAt();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        tvNoteCreatedAt.setText(formatter.format(createdat));

        return convertView;

    }

    private void deleteNote(ParseObject object)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notes");
        query.getInBackground(object.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    object.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {

                            //Intent intentHome = new Intent(context, HomeActivity.class);
                            //context.startActivity(intentHome);
                            refresh();
                        }
                    });
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    public void refresh()
    {
        listNotes.clear();
        loadNotes();
    }

    private void loadNotes()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notes");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    for (int i=0; i<objects.size();i++) {
                        ParseObject p = objects.get(i);
                        //listNotes.add(p.getString("note"));
                        listNotes.add(p);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Error reading notes...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

}
