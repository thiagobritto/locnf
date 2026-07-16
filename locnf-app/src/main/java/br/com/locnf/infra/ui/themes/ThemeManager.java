package br.com.locnf.infra.ui.themes;

import java.util.prefs.Preferences;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class ThemeManager {

	private static final String THEME_KEY = "current_theme";
	private static final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);

	public static void changeTheme(Theme theme) {
		applayTheme(theme);
		prefs.put(THEME_KEY, theme.name());
	}

	public static void applayTheme(Theme theme) {
		try {

			switch (theme) {
			case DARK:
				UIManager.setLookAndFeel(new FlatDarkLaf());
				break;
			case DARCULA:
				UIManager.setLookAndFeel(new FlatDarculaLaf());
				break;
			case INTELLIJ:
				UIManager.setLookAndFeel(new FlatIntelliJLaf());
				break;
			default:
				UIManager.setLookAndFeel(new FlatLightLaf());
				break;
			}

			FlatLaf.updateUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadSavedTheme() {
		switch (getSavedTheme()) {
		case DARK:
			FlatDarkLaf.setup();
			break;
		case DARCULA:
			FlatDarculaLaf.setup();
			break;
		case INTELLIJ:
			FlatIntelliJLaf.setup();
			break;
		default:
			FlatLightLaf.setup();
			break;
		}
	}

	public static Theme getSavedTheme() {
		String themeName = prefs.get(THEME_KEY, Theme.LIGHT.name());
		return themeFromName(themeName);
	}

	private static Theme themeFromName(String themeName) {
		try {
			return Theme.valueOf(themeName);
		} catch (IllegalArgumentException e) {
			return Theme.LIGHT;
		}
	}

}
