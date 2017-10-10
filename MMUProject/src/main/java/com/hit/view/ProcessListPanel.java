package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ProcessListPanel extends JPanel {

	private static final long serialVersionUID = -7740088897713968620L;
	private MMUView view;
	private Map<Integer, Integer> selectedProcesses;
	private JList<String> processesList;
	private JScrollPane scrollPane;

	public ProcessListPanel(MMUView view) {
		this.view = view;
		createAndAddControls();
	}

	private void createAndAddControls() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 200));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Filter Process",
				TitledBorder.CENTER, TitledBorder.TOP, MMUView.getDefaultFontBold()));

		selectedProcesses = new HashMap<>();
		selectedProcesses.clear();
		DefaultListModel<String> processesListModel = new DefaultListModel<String>();

		for (int i = 1; i <= view.getNumProccesses(); i++) {
			processesListModel.addElement("Process " + i);
			selectedProcesses.put(i - 1, i - 1);
		}
		processesList = new JList<String>(processesListModel);

		processesList.setFont(MMUView.getDefaultFontPlain());
		processesList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setSelectedProcesses(processesList.getSelectedIndices());
				view.reset();
			}
		});

		processesList.setCellRenderer(new MyCellRenderer());
		processesList.setFixedCellHeight(25);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(processesList);
		scrollPane.setVisible(true);

		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void setSelectedProcesses(int[] selectedProcesses) {
		this.selectedProcesses.clear();
		for (int process : selectedProcesses) {
			this.selectedProcesses.put(process, process);
		}
	}

	public void resetProcessSelection() {
		processesList.clearSelection();
		for (int i = 0; i < view.getNumProccesses(); i++) {
			this.selectedProcesses.put(i, i);
		}
	}
	
	public boolean isProcessSelected(int processId) {
		return selectedProcesses.containsKey(processId);
	}

	/* Nested Class for List Cell Rendering */
	private class MyCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = -8997790759726017752L;

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (isSelected)
				this.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.black));
			if (index % 2 == 0)
				setBackground(new Color(109, 165, 255));
			else
				setBackground(new Color(145, 187, 255));

			setHorizontalAlignment(SwingConstants.CENTER);

			setOpaque(true);
			return this;
		}
	}

}
