/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;


/**
 *
 * @author Brady Shimp
 */
public class Kiosk extends JFrame
{
	//Private Class Members

	private GraphicBoneYard _BoneYard = null;
	private JScrollPane _ScrollYard = null;
	private JSlider _Slider = null;
	private int _SliderValue = 0;

	// Public Static variables
	public static String path = "bonexml/";

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
			@Override public void keyPressed(KeyEvent e) 
			{ 
			    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0); 
			    else
			    {
			        int incValue = 10;
			        switch( e.getKeyCode() ) { 
                        case (KeyEvent.VK_UP):
                             _BoneYard.AdjustScrollWithArrows(0, -incValue);
                             break;
                        case (KeyEvent.VK_DOWN):
                             _BoneYard.AdjustScrollWithArrows(0, incValue);
                             break;
                        case (KeyEvent.VK_LEFT):
                             _BoneYard.AdjustScrollWithArrows(-incValue, 0);
                             break;
                        case (KeyEvent.VK_RIGHT) :
                             _BoneYard.AdjustScrollWithArrows(incValue, 0);
                             break;
                        case (KeyEvent.VK_ADD) :
                        case (KeyEvent.VK_EQUALS) : //Couldn't figure out the keystroke for (SHIFT + '+')
                             _BoneYard.setScale(1);
                             break;
                        case (KeyEvent.VK_SUBTRACT) : 
                        case (KeyEvent.VK_MINUS) :
                             _BoneYard.setScale(-1);
                             break;
                        default: break;
                    }
			    }
			}
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
		content.add(CreateFilterMenu(), BorderLayout.WEST);
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
		File f = new File("bonexml/bones.xml");
		if(!f.exists()) {
			while(true)
		    {
		    	JFileChooser jfc = new JFileChooser();
    		    jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
    		    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    		    jfc.setDialogTitle("Select your bonexml folder");
    		    int retval = jfc.showOpenDialog(null);
    		    
    		    // Leave if they don't give us XML
    		    if (retval != JFileChooser.APPROVE_OPTION) {
    		    	System.exit(0);
    		    }
    
    		    // Get the selected file
    		    f = jfc.getSelectedFile();
    		    if (f.getName().equals("bonexml") && f.isDirectory()) {
    		    	break;
    		    }
		    }
		    Kiosk.path = f.getAbsolutePath() + "/";
		}

		SwingUtilities.invokeLater(new Runnable() { public void run() { CreateAndShowGUI(); } });
	}

	//End Public Class Methods


	//Private Class Methods

	private static void CreateAndShowGUI()
	{
	   Kiosk kiosk = new Kiosk();
	}

	private JPanel CreateFilterMenu()
	{
		JPanel filterMenu = new JPanel();

		// Add a border to our thing so it looks "nice"
		filterMenu.setBorder(BorderFactory.createTitledBorder("Filter Bones"));

		// Set left orientation
		filterMenu.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

		// Add check boxes
		// I really like anonymous class.  ISorry, not sorry :)
		JCheckBox cb = new JCheckBox("Show male bones", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawMale(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		cb = new JCheckBox("Show female bones", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawFemale(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		cb = new JCheckBox("Show undesignated bones", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawUndesignated(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		cb = new JCheckBox("Show mammuthus columbi", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawColumbi(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		cb = new JCheckBox("Show mammuthus primigenius", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawPrimigenius(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		cb = new JCheckBox("Show unidentified mammoth", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawUnidentifiedMammoth(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		cb = new JCheckBox("Show unidentified bones", true);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox b = (JCheckBox) e.getSource();
				_BoneYard.setDrawUnidentified(b.getModel().isSelected());
				((JCheckBox)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		});
		filterMenu.add(cb);

		filterMenu.setPreferredSize(new Dimension(250, 600));

		return filterMenu;
	}

	private JPanel CreateMainKioskBoneYardPanel()
	{
		JPanel top = new JPanel();
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		JPanel BoneYard = new JPanel();
		_BoneYard = new GraphicBoneYard();
		_ScrollYard = new JScrollPane(_BoneYard);
		BorderLayout boneyardLayout = new BorderLayout();

		_ScrollYard.setViewportView(_BoneYard);
		_ScrollYard.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		_ScrollYard.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		top.setMaximumSize(new Dimension(0,0));
		left.setMaximumSize(new Dimension(0,0));
		right.setMaximumSize(new Dimension(0,0));

		BoneYard.setLayout(boneyardLayout);
		BoneYard.add(top, BorderLayout.NORTH);
		BoneYard.add(left, BorderLayout.WEST);
		BoneYard.add(right, BorderLayout.EAST);
		BoneYard.add(_ScrollYard, BorderLayout.CENTER);
		BoneYard.add(CreateBottomOfScreenControls(), BorderLayout.SOUTH);

		return BoneYard;
	}

	private JPanel CreateBottomOfScreenControls()
	{
		JPanel kioskControls = new JPanel();
		JToolBar toolBar = new JToolBar();
		kioskControls.setLayout(new GridLayout(1, 0));
        
		SetSliderControl(toolBar);
		SetZoomControls(toolBar);
		SetResetControl(toolBar);
		SetHelpControl(toolBar);

        toolBar.setFloatable(false);
		kioskControls.add(toolBar);
		kioskControls.add(new JLabel("    "));

		return kioskControls;
	}

	private void SetSliderControl(JToolBar toolBar)
	{
		JPanel sliderPanel = new JPanel();
		_Slider = new JSlider();

		_Slider.addChangeListener((ChangeEvent e) ->
		{
			//From: http://docs.oracle.com/javase/tutorial/uiswing/events/changelistener.html
			JSlider source = (JSlider)e.getSource();
			if(!source.getValueIsAdjusting())
			{
				_SliderValue = source.getValue();
				source.transferFocusBackward(); //set focus back to JFrame
				_BoneYard.setSliderValue(_SliderValue);
			}
		});

		_Slider.setMinimum(1);
		_Slider.setMaximum(5);

		_Slider.setMajorTickSpacing(1);
		_Slider.setMinorTickSpacing(1);

		_Slider.setPaintTicks(true);
		_Slider.setPaintLabels(true);
		_Slider.setSnapToTicks(true);

        JLabel sliderLabel = new JLabel("Elevation: ");
		//sliderPanel.setLayout(new GridLayout(2, 1));
		sliderPanel.setPreferredSize(new Dimension(150, 75));
		sliderLabel.setAlignmentX(JLabel.CENTER);
		sliderPanel.add(sliderLabel);
		sliderPanel.add(_Slider);
		
		toolBar.add(sliderPanel);
	}

	private void SetZoomControls(JToolBar toolBar)
	{
		JButton zoomOut = new JButton ("-");
		JButton zoomIn = new JButton("+");
		JPanel zoomPanel = new JPanel();

		ActionListener zoomListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource() == zoomIn) setZoomFactor(1);
				else setZoomFactor(-1);
				((JButton)e.getSource()).getTopLevelAncestor().requestFocus();
			}
		};
		zoomIn.addActionListener(zoomListener);
		zoomOut.addActionListener(zoomListener);

		zoomOut.setPreferredSize(new Dimension(50, 30));
		zoomIn.setPreferredSize(new Dimension(50, 30));
		JLabel zoomLabel = new JLabel("Zoom: ");
		zoomLabel.setAlignmentX(JLabel.CENTER);
		zoomPanel.add(zoomLabel);
		zoomPanel.add(zoomIn);
		zoomPanel.add(zoomOut);

        zoomPanel.setPreferredSize(new Dimension(30, 90));
        zoomPanel.setAlignmentX(JToolBar.RIGHT);
		toolBar.add(zoomPanel);
	}
	
	private void SetResetControl(JToolBar toolBar)
	{
	    JButton reset = new JButton("Reset");
	    JPanel resetPanel = new JPanel();
	    
	    reset.addActionListener((ActionEvent e) ->{
	            _BoneYard.Reset();
	            _Slider.setValue(5);
	            ((JButton)e.getSource()).getTopLevelAncestor().requestFocus();
	    });
	    
	    resetPanel.add(reset);
	    toolBar.add(resetPanel);
	}
	
	private void SetHelpControl(JToolBar toolBar)
	{
	    JButton about = new JButton("About");
	    JPanel aboutPanel = new JPanel();
	    
	    about.addActionListener((ActionEvent e) -> {
	        DisplayAbout();
	        ((JButton)e.getSource()).getTopLevelAncestor().requestFocus();
	    });
	    
	    aboutPanel.add(about);
	    toolBar.add(aboutPanel);
	}
	
	private void DisplayAbout()
	{
	    System.out.println("About");
	}

	private void setZoomFactor(int newValue)
	{
	    _BoneYard.setScale(newValue);
	}

	//End Private Class Methods
}
