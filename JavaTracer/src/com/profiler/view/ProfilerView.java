package com.profiler.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.*;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.SortOrder;

import com.general.view.jtreetable.JTreeTable;
import com.general.view.jtreetable.TableTreeNode;
import com.profiler.presenter.ProfilerPresenterInterface;

@SuppressWarnings("serial")
public class ProfilerView extends JFrame implements ChartProgressListener,ComponentListener,ProfilerViewInterface, ActionListener{

	private static String TITLE = "Profiling stats";
	private static double SPLIT_PERCENTAGE = 0.7;
	private static double PERCENTAGE = 0.75;
	private static final int CLASSCHART=5;
	private static final String OTHERS_CLASSES = "Others Classes ";
	
	private ProfilerPresenterInterface presenter;
	private ProfileTableRenderer renderer;
	
	private JTreeTable table;
	private JPanel pieChartPanel;
	private JFreeChart chart;
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuBar menuBar_1;
	private JMenuItem mntmOpenProfile;
	private JMenuItem mntmSaveProfile;
	private JMenuItem mntmExportAs;
	private JMenu mnEdit;
	private JMenuItem mntmCheckAllClasses;
	private JMenuItem mntmInvertClasses;
	private JMenuItem mntmUncheckAllClasses;
	private JButton btnSave;
	private JButton btnCancel;
	private JPanel panelRight;
	private JPanel panel;
    		    
