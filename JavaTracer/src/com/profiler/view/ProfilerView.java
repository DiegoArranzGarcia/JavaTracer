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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
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
import com.profiler.model.ProfilerTree;
import com.profiler.model.data.ProfileData;
import com.profiler.presenter.ProfilerPresenterInterface;

@SuppressWarnings("serial")
public class ProfilerView extends JFrame implements ChartProgressListener,ComponentListener,ProfilerViewInterface, ActionListener,MouseListener{

	private static final String FILE = "File";
	private static final String OPEN_PROFILE = "Open profile";
	private static final String SAVE_PROFILE = "Save profile";
	private static final String EXPORT_AS = "Export as ...";
	private static final String CLOSE = "Close";
	
	private static final String CHECK_ALL_CLASSES = "Check all classes";
	private static final String UNCHECK_ALL_CLASSES = "Uncheck all classes";
	private static final String INVERT_CLASSES = "Invert check of all classes";
	
	private static final String JPEG_FILTER_FILES = "JPEG Files (*.jpeg)";
	private static final String PNG_FILTER_FILES = "PNG Files (*.png)";
	private static final String XML_FILTER_FILES = "XML Files (*.xml)";
	
	private static final String JPEG_EXT = "jpeg";
	private static final String XML_EXT = "xml";
	private static final String PNG = "png";
	
	public static final String OTHERS_CLASSES = "Others Classes";
	private static final String PIE_FONT = "Courier New";
	private static final String FONT_TITLE = "Arial";
	
	private static String TITLE = "Profiling stats";
	private static double SPLIT_PERCENTAGE = 0.54;
	private static double PERCENTAGE_WIDTH = 1.0;
	private static double PERCENTAGE_HEIGHT = 0.75;
	private static final int CLASSCHART=5;
	
	private ProfilerPresenterInterface presenter;
	private ProfileCellRenderer renderer;
	
	private JTreeTable table;
	private JPanel pieChartPanel;
	private JFreeChart chart;
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpenProfile;
	private JMenuItem mntmSaveProfile;
	private JMenuItem mntmExportAs;
	private JMenu mnEdit;
	private JMenuItem mntmCheckAllClasses;
	private JMenuItem mntmInvertClasses;
	private JMenuItem mntmUncheckAllClasses;
	private JButton btnSave;
	private JButton btnCancel;
	private JButton btnRefresh;
	private JPanel panelRight;
	private JPanel panel;
	private JMenuItem mntmExit;
	
    		    
	/**
	 * Creates the profile view. This view is not visible until the presenter (which must be set), make the
	 * view visible. The presenter of this view must implements {@link com.profiler.view.ProfilerViewInterface ProfilerViewInterface}.
	 * 
	 * @see ProfilerViewInterface
	 */
	
