

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author A.TUAN
 */
public class Main implements Runnable {

    GUI gui = new GUI();

    public static void main(String[] args) {

        new Thread(new Main()).start();

    }

    @Override
    public void run() {
        while (true) {
            gui.repaint();
            if(gui.resetter == false){
               gui.checkVictoryStatus(); 
               //System.out.println("Victory: "+gui.victory+ ", Defeat: "+ gui.defeat);
            }
           

        }
    }
    
}
