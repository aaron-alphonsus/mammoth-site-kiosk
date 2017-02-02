/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kiosk;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author 7210579
 */
public class Kiosk {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run(){ CreateAndShowGUI(); } 
        });
    }
    
    private static void CreateAndShowGUI()
    {
        //MasterWindow window = new MasterWindow();
        //window.setVisible(true);
    }
    
}
