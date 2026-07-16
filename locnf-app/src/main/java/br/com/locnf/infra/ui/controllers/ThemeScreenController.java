package br.com.locnf.infra.ui.controllers;

import java.awt.event.ActionEvent;

import br.com.locnf.infra.ui.commands.ThemeScreenActionCommand;
import br.com.locnf.infra.ui.themes.Theme;
import br.com.locnf.infra.ui.themes.ThemeManager;
import br.com.locnf.infra.ui.views.ThemeScreenDialog;

public class ThemeScreenController {

	private final ThemeScreenDialog view;
	private static Theme currentTheme;

	public ThemeScreenController(ThemeScreenDialog view) {
		this.view = view;

		loadTheme();
		this.view.addActionsListeners(this::actionListener);
	}
	
	private void loadTheme() {
		Theme theme = (currentTheme == null) ? ThemeManager.getSavedTheme() : currentTheme;
		view.setSelectedTheme(theme);
	}

	private void actionListener(ActionEvent e) {
		ThemeScreenActionCommand command = ThemeScreenActionCommand.valueOf(e.getActionCommand());
		
		switch (command) {
		case APPLAY_THEME:
			applayTheme();
			break;
		case SAVE_THEME:
			saveTheme();
			break;
		default:
			System.out.println("Comando não reconhecido: " + command.name());
			break;
		}
	}

	private void applayTheme() {
		currentTheme = view.getSelectedTheme();
		ThemeManager.applayTheme(currentTheme);
	}

	private void saveTheme() {
		currentTheme = view.getSelectedTheme();		
		ThemeManager.changeTheme(currentTheme);
		view.dispose();
	}
	

}
