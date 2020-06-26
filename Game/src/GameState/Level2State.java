/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.*;
import Entity.Enemies.Cat;
import java.awt.event.KeyEvent;
import Entity.Player;
import TileMap.*;
import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;


public class Level2State extends GameState {
   
    private TileMap tileMap;
    private Background bg;
    private Player player;
    private ArrayList<Target> targets;
    
    private HUD hud;
    private ArrayList<Enemy> enemies;
    
    public Level2State(GameStateManager gsm)
    {
        this.gsm=gsm;
        init();
    }

    @Override
    public void init() {
        
        tileMap= new TileMap(30);
        tileMap.loadTiles("/Tilesset/ruinstileset.gif");
        tileMap.loadMap("/Maps/level1c.map");
        tileMap.setPosition(0, 0);
        
        bg= new Background("/texture/Gamebg.png",0.1);
        
        player = new Player(tileMap);
       player.setPosition(250, 100);
       hud= new HUD(player);
        // player.draw(g);
       // allocate the enemiyes
       enemies = new ArrayList<Enemy>();
       
       populateEnemies();
      
       
       // allocate the targets
       populateTargets();
       
       
        
    }
    
     public void populateTargets(){
        
        targets= new ArrayList<Target>();
        
        Target t;
        Point [] points = new Point[]{
            new Point(90,360),
            new Point(240,500),
            new Point(390,840),
            new Point(90, 1410),
            new Point(90, 1650),
            new Point(380,1980),
            new Point(90,2550)
        };
        for(int i=0 ; i<points.length;i++){
            t = new Target(tileMap);
            t.setPosition(points[i].x, points[i].y);
            targets.add(t);
            
        }
    }
    
    public void populateEnemies(){
        
        enemies= new ArrayList<Enemy>();
        
        Cat c;
        Point [] points = new Point[]{
            new Point(200,200),
            new Point(300,400),
            new Point(200,600),
            new Point(300,800),
            new Point(200,1000)
        };
        for(int i=0 ; i<points.length;i++){
            c = new Cat(tileMap);
            c.setPosition(points[i].x, points[i].y);
            enemies.add(c);
            
        }
    }

    @Override
    public void update() {
        //set player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2-player.getx(),
                            GamePanel.HEIGHT/4-player.gety());
        // set background
    	bg.setPosition(tileMap.getx(), tileMap.gety());
        //attacking
        player.cheakScore(targets);
        player.checkAttack(enemies);

        if(player.dead) gsm.setState(4);
       
        
        //update enemies
        for(int i =0 ; i<enemies.size();i++){
            enemies.get(i).update();
            if(enemies.get(i).isDead()){
                enemies.remove(i);
                i--;
            }
        }
        
        //update targets
        for(int i=0;i<targets.size();i++){
           targets.get(i).update();
            if(targets.get(i).isDead()){
                targets.remove(i);
                i--;
            }
       }
       if(player.score==125) {
           gsm.setState(3);
           
       }
       
    }

    @Override
    public void draw(Graphics2D g) {
        
        //clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH,GamePanel.HEIGHT);
       
        //draw bg
        bg.draw(g);
        // draw tilemap
        
        tileMap.draw(g);
         
        //draw player
        player.draw(g);
        
        //draw enemies
        for(int i =0 ; i<enemies.size();i++){
            enemies.get(i).draw(g);
        }
        
       //draw hud
       hud.draw(g);
       
       //draw target
       for(int i=0;i<targets.size();i++){
           targets.get(i).draw(g);
           
       }
    }

    @Override
    public void keyPressed(int k) {
        if(k== KeyEvent.VK_LEFT) player.setLeft(true);
        if(k== KeyEvent.VK_RIGHT) player.setRight(true);
        if(k== KeyEvent.VK_UP) player.setUp(true);
        if(k== KeyEvent.VK_DOWN) player.setDown(true);
        if(k== KeyEvent.VK_CONTROL) player.setJumping(true);
        if(k== KeyEvent.VK_SHIFT) player.setGliding(true);
        if(k== KeyEvent.VK_SPACE )player.setFiring(true);
    }

    @Override
    public void keyReleased(int k) {
        if(k== KeyEvent.VK_LEFT) player.setLeft(false);
        if(k== KeyEvent.VK_RIGHT) player.setRight(false);
        if(k== KeyEvent.VK_UP) player.setUp(false);
        if(k== KeyEvent.VK_DOWN) player.setDown(false);
        if(k== KeyEvent.VK_CONTROL) player.setJumping(false);
        if(k== KeyEvent.VK_SHIFT) player.setGliding(false);
        if(k== KeyEvent.VK_SPACE )player.setFiring(false);
    }
    
    
}
