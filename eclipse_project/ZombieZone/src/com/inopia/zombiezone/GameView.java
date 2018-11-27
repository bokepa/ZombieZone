package com.inopia.zombiezone;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inopia.zombiezone.sound.AudioClip;
import com.inopia.zombiezone.sound.Tools;
 
public class GameView extends SurfaceView {
       private GameLoopThread gameLoopThread;
       private List<Sprite> sprites = new ArrayList<Sprite>();
       private List<TempSprite> temps = new ArrayList<TempSprite>();
       //private long lastClick;
       private Bitmap bmpBlood;
       private List<Zone> zones = new ArrayList<Zone>();
       private Zone mainZone;
       private Context thecontext ;
 
       AudioClip blast, crash, kill;


       public GameView(Context context) {
             super(context);
             thecontext = context;
             gameLoopThread = new GameLoopThread(this);
             getHolder().addCallback(new SurfaceHolder.Callback() {
 
                    
                    public void surfaceDestroyed(SurfaceHolder holder) {
                           boolean retry = true;
                           gameLoopThread.setRunning(false);
                           while (retry) {
                                  try {
                                        gameLoopThread.join();
                                        retry = false;
                                  } catch (InterruptedException e) {}
                           }
                    }
 
                    
                    public void surfaceCreated(SurfaceHolder holder) {
                    	initAudio();   
                    	createSprites();
                           createZones();
                           
                           gameLoopThread.setRunning(true);
                           gameLoopThread.start();
                    }
 
                    
                    public void surfaceChanged(SurfaceHolder holder, int format,
                                  int width, int height) {
                    }
             });
             bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);
       }
 
       private void createZones() {
    	   mainZone = new Zone(0,0, this.getWidth(), this.getHeight(), Color.BLACK, 0);
    	   Zone z = new Zone(0, 0, 100, 100, Color.RED, 1);
    	   Zone bz = new Zone(220, 140, 320,240, Color.GREEN, 2);
    	   zones.add(z);
    	   zones.add(bz);
    	   
       }
       private void createSprites() {
             sprites.add(createSprite(R.drawable.bad1, 1));
             sprites.add(createSprite(R.drawable.bad2, 2));
             sprites.add(createSprite(R.drawable.bad1, 1));
             sprites.add(createSprite(R.drawable.bad2, 2));
             sprites.add(createSprite(R.drawable.bad1, 1));
             sprites.add(createSprite(R.drawable.bad2, 2));
             sprites.add(createSprite(R.drawable.bad1, 1));
             sprites.add(createSprite(R.drawable.bad2, 2));
            /*
             sprites.add(createSprite(R.drawable.bad5));
             sprites.add(createSprite(R.drawable.bad6));
             sprites.add(createSprite(R.drawable.good1));
             sprites.add(createSprite(R.drawable.good2));
             sprites.add(createSprite(R.drawable.good3));
             sprites.add(createSprite(R.drawable.good4));
             sprites.add(createSprite(R.drawable.good5));
             sprites.add(createSprite(R.drawable.good6));
             */
             
       }
 
       private Sprite createSprite(int resouce, int type) {
             Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
             return new Sprite(this, bmp, type);
       }
 
       @Override
       protected void onDraw(Canvas canvas) {
    	   
             // Game AI
             updateAll();
             // Draw
	    	   canvas.drawColor(Color.BLACK);
	        // Pinta zonas
	    	   for (Zone zone: zones) {
	    		   zone.onDraw(canvas);
	    	   }
	           
             for (int i = temps.size() - 1; i >= 0; i--) {
                    temps.get(i).onDraw(canvas);
             }
             for (Sprite sprite : sprites) {
            	 	
                    sprite.onDraw(canvas);
             }
       }
  
       public void updateAll() {

    	   // Movemos persons
    	   for (Sprite sprite : sprites) {
               sprite.update();
               
    	   }
    	   
    	   
       }
       @Override
       public boolean onTouchEvent(MotionEvent event) {
    	   int x = (int)event.getX();
    	   int y =  (int)event.getY();
    	   if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		   synchronized (getHolder()) {
    			   for (int i = sprites.size() - 1; i >= 0; i--) {
    				   Sprite sprite = sprites.get(i);
    				   if (sprite.isCollision(x, y)) {
    					   sprite.stop();
    					   sprite.setSelected(true);
    				   }
    			   }
    		   }
    	   }
    	   if (event.getAction() == MotionEvent.ACTION_UP) {
    		   //synchronized (getHolder()) {
    			   for (int i = sprites.size() - 1; i >= 0; i--) {
    				   Sprite sprite = sprites.get(i);
    				   if (sprite.isSelected()) {
    					   //sprite.walk();
    					   sprite.setSelected(false);
    					   sprite.updateZone(mainZone);
    					   boolean hasZone = false;
    					   for (Zone zone: zones) {
    			    		   if (sprite.isZoneCollission(zone)) {
    			    			   if (sprite.getType() == zone.getType()) {
    			    				   sprite.updateZone(zone);
        			    			   hasZone = true;   
    			    			   } else
    			    			   {
    			    				   sprites.remove(sprite);
    			    				   crash.play();
                                       temps.add(new TempSprite(temps, this, x, y, bmpBlood));
    			    				//sprite.setX(200);
    			    				//sprite.setY(200);
    			    			   }
    			    			   
    			    		   } 
    			    	   }
    					   if (!hasZone) {
    					   	sprite.updateZone(mainZone);
    					   	sprite.walk();
    					   }
    				   }

    			   }
    	   }
    	   if (event.getAction() == MotionEvent.ACTION_MOVE) {
    		   //synchronized (getHolder()) {
    		   for (int i = sprites.size() - 1; i >= 0; i--) {
    			   Sprite sprite = sprites.get(i);
    			   if (sprite.isSelected()) {
    				   sprite.setX(x);
    				   sprite.setY(y); 
    			   }
    		   }
    	   }

    	   //}
    	   /*if (System.currentTimeMillis() - lastClick > 300) {
                    lastClick = System.currentTimeMillis();
                    float x = event.getX();
                    float y = event.getY();
                    synchronized (getHolder()) {
                           for (int i = sprites.size() - 1; i >= 0; i--) {
                                  Sprite sprite = sprites.get(i);
                                  if (sprite.isCollision(x, y)) {
                                        sprites.remove(sprite);
                                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                                        break;
                                  }
                           }
                    }
             }*/
    	   return true;
       }
       
       protected AudioClip getAudioClip(int id){
   		return new AudioClip(this.getContext(), id);
   	}

       private void initAudio() {
   		try{
   			//blast 	= getAudioClip(R.raw.sb_blast);
   			crash 	= getAudioClip(R.raw.sb_collisn);
   			//kill	= getAudioClip(R.raw.sb_mdestr);
   		}catch (Exception e){
   			Tools.MessageBox(thecontext, "Audio Error: "+e.toString());
   			
   		}
   	}


       
}
