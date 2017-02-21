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
	private Point2D.Double _WalkwayMax = null;
	private Point _MousePosition = null;
	private boolean _FirstLoad = true;
	private int sliderValue = 5;
	private double _DeltaX = 0;
	private double _DeltaY = 0;
	private double _XScale = 0;
	private double _YScale = 0;
	private int _Scale = 1;
	private double _minElevation = 0.0;
	private double _maxElevation = 0.0;

	public GraphicBoneYard() {
		super();
		_Bones = BonePit.readBones();
		_minElevation = BonePit.getMinElevation();
		_maxElevation = BonePit.getMaxElevation();
		this.setDoubleBuffered(true);

		this.addMouseListener(new MouseAdapter()
		{
			@Override public void mousePressed(MouseEvent e) { _MousePosition = new Point(e.getX(), e.getY()); }

			@Override public void mouseReleased(MouseEvent e) { _MousePosition = null; }
			
			@Override public void mouseClicked(MouseEvent e) 
			{ 
			    if(e.getClickCount() == 2)
			    {
			        ZoomToPoint( new Point(e.getX(), e.getY()) );
			    }
			    else ((GraphicBoneYard)e.getSource()).FindClosestBone( new Point2D.Double(e.getX(), e.getY()) );
			}
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

		if(_FirstLoad)
        {
            _OriginalDimension = this.getSize();
            _CurrentDimension = this.getSize();
            _FirstLoad = false;
        }
               
        Graphics2D graph = (Graphics2D)g;
        Draw(graph);
        graph.dispose();
        revalidate();
    }

	// Setters
	public void setScale(int scale) {
	    int newValue = scale + _Scale;
	    if(newValue > 0){
	        if(newValue < _Scale) UpdatePreferredSize(-1);
	        else if (newValue > _Scale) UpdatePreferredSize(1);
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
		_WalkwayMax = new Point2D.Double(ww.xMax, ww.yMax);
        _DeltaX = (ww.xMax + rightMargin) - (ww.xMin - leftMargin);
        _DeltaY = (ww.yMax + botMargin)- (ww.yMin - topMargin);
		_XScale = _CurrentDimension.getWidth() / (_DeltaX);
		_YScale = _CurrentDimension.getHeight() / (_DeltaY);

		for(ArrayList<Point2D.Double> line : ww.polylines)
		{
			for(int i = 0; i < line.size() - 1; i++)
			{
				Point2D.Double p1 = line.get(i);
				Point2D.Double p2 = line.get(i+1);
				graph.drawLine((int) ((p1.getX() - ww.xMin) * _XScale), (int)((ww.yMax - p1.getY())* _YScale), (int)((p2.getX() - ww.xMin)* _XScale), (int)((ww.yMax - p2.getY())* _YScale));
			}
		}

		for(Bone bone : _Bones)
		{
			// The slider value enables/disables bones based on an elevation threshhold
			double elev = bone.elevation;
			double threshhold = (((_maxElevation - _minElevation) / 5) * (sliderValue) + 1) + _minElevation;
			if (bone.elevation <= threshhold)
			{
				// Select our colour based on completeness
				if (bone.completeness.equals("CO")) { // Complete bone?
					graph.setColor(Color.green);
				} else if (bone.completeness.equals("PC")) { // Partially complete?
					graph.setColor(Color.orange);
				} else if (bone.completeness.equals("PE")) { // Partially Excavated?
					graph.setColor(Color.red);
				} else { // SHAFT?
					graph.setColor(Color.black);
				}

				for (ArrayList<Point2D.Double> line : bone.polylines)
				{
					for(int i = 0; i < line.size() - 1; i++)
					{
						Point2D.Double p1 = line.get(i);
						Point2D.Double p2 = line.get(i+1);
						graph.drawLine((int) ((p1.getX() - ww.xMin) * _XScale), (int)((ww.yMax - p1.getY())* _YScale), (int)((p2.getX() - ww.xMin)* _XScale), (int)((ww.yMax - p2.getY())* _YScale));
					}
				}
			}
		}
	}

	private void UpdatePreferredSize(int resize) {
		_CurrentDimension.setSize((int)(_OriginalDimension.getWidth() * (_Scale + resize)), (int)(_OriginalDimension.getHeight() * (_Scale + resize)));
		JViewport port = (JViewport)this.getParent();
		Rectangle visible = port.getViewRect();
		
	    visible.x = (int)((visible.x / _Scale) * (_Scale + resize));
		visible.y = (int)((visible.y / _Scale) * (_Scale + resize));

        port.setViewPosition(new Point(visible.x, visible.y));
        this.setPreferredSize(_CurrentDimension);
		_Scale = _Scale + resize;
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
	
	private void ZoomToPoint( Point p )
	{
	    JViewport port = (JViewport)this.getParent();
		Rectangle visible = port.getViewRect();
		
        port.setViewPosition(new Point((int)(p.getX() - visible.getWidth()/2.0), (int)(p.getY() - visible.getHeight()/2.0) ));
        UpdatePreferredSize(1);
	}
    
    private void FindClosestBone(Point2D.Double p)
    {
    	boolean closer = false;
    	boolean anyBone = false;
    	Bone closestBone = new Bone();
    	Point2D.Double boneCenter = new Point2D.Double(0,0);
	    Point2D.Double closestCenter = new Point2D.Double(0,0);
	    
    	for(Bone bone : _Bones)
    	{
            if(bone.xMin != null && bone.xMax != null && bone.yMin != null && bone.yMax != null)
            {
    	        if(p.getX() <= (bone.xMax - _WalkwayMin.getX()) * _XScale
    	         && p.getX() >= (bone.xMin - _WalkwayMin.getX()) * _XScale
    	         && p.getY() >= (_WalkwayMax.getY() - bone.yMax) * _YScale 
    	         && p.getY() <= (_WalkwayMax.getY() - bone.yMin) * _YScale)
    	        {
    	            boneCenter = new Point2D.Double(bone.xMax/2.0, bone.yMax/2.0);
    	    	
    	    	    if(closestBone == null) closer = true;
    	    	    else if( DistanceFormula(p, boneCenter) < DistanceFormula(p, closestCenter) ) closer = true;    		
    		
    		        if(closer)
    		        {
    		            anyBone = true;
    		    	    closer = false;
    		    	    closestBone = bone;
    		    	    closestCenter = new Point2D.Double(closestBone.xMax/2.0, closestBone.yMax/2.0);
    		        }
    		    }
            }
    	}
    	
        if(anyBone)
            ImageDisplay.DisplayBone(closestBone);	
    }
    
    private double DistanceFormula(Point2D.Double a, Point2D.Double b)
    {
    	double distance = (a.getX() - b.getX()) * (a.getX() - b.getX());
	    return distance += (a.getY() - b.getY()) * (a.getY() - b.getY());
    }
}
