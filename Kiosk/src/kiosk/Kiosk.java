/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kiosk;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;


/**
 *
 * @author Brady Shimp
 */
public class Kiosk extends JFrame
{   
    //Private Class Members
    
    private int _SliderValue = 0;
    private int _ZoomFactor = 0;
    
    //End Private Class Members
    
    
    // Class Constructor
    
    public Kiosk()
    {
        super("Welcome to Mammoth Site");
        Container content = getContentPane();
        GridBagLayout layout = new GridBagLayout( );
        this.addKeyListener( new KeyAdapter()
        {
            @Override public void keyPressed(KeyEvent e) { if(e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0); }
        });
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override public void mouseClicked(MouseEvent e) { if(!hasFocus()) requestFocus(); }
        });
        
        this.addComponentListener(new ComponentAdapter()
        {
            @Override public void componentShown(ComponentEvent e) { if(!hasFocus()) requestFocus(); }
            @Override public void componentMoved(ComponentEvent e) { if(!hasFocus()) requestFocus(); }
        });
        
        content.setLayout( layout );
        CreateMainKioskScrollPane(content);      
        CreateMainKioskBoneYardPanel(content);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setContentPane(content);  
        this.setResizable(false);
        this.setFocusable(true);
        this.setSize(950, 600);      //set Width, Height
        this.setVisible(true);
    }
    
    //End Class Constructor
    
    
    //Public Class Methods
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() { public void run() { CreateAndShowGUI(); } });
    }
    
    //End Public Class Methods
    
    
    //Private Class Methods
    
    private static void CreateAndShowGUI()
    {
       Kiosk kiosk = new Kiosk();
    }

    private void CreateMainKioskScrollPane(Container content)
    {      
        JLabel label = new JLabel("Inventory");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        JScrollPane pane = new JScrollPane(label);   
           
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.05;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;

        content.add( pane, c );  
    }
    
    private void CreateMainKioskBoneYardPanel(Container content)
    {
        JPanel allContentPanel = new JPanel();
        JPanel boneYardPanel = new JPanel();
        JLabel label = new JLabel("This is the Bone Yard");
        GridBagConstraints c = new GridBagConstraints();
        
        allContentPanel.setLayout(new GridBagLayout());
        
        label.setVerticalAlignment(JLabel.CENTER);
        boneYardPanel.add(label);
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = .5;
        c.fill = GridBagConstraints.BOTH;
        allContentPanel.add(boneYardPanel, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        allContentPanel.add(CreateBottomOfScreenControls(), c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 4;
        c.weightx = .1;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 0;
        
        content.add(allContentPanel, c);
    }
    
    private JPanel CreateBottomOfScreenControls()
    {
        JPanel kioskControls = new JPanel();
        kioskControls.setLayout(new GridBagLayout( ));

        SetZoomControls(kioskControls);
        SetSliderControl(kioskControls);

        return kioskControls;
    }
    
    private void SetSliderControl(JPanel panel)
    {
        JSlider slider = new JSlider();
        GridBagConstraints c = new GridBagConstraints();
        
        slider.addChangeListener((ChangeEvent e) -> 
        {
            //From: http://docs.oracle.com/javase/tutorial/uiswing/events/changelistener.html
            JSlider source = (JSlider)e.getSource();
            if(!source.getValueIsAdjusting())
            {
                _SliderValue = (int)source.getValue();
                source.transferFocusBackward(); //set focus back to JFrame
            }
        });
        
        slider.setMinimum(0);
        slider.setMaximum(10);
        
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 2;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        panel.add(slider, c);
    }
    
    private void SetZoomControls(JPanel panel)
    {
        GridBagConstraints c = new GridBagConstraints();
        JButton zoomOut = new JButton ("-");
        JButton zoomIn = new JButton("+");
        JLabel label = new JLabel( ((Integer)_ZoomFactor).toString() );
        JPanel zoomPanel = new JPanel();
        
        zoomIn.addActionListener((ActionEvent e) -> 
        {
            _ZoomFactor ++; 
            label.setText( ((Integer)_ZoomFactor).toString() );
            ((JButton)e.getSource()).getTopLevelAncestor().requestFocus();
        });
        zoomOut.addActionListener((ActionEvent e) ->  
        {
            _ZoomFactor--; 
            label.setText( ((Integer)_ZoomFactor).toString() );
            ((JButton)e.getSource()).getTopLevelAncestor().requestFocus();
        });
        
        zoomPanel.setLayout(new GridLayout(0, 1));
        zoomPanel.setBorder(BorderFactory.createEmptyBorder(0, 90, 0, 20));
        c.gridx = 4;
        c.gridy = 0;
        c.weighty = 1.1;
        c.gridheight = 2;
        c.fill = GridBagConstraints.VERTICAL;
        
        zoomPanel.add(zoomIn, 0);
        zoomPanel.add(label, 1);
        zoomPanel.add(zoomOut, 2);
        
        panel.add(zoomPanel, c);
    }
    
    //End Private Class Methods
}
