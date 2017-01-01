package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager= (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "एक", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "२ दोन", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is _ _ _", "३ तीन", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "४ चार", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good!", "५ पाच", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "६ सहा", R.raw.phrase_are_you_coming));
        words.add(new Word("Let's go", "७ सात", R.raw.phrase_lets_go));

        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_phrases);
        ListView listView = (ListView)findViewById(R.id.numberlist);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(PhrasesActivity.this, "Demo", Toast.LENGTH_SHORT).show();
                Word word = words.get(i);
                releaseMediaPlayer();
                int result= audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceID());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        releaseMediaPlayer();
        super.onBackPressed();
        Log.v("PhrasesActivity","onBackPressed: Back Pressed");
    }

    @Override
    protected void onPause() {
        releaseMediaPlayer();
        super.onPause();
        Log.v("PhrasesActivity", "onPause: Paused");
    }

    @Override
    protected void onStop() {
        releaseMediaPlayer();
        super.onStop();
        Log.v("PhrasesActivity", "onPause: Stopped");
    }

    private void releaseMediaPlayer(){
        if(mediaPlayer!= null){
            mediaPlayer.release();
            mediaPlayer= null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
