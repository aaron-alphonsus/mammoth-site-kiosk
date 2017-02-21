/*

    **** ImageDisplay.java ****

Display bone image and details in a JFrame
Assumes bonexml folder is located in same directory.

Author: John M. Weiss, Ph.D.
        Aaron G. Alphonsus
Class:  CSC468 GUI Programming
Date:   Spring 2017

TODO:   Create Resources folder for default img
        Right-align descriptions
        javadoc

*/

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.*;
import java.util.*;
import java.io.File;

public class ImageDisplay extends JFrame
{   
    // constructor: filename is image file
    public ImageDisplay(String boneImage, Bone bone)
    {  
        // call JFrame constructor with filename in title bar
        super( boneImage );
        
        // use KeyAdapter to handle keypresses (Escape exits)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 27) {
                    setVisible(false);
                    dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter()
        {
            @Override public void componentShown(ComponentEvent e) { if(!hasFocus()) requestFocus(); }
            @Override public void componentMoved(ComponentEvent e) { if(!hasFocus()) requestFocus(); }
        });
        
        // Creates arrays with bone details and labels for bone details.
        String[] labels = {"Bone Object", "Year", "Taxon", "Object Number",
            "Completeness", "Elevation"};
        String[] properties = CreatePropertiesArray(bone);
        
        /*
        Creates the 'details' panel which has following fields:
        id, year, taxon, objectNum, completeness, elevation
        */
        JPanel details = new JPanel();
        details.setLayout( new BoxLayout(details, BoxLayout.Y_AXIS));
        getContentPane().add(details, BorderLayout.PAGE_START );
        for (int i = 0; i < labels.length; i++)
            AddComponent(details, labels[i], properties[i]);
        
        
        // add JLabel with image to the content pane
        JLabel image = new JLabel(new ImageIcon(boneImage));
        getContentPane().add(image, BorderLayout.CENTER );
        
        // Creates the remarks panel.
        JPanel remarks = new JPanel();
        remarks.setLayout(new BoxLayout(remarks, BoxLayout.X_AXIS));
        getContentPane().add(remarks, BorderLayout.PAGE_END);
        remarks.add(new JLabel("Remarks: "));
        if(String.valueOf(bone.remarks) != null && 
            !String.valueOf(bone.remarks).isEmpty())
            AddTextArea(remarks, bone.remarks);
        else
            AddTextArea(remarks, "N/A");
        remarks.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pack();

        // make window visible, and exit app when all windows closed
        setVisible( true );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    }
    
    public String[] CreatePropertiesArray(Bone bone)
    {
        String[] properties = {"N/A", "N/A", "N/A", "N/A", "N/A", "N/A"};
        
        // boneID has been tested earlier
        properties[0] = bone.id;
        
        /*
        Checks if any property is empty so that N/A can be displayed
        instead of empty string.
        */
        if (String.valueOf(bone.year) != null &&
            !String.valueOf(bone.year).isEmpty())
            properties[1] = String.valueOf(bone.year);
            
        if (bone.taxon != null && !bone.taxon.isEmpty())
            properties[2] = bone.taxon;
            
        if (String.valueOf(bone.objectNum) != null && 
            !String.valueOf(bone.objectNum).isEmpty())
            properties[3] = String.valueOf(bone.objectNum);
            
        if (bone.completeness != null && !bone.completeness.isEmpty())
            properties[4] = bone.completeness;
            
        if (String.valueOf(bone.elevation) != null && 
            !String.valueOf(bone.elevation).isEmpty())
            properties[5] = String.valueOf(bone.elevation);

        return properties;
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
        /*
        Creates a text area and sets properties of fields to display as
        required.
        */
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
                // If boneID.jpg not found, displays default.jpg
                File boneImage = new File("bonexml/" + bone.id + ".jpg");
                if (boneImage.exists())
                    new ImageDisplay("bonexml/" + bone.id + ".jpg", bone);
                else 
                    new ImageDisplay("resources/default.jpg", bone);
            }
        });
    }

     // main() function for testing
     public static void main(String[] args)
     {
         // Creates bone list and displays first bone
         ArrayList<Bone> bones = BonePit.readBones();
		 DisplayBone(bones.get(4));
     }
}
