/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;


public class Player extends MapObject{
    
    //Player
    private int health, maxHealth, fire, maxFire;
    public boolean dead, flinching;
    private long flinchTimer;
    public int score;
    
    //fireball
    private boolean firing;
    private int fireCost, fireBallDamage;
    private BufferedImage image;
    private ArrayList<Egg> eggs;
    
    //scratching 
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;
    
    //gliding
    private boolean gliding;
    
    //animation
    private ArrayList<BufferedImage[]> sprites;
    private final int [] numFrames=
            {
                1,4,5,5,5
                
            };
    
    //animation actions
    private static final int IDLE =0;
    private static final int WALKING =1;
    private static final int JUMPING =2;
    private static final int FALLING =3;
    private static final int FIREBALL =4;
    private static final int SCRATCHING =5;
    
    
    
    public Player(TileMap tm) {
        super(tm);
        health=maxHealth=5;
        width=30;
        height=30;
        cwidth=45;
        cheight=45;

        moveSpeed=0.3;
        maxSpeed=1.6;
        stopSpeed=0.4;

        minFallSpeed=0.20;
        fallSpeed=0.15;
        maxFallSpeed= 0.60;
        jumpStart= -4.8;
        stopJumpSpeed= 0.3;
        
        facingRight=true;
        
        health = maxHealth=2;
        fire= maxFire= 50000;
        
        
        fireCost=200;
        fireBallDamage=5;
        eggs = new ArrayList<Egg>();
        
        scratchDamage=5;
        scratchRange=40;
        
        //load animation
        
        try{
           BufferedImage spritesheet= ImageIO.read(
                   getClass().getResource("/Player/player.png"));
           sprites = new ArrayList<BufferedImage[]>();
           for(int i=0; i<5;i++)
           {
               BufferedImage[] bi= new BufferedImage[numFrames[i]];
               for(int j=0;j<numFrames[i];j++){
              
                   bi[j]=spritesheet.getSubimage(j*50 
                           , i*50, 50, 50);
                
            }
           sprites.add(bi);
           }
         
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        animation= new Animation();
        currentAction =IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
        
        
    }
    
    public int getHealth() {return health;}
    public int getMaxHealth() {return maxHealth;}
    public int getFire() {return fire;}
    public int getMaxFire() {return maxFire;}

    public void setFiring(boolean b){
        firing=b;
    }
    
    public void setScratching(){
        scratching=true;
    }
    
    public void setGliding(boolean b){
        gliding=b;
    }
    
    private void getNextPosition(){
        
    // movement
    if(left) {
	dx = dx-moveSpeed;
	if(dx < -maxSpeed ) {
            dx = -1*(maxSpeed);
       
	}
    }
    else if(right) {
	dx += moveSpeed;
	if(dx > maxSpeed) {
            dx = maxSpeed;
	}
    }
    else {
	if(dx > 0) {
            dx -= stopSpeed;
            if(dx < 0) {
                dx = 0;
            }
	}
        else if(dx < 0) {
            dx += stopSpeed;
            if(dx > 0) {
            dx = 0;
            }
	}
    }
    
    
	
        //jumping
        if(jumping && !falling){
            dy=jumpStart;
            falling = true;
        }
        
	// falling
	if(falling) {
			
            if(dy > 0 && gliding) dy += fallSpeed * 0.01;
            else dy += fallSpeed;
			
            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;
			
            if(dy > maxFallSpeed) dy = maxFallSpeed;
            if(up){
                dy-=moveSpeed;
                if(dy<=moveSpeed){
                    dy=minFallSpeed;
                }
                
            }
            
	}

    }
   
    public void checkAttack(ArrayList<Enemy> enemies) {
		
	// loop through enemies
	for(int i = 0; i < enemies.size(); i++) {
		
		Enemy e = enemies.get(i);
	
		// fireballs
		for(int j = 0; j < eggs.size(); j++) {
			if(eggs.get(j).intersects(e)) {
				e.hit(fireBallDamage);
				eggs.get(j).setHit();
				break;
			}
		}
			
		//check cat coolision
                if(intersects(e)){
                    hit(e.damage);
                }
	}
		
    }
    
    public void hit( int damage){
        if(flinching)return;
        health-= damage;
        if(health<0) health=0;
        if(health==0) dead=true;
        flinching =true;
        flinchTimer = System.nanoTime();
    }
    
    public void cheakScore(ArrayList<Target> targets) {
		
	// loop through enemies
	for(int i = 0; i < targets.size(); i++) {
		
		Target e = targets.get(i);
	
		// fireballs
		for(int j = 0; j < eggs.size(); j++) {
			if(eggs.get(j).intersects(e)) {
				e.hit(fireBallDamage);
				eggs.get(j).setHit();
				score+=25;
                                break;
			}
		}
			
		
		
	}
		
    }
	
    public void update(){
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp,ytemp);
        
        //eggs 
        fire +=1;
        if(fire>maxFire) fire = maxFire;
        if(firing && currentAction!= FIREBALL){
            if(fire> fireCost){
                fire -=fireCost;
                Egg egg= new Egg(tileMap, facingRight);
                egg.setPosition(x, y);
                eggs.add(egg);
                
            }
        }
        // update eggs
        for(int i=0;i<eggs.size();i++){
            eggs.get(i).update();
            if(eggs.get(i).shouldRemove()){
                eggs.remove(i);
                i--;
            }
        }
        
        //cheak done flinching
        if(flinching){
            
            long elapsed = (System.nanoTime()-flinchTimer)/ 1000000;
            
            if(elapsed>1000) flinching = false;
        }
        //set Animtion
        if(scratching){
            
            if(currentAction!=SCRATCHING){
                currentAction =SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width=60;
            }
        }
        else if(firing){
            if(currentAction!= FIREBALL){
                currentAction= FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width=45;
                height=45;
                
            }
         
        }
        else if(dy>0){
            if(falling){
                if(currentAction != FALLING){
                    currentAction=FALLING;
                    animation.setFrames(sprites.get(FALLING));
                    animation.setDelay(100);
                    width=45;
                    height=45;
                }
            }
        }
        else if(dy<0){
            if(currentAction!= JUMPING){
                currentAction=JUMPING;
                animation.setFrames(sprites.get(JUMPING) );
                animation.setDelay(100);
                width=45;
                height=45;
            }
        }
        else if(left || right){
            if(currentAction != WALKING){
                currentAction= WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width=45;
                height=45;
            }
            
        }
        else{
            if(currentAction!=IDLE){
                animation.setFrames(sprites.get(IDLE));
                currentAction=IDLE;
                animation.setDelay(400);
                width=45;
                height=45;
            }
            
        }
        
        animation.update();
        
        //set direction
        
        if(currentAction != SCRATCHING && currentAction !=FIREBALL){
            if(right) facingRight=true;
            if(left) facingRight=false;
        }
    }
    
    @Override
    public void draw(Graphics2D g){
        
        setMapPosition();
        //draw Eggs
        for(int i=0; i<eggs.size();i++){
            eggs.get(i).draw(g);
        }
        
        
        //draw player
        if(flinching){
            
            long elapsed = (System.nanoTime()-flinchTimer)/100000;
            if(elapsed/100%2 == 0){
                return;
            }
            
            
        }
        if(facingRight){
            g.drawImage(animation.getImage(), (int)(x+xmap - width/2)
                    , (int) (y+ymap - height/2),width, height, null);
        }
        else {
            g.drawImage(animation.getImage(),
                   (int)(x+xmap - width/2 +width )
                    , (int) (y+ymap - height/2), -width , height, null
            );
        }
    }
}
