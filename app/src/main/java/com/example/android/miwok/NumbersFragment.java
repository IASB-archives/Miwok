package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }
        }
    };

    //Constructor
    public NumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //gain audio manager service permission from system
        audioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("1 One", "एक", R.raw.number_one, R.drawable.number_one));
        words.add(new Word("2 Two", "२ दोन",  R.raw.number_two, R.drawable.number_two));
        words.add(new Word("3 Three", "३ तीन",  R.raw.number_three, R.drawable.number_three));
        words.add(new Word("4 Four", "४ चार",  R.raw.number_four, R.drawable.number_four));
        words.add(new Word("5 Five", "५ पाच",  R.raw.number_five, R.drawable.number_five));
        words.add(new Word("6 Six", "६ सहा",  R.raw.number_six, R.drawable.number_six));
        words.add(new Word("7 Seven", "७ सात",  R.raw.number_seven, R.drawable.number_seven));
        words.add(new Word("8 Eight", "८ आठ",  R.raw.number_eight, R.drawable.number_eight));
        words.add(new Word("9 Nine", "९ नऊ",  R.raw.number_nine, R.drawable.number_nine));
        words.add(new Word("10 Ten", "१० दहा",  R.raw.number_ten, R.drawable.number_ten));
        //Logging for confirming to get exact output as expected!
        Log.v("NumbersActivity", "Word at index 0: " + words.get(0));
        Log.v("NumbersActivity", "Word at index 1: " + words.get(1));
        Log.v("NumbersActivity", "Word at index 2: " + words.get(2));
        Log.v("NumbersActivity", "Word at index 3: " + words.get(3));

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);
        ListView listView = (ListView) getActivity().findViewById(R.id.numberlist);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Demo", Toast.LENGTH_SHORT).show();
                Word word = words.get(i);
                releaseMediaPlayer();
                int result= audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceID());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        releaseMediaPlayer();
        super.onStop();
    }

    //User created functions
    private void releaseMediaPlayer(){
        if(mediaPlayer!= null){
            mediaPlayer.release();
            mediaPlayer= null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
