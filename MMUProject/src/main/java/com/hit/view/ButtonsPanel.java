package com.hit.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ButtonsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -2072609237693894506L;
	private MMUView view;
	private JButton playButton, playAllButton, clearFiltersButton;
	private JSlider speedSlider;
	private JLabel speedValueLabel;

	public ButtonsPanel(MMUView view) {
		this.view = view;
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Action",
				TitledBorder.CENTER, TitledBorder.TOP, MMUView.getDefaultFontBold()));
		this.setPreferredSize(new Dimension(150, 200));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createAndAddControls();
	}
	
	private void createAndAddControls() {
		playButton = new JButton("Play");
		playButton.addActionListener(this);
		playButton.setFont(MMUView.getDefaultFontBold());
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		playAllButton = new JButton("Play All");
		playAllButton.setFont(MMUView.getDefaultFontBold());
		playAllButton.addActionListener(this);
		playAllButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clearFiltersButton = new JButton("Start Over");
		clearFiltersButton.setFont(MMUView.getDefaultFontBold());
		clearFiltersButton.addActionListener(this);
		clearFiltersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		speedValueLabel = new JLabel();
		speedValueLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		speedValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		setSpeedLabel();
		speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 2000, view.getPlayAllSpeedMillis());
		speedSlider.setPaintTicks(true);
		speedSlider.setAlignmentX(Component.CENTER_ALIGNMENT);

		speedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				view.setSpeed(speedSlider.getValue());
				if (!speedSlider.getValueIsAdjusting())
					setSpeedLabel();
			}
		});
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(playButton);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(playAllButton);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(clearFiltersButton);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(speedSlider);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(speedValueLabel);
	}

	private void setSpeedLabel() {
		speedValueLabel.setText("Speed: " + view.getPlayAllSpeedMillis() + " (Millis)");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == playButton)
			view.startPlay();
		else if (arg0.getSource() == playAllButton) {
			view.startPlayAll();
		} else if (arg0.getSource() == clearFiltersButton) {
			view.reset();
			view.clearSelectedProcesses();
		}
	}

	public void disablePlayButtons() {
		playButton.setEnabled(false);
		playAllButton.setEnabled(false);
	}

	public void enablePlayButtons() {
		playButton.setEnabled(true);
		playAllButton.setEnabled(true);
	}
}
