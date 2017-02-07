/*

    **** ImageDisplay.java ****

Display an image in a JLabel.

Usage:
javac ImageDisplay.java
java  ImageDisplay imagefile.jpg (or .png, .gif, etc.)

Author: John M. Weiss, Ph.D.
Class:  CSC468 GUI Programming
Date:   Spring 2017

*/

import java.awt.event.*;
import javax.swing.*;

public class ImageDisplay extends JFrame
{
    // constructor: filename is image file
    public ImageDisplay( String filename )
    {
        // call JFrame constructor with filename in title bar
        super( filename );
        
        // use KeyAdapter to handle keypresses (Escape exits)
        addKeyListener( new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ( e.getKeyCode() == 27 ) System.exit( 0 );
            }
        });

        // add JLabel with image to the content pane
        JLabel image = new JLabel( new ImageIcon( filename ) );
        getContentPane().add( image );
        pack();

        // make window visible, and exit app when window is closed
        setVisible( true );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    // main() function
    public static void main( String [] args )
    {
        // check usage
        if ( args.length == 0 )
        {
            System.out.println( "Usage: java ImageDisplay imagefile" );
            return;
        }

        // display image
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                new ImageDisplay( args[0] );
            }
        });
    }
}
