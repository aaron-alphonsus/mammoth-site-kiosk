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
	private Point _MousePosition = null;
	private boolean _FirstLoad = true;
	private double _Scale = 1;
	private int sliderValue = 10;

	public GraphicBoneYard() {
		super();
		_Bones = BonePit.readBones();
		this.setDoubleBuffered(true);

		this.addMouseListener(new MouseAdapter()
		{
			@Override public void mousePressed(MouseEvent e) { _MousePosition = new Point(e.getX(), e.getY()); }

			@Override public void mouseReleased(MouseEvent e) { _MousePosition = null; }
		});

		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override public void mouseDragged(MouseEvent e)
			{
				double x = e.getX() - _MousePosition.getX();
				double y = e.getY() - _MousePosition.getY();

				JViewport port = (JViewport)((JPanel)e.getSource()).getParent();
				Rectangle visible = port.getViewRect();

				visible.x -= x / (_Scale * 1);
				visible.y -= y / (_Scale * 1);

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
		if(scale != _Scale && scale > 0)
		{
			_Scale = scale;
			UpdatePreferredSize();
		}
	}

	public void setSliderValue(int val) {
		this.sliderValue = val;
		repaint();
	}

	private void Draw(Graphics2D graph) {
		// JViewport port = (JViewport)this.getParent();
		double topMargin = 5;
		double botMargin = 5;
		double leftMargin = 5;
		double rightMargin = 5;

		Walkway ww = Walkway.getWalkwayInstance();
		double xDel = (ww.xMax + rightMargin) - (ww.xMin - leftMargin);
		double yDel = (ww.yMax + botMargin)- (ww.yMin - topMargin);

		double xScale = _CurrentDimension.getWidth() / (xDel);
		double yScale = _CurrentDimension.getHeight() / (yDel);


		for(ArrayList<Point2D.Double> line : ww.polylines)
		{
			for(int i = 0; i < line.size() - 1; i++)
			{
				Point2D.Double p1 = line.get(i);
				Point2D.Double p2 = line.get(i+1);
				graph.drawLine((int) ((p1.getX() - ww.xMin) *xScale), (int)((p1.getY()- ww.yMin)*yScale), (int)((p2.getX() - ww.xMin)*xScale), (int)((p2.getY() - ww.yMin)*yScale));
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
					graph.drawLine((int) ((p1.getX() - ww.xMin) *xScale), (int)((p1.getY()- ww.yMin)*yScale), (int)((p2.getX() - ww.xMin)*xScale), (int)((p2.getY() - ww.yMin)*yScale));
				}
			}
		}
	}

	private void UpdatePreferredSize() {
		double d = 1 + (_Scale);

		int w = (int) (_OriginalDimension.getWidth() * d);
		int h = (int) (_OriginalDimension.getHeight() * d);

		_CurrentDimension.setSize(w, h);
		this.setPreferredSize(_CurrentDimension);
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
}
