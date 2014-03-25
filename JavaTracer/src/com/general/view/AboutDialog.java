package com.general.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



import com.general.resources.FontLoader;
import com.general.resources.ImageLoader;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {

	private AboutDialog dialog;
	
	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setResizable(false);
		dialog = this;
		
		setTitle("About");
		setModal(true);
		setBounds(100, 100, 335, 299);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10px"),
				ColumnSpec.decode("70px"),
				ColumnSpec.decode("10dlu"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC},
			new RowSpec[] {
				RowSpec.decode("10dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("16px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC}));
		{
			JLabel lblJavatracer = new JLabel("JavaTracer 1.0.0");
			lblJavatracer.setFont(new Font(FontLoader.getInstance().getFontName(), Font.BOLD, 18));
			contentPanel.add(lblJavatracer, "4, 2, center, top");
		}
		{
			JLabel lblNewLabel = new JLabel("Application developed by:");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, "4, 6, center, top");
		}
		{
			JLabel label = new JLabel("");
			contentPanel.add(label, "2, 8, 1, 3, center, default");
			label.setIcon(ImageLoader.getInstance().getHelpIcon());
		}
		{
			JLabel lblSaskyaMosqueraLogroo = new JLabel("Saskya Mosquera Logro\u00F1o");
			lblSaskyaMosqueraLogroo.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblSaskyaMosqueraLogroo, "4, 10");
		}
		{
			JLabel lblDiegoArranzGarca = new JLabel("Diego Arranz Garc\u00EDa");
			lblDiegoArranzGarca.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblDiegoArranzGarca, "4, 12");
		}
		{
			JLabel lblJavierAlejosCastroviejo = new JLabel("Javier Alejos Castroviejo");
			lblJavierAlejosCastroviejo.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblJavierAlejosCastroviejo, "4, 14");
		}
		{
			JButton btnClose = new JButton("Close");
			contentPanel.add(btnClose, "3, 18, 3, 1");
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dialog.dispose();
				}
			});
		}
	}

}
