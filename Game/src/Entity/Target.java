/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import TileMap.TileMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Target extends MapObject{
        protected int health;
	protected int maxHealth;
	protected boolean dead;
	
	
	protected boolean flinching;
	protected long flinchTimer;
	private BufferedImage[] sprites;

	public Target(TileMap tm) {
		super(tm);
                
                width = 60;
                height = 60;
                cwidth = 40;
                cheight = 40;
                
        	

                try {
			
		BufferedImage spritesheet = ImageIO.read(
			getClass().getResourceAsStream(
				"/Player/target1.png"
			)
		);
			
		
		sprites = new BufferedImage[2];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = spritesheet.getSubimage(
				i * width,
				0,
				width,
				height
			);
		}
			
            }
            catch(Exception e) {
            	e.printStackTrace();
            }
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(300);
	}
	
	public boolean isDead() { return dead; }
	
	
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void update() {
	
	// update position
	//getNextPosition();
	//checkTileMapCollision();
	//setPosition(xtemp, ytemp);
	
	// check flinching
	if(flinching) {
		long elapsed =
			(System.nanoTime() - flinchTimer) / 1000000;
		if(elapsed > 400) {
			flinching = false;
		}
	}
	
	// update animation
	animation.update();
		
        }
        
        public void draw(java.awt.Graphics2D g) {
        
        

        setMapPosition();
	//super.draw(g);
	g.drawImage(
			animation.getImage(),
			(int)(x + xmap - width / 2),
			(int)(y + ymap - height / 2 ),
			null
		);
       
		
    }

    
}
