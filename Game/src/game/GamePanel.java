
package game;

import GameState.GameStateManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable, KeyListener{
    
    //dimention

    public static final int WIDTH =460 ;
    public static final int HEIGHT=640 , Scale=1;
    
    //Game Thread
    private Thread thread;
    private boolean running;
    private int FPS=60;
    private long targetTime =1000/FPS;
    
    //image
    private BufferedImage image;
    private Graphics2D g;
    private GameStateManager gsm;
    
    //methods
    public GamePanel()
    {
       super();
       setPreferredSize(new Dimension(WIDTH*Scale,HEIGHT*Scale));
       setFocusable(true);
       requestFocus();
    }
    
    public void addNotify()
    {
        super.addNotify();
        if(thread== null)
        {
            thread= new Thread(this);
            addKeyListener(this);
            thread.start();
            
        }
    }
    
    private void init()
    {
        image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
         g= (Graphics2D) image.getGraphics();
       
        running= true;
        
        gsm= new GameStateManager();
    }
    
    @Override
    public void run() {
        
        init();
        
        long start;
        long elapsed;
        long wait = 0;
        
        // game loop
        
        while(running)
        {
            start= System.nanoTime();
            
            upDate();
            draw();
            drawToScreen();
            
            elapsed= System.nanoTime()-start;
            
            wait = targetTime-elapsed/1000000;
        }
        try{
                Thread.sleep(wait);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
    }
    
    private void upDate()
    {
        gsm.update();
    }
    
    private void draw()
    {
        gsm.draw(g);
    }
    
    private void drawToScreen()
    {
    
         Graphics g2= getGraphics();
         g2.drawImage(image, 0, 0,WIDTH*Scale,HEIGHT*Scale, this);
         g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        gsm.keyPressed(ke.getKeyCode());
       
    }

    @Override
    public void keyReleased(KeyEvent ke) {
         gsm.keyReleased(ke.getKeyCode());
    }
    
}
