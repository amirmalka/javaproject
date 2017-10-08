package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class MMUView extends Observable implements View {
	// Data Section
	private List<MMUCommand> commands;
	private int numProcesses;
	private int ramCapacity;
	private int pageSize;
	private Integer currentLogRow = 2, currentTableCol = 0;
	private JFrame frame;
	private JTable processesTable;
	private JPanel statsPanel, rightWrapper, listPanel;
	private JScrollPane scrollPane;
	private JTextField pageReplacementField, pageFaultField;
	private Integer pageReplacementValue = 0, pageFaultValue = 0;
	private JButton playButton, playAllButton;
	private JList <String> processesList;
	private TableModel processesTableModel;
	private DefaultListModel<String> processesListModel;
	private Map<Integer, Integer> selectedProcesses;
	
	private JLabel pageReplacementLabel, pageFaultLabel;
	
	
	public void setNumProccesses(int numProccesses) {
		this.numProcesses = numProccesses;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}
	
	public MMUView() {
		selectedProcesses = new HashMap<>();
	}

	public void setCommands(List<String> data) {
		this.commands = new ArrayList<MMUCommand>();
		for (String line : data) {
			MMUCommand cmd = MMUCommand.parseCommand(line);
			if (cmd != null)
			{
				commands.add(cmd);	
			}
		}
		setPageSize();
	}
	
	private void setPageSize() {
		pageSize = 0;
		for (MMUCommand cmd: commands) {
			if (cmd.getType().equals(MMUCommand.TYPE_GET_PAGES)) {
				pageSize = cmd.getData().size();
				return;
			}
		}
	}
	
	@Override
	public void start() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	protected void createAndShowGUI() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createPageTable();
		frame.add(scrollPane, BorderLayout.CENTER);
		
		statsPanel = new JPanel();
		rightWrapper = new JPanel();
		
		rightWrapper.setLayout(new BorderLayout());
		rightWrapper.add(statsPanel, BorderLayout.NORTH);

		pageReplacementLabel = new JLabel("Page Replacement Count:");
		pageFaultLabel = new JLabel("Page Fault");

		playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});

		playAllButton = new JButton("Play All");
		playAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playAll();
			}
		});
		
		pageReplacementField = new JTextField();
		pageReplacementField.setColumns(10);
		pageFaultField = new JTextField();
		pageFaultField.setColumns(10);
		
		statsPanel.setPreferredSize(new Dimension(250,60));
		statsPanel.setLayout(new GridLayout(0,2));
		statsPanel.add(pageReplacementLabel);
		statsPanel.add(pageReplacementField);
		statsPanel.add(pageFaultLabel);
		statsPanel.add(pageFaultField);
		statsPanel.add(playAllButton);
		statsPanel.add(playButton);
		
		createProcessesList();
		resetInformation();
		frame.add(rightWrapper,  BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	private void resetInformation() {
		JTableHeader tableHeader = processesTable.getTableHeader();
		TableColumnModel tcm = tableHeader.getColumnModel();
		
		currentLogRow = 2;
		currentTableCol = 0;
		pageReplacementValue = 0;
		pageFaultValue = 0;
		pageReplacementField.setText("0");
		pageFaultField.setText("0");
		
		for (int col = 0; col < processesTableModel.getColumnCount(); col++) {
			for (int row = 0; row < processesTableModel.getRowCount(); row++) {
				processesTableModel.setValueAt(0, row, col);
			}
			
			TableColumn tableColumn = tcm.getColumn(col);
			tableColumn.setHeaderValue(0);
		}
		tableHeader.repaint();
	}
	
	private void createProcessesList() {
		selectedProcesses.clear();
		listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		processesListModel = new DefaultListModel<String>();
		
		for (int i = 1 ; i <= numProcesses ; i++) {
			processesListModel.addElement("Process " + i );
			selectedProcesses.put(i-1, i-1);
		}
		processesList = new JList<String>(processesListModel);
		processesList.addListSelectionListener( new ListSelectionListener() {
		
			@Override
			public void valueChanged(ListSelectionEvent e) {
		        JList<String> lsm = (JList<String>)e.getSource();
		        setSelectedProcesses(lsm.getSelectedIndices());
		     
		        resetInformation();
			}
			
		});
		rightWrapper.add(processesList, BorderLayout.SOUTH);

	}


	
	public void setSelectedProcesses(int[] selectedProcesses) {
		this.selectedProcesses.clear();
		for (int process : selectedProcesses) {
			this.selectedProcesses.put(process, process);
		}
	}

	private void play() {
		// read the next log command and change the user interface
		if (currentLogRow < commands.size()) {
			MMUCommand currentCommand = commands.get(currentLogRow);
			currentLogRow++;
			
			switch (currentCommand.getType()) {
				case MMUCommand.TYPE_GET_PAGES:
					if (selectedProcesses.containsKey(currentCommand.getProcessId()))
						pagesDataToTable(currentCommand);
					else {
						updateTableForUnselectedProcess();
					}
					break;
					
				case MMUCommand.TYPE_PAGE_FAULT:
					pageFaultValue++;
					pageFaultField.setText(pageFaultValue.toString());
					break;
					
				case MMUCommand.TYPE_PAGE_REPLACEMENT:
					pageReplacementValue++;
					pageReplacementField.setText(pageReplacementValue.toString());
					break;
			}
		}
	}
	
	private void pagesDataToTable(MMUCommand mmuCommand) {
		// update column header with page id
		JTableHeader tableHeader = processesTable.getTableHeader();
		TableColumnModel tcm = tableHeader.getColumnModel();
		TableColumn tableColumn = tcm.getColumn(currentTableCol);
		tableColumn.setHeaderValue(mmuCommand.getPageId().toString());
		tableHeader.repaint();

		int row = 0;
		// update page data into the table
		for (String data : mmuCommand.getData()) {
			processesTableModel.setValueAt(data, row, currentTableCol);
			row++;
		}

		if (processesTableModel.getColumnCount() - 1 == currentTableCol) {
			currentTableCol = 0;
		} else {
			currentTableCol++;
		}
	}
	
	private void updateTableForUnselectedProcess() {
		// update column header with page id
		JTableHeader tableHeader = processesTable.getTableHeader();
		TableColumnModel tcm = tableHeader.getColumnModel();
		TableColumn tableColumn = tcm.getColumn(currentTableCol);
		tableColumn.setHeaderValue("");
		tableHeader.repaint();

		// update page data into the table
		for (int row=0; row< pageSize; row++) {
			processesTableModel.setValueAt("", row, currentTableCol);
		}

		if (processesTableModel.getColumnCount() - 1 == currentTableCol) {
			currentTableCol = 0;
		} else {
			currentTableCol++;
		}
	}	
	
	private void playAll() {
		// read the next log command and change the user interface
		while (currentLogRow < commands.size()) {
			play();
		}
	}
	
	private void createPageTable() {
		processesTableModel = new DefaultTableModel(pageSize, ramCapacity);
		processesTable = new JTable(processesTableModel);
		processesTable.setVisible(true);
		scrollPane = new JScrollPane(processesTable);
		scrollPane.getViewport().setViewPosition(new Point(0, 0));
	}
}
