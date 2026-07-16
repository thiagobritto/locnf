package br.com.locnf.domain.repositories;

public interface AppSettingsRepository {

	boolean isSelectedTermFilter();

	void setSelectedTermFilter(boolean selected);

	String getNfeUri();

	void setNfeUri(String uri);

}
