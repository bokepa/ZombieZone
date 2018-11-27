package com.inopia.zombiezone;
import java.util.ArrayList;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
 
public class Sprite {
       // direction = 0 up, 1 left, 2 down, 3 right,
       // animation = 3 back, 1 left, 0 front, 2 right
       int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
       private static final int BMP_ROWS = 4;
       private static final int BMP_COLUMNS = 3;
       private static final int MAX_SPEED = 5;
       //private GameView gameView;
       private Bitmap bmp;
       private int x = 0;
       private int y = 0;
       private int xSpeed;
       private int ySpeed;
       private int currentFrame = 0;
       private int width;
       private int height;
       private Rect zonaDesp;
       private boolean selected;
       private int type;
 
       public Sprite(GameView gameView, Bitmap bmp, int type) {
             this.width = bmp.getWidth() / BMP_COLUMNS;
             this.height = bmp.getHeight() / BMP_ROWS;
             //this.gameView = gameView;
             this.bmp = bmp;
 
             Random rnd = new Random();
             x = rnd.nextInt(gameView.getWidth() - width);
             y = rnd.nextInt(gameView.getHeight() - height);
             xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
             ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
             zonaDesp = new Rect(0,0, gameView.getWidth(), gameView.getHeight());
             this.selected = false;
             this.type = type;
       }
 
       public void update() {
    	   	
    	     if (x >= zonaDesp.right || x <= zonaDesp.left -20) {
    	    	 xSpeed = -xSpeed;
    	     }
    	     x = x + xSpeed;
    	     
    	     if (y >= zonaDesp.bottom -20|| y <= zonaDesp.top ) {
    	    	 ySpeed = -ySpeed;
    	     }
    	     y = y + ySpeed;
    	    		 /*
             if (x >= zonaDesp.right - width - xSpeed || x + xSpeed <= 0) {
                    xSpeed = -xSpeed;
             }
             x = x + xSpeed;
             if (y >= zonaDesp.bottom - height - ySpeed || y + ySpeed <= 0) {
                    ySpeed = -ySpeed;
             }
             y = y + ySpeed;*/
             
             currentFrame = ++currentFrame % BMP_COLUMNS;
       }
 
       public void onDraw(Canvas canvas) {
             
             int srcX = currentFrame * width;
             int srcY = getAnimationRow() * height;
             Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
             Rect dst = new Rect(x, y, x + width, y + height);
             canvas.drawBitmap(bmp, src, dst, null);
       }
 
       private int getAnimationRow() {
             double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
             int direction = (int) Math.round(dirDouble) % BMP_ROWS;
             return DIRECTION_TO_ANIMATION_MAP[direction];
       }
 
       public boolean isCollision(float x2, float y2) {
             return x2 > x && x2 < x + width && y2 > y && y2 < y + height; 
       }
       public void stop() {
    	   ySpeed = 0;
    	   xSpeed = 0;
       }
       public void walk() {
    	   Random rnd = new Random(); 
    	   xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
           ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
       }
       public void setSpeedY(int y) {
    	   ySpeed =y;
    	   xSpeed = y;
       }
       public void setX(int x) {
    	   this.x = x;
    	   
       }
       public void setY(int y) {
    	   this.y = y;
       }
       
       public void setSelected(boolean s){
    	   this.selected = s;
       }
       public boolean isSelected() {
    	   return this.selected;
       }
       
       public boolean isZoneCollission(Zone z) {
    	   Rect r = z.getRect();
    	   if (this.x < r.right && this.x > r.left -20 && this.y > r.top && this.y < r.bottom - 20)
    		   return true;
    	   else
    		   return false;
       }
       public void updateZone(Zone z) {
    	   this.zonaDesp.set(z.getRect());
       }
       
       public int getType() {
    	   return this.type;
       }
       
       
}
