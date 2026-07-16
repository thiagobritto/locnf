package br.com.locnf.infra.ui.config;

import javax.swing.JFrame;

import br.com.locnf.infra.ui.controllers.ThemeScreenController;
import br.com.locnf.infra.ui.views.ThemeScreenDialog;

public class ScreenManager {
	
	public void openThemeDialog(JFrame parent) {
		ThemeScreenDialog dialog = new ThemeScreenDialog(parent);
		new ThemeScreenController(dialog);
		dialog.setVisible(true);
	}

}
