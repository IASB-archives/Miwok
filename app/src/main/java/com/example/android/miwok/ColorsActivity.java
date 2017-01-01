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

public class ColorsActivity extends AppCompatActivity {
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
        words.add(new Word("1 One", "एक", R.raw.color_red, R.drawable.color_red));
        words.add(new Word("2 Two", "२ दोन", R.raw.color_mustard_yellow, R.drawable.color_mustard_yellow));
        words.add(new Word("3 Three", "३ तीन", R.raw.color_dusty_yellow, R.drawable.color_dusty_yellow));
        words.add(new Word("4 Four", "४ चार", R.raw.color_gray, R.drawable.color_gray));
        words.add(new Word("5 Five", "५ पाच", R.raw.color_green, R.drawable.color_green));
        words.add(new Word("6 Six", "६ सहा", R.raw.color_brown, R.drawable.color_brown));
        words.add(new Word("7 Seven", "७ सात", R.raw.color_black, R.drawable.color_black));
        words.add(new Word("8 Eight", "८ आठ", R.raw.color_white, R.drawable.color_white));

        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_colors);
        ListView listView = (ListView)findViewById(R.id.numberlist);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ColorsActivity.this, "Demo", Toast.LENGTH_SHORT).show();
                Word word = words.get(i);
                releaseMediaPlayer();
                int result= audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioResourceID());
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