    public ProfilerView() {
        
        setTitle(TITLE);
		addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    		presenter.cancel();
	    	   }
		});

        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(d.width*PERCENTAGE_WIDTH),(int)(d.height*PERCENTAGE_HEIGHT));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
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
        
        btnRefresh = new JButton("Refresh");
        panel.add(btnRefresh);
        btnRefresh.addActionListener(this);
        
        
        btnCancel = new JButton("Cancel");
        panel.add(btnCancel);
        
        JScrollPane scrollPane = new JScrollPane();
        panelRight.add(scrollPane, BorderLayout.CENTER);
        
        table = new JTreeTable();
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"", "Name", "Complete name", "Count", "Excluded"
        	}
        ) {
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false, false
        	};
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ProfilerHeaderData headerData = new ProfilerHeaderData();
        table.setRoot(headerData);
        table.setModel(new DefaultTableModel(headerData.getValues(),0) {
        	Class<?>[] columnTypes = new Class[] {
        		Object.class, Object.class, Object.class, Object.class,Boolean.class
        	};
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false, true
        	};
        	public Class<?> getColumnClass(int columnIndex) {
        		return columnTypes[columnIndex];
        	}
        	public boolean isCellEditable(int row, int column) {
        		return columnEditables[column];
        	}
        });
        
        renderer = new ProfileCellRenderer(table.getTreeModel());
        table.setCellRenderer(renderer);
        table.getTableHeader().setReorderingAllowed(false);
        table.setExpandbleColumn(1);       
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(0).setMinWidth(25);
        table.getColumnModel().getColumn(0).setMaxWidth(25);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        scrollPane.setViewportView(table);
        btnCancel.addActionListener(this);
        
        pieChartPanel = new JPanel();
        splitPane.setLeftComponent(pieChartPanel);
        
        addComponentListener(this);
        table.addMouseListener(this);
        
        splitPane.setDividerLocation(560);
        
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        mnFile = new JMenu(FILE);
        menuBar.add(mnFile);
        
        mntmOpenProfile = new JMenuItem(OPEN_PROFILE);
        mntmOpenProfile.addActionListener(this);
        mnFile.add(mntmOpenProfile);
        mntmOpenProfile.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
        
        mntmSaveProfile = new JMenuItem(SAVE_PROFILE);
        mntmSaveProfile.addActionListener(this);
        mnFile.add(mntmSaveProfile);
        mntmSaveProfile.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        
        mntmExportAs = new JMenuItem(EXPORT_AS);
        mntmExportAs.addActionListener(this);
        mnFile.add(mntmExportAs);
        
        mntmExit = new JMenuItem(CLOSE);
        mntmExit.addActionListener(this);
        mnFile.add(mntmExit);
        mntmExit.setAccelerator(KeyStroke.getKeyStroke('E', KeyEvent.CTRL_DOWN_MASK));
        
        mnEdit = new JMenu("Edit");
        menuBar.add(mnEdit);
        
        mntmCheckAllClasses = new JMenuItem(CHECK_ALL_CLASSES);
        mntmCheckAllClasses.addActionListener(this);
        mnEdit.add(mntmCheckAllClasses);
        
        mntmUncheckAllClasses = new JMenuItem(UNCHECK_ALL_CLASSES);
        mntmUncheckAllClasses.addActionListener(this);
        mnEdit.add(mntmUncheckAllClasses);
        
        mntmInvertClasses = new JMenuItem(INVERT_CLASSES);
        mntmInvertClasses.addActionListener(this);
        mnEdit.add(mntmInvertClasses);
        
    }

    public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if (source.equals(mntmExportAs)){
			clickedExportAs();
		} else if (source.equals(mntmOpenProfile)){
			clickedOpenProfile();
		} else if (source.equals(mntmSaveProfile)){
			clickedSaveProfile();
		} else if (source.equals(mntmExit)){
			clickedOnExit();	
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
		} else if (source.equals(btnRefresh)){
			clickedOnRefresh();
		}
	}
    
	

	public void chartProgress(ChartProgressEvent event) {
		if (event.getType() == ChartProgressEvent.DRAWING_FINISHED)
			loadTable();
	}
   
    private DefaultPieDataset chosenClasses(DefaultPieDataset dataset) {
			
		dataset.sortByValues(SortOrder.DESCENDING);
		@SuppressWarnings("unchecked") 
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

	private void clickedCheckAllClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(true,i,4);
		}
	}

	private void clickedExportAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(PNG_FILTER_FILES, PNG));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(JPEG_FILTER_FILES, JPEG_EXT));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle(EXPORT_AS);
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		//return directory file
		
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				
				String file_path = chooser.getSelectedFile().getCanonicalPath();
				File file = new File(file_path);
				if (com.general.model.FileUtilities.isExtension(file,PNG)){
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
	
	private void clickedInvertClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(!(boolean)model.getValueAt(i,4),i,4);
		}
	}
    
    private void clickedOnCancel() {
		presenter.cancel();
	}

	// ProfilerViewInterface methods
    
    private void clickedOnExit() {
		presenter.cancel();
	}

	private void clickedOnSave() {
		presenter.save();
	}
	
	private void clickedOnRefresh() {
	
		presenter.refresh();
	}

	private void clickedOpenProfile() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(XML_FILTER_FILES, XML_EXT));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle(OPEN_PROFILE);
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
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(XML_FILTER_FILES, XML_EXT));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle(SAVE_PROFILE);
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
    
    // Component Listeners
    
	private void clickedUncheckAllClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(false,i,4);
		}
	}
	public void componentHidden(ComponentEvent e) {}
	
	public void componentMoved(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
        splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}
	
	// Chart progress listener

	public void componentShown(ComponentEvent e) {
        splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}

	// Action Listener
	
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
        chart.addProgressListener(this);
        return chartPanel;
    }

	private void createTreeVisitor(ProfileTreeVisitor visitor, ProfileData data) {
		List<ProfileData> children = data.getChildren();
		for (int i=0;i<children.size();i++){
			children.get(i).accept(visitor);
		}
	}

	public HashMap<List<String>, Boolean> getDataState() {
		HashMap<List<String>,Boolean> classesState = new HashMap<>();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			List<String> path = getPath(i);
			boolean checked = (boolean) model.getValueAt(i,4);
			classesState.put(path, checked);
		}
		return classesState;
	}
	
	private List<String> getPath(int i) {
		List<String> path = new ArrayList<String>();
		TableTreeNode node = table.getTreeModel().getNodeFromRow(i+1);
		
		while (!node.isRoot()){
			path.add(0,((ProfilerRowData)node.getUserObject()).getName());
			node = node.getParent();
		}
		
		return path;
	}

	public void load(HashMap<String, Integer> classes, int numCalledMethods) {
		
		if (numCalledMethods > 0 )
			pieChartPanel = createPiePanel(createDataset(classes, numCalledMethods));
		else 
			pieChartPanel = createNoLoadPanel();
		
    	splitPane.setLeftComponent(pieChartPanel);
    	splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}

	public void load(ProfilerTree currentProfileTree) {
    	if (currentProfileTree.getNumCalls() > 0 )
			pieChartPanel = createPiePanel(createDataset(currentProfileTree.getClasses(),currentProfileTree.getNumCalls()));
		else 
			pieChartPanel = createNoLoadPanel();
		
    	splitPane.setLeftComponent(pieChartPanel);
    	splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}
	
	public void loadTable() {
    	
    	table.clearTable();
    	
    	ProfilerTree tree = presenter.getTree();
    	PiePlot plot = (PiePlot)chart.getPlot();
    	
    	TableTreeNode rootNode = table.getRoot();
    	ProfileData rootData = tree.getRoot();
    	
    	ProfileTreeVisitor visitor = new ProfileTreeVisitor(rootNode,plot);
    	createTreeVisitor(visitor,rootData);
        	
    	table.refreshTable(-1);
    	
    }

	public void setPresenter(ProfilerPresenterInterface presenter) {
		this.presenter = presenter;
	}

	public void mouseClicked(MouseEvent e) {
	
	
		if (e.getClickCount() == 2){ 
			int column=table.getSelectedColumn();
			int row=table.getSelectedRow();
			String completeName=(String)table.getValueAt(row, 2);
			presenter.doubleClick(completeName);
		}
	
	
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	
	public void mouseExited(MouseEvent e) {
			
	}

	
	public void mousePressed(MouseEvent e) {
		
		
	}

	
	public void mouseReleased(MouseEvent e) {
		
		
	}

}

