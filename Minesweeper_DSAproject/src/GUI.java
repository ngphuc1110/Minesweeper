/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author A.TUAN
 */
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
public class GUI extends JFrame{
    
    public boolean resetter = false;
    
    public boolean flagger = false;
    
    Date startDate = new Date();
    Date endDate;
    
    int spacing = 1;
    
    int neighs = 0;
    
    public int mx = -100;
    public int my = -100;
    
    String vicMes = " ";
    
    public int sec = 0;
    
    public boolean happiness = true;
    
    public boolean victory = false;
    
    public boolean defeat = false;
    
    public boolean undo = false;
   
    public int smileyx = 305;
    public int smileyy = 3;
    
    public int mesX = 380;
    public int mesY = -200;
   
    public int flaggerX = 205;
    public int flaggerY = 3;
    
    public int flaggerCenterX = flaggerX + 15;
    public int flaggerCenterY = flaggerY + 15;
    
    public int undoX = 80;
    public int undoY = 3;
    
    public int undoCenterX = undoX+ 15;
    public int undoCenterY = undoY+ 15;
        
    public int smileyCenterX= smileyx+ 15;
    public int smileyCenterY = smileyy+ 15;
    
    public int timex = 570;
    public int timey = 3;
        
    Random rand = new Random();
    // 0 and 1 blocks have mine will assign 1, and otherwise
    int[][] mines = new int[16][16];
    // To store all of minein 8- adjacent blocks
    int[][] neighbours = new int[16][16];
    // To identify when the blocks are reached by the player or system 
    boolean[][] revealed = new boolean[16][16];
    boolean[][] flagged = new boolean[16][16];
    // when the player loses, they cannot click the other blocks
    boolean[][] stopClick = new boolean[16][16];
    // To identify the blocks when is opened by empty blocks.
    boolean[][] closeEmptyReveal = new boolean[16][16];
    // To identify the blocks which neighbour isn't equal zero is opened
    boolean[][] closeReveal = new boolean[16][16];
    
    Stack<Integer> stackX = new Stack<>();
    Stack<Integer> stackY = new Stack<>();
    
    public random randoMine; 
    public GUI(){
        this.setTitle("Minesweeper");
        this.setSize(658,730);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        
        randoMine  = new random();
        randoMine.mineRandom();
        
        Board board = new Board();
        this.setContentPane(board);
        
        Move move = new Move();
        this.addMouseMotionListener(move);
        
        Click click = new Click();
        this.addMouseListener(click);
        
    }
    public class Board extends JPanel{
        Open open = new Open();
        close close = new close();
        @Override
        public void paintComponent(Graphics g){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 680, 680);
            
