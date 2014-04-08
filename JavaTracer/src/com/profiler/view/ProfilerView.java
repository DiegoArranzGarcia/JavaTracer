package com.profiler.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.List;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

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

import com.alee.laf.label.WebLabel;
import com.general.model.FileUtilities;
import com.general.resources.ImageLoader;
import com.general.view.jtreetable.*;
import com.profiler.model.ProfilerTree;
import com.profiler.model.data.ProfileData;
import com.profiler.presenter.ProfilerPresenterInterface;

@SuppressWarnings("serial")
public class ProfilerView extends JFrame implements ChartProgressListener,ComponentListener,ProfilerViewInterface,
	ActionListener,MouseListener,TableModelListener {

	private static final String SETTINGS = "Settings";
	private static final String HELP = "Help";
	private static final String ABOUT = "About";
	private static final String NUM_CALLS = "Total calls: ";
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
	
	private static final String JPEG = "jpeg";
	private static final String XML = "xml";
	private static final String PNG = "png";
	
	public static final String OTHERS_CLASSES = "Others Classes";
	private static final String PIE_FONT = "Courier New";
	private static final String FONT_TITLE = "Arial";
	private static final int FONT_SIZE_TITLE = 26;
	private static final int FONT_SIZE_LABEL_PLOT = 16;
	
	private static String TITLE = "Profiling stats";
	private static double SPLIT_PERCENTAGE = 0.52;
	private static double PERCENTAGE_WIDTH = 0.75;
	private static double PERCENTAGE_HEIGHT = 0.75;
	private static final int NUM_CLASSES = 10;
	private static String HELP_SAVE_TOOLTIP ="Exclude useless methods/classes/packages the trace will be faster ";
	private static String HELP_DOUBLECLICK_TOOLTIP ="class's methods by double click in her";
	private static String HELP_EXCLUDES_TOOLTIP ="recalculate the chart but you must save for next trace";
	
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
	private JPanel panelRight;
	private JPanel panel;
	private JMenuItem mntmExit;
	private boolean tableLoaded;
	private JMenuItem mntmSettings;
	private JMenuItem mntmAbout;
	private WebLabel helpSave;
	private WebLabel helpDoubleClick;
    		    
	/**
	 * Creates the profile view. This view is not visible until the presenter (which must be set), make the
	 * view visible. The presenter of this view must implements {@link com.profiler.view.ProfilerViewInterface ProfilerViewInterface}.
	 * 
	 * @see ProfilerViewInterface
	 */
	
    public ProfilerView() {
        
		addWindowListener(new WindowAdapter() {
	    	public void windowClosing(WindowEvent e) {
	    		presenter.cancel();
	    	   }
		});
		
		setIconImage(ImageLoader.getInstance().getApplicationIcon().getImage());
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
        
        ImageLoader imageLoader = ImageLoader.getInstance();
        helpSave = new WebLabel();
		helpSave.setBounds(707, 62, 24, 24);
		helpSave.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpSave.setToolTipText(HELP_SAVE_TOOLTIP);

        helpDoubleClick= new WebLabel();
		helpDoubleClick.setBounds(707, 62, 24, 24);
		helpDoubleClick.setIcon(new ImageIcon(imageLoader.getHelpIcon().getImage().getScaledInstance(24,24,Image.SCALE_SMOOTH)));
		helpDoubleClick.setToolTipText(HELP_DOUBLECLICK_TOOLTIP);

		
        panel.add(helpSave);
		panel.add(btnSave);
        btnSave.addActionListener(this);        
        
        btnCancel = new JButton("Cancel");
        panel.add(btnCancel);
        
        JScrollPane scrollPane = new JScrollPane();
        panelRight.add(helpDoubleClick, BorderLayout.NORTH); 
        panelRight.add(scrollPane, BorderLayout.CENTER);
        

        
        
        
        table = new JTreeTable();
        table.setToolTipText(HELP_EXCLUDES_TOOLTIP);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ProfilerHeaderData headerData = new ProfilerHeaderData();
        table.setRoot(headerData);
        table.setModel(new DefaultTableModel(headerData.getValues(),0) {
        	Class<?>[] columnTypes = new Class[] {
        		Object.class, Object.class, Object.class, Object.class, Object.class,Boolean.class
        	};
        	boolean[] columnEditables = new boolean[] {
        		false, false, false, false, false, true
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
        table.setExpandbleColumn(2);       
        table.getModel().addTableModelListener(this);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(0).setMinWidth(25);
        table.getColumnModel().getColumn(0).setMaxWidth(25);
        table.getColumnModel().getColumn(1).setMaxWidth(25);
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
        
        mntmSettings = new JMenuItem(SETTINGS);
        mntmSettings.addActionListener(this);
        menuBar.add(mntmSettings);
        
        
        mntmAbout = new JMenuItem(ABOUT);
        mntmAbout.addActionListener(this);
        menuBar.add(mntmAbout);
    }

    /**
     *  ProfilerView implements action listener for all components that need it.
     */
    
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
		} else if (source.equals(mntmSettings)){
			clickedOnSettings();
		} else if (source.equals(mntmAbout)){
			clickedOnAbout();
		} else if (source.equals(btnCancel)){
			clickedOnCancel();
		}
	}
    
    /**
     * Action performed when clicked on About. 
     * The final result it's to show a AboutDialog. 
     * @see {@link com.general.presenter.JavaTracerPresenter#clickedOnAbout()}
     */
    
	private void clickedOnAbout() {
		presenter.clickedOnAbout();
	}
	
	 /**
     * Action performed when clicked on Help. 
     * The final result it's to show a HelpDialog. 
     * @see {@link com.general.presenter.JavaTracerPresenter#clickedOnHelp()}
     */
	
	
	 /**
     * Action performed when clicked on Settings. 
     * The final result it's to show the settings of application. 
     * @see {@link com.general.presenter.JavaTracerPresenter#clickedOnSettings()()}
     */
	
	private void clickedOnSettings() {
		presenter.clickedOnSettings();
	}

	 /**
     * Action performed when clicked on CheckAllClasses. 
     * This action checks all the excluded column values of the table.
     */
	
	private void clickedCheckAllClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(true,i,4);
		}
	}
	
	 /**
     * Action performed when clicked on UncheckAllClasses. 
     * This action unchecks all the excluded column values of the table.
     */
	
	private void clickedUncheckAllClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(false,i,4);
		}
	}
	
	 /**
     * Action performed when clicked on InvertCheckClasses. 
     * This action inverts all the excluded column values of the table.
     */
	
	private void clickedInvertClasses() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		for (int i=0;i<model.getRowCount();i++){
			model.setValueAt(!(boolean)model.getValueAt(i,4),i,4);
		}
	}

	 /**
     * Action performed when clicked on ExportAs. 
     * This action open a dialog for exporting the current pieplot 
     * to a jpeg or png image.
     */
	
	private void clickedExportAs() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(PNG_FILTER_FILES, PNG));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(JPEG_FILTER_FILES, JPEG));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle(EXPORT_AS);
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		//return directory file
		
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				
				String file_path = chooser.getSelectedFile().getCanonicalPath();
				File file = new File(file_path);
				if (FileUtilities.isExtension(file,PNG)){
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
    
	/**
	 * Action performed when clicked on Cancel.
	 * The behaviour is to hide the ProfilerView. 
	 */
	
    private void clickedOnCancel() {
		presenter.cancel();
	}
    
    /**
     * Action performed when clicked on Exit.
     * The behaviour is to hide the ProfilerView. 
     */
    
    private void clickedOnExit() {
		presenter.cancel();
	}

    /**
     * Action performed when clicked on Save.
     * All the information of the excluded classes are saved
     * in the settings for the next trace.
     */
    
	private void clickedOnSave() {
		presenter.save();
	}
	
	/**
	 * Action performed when clicked on open profile.
	 */
	
	public void clickedOpenProfile() {
		presenter.clickedOnOpenProfile(this);
	}
	
	private void clickedSaveProfile() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(XML_FILTER_FILES,XML));
		chooser.setAcceptAllFileFilterUsed(false);
		//Title window
		chooser.setDialogTitle(SAVE_PROFILE);
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		//return directory file
		
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (FileUtilities.isExtension(file,XML)){
				presenter.saveProfile(file);
			} else {
				try {
					presenter.saveProfile(new File(file.getCanonicalPath()+"."+XML));
				} catch (IOException e) {}
			}
		} else { 
			chooser.cancelSelection();
		}
	}

    // Component Listeners
    
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}

	/**
	 *  When the component it's resized, the columns widths are recalculated for a better look.
	 */
	
	public void componentResized(ComponentEvent e) {
		if (table.getWidth() < splitPane.getRightComponent().getWidth()){
			TableColumnModel columns = table.getColumnModel();
	        int adjustedSize =  (splitPane.getRightComponent().getWidth() - columns.getColumn(0).getWidth() - columns.getColumn(1).getWidth() 
	        		- columns.getColumn(4).getWidth() - columns.getColumn(5).getWidth())/2;
	        columns.getColumn(2).setPreferredWidth(adjustedSize);
	        columns.getColumn(3).setPreferredWidth(adjustedSize);
        }
	}

	/**
	 *  When the component it's shown, the columns widths are recalculated for a better look.
	 */
	
	public void componentShown(ComponentEvent e) {
        TableColumnModel columns = table.getColumnModel();
        int adjustedSize =  (splitPane.getRightComponent().getWidth() - columns.getColumn(0).getWidth() - columns.getColumn(1).getWidth() 
        		- columns.getColumn(4).getWidth() - columns.getColumn(5).getWidth())/2;
        columns.getColumn(2).setPreferredWidth(adjustedSize);
        columns.getColumn(3).setPreferredWidth(adjustedSize);
        
	}
	
	// Chart progress listener
	
	/**
	 * A JFreeChart is created with the input data.
	 * @param dataset - data to show in the chart.
	 * @return - JFreeChart
	 */
	
    private JFreeChart createChart(PieDataset dataset,int numCalls) {
    	
        JFreeChart chart = ChartFactory.createPieChart(
        	NUM_CALLS + numCalls,  // chart 
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
        t.setFont(new Font(FONT_TITLE, Font.BOLD,FONT_SIZE_TITLE ));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        // use gradients and white borders for the section colours
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        // customise the section label appearance
        plot.setLabelFont(new Font(PIE_FONT, Font.BOLD, FONT_SIZE_LABEL_PLOT ));
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
        
        DefaultPieDataset definitiveDataSet= chosenClasses(dataset); 
        
        return definitiveDataSet;
    }
	
	private void refreshChart() {
		
		HashMap<String,Integer> classes = new HashMap<>();
		TableTreeNode node = table.getRoot();
		
		for (int i=0;i<node.getChildCount();i++){
			getClasses(classes,node.getChildAt(i));
		}
		
		int calls = calculateTotalCalls(classes);
		
		((PiePlot)chart.getPlot()).setDataset(createDataset(classes,calls));
		chart.setTitle(NUM_CALLS + calls);
		
		
	}

	public void setTitle(String pathFile){
		super.setTitle(TITLE + " - " + pathFile);
	}
	
	private int calculateTotalCalls(HashMap<String, Integer> classes) {
		
		Iterator<Entry<String,Integer>> iterator = classes.entrySet().iterator();
		int numCalls = 0;
		
		while (iterator.hasNext()){
			Entry<String, Integer> next = iterator.next();
			numCalls += next.getValue();
		}
		
		return numCalls;
	}

	private void getClasses(HashMap<String, Integer> classes,TableTreeNode node) {
		
		ProfilerRowData data = (ProfilerRowData) node.getUserObject();
		
		switch (data.getType()) {
			case CLASS:
				countClass(classes,node);
				break;
			case PACKAGE:
				countPackagesClasses(classes,node);
				break;
			default:
				break;
		}
		
		
	}

	private void countPackagesClasses(HashMap<String, Integer> classes,TableTreeNode node) {
		
		ProfilerRowData data = (ProfilerRowData) node.getUserObject();
		
		if (!data.isExcluded()){
			for (int i=0;i<node.getChildCount();i++)
				getClasses(classes,node.getChildAt(i));
		}
		
	}

	private void countClass(HashMap<String, Integer> classes, TableTreeNode node) {
		
		ProfilerRowData data = (ProfilerRowData) node.getUserObject();
		
		if (!data.isExcluded()){
			int numCalls = 0;
			for (int i=0;i<node.getChildCount();i++){
				ProfilerRowData childData = ((ProfilerRowData)node.getChildAt(i).getUserObject());
				if (!childData.isExcluded())
					numCalls += childData.getCount(); 
			}
			
			if (numCalls>0)
				classes.put(data.getCompleteNameClass(),numCalls);
		
		}
	}

	public void chartProgress(ChartProgressEvent event) {
		if (event.getType() == ChartProgressEvent.DRAWING_FINISHED && !tableLoaded)
			loadTable();
	}
   
    @SuppressWarnings("unchecked")
	private DefaultPieDataset chosenClasses(DefaultPieDataset dataset) {
			
		dataset.sortByValues(SortOrder.DESCENDING);
        List<String> keys = dataset.getKeys();
		DefaultPieDataset definitivedataset = new DefaultPieDataset();
		
		int i=0;
		double percentage=0;
		
		while(i<keys.size() && i<NUM_CLASSES){
			definitivedataset.setValue(keys.get(i), dataset.getValue(keys.get(i)));	
			percentage = percentage + dataset.getValue(keys.get(i)).doubleValue();
			i++;	
		}
	
    	if(i<keys.size())
    		definitivedataset.setValue(OTHERS_CLASSES, 100-percentage);
    	
    	return definitivedataset;
	}

    
	/**
     * This panel is created when there is no data to show.
     * @return returns and empty panel.
     */
    
	private JPanel createNoLoadPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		return panel;
	}


	private JPanel createPiePanel(PieDataset data,int numCalls) {
        chart = createChart(data,numCalls);
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPopupMenu(null);
        chartPanel.setLayout(new GridLayout(1, 0, 0, 0));
        chart.addProgressListener(this);
        return chartPanel;
    }

	private void createTreeVisitor(ProfileTreeVisitor visitor,ProfileData data) {
		List<ProfileData> children = data.getChildren();
		for (int i=0;i<children.size();i++){
			children.get(i).accept(visitor);
		}
	}

	public HashMap<String,Boolean> getDataState() {
		HashMap<String, Boolean> classesState = new HashMap<>();
		TreeModel model = (TreeModel) table.getTreeModel();
		List<TableTreeNode> nodes = model.getRoot().getPreorder();
		for (int i=1;i<nodes.size();i++){
			ProfilerRowData row = (ProfilerRowData)nodes.get(i).getUserObject();
			classesState.put(row.getCompleteNameClass(),row.isExcluded());
		}
		
		return classesState;
	}

	public void load(HashMap<String, Integer> classes, int numCalledMethods) {
		table.clearTable();
		if (numCalledMethods > 0 )
			pieChartPanel = createPiePanel(createDataset(classes,numCalledMethods),numCalledMethods);
		else 
			pieChartPanel = createNoLoadPanel();
		
		
		tableLoaded = false;
    	splitPane.setLeftComponent(pieChartPanel);
    	splitPane.setDividerLocation(SPLIT_PERCENTAGE);
	}
	
	public void loadTable() {
    	    	
    	ProfilerTree tree = presenter.getTree();
    	PiePlot plot = (PiePlot)chart.getPlot();
    	
    	TableTreeNode rootNode = table.getRoot();
    	ProfileData rootData = tree.getRoot();
    	
    	ProfileTreeVisitor visitor = new ProfileTreeVisitor(rootNode,plot);
    	createTreeVisitor(visitor,rootData);
    	
    	table.refreshTable(-1);
    	tableLoaded = true;
    	
    }

	public void setPresenter(ProfilerPresenterInterface presenter) {
		this.presenter = presenter;
	}

	public void mouseClicked(MouseEvent e) {
	
		if (e.getClickCount() == 2){ 
			int row = table.getSelectedRow();
			String completeName=(String)table.getValueAt(row,3);
			presenter.doubleClick(completeName);
		}
	
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}	
	public void mouseReleased(MouseEvent e) {}

	public void tableChanged(TableModelEvent arg0) {
		
		if (arg0.getType() == TableModelEvent.UPDATE){
			updateProfileRowData(arg0.getFirstRow());
			refreshChart();
		}
			
	}

	private void updateProfileRowData(int row) {
		TableTreeNode node = table.getTreeModel().getNodeFromRow(row+1);
		((ProfilerRowData)node.getUserObject()).setExcluded((boolean) table.getModel().getValueAt(row, 5));
	}

}

