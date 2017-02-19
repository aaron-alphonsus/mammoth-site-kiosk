/*

	**** Bone.java ****

This file is a data model for a bone.  It will keep track of wether or not to
draw itself, as well as all polyline data to do so.

Author: Elliott Rarden
Class:  CSC468 GUI Programming
Date:   Spring 2017
*/

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class Bone
{
	// Bone info
	private String id;
	private int year;
	private String taxon;
	private int objectNum;
	private String completeness;
	private String remarks;
	
	// Drawing info
	// private int nShapes;
	// private int nVerticies;
	public Double xMin, xMax, yMin, yMax;
	public ArrayList<ArrayList<Point2D.Double>> polylines = new ArrayList<ArrayList<Point2D.Double>>();

	// DONT USE THIS CONSTRUCTOR
	public Bone() {
		super();
	}

	// USE THIS CONSTRUCTOR
	public Bone(String id, int year, String taxon, int objectNum, String completeness, String remarks) {

		// Put in all the stuff given to us
		this.id = id;
		this.year = year;
		this.taxon = taxon;
		this.objectNum = objectNum;
		this.completeness = completeness;
		this.remarks = remarks;

		// Parse the bone-specific xml file
		this.parseXML();

		// Debug output
		// System.out.println(this);
	}

	// toString method for debug output
	public String toString() {
		return "Bone Object " + id + "\n         year = " + year + "\n        taxon = " + taxon + "\n    objectNum = " + objectNum + "\n completeness = " + completeness + "\n      remarks = " + remarks + "\n   xMin, xMax = (" + xMin + ", " + xMax + ")\n   yMin, yMax = (" + yMin + ", " + yMax + ")\n";

	}

	// Fetching drawing info from XML files
	// Thanks Dr. Weiss for the bulk of this function!
	void parseXML() {
		// Get our root node
		Element root = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( "bonexml/" + id + ".xml" );
			root = document.getDocumentElement();
		} catch (Exception e) {
			// System.out.println( e.toString() );
		}

		if (root == null) {
			return;
		}

		// Get NodeLists because they're easy to handle
		// Man Java has a massive encapsulation fetish
		NodeList mins = root.getElementsByTagName("xymin");
		NodeList maxs = root.getElementsByTagName("xymax");
		NodeList polys = root.getElementsByTagName("polyline");

		// Get read out our mins and maxes
		if ( mins.getLength() > 0 ) {
			Scanner sc = new Scanner( mins.item(0).getTextContent().trim() );
			xMin = sc.nextDouble();
			yMin = sc.nextDouble();
		}
		if ( maxs.getLength() > 0 ) {
			Scanner sc = new Scanner( maxs.item(0).getTextContent().trim() );
			xMax = sc.nextDouble();
			yMax = sc.nextDouble();
		}

		// Read out our polylines
		for (int i = 0; i < polys.getLength(); i++) {
			Node current = polys.item(i);
			ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>();
			NodeList xyPairs = current.getChildNodes();
			
			for (int j = 0; j < xyPairs.getLength(); j++) {
				Node pair = xyPairs.item(j);
				
				if(pair.getNodeName().equals("xy")) {
					Scanner sc = new Scanner( pair.getTextContent().trim() );
					double x = sc.nextDouble();
					double y = sc.nextDouble();
					Point2D.Double p = new Point2D.Double(x, y);
					temp.add(p);
				}
			}
			this.polylines.add(temp);
		}
	} // End parseXML

}
