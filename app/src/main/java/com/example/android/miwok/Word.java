package com.example.android.miwok;

/**
 * Created by Suraj on 10/28/2016.
 */

public class Word {
    private static final int NO_IMAGE_PROVIDED = -1;

    private String defaultTranslation;
    private String miwokTranslation;
    private int imageResourceID= NO_IMAGE_PROVIDED;
    private int audioResourceID;

    public Word(String miwokTranslation, String defaultTranslation, int audioResourceID) {
        this.miwokTranslation = miwokTranslation;
        this.defaultTranslation = defaultTranslation;
        this.audioResourceID= audioResourceID;
    }
    public Word(String miwokTranslation, String defaultTranslation,int audioResourceID, int imageResourceID) {
        this.miwokTranslation = miwokTranslation;
        this.defaultTranslation = defaultTranslation;
        this.audioResourceID= audioResourceID;
        this.imageResourceID= imageResourceID;
    }

    public String getDefaultTranslation() {
        return defaultTranslation;
    }

    public String getMiwokTranslation() {
        return miwokTranslation;
    }

    public int getAudioResourceID() {
        return audioResourceID;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public boolean isImageProvided(){
        return imageResourceID != NO_IMAGE_PROVIDED;
    }

}