            for(int i = 0; i< 16;i++){
                for(int j =0; j< 16; j++){
                    
                    g.setColor(Color.GRAY);
                    
                    if(revealed[i][j]== true){
                        g.setColor(Color.WHITE);
                        if(mines[i][j] == 1)
                            g.setColor(Color.red);
                    }
                    
                    if(mx>=spacing + i*40&&mx<i*40+40-spacing&&
                            my>=spacing + j*40+40+18&&my< j*40+40+18+40-spacing){;
                        g.setColor(Color.lightGray);
                        
                    }
                    
                    g.fillRect(spacing + i*40,spacing + j*40+40,
                            40-2*spacing, 40-2*spacing);
                    if(i == 8 && j == 8){
                        g.setColor(Color.WHITE);
                        g.fillRect(spacing + i*40,spacing + j*40+40,
                            40-2*spacing, 40-2*spacing);
                        switch (neighbours[i][j]) {
                            case 1 -> g.setColor(Color.blue);
                            case 2 -> g.setColor(Color.green);
                            case 3 -> g.setColor(Color.red);
                            case 4 -> g.setColor(new Color(0, 0, 128));
                            case 5 -> g.setColor(new Color(178, 34, 34));
                            case 6 -> g.setColor(new Color(72, 209, 204));
                            case 8 -> g.setColor(Color.darkGray);
                            case 0 -> g.setColor(Color.blue);
                            default -> {
                             }
                        }

                        g.setFont(new Font("Tahoma", Font.BOLD, 25));
                        g.drawString(Integer.toString(neighbours[i][j]),
                                i * 40 + 10, j * 40 + 40 + 30);
                    }
                    if(revealed[i][j]== true){
                        if(stopClick[i][j] == false){
                            g.setColor(Color.black);

                            if (mines[i][j] == 1) {
                                g.fillRect(i * 40 + 5 + 10, j * 40 + 40 + 10, 10, 20);
                                g.fillRect(i * 40 + 10, j * 40 + 40 + 5 + 10, 20, 10);
                                g.fillRect(i * 40 + 2 + 10, j * 40 + 40 + 2 + 10, 15, 15);
                            }else if (mines[i][j] == 0 && neighbours[i][j] == 0) {
                                open.openEmpty(g, i, j);
                            }else{
                                closeReveal[i][j] = true;
                                open.open(g, i, j);
                                

                            }
                        }
                        if(stopClick[i][j] == true){
                            g.setColor(Color.GRAY);
                            g.fillRect(spacing + i*40,spacing + j*40+40,
                            40-2*spacing, 40-2*spacing);
                            
                        }
                            
                    }
                    if(flagged[i][j]== true){
                        g.setColor(Color.BLACK);
                        g.fillRect(i*40+i+12,j*40+40+5 , 3, 25);
                        g.fillRect(i*40+i+ 8, j*40+40+24,13, 6);
                        g.setColor(Color.red); 
                        g.fillRect(i*40+i+4, j*40+40+5, 8, 8);
                        g.setColor(Color.BLACK);
                        g.drawRect(i*40+i+4, j*40+40+5, 10, 9);
                        g.drawRect(i*40+i+5, j*40+40+6, 7, 7);
                        
                    }
                }
            }
            // Undo feature
            if(undo == true){
                try {
                    if(closeEmptyReveal[stackX.peek()][stackY.peek()]==true && mines[stackX.peek()][stackY.peek()]==0
                            &&neighbours[stackX.peek()][stackY.peek()]==0){
                        close.closeEmpty(g, stackX.pop(), stackY.pop());
                    }else if (closeReveal[stackX.peek()][stackY.peek()]== true){
                        revealed[stackX.peek()][stackY.peek()]= false;
                        g.setColor(Color.GRAY);
                        g.fillRect(spacing + stackX.pop()*40,spacing + stackY.pop()*40+40,
                                40-2*spacing, 40-2*spacing);
                        
                    }else if(flagged[stackX.peek()][stackY.peek()] == true){
                        revealed[stackX.peek()][stackY.peek()]= false;
                        flagged[stackX.peek()][stackY.peek()] = false;
                        g.setColor(Color.GRAY);
                        g.fillRect(spacing + stackX.pop()*40,spacing + stackY.pop()*40+40,
                                40-2*spacing, 40-2*spacing);
                        
                    } 
                } catch (Exception e) {
                }
                System.out.println("Undo");
                undo = false;
                    
                    
                
            }
           
                    
            
            
            // Smiley painting
            g.setColor(Color.yellow);
            g.fillOval(smileyx, smileyy, 30, 30);
            g.setColor(Color.BLACK);
            g.fillOval(smileyx+5, smileyy+8, 5,5 );
            g.fillOval(smileyx+20, smileyy+8, 5, 5);
            if(happiness== true){
                g.fillRect(smileyx+10, smileyy+20, 12, 4);
                g.fillRect(smileyx+8, smileyy+18, 4, 4);
                g.fillRect(smileyx+20, smileyy+18, 4, 4);
            }else{
                g.fillRect(smileyx+10, smileyy+20, 12, 4);
                g.fillRect(smileyx+8, smileyy+22, 4, 4);
                g.fillRect(smileyx+20, smileyy+22, 4, 4);
            }
            
            // Flagger painting 
            g.setColor(Color.BLACK);
            g.fillRect(flaggerX+12,flaggerY+5 , 3, 25);
            g.fillRect(flaggerX+ 8, flaggerY+24,13, 6);
            g.setColor(Color.red); 
            g.fillRect(flaggerX+4, flaggerY+5, 8, 8);
            g.setColor(Color.BLACK);
            g.drawRect(flaggerX+4, flaggerY+5, 10, 9);
            g.drawRect(flaggerX+5, flaggerY+6, 7, 7);
            if(flagger == true){
                g.setColor(Color.red);
                g.drawOval(flaggerX-5, flaggerY, 35, 35);
                g.drawOval(flaggerX-4, flaggerY+1, 33, 33);
            }
            
            g.setColor(Color.BLACK);
            // Undo painting:
            g.drawOval(undoX-5, undoY, 35, 35);
            g.drawOval(undoX-4, undoY+1, 33, 33);
            g.setColor(Color.red);
            g.setFont(new Font("Tahoma", Font.BOLD, 21));
            g.drawString("U",undoCenterX-10,undoCenterY+12);
            
            // Time Counter painting 
            g.setColor(Color.black);
            g.fillRect(timex, timey, 80 , 35);
            if(defeat == false && victory == false)
                sec = (int) ((new Date().getTime()-startDate.getTime())/1000);
            if(sec >999)
                sec = 999;
            g.setColor(Color.WHITE);
            if(victory == true){
                g.setColor(Color.GREEN);
            }else if (defeat == true){
                g.setColor(Color.red);
            }
            g.setFont(new Font("Tahoma", Font.PLAIN,35));
            if(sec < 10){
                g.drawString("00"+Integer.toString(sec), timex, timey+30);
            }else if(sec < 100){
                g.drawString("0"+Integer.toString(sec), timex, timey+30);
            }else{
                g.drawString(Integer.toString(sec), timex, timey+30);
            }
            
