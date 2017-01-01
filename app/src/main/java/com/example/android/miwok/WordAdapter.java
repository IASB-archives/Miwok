package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.miwok.R.id.image;

/**
 * Created by Suraj on 10/28/2016.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int colorResourceId;
    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId){
        super(context, 0, words);
        this.colorResourceId= colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        if(listViewItem == null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Word currentWord= getItem(position);
        //set default number
        TextView defaultName = (TextView) listViewItem.findViewById(R.id.default_number);
        defaultName.setText(currentWord.getDefaultTranslation());
        //set Marathi number
        TextView marathiName = (TextView) listViewItem.findViewById(R.id.marathi_number);
        marathiName.setText(currentWord.getMiwokTranslation());
        //set image of context name
        ImageView imageID = (ImageView)listViewItem.findViewById(image);
        if(currentWord.isImageProvided()){
            imageID.setImageResource(currentWord.getImageResourceID());
            imageID.setVisibility(View.VISIBLE);
        }else{
            imageID.setVisibility(View.GONE);
        }

        View textContainer = listViewItem.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), colorResourceId);
        textContainer.setBackgroundColor(color);
        return listViewItem;
    }
}
