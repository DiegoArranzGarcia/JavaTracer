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
package com.javatracer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

import com.javatracer.view.tree.TextInBox;

@SuppressWarnings("serial")
public class TreePanel extends JPanel {
	
	private TreeLayout<TextInBox> treeLayout;

	private TreeForTreeLayout<TextInBox> getTree() {
		return treeLayout.getTree();
	}

	private Iterable<TextInBox> getChildren(TextInBox parent) {
		return getTree().getChildren(parent);
	}

	private Rectangle2D.Double getBoundsOfNode(TextInBox node) {
		return treeLayout.getNodeBounds().get(node);
	}
	
	public void setTree(TreeLayout<TextInBox> treeLayout){
		this.treeLayout = treeLayout;
		setPreferredSize(new Dimension((int)treeLayout.getBounds().getWidth()+1,(int)treeLayout.getBounds().getHeight()+1));
	}

	public TreePanel(TreeLayout<TextInBox> treeLayout) {
		
		this.treeLayout = treeLayout;
		setFont(new Font("Trebuchet MS",Font.PLAIN,20));
		setPreferredSize(new Dimension((int)treeLayout.getBounds().getWidth()+1,(int)treeLayout.getBounds().getHeight()+1));
		
	}

	private final static int ARC_SIZE = 20;
	private final static Color BOX_COLOR = new Color(0x008EAB);
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color TEXT_COLOR = Color.white;

	private void paintEdges(Graphics g, TextInBox parent) {
		if (!getTree().isLeaf(parent)) {
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (TextInBox child : getChildren(parent)) {
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(),
						(int) b2.getCenterY());

				paintEdges(g, child);
			}
		}
	}

	private void paintBox(Graphics g, TextInBox textInBox) {
		
		g.setColor(BOX_COLOR);
		Rectangle2D.Double box = getBoundsOfNode(textInBox);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);
		g.setColor(BORDER_COLOR);
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
				(int) box.height - 1, ARC_SIZE, ARC_SIZE);

		g.setColor(TEXT_COLOR);
		String[] lines = textInBox.text.split("\n");
		FontMetrics m = getFontMetrics(getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + textInBox.height/2 + m.getAscent()/2;// - m.getLeading() + 1;
		for (int i = 0; i < lines.length; i++) {
			g.drawString(lines[i], x, y);
			y += m.getHeight();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);	
		
		if (treeLayout!=null){			
			paintEdges(g, getTree().getRoot());
	
			for (TextInBox textInBox : treeLayout.getNodeBounds().keySet()) {
				paintBox(g, textInBox);
			}
		}
	}
}