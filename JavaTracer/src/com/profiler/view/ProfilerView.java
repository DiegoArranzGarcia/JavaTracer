package com.profiler.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class ProfilerView extends JFrame implements ChartProgressListener,ComponentListener,ProfilerViewInterface{

	private static String TITLE = "Profiling stats: ";
	
	private ProfileTableRenderer renderer;
	
	private JTable table;
	private JPanel pieChartPanel;
	private JFreeChart chart;
	private JSplitPane splitPane;
	
	private HashMap<String, Integer> chartInfo;
	    	    
    public ProfilerView() {
        setSize(new Dimension(800, 450));
        setTitle(TITLE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        splitPane = new JSplitPane();
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane();
        splitPane.setRightComponent(scrollPane);
        
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Used classes", "", "Class name", "Count"
        	}
        ) {
        	Class[] columnTypes = new Class[] {
        		Boolean.class, Object.class, Object.class, Object.class
        	};
        	public Class getColumnClass(int columnIndex) {
        		return columnTypes[columnIndex];
        	}
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(1).setResizable(false);
        table.getColumnModel().getColumn(1).setPreferredWidth(25);
        table.getColumnModel().getColumn(1).setMinWidth(25);
        table.getColumnModel().getColumn(1).setMaxWidth(25);
        table.getColumnModel().getColumn(2).setPreferredWidth(526);
        table.getColumnModel().getColumn(3).setResizable(false);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setMinWidth(80);
        renderer =  new ProfileTableRenderer();
        table.setDefaultRenderer(Object.class,renderer);
        scrollPane.setViewportView(table);
        
        pieChartPanel = new JPanel();
        splitPane.setLeftComponent(pieChartPanel);
        setVisible(true);
        addComponentListener(this);
        
    }

    private JFreeChart createChart(PieDataset dataset) {
    	
        JFreeChart chart = ChartFactory.createPieChart(
            "",  // chart title
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
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - {2}"));
        

        return chart;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    
    public void loadTable(HashMap<String, Integer> classes) {
    	DefaultTableModel model = (DefaultTableModel) table.getModel();
    	
    	while (model.getRowCount()!=0)
    		model.removeRow(0);
    	
    	Iterator<Entry<String,Integer>> iterator = classes.entrySet().iterator();
    	PiePlot plot = (PiePlot)chart.getPlot();
    	List<Color> colors = new ArrayList<>();
    	while (iterator.hasNext()){
    		Entry<String,Integer> entry = iterator.next();
    		colors.add((Color) plot.getSectionPaint(entry.getKey()));
    		model.addRow(new Object[]{true,"",entry.getKey(),entry.getValue()});
    	}
    	
    	renderer.setColors(colors);
    	
	}
    
	public void loadPieChart(HashMap<String, Integer> classes, int numCalledMethods) {
		
		this.chartInfo = classes;		
		pieChartPanel = createPiePanel(createDataset(classes, numCalledMethods));
    	splitPane.setLeftComponent(pieChartPanel);
	}
	
	public PieDataset createDataset(HashMap<String, Integer> classes, int numCalledMethods) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Iterator<Entry<String,Integer>> iterator = classes.entrySet().iterator();        
       
        while (iterator.hasNext()){
        	Entry<String,Integer> entry = iterator.next();
        	double times = entry.getValue().doubleValue();
        	double percentage = (times/numCalledMethods)*100;
        	dataset.setValue(entry.getKey(),percentage);
        }
        
        return dataset;
    }

    
    private JPanel createPiePanel(PieDataset data) {
        chart = createChart(data);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel_1 = new ChartPanel(chart);
        panel_1.setMouseWheelEnabled(true);
        panel_1.setLayout(new GridLayout(1, 0, 0, 0));
        chart.addProgressListener(this);
        return panel_1;
    }

	
    
    // Component Listeners
    
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	
	public void componentResized(ComponentEvent e) {
        splitPane.setDividerLocation(0.7);
	}

	public void componentShown(ComponentEvent e) {
        splitPane.setDividerLocation(0.7);
	}

	public void chartProgress(ChartProgressEvent event) {
		if (event.getType() == ChartProgressEvent.DRAWING_FINISHED)
			loadTable(chartInfo);
	}
}

