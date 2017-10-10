package com.hit.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class StatsPanel extends JPanel {

	private static final long serialVersionUID = 4155565769736618463L;

	private JTextField pageReplacementField, pageFaultField;
	private JLabel pageReplacementLabel, pageFaultLabel, progressBarLabel;
	private JProgressBar progressBar;
	private Integer pageReplacementValue, pageFaultValue;

	public StatsPanel() {
		setPreferredSize(new Dimension(300, 200));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Statistics",
				TitledBorder.CENTER, TitledBorder.TOP, MMUView.getDefaultFontBold()));
		setLayout(new GridBagLayout());
		createAndAddControls();
		resetStats();
	}

	private void createAndAddControls() {
		pageReplacementLabel = new JLabel("Page Replacements");
		pageReplacementLabel.setFont(MMUView.getDefaultFontPlain());
		pageFaultLabel = new JLabel("Page Faults");
		pageFaultLabel.setFont(MMUView.getDefaultFontPlain());
		progressBarLabel = new JLabel("Command Progress:");
		progressBarLabel.setFont(MMUView.getDefaultFontPlain());
		progressBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pageReplacementField = new JTextField();

		pageReplacementField.setFont(MMUView.getDefaultFontPlain());
		pageReplacementField.setHorizontalAlignment(JTextField.CENTER);
		pageReplacementField.setPreferredSize(new Dimension(35, 25));
		pageFaultField = new JTextField();

		pageFaultField.setFont(MMUView.getDefaultFontPlain());
		pageFaultField.setHorizontalAlignment(JTextField.CENTER);
		pageFaultField.setPreferredSize(new Dimension(35, 25));
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setFont(MMUView.getDefaultFontBold());
		progressBar.setPreferredSize(new Dimension(70, 25));

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2, 2, 2, 2);
		add(pageReplacementLabel, c);
		
		c.gridwidth = 2;
		c.gridx = 2;
		add(pageReplacementField, c);
		
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		add(pageFaultLabel, c);
		
		c.gridwidth = 2;
		c.gridx = 2;
		add(pageFaultField, c);
		
		c.gridwidth = 4;
		c.gridy = 2;
		c.gridx = 0;
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(5, 2, 2, 2);
		add(progressBarLabel, c);
		
		c.gridy = 3;
		c.gridx = 0;
		add(progressBar, c);
	}

	public void resetStats() {
		pageReplacementValue = 0;
		pageFaultValue = 0;
		pageReplacementField.setText("0");
		pageFaultField.setText("0");
		progressBar.setValue(0);
	}

	public void incrementPageFaults() {
		pageFaultValue++;
		pageFaultField.setText(pageFaultValue.toString());
	}

	public void incrementPageReplacements() {
		pageReplacementValue++;
		pageReplacementField.setText(pageReplacementValue.toString());
	}

	public void updateProgressBar(int val) {
		if (val > 100)
			progressBar.setValue(100);
		else
			progressBar.setValue(val);
	}

}
