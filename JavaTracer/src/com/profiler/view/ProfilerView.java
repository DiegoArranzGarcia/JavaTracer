package com.profiler.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;

public class ProfilerView extends JDialog {

		private static String TITLE = "Profiling stats: ";
	
	    private static final long serialVersionUID = 1L;
	    	    
	    public ProfilerView(PieDataset data) {
	        setContentPane(createDemoPanel(data));
	        setSize(new Dimension(600, 300));
	        setVisible(true);
	    }

	    private JFreeChart createChart(PieDataset dataset) {

	        JFreeChart chart = ChartFactory.createPieChart(
	            TITLE,  // chart title
	            dataset,            // data
	            false,              // no legend
	            false,               // tooltips
	            false               // no URL generation
	        );

	        // set a custom background for the chart
	        chart.setBackgroundPaint(new GradientPaint(new Point(0, 0), 
	                new Color(20, 20, 20), new Point(400, 200), Color.DARK_GRAY));

	        // customise the title position and font
	        TextTitle t = chart.getTitle();
	        t.setPaint(new Color(240, 240, 240));
	        t.setFont(new Font("Arial", Font.BOLD, 26));

	        PiePlot plot = (PiePlot) chart.getPlot();
	        plot.setBackgroundPaint(null);
	        plot.setInteriorGap(0.04);
	        plot.setOutlineVisible(false);

	        // use gradients and white borders for the section colours
	        plot.setBaseSectionOutlinePaint(Color.WHITE);
	        plot.setSectionOutlinesVisible(true);
	        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

	        // customise the section label appearance
	        plot.setLabelFont(new Font("Courier New", Font.BOLD, 20));
	        plot.setLabelLinkPaint(Color.WHITE);
	        plot.setLabelLinkStroke(new BasicStroke(2.0f));
	        plot.setLabelOutlineStroke(null);
	        plot.setLabelPaint(Color.WHITE);
	        plot.setLabelBackgroundPaint(null);
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2} percent)"));

	        return chart;

	    }

	    /**
	     * A utility method for creating gradient paints.
	     * 
	     * @param c1  color 1.
	     * @param c2  color 2.
	     * 
	     * @return A radial gradient paint.
	     */
	    private RadialGradientPaint createGradientPaint(Color c1, Color c2) {
	        Point2D center = new Point2D.Float(0, 0);
	        float radius = 200;
	        float[] dist = {0.0f, 1.0f};
	        return new RadialGradientPaint(center, radius, dist,
	                new Color[] {c1, c2});
	    }

	    /**
	     * Creates a panel for the demo (used by SuperDemo.java).
	     *
	     * @return A panel.
	     */
	    private JPanel createDemoPanel(PieDataset data) {
	        JFreeChart chart = createChart(data);
	        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
	        ChartPanel panel = new ChartPanel(chart);
	        panel.setMouseWheelEnabled(true);
	        return panel;
	    }

}

