/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiosk;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 *
 * @author Brady Shimp
 */
public class GraphicBoneYard extends JPanel
{
    private Dimension _OriginalDimension;
    private boolean _FirstLoad = true;
    private double _Scale = 1;
    
    public GraphicBoneYard() 
    {
        super();
        this.setDoubleBuffered(true);
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        double d = 1 + (_Scale/10);
        if(_OriginalDimension != null)
        {
            int w = (int) (_OriginalDimension.getWidth() * d);
            int h = (int) (_OriginalDimension.getHeight() * d);
            return new Dimension(w, h);
        }
        else
        {
            return new Dimension(0,0);
        }
        
    }
    
    @Override
    public void paintComponent(Graphics g)
    {       
        super.paintComponent( g );
        if(_FirstLoad)
        {
            _OriginalDimension = this.getSize();
            _FirstLoad = false;
        }
        
        Graphics2D graph = (Graphics2D)g;
        AffineTransform at = new AffineTransform();

        if(_Scale > 1)
        {
            at.scale(1 + (_Scale/10), 1 + (_Scale/10));
            graph.setTransform(at);
        }
        
        Draw(graph);

        graph.dispose();
    }
    
    public void setScale(int scale)
    {
        if(scale != _Scale && scale > 0) 
        {
            _Scale = scale;
            UpdatePreferredSize();
        }
    }
    
    private void Draw(Graphics2D graph)
    {
        int x2 = 500;
        int y2 = 500;
        
        graph.drawLine(0,0,x2, y2);
        graph.drawLine(x2/2, 0, x2/2, y2);
        graph.drawLine(x2, 0, 0, y2);
        graph.drawLine(0, y2/2, x2, y2/2); 
    }
    
    private void UpdatePreferredSize() 
    {
        double d = 1 + (_Scale/10);

        int w = (int) (_OriginalDimension.getWidth() * d);
        int h = (int) (_OriginalDimension.getHeight() * d);
    
        this.setPreferredSize(new Dimension(w, h));
        getParent().doLayout();
        revalidate();
        repaint();
    }
    
    private Point getViewPortCenter()
    {
        JViewport port = (JViewport)this.getParent();
        Point center = port.getViewPosition();
        
        center.x += port.getWidth()/2;
        center.y += port.getHeight()/2;
        
        return center;
    }
}
