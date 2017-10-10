package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class CacheTablePanel extends JPanel {

	private static final long serialVersionUID = -7316683710217925063L;
	private MMUView view;
	private JTable cacheTable;
	private HashMap<Integer, Long> colToPageId; 
	private HashMap<Long, Integer> pageIdToCol;
	private Integer currentCol;
	private JScrollPane scrollPane;

	public CacheTablePanel(MMUView view) {
		this.view = view;
		currentCol = 0;
		colToPageId = new HashMap<Integer, Long>();
		pageIdToCol = new HashMap<Long, Integer>(); 
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 200));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Cache Viewer",
				TitledBorder.CENTER, TitledBorder.TOP, MMUView.getDefaultFontBold()));

		createAndAddControls();
		resetTable();
	}

	private void createAndAddControls() {
		cacheTable = new JTable(view.getPageSize(), view.getRamCapacity());
		cacheTable.setFont(MMUView.getDefaultFontPlain());
		cacheTable.getTableHeader().setReorderingAllowed(false);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		cacheTable.setRowHeight(25);
		if (view.getRamCapacity() > 12)
			cacheTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableColumnModel tcm = cacheTable.getColumnModel();

		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setPreferredWidth(50);
			cacheTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		cacheTable.setVisible(true);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(cacheTable);
		scrollPane.setVisible(true);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void commandDataToTable(MMUCommand mmuCommand, boolean colVisible) {
		// update column header with page id
		JTableHeader tableHeader = cacheTable.getTableHeader();
		TableColumnModel tcm = tableHeader.getColumnModel();
		int thisPageColumn = pageIdToCol.get(mmuCommand.getPageId()); 
		TableColumn tableColumn = tcm.getColumn(thisPageColumn);
		if (colVisible)
			tableColumn.setHeaderValue(mmuCommand.getPageId().toString());
		else
			tableColumn.setHeaderValue(" ");
		tableHeader.repaint();

		int row = 0;
		// update page data into the table
		for (String data : mmuCommand.getData()) {
			if (colVisible)
				cacheTable.setValueAt(data, row, thisPageColumn);
			else
				cacheTable.setValueAt(" ", row, thisPageColumn);
			row++;
		}
	}

	public void resetTable() {
		JTableHeader tableHeader = cacheTable.getTableHeader();
		tableHeader.setFont(MMUView.getDefaultFontBold());
		TableColumnModel tcm = tableHeader.getColumnModel();

		view.setCurrentLogRow(2);
		currentCol = 0;
		colToPageId.clear();
		pageIdToCol.clear();

		for (int col = 0; col < cacheTable.getColumnCount(); col++) {
			for (int row = 0; row < cacheTable.getRowCount(); row++) {
				cacheTable.setValueAt(0, row, col);
			}

			TableColumn tableColumn = tcm.getColumn(col);
			tableColumn.setHeaderValue(" ");
		}
		tableHeader.repaint();
	}

	public void handlePageFault(MMUCommand currentCommand) {
		 colToPageId.put(currentCol, currentCommand.getPageId());
		 pageIdToCol.put(currentCommand.getPageId(), currentCol);
		 currentCol++;
	}

	public void handlePageReplacement(MMUCommand currentCommand) {
		int columnOfPageToHd = pageIdToCol.get(currentCommand.getMth());
		colToPageId.put(columnOfPageToHd, currentCommand.getMtr());
		pageIdToCol.put(currentCommand.getMtr(), columnOfPageToHd);
	}

}
