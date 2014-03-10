package com.profiler.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.plot.PiePlot;

import com.general.view.jtreetable.TableTreeNode;
import com.profiler.model.data.ProfileClass;
import com.profiler.model.data.ProfileDataVisitor;
import com.profiler.model.data.ProfileMethod;
import com.profiler.model.data.ProfilePackage;

public class ProfileTreeVisitor implements ProfileDataVisitor {

	private List<TableTreeNode> parents;
	private PiePlot plot;
	
	public ProfileTreeVisitor(TableTreeNode rootNode, PiePlot plot) {
		this.parents = new ArrayList<>();
		this.parents.add(rootNode);
		this.plot = plot;
	}

	public void visit(ProfilePackage data) {
				
		TableTreeNode tableTreeNode = new TableTreeNode(new ProfilerRowData(data.getName(),Color.WHITE,data.getCompleteName(),data.getNumCalls(),true));
		
		TableTreeNode parentNode = getLastParent();
		parentNode.add(tableTreeNode);
		
		parents.add(tableTreeNode);
		
		for (int i=0;i<data.getNumChildren();i++){
			data.getChild(i).accept(this);
		}
		
		removeLastParent();
		
	}
	
	public void visit(ProfileMethod data) {
		
		TableTreeNode tableTreeNode = new TableTreeNode(new ProfilerRowData(data.getName(),Color.WHITE,data.getCompleteName(),data.getNumCalls(),true));
		
		TableTreeNode parentNode = getLastParent();
		parentNode.add(tableTreeNode);
		
	}
	
	public void visit(ProfileClass data) {
			
		Color color = (Color) plot.getSectionPaint(data.getCompleteName());
		if (color == null)
			color = (Color) plot.getSectionPaint(ProfilerView.OTHERS_CLASSES);
		
		
		TableTreeNode tableTreeNode = new TableTreeNode(new ProfilerRowData(data.getName(),color,data.getCompleteName(),data.getNumCalls(),true));
		
		TableTreeNode parentNode = getLastParent();
		parentNode.add(tableTreeNode);
		
		parents.add(tableTreeNode);
		
		for (int i=0;i<data.getNumChildren();i++){
			data.getChild(i).accept(this);
		}
		
		removeLastParent();
		
	}

	private void removeLastParent() {
		parents.remove(parents.size()-1);
	}

	public TableTreeNode getLastParent(){
		return parents.get(parents.size()-1);
	}


}
