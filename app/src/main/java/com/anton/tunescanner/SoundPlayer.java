package com.anton.tunescanner;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

class SoundPlayer {

    public SoundPlayer(File soundFile) throws IllegalArgumentException {
        if(!checkFile(soundFile))
            throw new IllegalArgumentException("wrong file type: must be 3gp, wav, mp3, or other sound file");
        this.soundFile = soundFile;
        player = new MediaPlayer();
    }

    private boolean checkFile(File file) {
        boolean isValidSoundFile = false;

        String filename = file.getName();
        String[] filenameTokens = filename.split("\\.");

        for(String token : filenameTokens) {
            if(token.matches("3gp|mp3|wav")) {
                isValidSoundFile = true;
                break;
            }
        }
        return isValidSoundFile;
    }

    public void startPlayback() throws IOException {
        if (player == null) {
            player = new MediaPlayer();
        }
            player.setDataSource(soundFile.getAbsolutePath());
            player.prepare();
            player.start();

    }

    public void stopPlayback() {
        if (player!=null){
            player.stop();
            player.release();
            player=null;
        }
    }

    private MediaPlayer player;
    private File soundFile;
}
