/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package kiosk;

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

	private GraphicBoneYard _BoneYard = null;
	private int _SliderValue = 0;
	private int _ZoomFactor = 1;

	//End Private Class Members


	// Class Constructor

	public Kiosk()
	{
		super("Welcome to Mammoth Site");
		Container content = getContentPane();
		BorderLayout layout = new BorderLayout();
		JPanel right = new JPanel();
		JPanel bottom = new JPanel();
		JMenuBar menuBar = new JMenuBar();

		right.setMaximumSize(new Dimension(0,0));
		bottom.setMaximumSize(new Dimension(0,0));

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

		menuBar.add(new JMenu("File"));

		content.setLayout( layout );
		content.add(right, BorderLayout.EAST);
		content.add(bottom, BorderLayout.SOUTH);
		content.add(menuBar, BorderLayout.NORTH);
		content.add(CreateMainKioskScrollPane(), BorderLayout.WEST);
		content.add(CreateMainKioskBoneYardPanel(), BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(content);
		this.setResizable(false);
		this.setFocusable(true);
		this.setSize(1600, 900);      //set Width, Height
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

	private JPanel CreateMainKioskScrollPane()
	{
		JPanel scrollPanel = new JPanel();
		JScrollPane pane = new JScrollPane();

		scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("Inventory");
		label.setHorizontalAlignment(JLabel.CENTER);

		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setPreferredSize(new Dimension(250, 600));

		scrollPanel.add(label);
		scrollPanel.add(pane);

		return scrollPanel;
	}

	private JPanel CreateMainKioskBoneYardPanel()
	{
		JPanel top = new JPanel();
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		JPanel BoneYard = new JPanel();
		_BoneYard = new GraphicBoneYard();
		BorderLayout boneyardLayout = new BorderLayout();
		JScrollPane scrollYard = new JScrollPane(_BoneYard);

		scrollYard.setViewportView(_BoneYard);
		scrollYard.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollYard.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		top.setMaximumSize(new Dimension(0,0));
		left.setMaximumSize(new Dimension(0,0));
		right.setMaximumSize(new Dimension(0,0));

		BoneYard.setLayout(boneyardLayout);
		BoneYard.add(top, BorderLayout.NORTH);
		BoneYard.add(left, BorderLayout.WEST);
		BoneYard.add(right, BorderLayout.EAST);
		BoneYard.add(scrollYard, BorderLayout.CENTER);
		BoneYard.add(CreateBottomOfScreenControls(), BorderLayout.SOUTH);

		return BoneYard;
	}

	private JPanel CreateBottomOfScreenControls()
	{
		JPanel kioskControls = new JPanel();
		JToolBar toolBar = new JToolBar();

		SetSliderControl(toolBar);
		SetZoomControls(toolBar);

		kioskControls.add(toolBar);

		return kioskControls;
	}

	private void SetSliderControl(JToolBar toolBar)
	{
		JPanel sliderPanel = new JPanel();
		JSlider slider = new JSlider();
		JLabel label = new JLabel( (" Value: " + (Integer)_SliderValue) );

		slider.addChangeListener((ChangeEvent e) ->
		{
			//From: http://docs.oracle.com/javase/tutorial/uiswing/events/changelistener.html
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting())
			{
				_SliderValue = source.getValue();
				label.setText( (" Value: " + (Integer)_SliderValue) );
				source.transferFocusBackward(); //set focus back to JFrame
				_BoneYard.setSliderValue(_SliderValue);
			}
		});

		slider.setMinimum(1);
		slider.setMaximum(5);

		slider.setMajorTickSpacing(1);
		slider.setMinorTickSpacing(1);

		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);

		sliderPanel.setLayout(new GridLayout(0, 2));
		sliderPanel.add(slider);

		sliderPanel.add(label);

		toolBar.add(sliderPanel);
	}

	private void SetZoomControls(JToolBar toolBar)
	{
		JButton zoomOut = new JButton ("-");
		JButton zoomIn = new JButton("+");
		JLabel label = new JLabel(" Value: " + _ZoomFactor);
		JPanel zoomPanel = new JPanel();

		ActionListener zoomListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource() == zoomIn) setZoomFactor(_ZoomFactor + 1);
				else setZoomFactor(_ZoomFactor - 1);
				label.setText((" Value: " + _ZoomFactor));
				((JButton)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		};
		zoomIn.addActionListener(zoomListener);
		zoomOut.addActionListener(zoomListener);

		zoomPanel.setLayout(new GridLayout(0, 2));
		zoomPanel.add(zoomIn);
		zoomPanel.add(label);
		zoomPanel.add(zoomOut);

		toolBar.add(zoomPanel);
	}

	private void setZoomFactor(int newValue)
	{
		if(newValue > 0)
		{
			_ZoomFactor = newValue;
			_BoneYard.setScale(_ZoomFactor);
		}
	}

	//End Private Class Methods
}
