package com.demem.barcodescanner;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.demem.barcodescanner.R;

public class SoundPlayer {

    public static final int ACCEPT = 1;
    public static final int DECLINE = 2;

    private SoundPool soundPool = new SoundPool(2, AudioManager.STREAM_ALARM, 100);
    private HashMap<Integer, Integer> soundsMap = null;
    
    private static SoundPlayer instance = null;

    public static SoundPlayer getInstance() {
    	if(instance == null) {
    		instance = new SoundPlayer();
    	}
    	return instance;
    }

    private SoundPlayer(){}

    public void loadSounds(Context context) {
        soundsMap = new HashMap<Integer, Integer>();
        soundsMap.put(ACCEPT, soundPool.load(context, R.raw.beeps, 1));
        soundsMap.put(DECLINE, soundPool.load(context, R.raw.basso, 1));
    }

    public void playSound(int soundID, float volume) {
        soundPool.play((Integer)soundsMap.get(soundID), volume, volume, 1, 0, 1f);
    }
}
