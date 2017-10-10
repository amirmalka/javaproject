package com.hit.controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.hit.driver.CLI;
import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.view.MMUView;
import com.hit.view.View;

public class MMUController implements Controller, Observer {
	private Model model;
	private View view;
	private String[] command;

	public MMUController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof CLI) {
			if (arg instanceof String && arg.equals(CLI.STOP)) {
				((MMUView) view).closeOpenFrame();
			} else {
				command = (String[]) arg;
				model.start(command);
			}
		} else if (o instanceof MMUModel) {
			MMUModel model = (MMUModel) o;
			MMUView mmuView = (MMUView) view;
			mmuView.closeOpenFrame();
			@SuppressWarnings("unchecked")
			List<String> data = (List<String>) arg;
			mmuView.setNumProccesses(model.numProcesses);
			mmuView.setRamCapacity(model.ramCapacity);
			mmuView.setCommands(data);
			view.start();
		}
	}

}
