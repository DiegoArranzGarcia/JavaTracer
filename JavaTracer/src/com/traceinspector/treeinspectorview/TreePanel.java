/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.traceinspector.treeinspectorview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

@SuppressWarnings("serial")
public class TreePanel extends JPanel {
	
	static final double DEFAULT_WIDTH_BOX = 80;
	static final double DEFAULT_HEIGHT_BOX = 60;
	static final double WIDTH_BY_LETTER = 8;
	static final int DEFAULT_STROKE = 2;
	static final int SELECTED_STROKE = 5;

	private final static int ARC_SIZE = 20;
	private final static Color BOX_COLOR = new Color(0x008EAB);
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color TEXT_COLOR = Color.white;
	
	private double verticalGap;
	
	private TreeLayout<TextInBoxExt> treeLayout;
	
	public TreePanel(TreeLayout<TextInBoxExt> treeLayout2) {
		this.verticalGap = TreeInspectorView.GAP_BETWEEN_LEVELS;
		this.treeLayout = treeLayout2;
		setFont(new Font("Trebuchet MS",Font.PLAIN,20));
		setPreferredSize(new Dimension((int)treeLayout2.getBounds().getWidth()+1,(int)treeLayout2.getBounds().getHeight()+1));
		setBackground(Color.WHITE);
	}

	private TreeForTreeLayout<TextInBoxExt> getTree() {
		return treeLayout.getTree();
	}

	private Iterable<TextInBoxExt> getChildren(TextInBoxExt parent) {
		return getTree().getChildren(parent);
	}

	private Rectangle2D.Double getBoundsOfNode(TextInBoxExt node) {
		return treeLayout.getNodeBounds().get(node);
	}
	
	public void setTree(TreeLayout<TextInBoxExt> treeLayout){
		this.treeLayout = treeLayout;
		setPreferredSize(new Dimension((int)treeLayout.getBounds().getWidth()+1,(int)treeLayout.getBounds().getHeight()+1));
	}

	private void paintEdges(Graphics g, TextInBoxExt parent) {
		if (!getTree().isLeaf(parent)) {
			
			Graphics2D  g2D = (Graphics2D)g;
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getMaxY();
			
			g2D.setStroke(new BasicStroke(DEFAULT_STROKE));
			g.drawLine((int)x1,(int)y1,(int)x1,(int)(y1+verticalGap/2));
			
			for (TextInBoxExt child : getChildren(parent)) {
				
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				double x2 = b2.getCenterX();
				
				g2D.setStroke(new BasicStroke(DEFAULT_STROKE));
				g.drawLine((int)x1,(int)(y1+verticalGap/2),(int)x2,(int)(y1+verticalGap/2));
				g.drawLine((int)x2,(int)(y1+verticalGap/2),(int)x2,(int)(y1+verticalGap));
				g2D.setStroke(new BasicStroke());
				
				paintEdges(g, child);
				
			}
			g2D.setStroke(new BasicStroke());
			
		}
	}

	private void paintBox(Graphics g,TextInBoxExt textInBox) {
		
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		g.setColor(BORDER_COLOR);
		
		BufferedImage imagePlus= null;
		BufferedImage imageMinus=null;
		try {
			imagePlus = ImageIO.read(getClass().getResource("plus1.png"));
			imageMinus = ImageIO.read(getClass().getResource("minus1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		if(textInBox.haveChildren()){
			if(!textInBox.isExpanded()){
				g.drawImage(imagePlus,(int) box.getMaxX()-22,(int) box.getMaxY()-22,16,16,null);
			}else{
				g.drawImage(imageMinus,(int) box.getMaxX()-22,(int) box.getMaxY()-22,16,16,null);
			}
		}
		
		
		Graphics2D g2D = (Graphics2D)g; 
		
		if (textInBox.isSelected())
			g2D.setStroke(new BasicStroke(SELECTED_STROKE));
		else 
			g2D.setStroke(new BasicStroke(DEFAULT_STROKE));
		
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		g2D.setStroke(new BasicStroke());
		g.setColor(TEXT_COLOR);
		String[] lines = textInBox.giveMeTextInBox().split("\n");
		FontMetrics m = getFontMetrics(getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + (int)DEFAULT_HEIGHT_BOX/2 + m.getAscent()/2;
		for (int i = 0; i < lines.length; i++) {
			g.drawString(lines[i], x, y);
			y += m.getHeight();
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);	
		
		if (treeLayout!=null){			
			paintEdges(g, getTree().getRoot());
	
			for (TextInBoxExt textInBox : treeLayout.getNodeBounds().keySet()) {
				paintBox(g, textInBox);
			}
		}
	}
}