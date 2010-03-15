/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bot;

import javax.swing.JOptionPane;
import bot.Dice;

/**
 *
 * @author b9
 */
public class testrandom {


     public static void main(String[] args) {
         // Dette er bare en tidlig prototype.

 String test = JOptionPane.showInputDialog("Terning øyne");

  String test2 = JOptionPane.showInputDialog("Terningkast");


//String test3 = JOptionPane.showInputDialog("bonus");

 
  int a = Integer.parseInt(test);
  int b = Integer.parseInt(test2);
  int c = 1;
  for(int i=b; i>0; i--){
      
      Dice rand = new Dice();
      System.out.print("Roll " + c + ": " + rand.rand(a) + "\n");
      c++;

  }
     }
}