            // Display the win/lose message.
            
            if(victory == true){
                g.setColor(Color.green);
                vicMes = "YOU WIN";
            }else if(defeat == true){
                g.setColor(Color.red);
                vicMes = "YOU LOSE"; 
                for(int i = 0 ; i<16;i++){
                    for(int j =0;j<16; j++){
                        if(mines[i][j]==1){
                          revealed[i][j] = true;
                        }else if(revealed[i][j]== false){
                           stopClick[i][j] = true;
                        }
            }
        }
            }
            
            if(victory == true || defeat == true){
                mesY = -50+(int) ((new Date().getTime()-endDate.getTime() ))/10;
                if(mesY > 40){
                    mesY = 30;
                }
                g.setColor(Color.GREEN);
                g.setFont(new Font ("Tahoma", Font.PLAIN, 30));
                g.drawString(vicMes, mesX, mesY);
            }
            
            
                
            
        }
    }
    public class Move implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
             //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
            /*System.out.println("The mouse was moved! ");
            
            System.out.println("X : " +mx+" Y : "+my);*/
        }
        
    }
    public class Click implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            
            mx = e.getX();
            my = e.getY();
            
             
            if(inBoxX()!= -1&& inBoxY()!=-1){
                System.out.println("The mouse was clicked in the[" +inBoxX()+","
                        +inBoxY()+"]"+ "Numbers of mines neighbours "+
                        neighbours[inBoxX()][inBoxY()]);
                stackX.push(inBoxX());
                stackY.push(inBoxY());
                if(flagger == true &&revealed[inBoxX()][inBoxY()]== false){
                    if(flagged[inBoxX()][inBoxY()]== false){
                        flagged[inBoxX()][inBoxY()]= true;
                        
                    }else{
                        flagged[inBoxX()][inBoxY()]= false;
                        
                    }
                }else{
                    if(flagged[inBoxX()][inBoxY()] == false)
                        revealed[inBoxX()][inBoxY()]= true;
                }
                /*if(flagged[inBoxX()][inBoxY()] == false){
                    stackX.push(inBoxX());
                    stackY.push(inBoxY());
                }*/
            }
            if(inSmiley()== true){
                resetAll();
            }
            if(inFlagger() == true){
                if(flagger == false){
                    flagger = true;
                }else{
                    flagger = false;
            }
            
        }
            if(inUndo()==true){
                undo = undo == false;
            }
           
           
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
             //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
           //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
             //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    public void checkVictoryStatus(){
        if(defeat== false){
            for(int i = 0 ; i<16;i++){
                for(int j =0;j<16; j++){
                    if(revealed[i][j]==true && mines[i][j]==1){
                        defeat = true;
                        happiness = false;
                        endDate = new Date();
                        
                    }        
                }
            }
        }
        if(totalRevealed()>=256-totalMines()&& victory== false){
            victory = true;
            endDate = new Date();
            
        }
    }
    public int totalMines(){
        int total =0;
        for(int i = 0 ; i<16;i++){
            for(int j =0;j<16; j++){
                if(mines[i][j]==1)
                    total++;
            }
        }
        return total;
    }
    public int totalRevealed(){
        int totalRevealed =0;
        for(int i = 0 ; i<16;i++){
            for(int j =0;j<16; j++){
                if(revealed[i][j]==true)
                    totalRevealed++;
            }
        }
        return totalRevealed;
    }
    
    public void resetAll(){
        
        resetter = true;
        startDate = new Date();
        happiness = true;
        victory = false;
        flagger = false;
        mesY = -200;
        defeat = false;
        random randoMine1 = new random();
        randoMine1.mineRandom();
        resetter = false;
        while(!stackX.isEmpty()&& !stackY.isEmpty()){
            stackX.pop();
            stackY.pop();
        }
            
    }
    public boolean inSmiley(){
        int dif = (int) Math.sqrt(Math.abs(mx-smileyCenterX)*Math.abs(mx-smileyCenterX)
                +Math.abs(my-smileyCenterY)*Math.abs(my-smileyCenterY));
        return dif > 20&& dif < 45;
    }
    public boolean inFlagger(){ 
        int dif1 = (int) Math.sqrt(Math.abs(mx-flaggerCenterX)*Math.abs(mx-flaggerCenterX)
                +Math.abs(my-flaggerCenterY)*Math.abs(my-flaggerCenterY));
        return dif1 > 20&& dif1 < 45;
    }
    public boolean inUndo(){ 
        int dif1 = (int) Math.sqrt(Math.abs(mx-undoCenterX)*Math.abs(mx-undoCenterX)
                +Math.abs(my-undoCenterY)*Math.abs(my-undoCenterY));
        return dif1 > 20&& dif1 < 45;
    }
    
    public int inBoxX(){
        for(int i = 0; i< 16;i++){
            for(int j =0; j< 16; j++){
                    
                if(mx>=spacing + i*40&&mx<i*40+40-spacing&&
                    my>=spacing + j*40+40+18&&my< j*40+40+18+40-spacing){;
                   return i;
                }
                    
            }
        }
        return -1;
        
    }
    
    public int inBoxY(){
        for(int i = 0; i< 16;i++){
            for(int j =0; j< 16; j++){
                    
                if(mx>=spacing + i*40&&mx<i*40+40-spacing&&
                    my>=spacing + j*40+40+18&&my< j*40+40+18+40-spacing){;
                   return j;
                }
                    
            }
        }
        return -1;
        
    }
    public boolean isN(int mX, int mY, int cX , int cY ){
        return mX-cX<2&&mX-cX > -2 &&mY-cY<2&&mY-cY > -2&& 
                mines[cX][cY]==1;
        
    }
    public class random{
        public void mineRandom(){
            for(int i = 0 ; i<16;i++){
            for(int j =0;j<16; j++){
                if(rand.nextInt(100)<15&& i !=8 && j !=8){
                    mines[i][j]=1;
                }else{
                    mines[i][j] = 0;
                }
                revealed[i][j]= false;
                flagged[i][j]= false;
                stopClick[i][j] = false;
                
            }
            
        }
        
        for(int i = 0 ; i<16;i++){
            for(int j =0;j<16; j++){
                neighs = 0;
                for(int n = 0 ; n<16;n++){
                    for(int m =0;m<16; m++){
                        if(!(n==i&& m==j)){
                            if(isN(i, j, n, m)== true)
                                neighs++;
                        
                    }   
                    }
                }
                neighbours[i][j] = neighs;
            }
        }
            
        }
        
    }
    public class Open{
        public void open(Graphics g, int i, int j) {
            if (mines[i][j] == 0 && neighbours[i][j] != 0) {
                
                switch (neighbours[i][j]) {
                    case 1 -> g.setColor(Color.blue);
                    case 2 -> g.setColor(Color.green);
                    case 3 -> g.setColor(Color.red);
                    case 4 -> g.setColor(new Color(0, 0, 128));
                    case 5 -> g.setColor(new Color(178, 34, 34));
                    case 6 -> g.setColor(new Color(72, 209, 204));
                    case 8 -> g.setColor(Color.darkGray);
                    default -> {
                    }
                }

                g.setFont(new Font("Tahoma", Font.BOLD, 25));
                g.drawString(Integer.toString(neighbours[i][j]),
                        i * 40 + 10, j * 40 + 40 + 30);
            }

        }
        public void openEmpty(Graphics g,int i , int j){
            g.setColor(Color.lightGray);
            g.fillRect(spacing + i*40,spacing + j*40+40,
                            40-2*spacing, 40-2*spacing);
            revealed[i][j]= true;
            closeEmptyReveal[i][j] = true;
            for (int h = i - 1; h <= i + 1; h++)
		for (int k = j - 1; k <= j + 1; k++)
                    if (h >= 0 && h <= 15&& k >=0 && k <= 15) {
                        if(mines[h][k]== 0 && revealed[h][k]== false && 
                                neighbours[i][j]==0&& flagged[h][k] == false){
                            
                            openEmpty(g, h, k);
                            
                        }else{
                            if(flagged[h][k] == false)
                                open(g, h, k);
                        }
                            
                        
                    }
        }
    }
    
    public class close{
       
        public void closeEmpty(Graphics g, int i , int j){
            g.setColor(Color.GRAY);
            g.fillRect(spacing + i*40,spacing + j*40+40,
                            40-2*spacing, 40-2*spacing);
            revealed[i][j]= false;
            closeEmptyReveal[i][j] = false;
            for (int h = i - 1; h <= i + 1; h++)
		for (int k = j - 1; k <= j + 1; k++)
                    if (h >= 0 && h <= 15&& k >=0 && k <= 15) {
                        if(mines[h][k]== 0 && closeEmptyReveal[h][k]== true && 
                                neighbours[i][j]==0&& flagged[h][k] == false){
                            
                            closeEmpty(g, h, k);
                            
                        }else{
                            if(flagged[h][k] == false)
                                g.setColor(Color.GRAY);
                                g.fillRect(spacing + i*40,spacing + j*40+40,
                                40-2*spacing, 40-2*spacing);
                        }
                            
                        
                    }
        }
            
        }
    
}
    
    
    
    
    
    
    

