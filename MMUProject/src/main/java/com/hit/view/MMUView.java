package com.hit.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.JFrame;

public class MMUView extends Observable implements View {
	/* Panels */
	private CacheTablePanel cacheTablePanel;
	private StatsPanel statsPanel;
	private ProcessListPanel processListPanel;
	private ButtonsPanel buttonsPanel;
	private JFrame frame;
	
	/* Data */
	private List<MMUCommand> commands;
	private int numProcesses;
	private int ramCapacity;
	private int pageSize;
	
	
	private Integer currentLogRow = 2;
	private volatile boolean shouldPlayThreadStop = false;
	private Thread playAllThread;
	private int playAllSpeedMillis = 1000;

	public MMUView() {

	}
	
	@Override
	public void start() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/* Getters & Setters */
	public int getPlayAllSpeedMillis() {
		return playAllSpeedMillis;
	}

	public void setNumProccesses(int numProccesses) {
		this.numProcesses = numProccesses;
	}

	public void setRamCapacity(int ramCapacity) {
		this.ramCapacity = ramCapacity;
	}

	public int getNumProccesses() {
		return this.numProcesses;
	}

	public int getRamCapacity() {
		return this.ramCapacity;
	}

	public void setCommands(List<String> data) {
		this.commands = new ArrayList<MMUCommand>();
		for (String line : data) {
			MMUCommand cmd = MMUCommand.parseCommand(line);
			if (cmd != null) {
				commands.add(cmd);
			}
		}
		setPageSize();
	}

	public List<MMUCommand> getCommands() {
		return commands;
	}

	private void setPageSize() {
		pageSize = 0;
		for (MMUCommand cmd : commands) {
			if (cmd.getType().equals(MMUCommand.TYPE_GET_PAGES)) {
				pageSize = cmd.getData().size();
				return;
			}
		}
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setSpeed(int speed) {
		playAllSpeedMillis = speed;
	}
	
	
	public Integer getCurrentLogRow() {
		return currentLogRow;
	}

	public void setCurrentLogRow(Integer newValue) {
		this.currentLogRow = newValue;
	}

	private void createAndShowGUI() {
		// try {

		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		// UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		// } catch (ClassNotFoundException | InstantiationException |
		// IllegalAccessException
		// | UnsupportedLookAndFeelException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		frame = new JFrame();
		frame.setPreferredSize(new Dimension(758, 542));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("MMU Simulator - Copyrights Amir Malka & Ran Yagil");
		frame.setLayout(new GridBagLayout());
		processListPanel = new ProcessListPanel(this);
		cacheTablePanel = new CacheTablePanel(this);
		statsPanel = new StatsPanel();
		buttonsPanel = new ButtonsPanel(this);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(20, 20, 20, 20);
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		frame.getContentPane().add(cacheTablePanel, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		frame.getContentPane().add(statsPanel, c);
		c.gridx = 1;
		c.gridy = 1;

		frame.getContentPane().add(buttonsPanel, c);
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		frame.getContentPane().add(processListPanel, c);

		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void closeOpenFrame() {
		if (frame != null)
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	public void startPlay() {

		// read the next log command and change the user interface
		if (currentLogRow < commands.size()) {
			MMUCommand currentCommand = commands.get(currentLogRow);

			Double precentageOfProgress = new Double((double) (currentLogRow - 1) / (double) (commands.size() - 2) * 100);
			statsPanel.updateProgressBar(precentageOfProgress.intValue());

			++currentLogRow;

			switch (currentCommand.getType()) {
			case MMUCommand.TYPE_GET_PAGES:
				if (processListPanel.isProcessSelected(currentCommand.getProcessId()))
					cacheTablePanel.commandDataToTable(currentCommand);
				else {
					cacheTablePanel.updateTableForUnselectedProcess();
				}
				break;

			case MMUCommand.TYPE_PAGE_FAULT:
				statsPanel.incrementPageFaults();
				break;

			case MMUCommand.TYPE_PAGE_REPLACEMENT:
				statsPanel.incrementPageReplacements();
				
				break;
			}
		}

		if (currentLogRow + 1 == commands.size()) {
			buttonsPanel.disablePlayButtons();
		}
	}

	public void startPlayAll() {
		buttonsPanel.disablePlayButtons();
		ClickSimulator c = new ClickSimulator();
		playAllThread = new Thread(c);
		playAllThread.start();
	}
	
	public void reset() {
		// Stop 'Play All' thread
		while (playAllThread != null && playAllThread.isAlive())
			shouldPlayThreadStop = true;
		shouldPlayThreadStop = false;
		
		cacheTablePanel.resetTable();
		statsPanel.resetStats();
		buttonsPanel.enablePlayButtons();
	}

	public void clearSelectedProcesses() {
		processListPanel.resetProcessSelection();
	}

	public static Font getDefaultFontPlain() {
		return new Font("Arial", Font.PLAIN, 15);
	}

	public static Font getDefaultFontBold() {
		return new Font("Arial", Font.BOLD, 15);
	}
	
	/* Nested Class to simulate "Play" clicks with interval */
	public class ClickSimulator implements Runnable {
		public ClickSimulator() {

		}

		public void run() {
			while (currentLogRow < commands.size() && !shouldPlayThreadStop) {
				startPlay();
				try {
					Thread.sleep(playAllSpeedMillis);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
