package br.com.locnf.main;

import javax.swing.SwingUtilities;

import br.com.locnf.domain.repositories.AppSettingsRepository;
import br.com.locnf.domain.repositories.NfeReaderGateway;
import br.com.locnf.domain.repositories.TermRepository;
import br.com.locnf.domain.services.TermService;
import br.com.locnf.infra.config.PreferencesAppSettingsRepository;
import br.com.locnf.infra.database.config.ConnectionProvider;
import br.com.locnf.infra.database.config.JdbcExecutor;
import br.com.locnf.infra.database.config.sqlite.SqliteConnectionProvider;
import br.com.locnf.infra.database.config.sqlite.SqliteJdbcExecutor;
import br.com.locnf.infra.database.repositories.SqliteTermRepository;
import br.com.locnf.infra.integracoes.XmlNfeGateway;
import br.com.locnf.infra.integracoes.xml.utils.XmlNfeReader;
import br.com.locnf.infra.ui.config.ScreenManager;
import br.com.locnf.infra.ui.controllers.MainScreenController;
import br.com.locnf.infra.ui.themes.ThemeManager;
import br.com.locnf.infra.ui.views.MainScreenFrame;
import br.com.locnf.infra.ui.views.MainScreenMenuBar;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		
			AppSettingsRepository settings = new PreferencesAppSettingsRepository();
			ThemeManager.loadSavedTheme();
			
			XmlNfeReader xmlNfeReader = new XmlNfeReader();
			NfeReaderGateway nfeReaderGateway = new XmlNfeGateway(xmlNfeReader);
			
			ConnectionProvider connectionProvider = new SqliteConnectionProvider();
			JdbcExecutor jdbcExecutor = new SqliteJdbcExecutor(connectionProvider);
			
			TermRepository termRepository = new SqliteTermRepository(jdbcExecutor);
			TermService termService = new TermService(termRepository, nfeReaderGateway);
			
			ScreenManager screenManager = new ScreenManager();
			
			MainScreenFrame view = new MainScreenFrame();
			new MainScreenController(view, termService, settings);
			
			view.setJMenuBar(new MainScreenMenuBar(screenManager, view));
			view.setVisible(true);
		});		
	}

}
