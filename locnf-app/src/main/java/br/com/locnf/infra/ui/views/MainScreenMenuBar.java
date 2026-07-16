package br.com.locnf.infra.ui.views;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import br.com.locnf.infra.ui.config.ScreenManager;

public class MainScreenMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	private final ScreenManager screenManager;
	private final JFrame parent;

	public MainScreenMenuBar(ScreenManager screenManager, JFrame parent) {
		this.screenManager = screenManager;
		this.parent = parent;
		
		createSettingsMenu();
	}

	private void createSettingsMenu() {
		JMenu settingsMenu = new JMenu("Configurações");
		add(settingsMenu);
		
		JMenuItem btnChangeTheme = new JMenuItem("Tema");
		btnChangeTheme.addActionListener(e -> screenManager.openThemeDialog(parent));
		settingsMenu.add(btnChangeTheme);
		
		
	}

	
}
