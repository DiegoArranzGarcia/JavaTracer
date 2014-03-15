package com.profiler.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.SortOrder;

import com.general.view.jtreetable.JTreeTable;
import com.profiler.presenter.ProfilerPresenterInterface;

public class ProfilerViewWithoutSplit extends JFrame {
	
	public static final String OTHERS_CLASSES = "Others Classes";
	private static final String PIE_FONT = "Courier New";
	private static final String FONT_TITLE = "Arial";
	
	private static String TITLE = "Profiling stats";
	private static double PERCENTAGE= 0.75;
	private static final int CLASSCHART=5;
	private JPanel pieChartPanel;
	private JFreeChart chart;

public ProfilerViewWithoutSplit() {
        
        setTitle(TITLE);
		
		Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(d.width*PERCENTAGE),(int)(d.height*PERCENTAGE));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        pieChartPanel = new JPanel();
        add(pieChartPanel);    
        
        
    }



private DefaultPieDataset chosenClasses(DefaultPieDataset dataset) {
		
	dataset.sortByValues(SortOrder.DESCENDING);
	List<String> keys = dataset.getKeys();
	DefaultPieDataset definitivedataset = new DefaultPieDataset();
	
	int i=0;
	double percentage=0;
	
	while(i<keys.size() && i<CLASSCHART){
		definitivedataset.setValue(keys.get(i), dataset.getValue(keys.get(i)));	
		percentage=percentage + dataset.getValue(keys.get(i)).doubleValue();
		i++;	
	}

	if(i<keys.size())
		definitivedataset.setValue(OTHERS_CLASSES, 100-percentage);
	
	return definitivedataset;
}


/**
 * A JFreeChart is created with the input data.
 * @param dataset - data to show in the chart.
 * @return - JFreeChart
 */
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
    t.setFont(new Font(FONT_TITLE, Font.BOLD, 26));

    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setBackgroundPaint(null);
    plot.setInteriorGap(0.04);
    plot.setOutlineVisible(false);

    // use gradients and white borders for the section colours
    plot.setBaseSectionOutlinePaint(Color.WHITE);
    plot.setSectionOutlinesVisible(true);
    plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

    // customise the section label appearance
    plot.setLabelFont(new Font(PIE_FONT, Font.BOLD, 20));
    plot.setLabelLinkPaint(Color.WHITE);
    plot.setLabelLinkStroke(new BasicStroke(2.0f));
    plot.setLabelOutlineStroke(null);
    plot.setLabelPaint(Color.WHITE);
    plot.setLabelBackgroundPaint(null);
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - {2}"));

    return chart;

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
    
    DefaultPieDataset definitivedataset= chosenClasses(dataset); 
    
    return definitivedataset;
}

/**
 * This panel is created when there is no data to show.
 * @return
 */
private JPanel createNoLoadPanel() {
	JPanel panel = new JPanel();
	panel.setBackground(Color.DARK_GRAY);
	return panel;
}

private JPanel createPiePanel(PieDataset data) {
    chart = createChart(data);
    chart.setPadding(new RectangleInsets(4, 8, 2, 2));
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setPopupMenu(null);
    chartPanel.setLayout(new GridLayout(1, 0, 0, 0));
    return chartPanel;
}


public void load(HashMap<String, Integer> classes, int numCalledMethods) {
	
	if (numCalledMethods > 0 )
		pieChartPanel = createPiePanel(createDataset(classes, numCalledMethods));
	else 
		pieChartPanel = createNoLoadPanel();
	
      add(pieChartPanel);
      
}








}




