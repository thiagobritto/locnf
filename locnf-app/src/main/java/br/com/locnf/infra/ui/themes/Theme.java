package br.com.locnf.infra.ui.themes;

public enum Theme {

	LIGHT("Claro"),
	DARK("Escuro"),
	INTELLIJ("IntelliJ"),
	DARCULA("Darcula");
	
	private String label;
	private Theme(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return label;
	}
	
}
