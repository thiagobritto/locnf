package br.com.locnf.infra.ui.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.com.locnf.infra.ui.commands.ThemeScreenActionCommand;
import br.com.locnf.infra.ui.themes.Theme;

public class ThemeScreenDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<Theme> cboThemes;
	private JButton btnApplay;
	private JButton btnSave;

	public ThemeScreenDialog(Window owner) {
		super(owner, "LocNF - Temas", ModalityType.APPLICATION_MODAL);
		setSize(300, 200);
		setLocationRelativeTo(null);

		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel sectionPanel = new JPanel(new GridBagLayout());
		contentPane.add(sectionPanel, BorderLayout.CENTER);

		cboThemes = new JComboBox<Theme>(Theme.values());

		GridBagConstraints cboGbc = createGbc(0, 1);
		cboGbc.weightx = 1.0;
		cboGbc.fill = GridBagConstraints.HORIZONTAL;

		sectionPanel.add(new JLabel("Tema: "), createGbc(0, 0));
		sectionPanel.add(cboThemes, cboGbc);

		JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		contentPane.add(footerPanel, BorderLayout.SOUTH);

		btnApplay = new JButton("Aplicar");
		btnApplay.setActionCommand(ThemeScreenActionCommand.APPLAY_THEME.name());
		footerPanel.add(btnApplay);
		
		btnSave = new JButton("Salvar");
		btnSave.setActionCommand(ThemeScreenActionCommand.SAVE_THEME.name());
		footerPanel.add(btnSave);
		
		getRootPane().setDefaultButton(btnSave);
	}
	
	private GridBagConstraints createGbc(int row, int col) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridy = row;
		gbc.gridx = col;
		return gbc;
	}

	public void addActionsListeners(ActionListener l) {
		btnSave.addActionListener(l);
		btnApplay.addActionListener(l);
	}

	public Theme getSelectedTheme() {
		cboThemes.setSelectedItem(btnApplay);
		return cboThemes.getItemAt(cboThemes.getSelectedIndex());
	}
	
	public void setSelectedTheme(Theme theme) {
		cboThemes.setSelectedItem(theme);
	}

}
