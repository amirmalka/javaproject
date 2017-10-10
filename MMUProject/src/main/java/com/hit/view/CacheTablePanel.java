package com.hit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
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
	private Integer currentTableCol;
	private JScrollPane scrollPane;

	public CacheTablePanel(MMUView view) {
		this.view = view;
		currentTableCol = 0;
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

	public void commandDataToTable(MMUCommand mmuCommand) {
		// update column header with page id
		JTableHeader tableHeader = cacheTable.getTableHeader();
		TableColumnModel tcm = tableHeader.getColumnModel();
		TableColumn tableColumn = tcm.getColumn(currentTableCol);
		tableColumn.setHeaderValue(mmuCommand.getPageId().toString());
		tableHeader.repaint();

		int row = 0;
		// update page data into the table
		for (String data : mmuCommand.getData()) {
			cacheTable.setValueAt(data, row, currentTableCol);
			row++;
		}

		if (cacheTable.getColumnCount() - 1 == currentTableCol) {
			currentTableCol = 0;
		} else {
			currentTableCol++;
		}
	}

	public void updateTableForUnselectedProcess() {
		JTableHeader tableHeader = cacheTable.getTableHeader();
		TableColumnModel tcm = tableHeader.getColumnModel();
		TableColumn tableColumn = tcm.getColumn(currentTableCol);
		tableColumn.setHeaderValue("");
		tableHeader.repaint();

		// update page data into the table
		for (int row = 0; row < view.getPageSize(); row++) {
			cacheTable.setValueAt(" ", row, currentTableCol);
		}

		if (cacheTable.getColumnCount() - 1 == currentTableCol) {
			currentTableCol = 0;
		} else {
			currentTableCol++;
		}
	}

	public void resetTable() {
		JTableHeader tableHeader = cacheTable.getTableHeader();
		tableHeader.setFont(MMUView.getDefaultFontBold());
		TableColumnModel tcm = tableHeader.getColumnModel();

		view.setCurrentLogRow(2);
		currentTableCol = 0;

		for (int col = 0; col < cacheTable.getColumnCount(); col++) {
			for (int row = 0; row < cacheTable.getRowCount(); row++) {
				cacheTable.setValueAt(0, row, col);
			}

			TableColumn tableColumn = tcm.getColumn(col);
			tableColumn.setHeaderValue(" ");
		}
		tableHeader.repaint();
	}

}