    public ProfilerView() {
        
        setTitle(TITLE);
        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(d.width*PERCENTAGE),(int)(d.height*PERCENTAGE));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    	    // System.exit(0);
	    		presenter.closeWindow();
	    	   }
		});
        renderer =  new ProfileTableRenderer();
        
        splitPane = new JSplitPane();
        getContentPane().add(splitPane, BorderLayout.CENTER);

        panelRight = new JPanel();
        splitPane.setRightComponent(panelRight);
        panelRight.setLayout(new BorderLayout(0, 0));
        
        panel = new JPanel();
        panelRight.add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 7));
        
        btnSave = new JButton("Save");
        panel.add(btnSave);
        btnSave.addActionListener(this);
        
        btnCancel = new JButton("Cancel");
        panel.add(btnCancel);
        
        JScrollPane scrollPane = new JScrollPane();
        panelRight.add(scrollPane, BorderLayout.CENTER);
        
        table = new JTreeTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ProfilerHeaderData headerData = new ProfilerHeaderData();
        table.setRoot(headerData);
        table.setModel(new DefaultTableModel(headerData.getValues(),0) {
        	Class<?>[] columnTypes = new Class[] {
        		Boolean.class, Object.class, Object.class, Object.class
        	};
        	public Class<?> getColumnClass(int columnIndex) {
        		return columnTypes[columnIndex];
        	}
        	boolean[] columnEditables = new boolean[] {
        		true, false, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        
        table.setCellRenderer(renderer);
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
        //table.setDefaultRenderer(Object.class,renderer);
        scrollPane.setViewportView(table);
        btnCancel.addActionListener(this);
        
        pieChartPanel = new JPanel();
        splitPane.setLeftComponent(pieChartPanel);
        
        addComponentListener(this);
        
        splitPane.setDividerLocation(560);
        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        mntmOpenProfile = new JMenuItem("Open profile");
        mntmOpenProfile.addActionListener(this);
        mnFile.add(mntmOpenProfile);
        
        mntmSaveProfile = new JMenuItem("Save profile");
        mntmSaveProfile.addActionListener(this);
        mnFile.add(mntmSaveProfile);
        
        mntmExportAs = new JMenuItem("Export as ...");
        mntmExportAs.addActionListener(this);
        mnFile.add(mntmExportAs);
        
        menuBar_1 = new JMenuBar();
        mnFile.add(menuBar_1);
        
        mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);
        
        mntmCheckAllClasses = new JMenuItem("Check all classes");
        mntmCheckAllClasses.addActionListener(this);
        mnEdit.add(mntmCheckAllClasses);
        
        mntmUncheckAllClasses = new JMenuItem("Uncheck all classes");
        mntmUncheckAllClasses.addActionListener(this);
        mnEdit.add(mntmUncheckAllClasses);
        
        mntmInvertClasses = new JMenuItem("Invert classes");
        mntmInvertClasses.addActionListener(this);
        mnEdit.add(mntmInvertClasses);
        
    }

	private JPanel createNoLoadPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		return panel;
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
   
    public void loadTable() {
    	
    	table.clearTable();
    	
    	Iterator<Entry<String,Integer>> iterator = presenter.getClassesInfo();
    	PiePlot plot = (PiePlot)chart.getPlot();
    	List<Color> colors = new ArrayList<>();
    	TableTreeNode rootNode = table.getRoot();
    	
    	while (iterator.hasNext()){
    		Entry<String,Integer> entry = iterator.next();
    		
    		Color color = (Color) plot.getSectionPaint(entry.getKey());
    		if (color == null)
    			color = (Color)plot.getSectionPaint(OTHERS_CLASSES);
    		
    		colors.add(color);
    		rootNode.add(new TableTreeNode(new ProfilerRowData(true,"",entry.getKey(),entry.getValue())));	
    	}
    	
    	renderer.setColors(colors);
    	table.refreshTable(-1);
    	
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
	
	private DefaultPieDataset chosenClasses(DefaultPieDataset dataset) {
			
		dataset.sortByValues(SortOrder.DESCENDING);
		List<String> keys=dataset.getKeys();
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


    
    private JPanel createPiePanel(PieDataset data) {
        chart = createChart(data);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel_1 = new ChartPanel(chart);
        panel_1.setMouseWheelEnabled(true);
        panel_1.setLayout(new GridLayout(1, 0, 0, 0));
        chart.addProgressListener(this);
        return panel_1;
    }

	// ProfilerViewInterface methods
    
	public void load(HashMap<String, Integer> classes, int numCalledMethods) {
		
		if (numCalledMethods > 0 )
			pieChartPanel = createPiePanel(createDataset(classes, numCalledMethods));
		else 
			pieChartPanel = createNoLoadPanel();
		
    	splitPane.setLeftComponent(pieChartPanel);
    	splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}

	public void setPresenter(ProfilerPresenterInterface presenter) {
		this.presenter = presenter;
	}
	
	public HashMap<String, Boolean> getClassesState() {
		HashMap<String,Boolean> classesState = new HashMap<>();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			String nameClass = (String) model.getValueAt(i,2);
			boolean checked = (boolean) model.getValueAt(i, 0);
			classesState.put(nameClass, checked);
		}
		return classesState;
	}
    
    // Component Listeners
    
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	
	public void componentResized(ComponentEvent e) {
        splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}

	public void componentShown(ComponentEvent e) {
        splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}
	
	// Chart progress listener

	public void chartProgress(ChartProgressEvent event) {
		if (event.getType() == ChartProgressEvent.DRAWING_FINISHED)
			loadTable();
	}

	// Action Listener
	
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if (source.equals(mntmExportAs)){
			clickedExportAs();
		} else if (source.equals(mntmOpenProfile)){
			clickedOpenProfile();
		} else if (source.equals(mntmSaveProfile)){
			clickedSaveProfile();
		} else if (source.equals(mntmCheckAllClasses)){
			clickedCheckAllClasses();
		} else if (source.equals(mntmUncheckAllClasses)){
			clickedUncheckAllClasses();
		} else if (source.equals(mntmInvertClasses)){
			clickedInvertClasses();
		} else if (source.equals(btnSave)){
			clickedOnSave();
		} else if (source.equals(btnCancel)){
			clickedOnCancel();
		}
	}

	private void clickedExportAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Files (*.png)", "png"));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Files (*.jpeg)", "jpeg"));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle("Save as");
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		//return directory file
		
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				
				String file_path = chooser.getSelectedFile().getCanonicalPath();
				File file = new File(file_path);
				if (com.general.model.FileUtilities.isExtension(file,"png")){
					ChartUtilities.saveChartAsPNG(file, chart,pieChartPanel.getWidth(),pieChartPanel.getHeight());
				} else {
					ChartUtilities.saveChartAsJPEG(file, chart,pieChartPanel.getWidth(),pieChartPanel.getHeight());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { 
			chooser.cancelSelection();
		}
	}

	private void clickedOpenProfile() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files (*.xml)", "xml"));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle("Save as");
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		//return directory file
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				String path = chooser.getSelectedFile().getCanonicalPath();
				presenter.openProfile(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { 
			chooser.cancelSelection();
		}
	}

	private void clickedSaveProfile() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files (*.xml)", "xml"));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle("Save as");
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		//return directory file
		
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				String path = chooser.getSelectedFile().getCanonicalPath();
				presenter.saveProfile(new File(path+".xml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { 
			chooser.cancelSelection();
		}
	}

	private void clickedCheckAllClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(true,i,0);
		}
	}
	
	private void clickedUncheckAllClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(false,i,0);
		}
	}

	private void clickedInvertClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(!(boolean)model.getValueAt(i,0),i,0);
		}
	}
	
	private void clickedOnCancel() {
		presenter.cancel();
	}

	private void clickedOnSave() {
		presenter.save();
	}

}

