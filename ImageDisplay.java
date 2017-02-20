/*

    **** ImageDisplay.java ****

Display bone image as a JLabel.
Assumes bonexml folder is located in same directory.

Author: John M. Weiss, Ph.D.
        Aaron G. Alphonsus
Class:  CSC468 GUI Programming
Date:   Spring 2017

*/

import java.awt.event.*;
import java.awt.Component;
import javax.swing.*;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class ImageDisplay extends JFrame
{   
    // constructor: filename is image file
    public ImageDisplay(String boneImage, Bone bone)
    {  
        // call JFrame constructor with filename in title bar
        super( boneImage );
        
        // use KeyAdapter to handle keypresses (Escape exits)
        addKeyListener( new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ( e.getKeyCode() == 27 ) System.exit( 0 );
            }
        });
        
        // Creates the 'details' panel which has following fields:
        // id, year, taxon, objectNum, completeness, elevation
        JPanel details = new JPanel();
        details.setLayout( new BoxLayout(details, BoxLayout.Y_AXIS));
        getContentPane().add(details, BorderLayout.PAGE_START );
        AddComponent(details, "Bone Object", bone.id);
        AddComponent(details, "Year", String.valueOf(bone.year));
        AddComponent(details, "Taxon", bone.taxon);
        AddComponent(details, "Object Number", String.valueOf(bone.objectNum));
        AddComponent(details, "Completeness", bone.completeness);
        AddComponent(details, "Elevation", String.valueOf(bone.elevation));
        
        // add JLabel with image to the content pane
        JLabel image = new JLabel(new ImageIcon(boneImage));
        getContentPane().add(image, BorderLayout.CENTER );
        
        // Creates the remarks panel.
        JPanel remarks = new JPanel();
        remarks.setLayout(new BoxLayout(remarks, BoxLayout.X_AXIS));
        getContentPane().add(remarks, BorderLayout.PAGE_END);
        remarks.add(new JLabel("Remarks: "));
        AddTextArea(remarks, bone.remarks);
        remarks.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pack();

        // make window visible, and exit app when all windows closed
        setVisible( true );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    }
    
    public void AddComponent (JPanel container, String label, String property)
    {
        // Create panel for bone field
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        // Add label name and description to the field
        JLabel labelname = new JLabel(label + ": ");
        panel.add(labelname);
        AddTextArea(panel, property);
        
        // Sets the panel containing the fields to align left
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(panel);
    }
    
    public void AddTextArea (JPanel container, String text)
    {
        // Creates a text area and sets properties of fields to display as
        // required.
        JTextArea textarea = new JTextArea();
        textarea.setText(text);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setEditable(false);
        textarea.setOpaque(false);
        container.add(textarea);
    }
    
    public static void DisplayBone(Bone bone)
    {
        // display bone image. 
        // looks in cwd for bonexml folder. 
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                // If boneID.jpg not found, displays empty window.
                new ImageDisplay("bonexml/" + bone.id + ".jpg", bone);
            }
        });
    }

    // // main() function for testing
    // public static void main(String[] args)
    // {   
        // // Creates bone list and displays first bone
        // ArrayList<Bone> bones = BonePit.readBones();
		// DisplayBone(bones.get(0));
    // }
}
