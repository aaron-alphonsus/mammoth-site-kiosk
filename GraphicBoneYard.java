/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package kiosk;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author Brady Shimp and Elliott Rarden
 */
public class GraphicBoneYard extends JPanel
{
	private Dimension _OriginalDimension = new Dimension(0,0);
	private Dimension _CurrentDimension = new Dimension(0,0);
	private ArrayList<Bone> _Bones = new ArrayList<>();
	private Point2D.Double _WalkwayMin = null;
	private Point _MousePosition = null;
	private boolean _FirstLoad = true;
	private int sliderValue = 10;
	private double _DeltaX = 0;
	private double _DeltaY = 0;
	private double _XScale = 0;
	private double _YScale = 0;
	private double _Scale = 1;

	public GraphicBoneYard() {
		super();
		_Bones = BonePit.readBones();
		this.setDoubleBuffered(true);

		this.addMouseListener(new MouseAdapter()
		{
			@Override public void mousePressed(MouseEvent e) { _MousePosition = new Point(e.getX(), e.getY()); }

			@Override public void mouseReleased(MouseEvent e) { _MousePosition = null; }
			
			@Override public void mouseClicked(MouseEvent e) { ((GraphicBoneYard)e.getSource()).FindClosestBone( new Point2D.Double(e.getX(), e.getY()) ); }
		});

		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override public void mouseDragged(MouseEvent e)
			{
				double x = e.getX() - _MousePosition.getX();
				double y = e.getY() - _MousePosition.getY();

				JViewport port = (JViewport)((JPanel)e.getSource()).getParent();
				Rectangle visible = port.getViewRect();

				visible.x -= x / (_Scale);
				visible.y -= y / (_Scale);

				scrollRectToVisible(visible);
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return _CurrentDimension;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent( g );
		AffineTransform at = new AffineTransform();
		if(_FirstLoad)
        {
            _OriginalDimension = this.getSize();
            _CurrentDimension = this.getSize();
            _FirstLoad = false;
        }
               
        Graphics2D graph = (Graphics2D)g;
        //at.rotate(Math.PI);
        //graph.setTransform(at);
        
        Draw(graph);
        graph.dispose();
        revalidate();
    }

	// Setters
	public void setScale(int scale) {
	
	    if(scale > 0 && scale < _Scale)
	    {
	        _Scale = scale;
	        UpdatePreferredSize(-1);
	    }
	    else if (scale > 0 && scale > _Scale)
	    {
	        _Scale = scale;
	        UpdatePreferredSize(1);
	    }
	}

	public void setSliderValue(int val) {
		this.sliderValue = val;
		repaint();
	}

	private void Draw(Graphics2D graph) {
		double topMargin = 5;
		double botMargin = 5;
		double leftMargin = 5;
		double rightMargin = 5;
		Walkway ww = Walkway.getWalkwayInstance();
		_WalkwayMin = new Point2D.Double(ww.xMin, ww.yMin);
        _DeltaX = (ww.xMax + rightMargin) - (ww.xMin - leftMargin);
        _DeltaY = (ww.yMax + botMargin)- (ww.yMin - topMargin);
		_XScale = _CurrentDimension.getWidth() / (_DeltaX);
		_YScale = _CurrentDimension.getHeight() / (_DeltaY);
        double yMax = ww.yMax;

		for(ArrayList<Point2D.Double> line : ww.polylines)
		{
			for(int i = 0; i < line.size() - 1; i++)
			{
				Point2D.Double p1 = line.get(i);
				Point2D.Double p2 = line.get(i+1);
				graph.drawLine((int) ((p1.getX() - ww.xMin) * _XScale), (int)((yMax - p1.getY())* _YScale), (int)((p2.getX() - ww.xMin)* _XScale), (int)((yMax - p2.getY())* _YScale));
			}
		}

		for(Bone bone : _Bones)
		{
			for (ArrayList<Point2D.Double> line : bone.polylines)
			{
				for(int i = 0; i < line.size() - 1; i++)
				{
					Point2D.Double p1 = line.get(i);
					Point2D.Double p2 = line.get(i+1);
					graph.drawLine((int) ((p1.getX() - ww.xMin) * _XScale), (int)((yMax - p1.getY())* _YScale), (int)((p2.getX() - ww.xMin)* _XScale), (int)((yMax - p2.getY())* _YScale));
				}
			}
		}
	}

	private void UpdatePreferredSize(int resize) {
        Point2D.Double oldCenter = new Point2D.Double(_CurrentDimension.getWidth()/2, _CurrentDimension.getHeight()/2);
		int w = (int) (_OriginalDimension.getWidth() * _Scale);
		int h = (int) (_OriginalDimension.getHeight() * _Scale);

		_CurrentDimension.setSize(w, h);
		this.setPreferredSize(_CurrentDimension);
		
        Point2D.Double scaledCenter = new Point2D.Double(_CurrentDimension.getWidth()/2, _CurrentDimension.getHeight()/2);
		Point viewCenter = getViewPortCenter();
		JViewport port = (JViewport)this.getParent();
		Rectangle visible = port.getViewRect();
		
		if(resize > 0)
		{
		    visible.x = (int)viewCenter.getX() + (int)(scaledCenter.getX() / oldCenter.getX()) * (int)_Scale;
		    visible.y = (int)viewCenter.getY() + (int)(scaledCenter.getY() / oldCenter.getY()) * (int)_Scale;
	    }
	    else
	    {
	        visible.x = (int)scaledCenter.getX();
		    visible.y = (int)scaledCenter.getX();
	    }	

        scrollRectToVisible(visible);
		getParent().doLayout();
		revalidate();
		repaint();
	}

	private Point getViewPortCenter() {
		JViewport port = (JViewport)this.getParent();
		Point center = port.getViewPosition();

		center.x += port.getWidth()/2;
		center.y += port.getHeight()/2;

		return center;
	}
    
    private void FindClosestBone(Point2D.Double p)
    {
    	boolean closer = false;
    	Bone closestBone = new Bone();
    	Point2D.Double boneCenter = new Point2D.Double(0,0);
	    Point2D.Double closestCenter = new Point2D.Double(0,0);
	    
    	for(Bone bone : _Bones)
    	{
            if(bone.xMin != null && bone.xMax != null && bone.yMin != null && bone.yMax != null)
            {
    	        if(p.getX() <= (bone.xMax - _WalkwayMin.getX()) * _XScale && p.getX() >= (bone.xMin - _WalkwayMin.getX()) * _XScale &&
    	            p.getY() <= (bone.yMax - _WalkwayMin.getY()) * _YScale && p.getY() >= (bone.yMin - _WalkwayMin.getY()) * _YScale)
    	        {
    	            boneCenter = new Point2D.Double(bone.xMax/2.0, bone.yMax/2.0);
    	    	
    	    	    if(closestBone == null) closer = true;
    	    	    else if( DistanceFormula(p, boneCenter) < DistanceFormula(p, closestCenter) ) closer = true;    		
    		
    		        if(closer)
    		        {
    		    	    closer = false;
    		    	    closestBone = bone;
    		    	    closestCenter = new Point2D.Double(closestBone.xMax/2.0, closestBone.yMax/2.0);
    		        }
    		    }
            }
    	}
        System.out.println(closestBone);    	
    }
    
    private double DistanceFormula(Point2D.Double a, Point2D.Double b)
    {
    	double distance = (a.getX() - b.getX()) * (a.getX() - b.getX());
	    return distance += (a.getY() - b.getY()) * (a.getY() - b.getY());
    }
}
