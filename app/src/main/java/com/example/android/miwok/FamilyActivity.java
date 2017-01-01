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

public class FamilyActivity extends AppCompatActivity {
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
        words.add(new Word("1 One", "एक", R.raw.family_father, R.drawable.family_father));
        words.add(new Word("2 Two", "२ दोन", R.raw.family_mother, R.drawable.family_mother));
        words.add(new Word("3 Three", "३ तीन", R.raw.family_son, R.drawable.family_son));
        words.add(new Word("4 Four", "४ चार", R.raw.family_daughter, R.drawable.family_daughter));
        words.add(new Word("5 Five", "५ पाच", R.raw.family_older_brother, R.drawable.family_older_brother));
        words.add(new Word("6 Six", "६ सहा", R.raw.family_younger_brother, R.drawable.family_younger_brother));
        words.add(new Word("7 Seven", "७ सात", R.raw.family_older_sister, R.drawable.family_older_sister));
        words.add(new Word("8 Eight", "८ आठ", R.raw.family_younger_sister, R.drawable.family_younger_sister));
        words.add(new Word("9 Nine", "९ नऊ", R.raw.family_grandfather, R.drawable.family_grandfather));
        words.add(new Word("10 Ten", "१० दहा", R.raw.family_grandmother, R.drawable.family_grandmother));

        WordAdapter wordAdapter = new WordAdapter(this, words, R.color.category_family);
        ListView listView = (ListView)findViewById(R.id.numberlist);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(FamilyActivity.this, "Demo", Toast.LENGTH_SHORT).show();
                Word word = words.get(i);
                releaseMediaPlayer();
                int result= audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceID());
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
