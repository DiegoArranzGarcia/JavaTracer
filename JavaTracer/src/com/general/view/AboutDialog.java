package com.general.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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
		setIconImage(ImageLoader.getInstance().getApplicationIcon().getImage());
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
		contentPanel.setLayout(null);
		{
			JLabel lblJavatracer = new JLabel("JavaTracer 1.0.0");
			lblJavatracer.setBounds(93, 27, 142, 24);
			lblJavatracer.setFont(new Font(FontLoader.getInstance().getFontName(), Font.BOLD, 18));
			contentPanel.add(lblJavatracer);
		}
		{
			JLabel lblNewLabel = new JLabel("Application developed by:");
			lblNewLabel.setBounds(64, 64, 200, 27);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel label = new JLabel("");
			label.setBounds(24, 116, 83, 78);
			contentPanel.add(label);
			label.setIcon(new ImageIcon(ImageLoader.getInstance().getApplicationIcon().getImage().getScaledInstance(70,70,Image.SCALE_SMOOTH)));
			
		}
		{
			JLabel lblSaskyaMosqueraLogroo = new JLabel("Saskya Mosquera Logro\u00F1o");
			lblSaskyaMosqueraLogroo.setBounds(113, 116, 184, 24);
			lblSaskyaMosqueraLogroo.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblSaskyaMosqueraLogroo);
		}
		{
			JLabel lblDiegoArranzGarca = new JLabel("Diego Arranz Garc\u00EDa");
			lblDiegoArranzGarca.setBounds(119, 142, 172, 21);
			lblDiegoArranzGarca.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblDiegoArranzGarca);
		}
		{
			JLabel lblJavierAlejosCastroviejo = new JLabel("Javier Alejos Castroviejo");
			lblJavierAlejosCastroviejo.setBounds(119, 165, 172, 24);
			lblJavierAlejosCastroviejo.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblJavierAlejosCastroviejo);
		}
		{
			JButton btnClose = new JButton("Close");
			btnClose.setBounds(103, 226, 123, 25);
			contentPanel.add(btnClose);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dialog.dispose();
				}
			});
		}
	}

}
