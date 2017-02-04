/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kiosk;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 *
 * @author 7210579
 */
public class Kiosk extends JFrame
{   
    public Kiosk()
    {
        super("Welcome to Mammoth Site");
        addKeyListener( new JComponentCloseOnEscapeKey() );
        
        Container content = getContentPane();
        
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Some Text");
        panel.add(label);
        
        content.setLayout(new FlowLayout() );
        content.add( CreateMainKioskScrollPane() );
        content.add(panel);
        
        setContentPane(content);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(700, 600);      //set Width, Height

        setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run()
            { 
                CreateAndShowGUI();
            } 
        });
    }
    
    private static void CreateAndShowGUI()
    {
       Kiosk kiosk = new Kiosk();
    }
    
    private static void Exit(Kiosk kiosk)
    {
        kiosk.dispose();
    }
    
    private JScrollPane CreateMainKioskScrollPane()
    {
        JScrollPane pane = new JScrollPane();
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        return pane;
    }
}
