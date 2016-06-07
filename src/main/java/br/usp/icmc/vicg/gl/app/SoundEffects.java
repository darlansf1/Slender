package br.usp.icmc.vicg.gl.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.player.Player;
/**
 *
 * @author Darlan
 */
public class SoundEffects {
    private float deltax = 0;
    private float deltaz = 0;
    private boolean gameOver = false;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public void setDeltax(float deltax) {
        this.deltax = deltax;
    }

    public void setDeltaz(float deltaz) {
        this.deltaz = deltaz;
    }
    
   public void playClip(final String location){
      Thread t = new Thread(new Runnable() {
          @Override
          public void run() {
            
            //System.out.println(location);
          }
      });
    t.start();
  }
   
  public void playSoundEffects(){
    final Sound breathing = new Sound("./sounds/breathing.mp3");
    final Sound step = new Sound("./sounds/running.mp3");
            
    new Thread(new Runnable() {

        @Override
        public void run() {
            
            while(true){
                if(gameOver){
                    step.pauseSound();
                    breathing.pauseSound();
                }
                if(Math.abs(deltax) < 0.000001 && Math.abs(deltaz) < 0.000001){
                    step.pauseSound();
                    breathing.playSound();
                }else{
                    step.playSound();
                    breathing.playSound();
                }
            }
        }
    }).start();
  }
  
  public void playGameOverAudio(boolean hasWon){
      String file = "./sounds/catra.mp3";
      if(hasWon)
        file = "./sounds/ines.mp3";
      final Sound sound = new Sound(file);
            
        new Thread(new Runnable() {
            @Override
            public void run() {
                sound.playSound();
            }
        }).start();
  }
  
  private class Sound{
        private boolean running = false;
        private String location;
        private Player player = null;
        
        public Sound(String location) {
            this.location = location;
        }
        
        public void pauseSound() {
            if(isRunning()){
                System.out.println("pause");
                player.close();
            }
            
            running = false;
        }
        
        public synchronized boolean isRunning(){
            return running;
        }
        
        public void playSound(){
            if(!isRunning()){
                System.out.println("play");
                running = true;
                try {
                    File mp3File = new File(location);
                    FileInputStream fis = new FileInputStream(mp3File);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    player = new Player(bis);
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                player.play();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }
                            pauseSound();
                        }
                    });
                    t.start();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
  }
}