package com.thoni.main;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable{

    public static void main(String[] args) {
        Game game = new Game();
        game.start();

    }
    public static JFrame frame;
    private Thread thread;
    private boolean isRunning;
    private final int WIDTH = 160;
    private final int HEIGHT = 120;
    private final int SCALE = 3;

    private BufferedImage image;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public void initFrame(){
        frame = new JFrame("Game Zelda");
        frame.add(this); //adicionar o proprio canvas pro frame conseguir pegar as propriedades da tela
        frame.setResizable(false); // Usuario nao pode mudar o tamanho da janela
        frame.pack();
        frame.setLocationRelativeTo(null); //Janela no centro
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //clicar pra fechar realmente fecha
        frame.setVisible(true); // inicializar visivel
    }

    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop(){
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void tick(){

    }
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0,0,WIDTH, HEIGHT);
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT * SCALE, null);
        bs.show();

    }
    @Override
    public void run() {
        requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer =  System.currentTimeMillis();
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if(delta >= 1){
                tick();
                render();
                frames++;
                delta--;
            }
            if(System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }
}
