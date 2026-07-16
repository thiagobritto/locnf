package br.com.locnf.infra.config;

import java.util.prefs.Preferences;

import br.com.locnf.domain.repositories.AppSettingsRepository;

public class PreferencesAppSettingsRepository implements AppSettingsRepository {

	private final Preferences prefs = Preferences.userNodeForPackage(PreferencesAppSettingsRepository.class);

	@Override
	public boolean isSelectedTermFilter() {
		return prefs.getBoolean("term_filter_checkbox", true);
	}

	@Override
	public void setSelectedTermFilter(boolean selected) {
		prefs.putBoolean("term_filter_checkbox", selected);
	}

	@Override
	public String getNfeUri() {
		return prefs.get("nfe_uri", "");
	}

	@Override
	public void setNfeUri(String uri) {
		prefs.put("nfe_uri", uri);
	}

}
