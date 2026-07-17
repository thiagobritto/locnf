package br.com.locnf.infra.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import br.com.locnf.infra.ui.commands.MainScreenActionCommand;
import br.com.locnf.infra.ui.views.models.NfeItemTableModel;
import br.com.locnf.infra.ui.views.models.NfeTableModel;
import br.com.locnf.infra.ui.views.models.TermListModel;

public class MainScreenFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPane;
	private JTextField txtTerm;
	private JButton btnAddTerm;
	private JList<String> termList;
	private JCheckBox cbxTermFilter;
	private JButton btnRemoveTerm;
	private JLabel lblResult;
	private JTextField txtPath;
	private JButton btnBrowser;
	private JTable tableNf;
	private JButton btnUpdate;
	private JButton btnExportNfCsv;
	private JButton btnExportNfXlsx;
	private JTable tableItems;
	private JTextField txtItemsSum;

	public MainScreenFrame() throws HeadlessException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880, 660);
		setLocationRelativeTo(null);
		setTitle("LocNF");
		
		ImageIcon icone = new ImageIcon(getClass().getResource("/br/com/locnf/infra/ui/assets/locnf_icon.png"));
		setIconImage(icone.getImage());
		
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel lAsidePanel = new JPanel(new GridLayout(1, 1));
		contentPane.add(lAsidePanel, BorderLayout.WEST);

		lAsidePanel.add(createTermsPanel());

		JPanel sectionPanel = new JPanel(new GridLayout(2, 1));
		contentPane.add(sectionPanel, BorderLayout.CENTER);

		sectionPanel.add(createNfePanel());
		sectionPanel.add(createItemsPanel());
	}

	private JPanel createTermsPanel() {
		JPanel termsPanel = new JPanel(new BorderLayout(5, 5));
		termsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
		termsPanel.add(headerPanel, BorderLayout.NORTH);

		JLabel lblTitel = new JLabel("Termos para filtro de produtos");
		lblTitel.setFont(lblTitel.getFont().deriveFont(16f));
		headerPanel.add(lblTitel, BorderLayout.NORTH);

		KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		txtTerm = new JTextField();
		txtTerm.getInputMap(JComponent.WHEN_FOCUSED).put(enterKey, "on_enter_term_text");
		txtTerm.getActionMap().put("on_enter_term_text", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnAddTerm.doClick();
			}
		});
		headerPanel.add(txtTerm, BorderLayout.CENTER);

		btnAddTerm = new JButton("Adicionar");
		btnAddTerm.setActionCommand(MainScreenActionCommand.ADD_TERM.name());
		headerPanel.add(btnAddTerm, BorderLayout.EAST);

		KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		termList = new JList<>(new TermListModel());
		termList.getInputMap(JComponent.WHEN_FOCUSED).put(deleteKey, "remove_term");
		termList.getActionMap().put("remove_term", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnRemoveTerm.isEnabled()) {
					btnRemoveTerm.doClick();
				}
			}
		});
		termsPanel.add(new JScrollPane(termList), BorderLayout.CENTER);

		JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
		termsPanel.add(footerPanel, BorderLayout.SOUTH);

		btnRemoveTerm = new JButton("Remover");
		btnRemoveTerm.setActionCommand(MainScreenActionCommand.REMOVE_TERM.name());
		footerPanel.add(btnRemoveTerm, BorderLayout.EAST);

		cbxTermFilter = new JCheckBox("Ativar filtro por termo");
		cbxTermFilter.setActionCommand(MainScreenActionCommand.TOGGLE_TERM_FILTER.name());
		footerPanel.add(cbxTermFilter, BorderLayout.WEST);

		return termsPanel;
	}

	private JPanel createNfePanel() {
		JPanel nfePanel = new JPanel(new BorderLayout(5, 5));
		nfePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
		nfePanel.add(headerPanel, BorderLayout.NORTH);

		JLabel lblTitel = new JLabel("Notas encontradas");
		lblTitel.setFont(lblTitel.getFont().deriveFont(16f));
		headerPanel.add(lblTitel, BorderLayout.NORTH);

		KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		txtPath = new JTextField();
		txtPath.getInputMap(JComponent.WHEN_FOCUSED).put(enterKey, "on_enter_path_text");
		txtPath.getActionMap().put("on_enter_path_text", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnUpdate.doClick();
			}
		});
		headerPanel.add(txtPath, BorderLayout.CENTER);

		btnBrowser = new JButton("Localizar ...");
		btnBrowser.setActionCommand(MainScreenActionCommand.BROWSE.name());
		headerPanel.add(btnBrowser, BorderLayout.EAST);

		tableNf = new JTable();
		tableNf.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableNf.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableNf.setRowSelectionAllowed(true);
		tableNf.setColumnSelectionAllowed(true);
		nfePanel.add(new JScrollPane(tableNf), BorderLayout.CENTER);
		
		updateNfeTable(new NfeTableModel());
		
		JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
		nfePanel.add(footerPanel, BorderLayout.SOUTH);

		String strLabelResult = "Resultados: ";
		lblResult = new JLabel("0") {
			@Override
			public void setText(String text) {
				super.setText(strLabelResult + text);
			}
		};
		footerPanel.add(lblResult, BorderLayout.WEST);

		JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		navPanel.setBorder(new EmptyBorder(0, -5, 0, -5));
		footerPanel.add(navPanel, BorderLayout.EAST);

		btnUpdate = new JButton("Atualizar");
		btnUpdate.setActionCommand(MainScreenActionCommand.UPDATE.name());
		navPanel.add(btnUpdate);

		btnExportNfCsv = new JButton("Exportar CSV");
		btnExportNfCsv.setActionCommand(MainScreenActionCommand.EXPORT_CSV.name());
		btnExportNfCsv.setVisible(false);
		navPanel.add(btnExportNfCsv);

		btnExportNfXlsx = new JButton("Exportar Excel");
		btnExportNfXlsx.setActionCommand(MainScreenActionCommand.EXPORT_EXCEL.name());
		navPanel.add(btnExportNfXlsx);

		return nfePanel;
	}

	private JPanel createItemsPanel() {
		JPanel itemsPanel = new JPanel(new BorderLayout(5, 5));
		itemsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
		itemsPanel.add(headerPanel, BorderLayout.NORTH);

		JLabel lblItems = new JLabel("Lista de produtos");
		lblItems.setFont(lblItems.getFont().deriveFont(16f));
		headerPanel.add(lblItems, BorderLayout.NORTH);

		tableItems = new JTable();
		tableItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		itemsPanel.add(new JScrollPane(tableItems), BorderLayout.CENTER);

		updateNfeItemTable(new NfeItemTableModel());
		
		JPanel footerPanel = new JPanel(new BorderLayout(5, 5));
		itemsPanel.add(footerPanel, BorderLayout.SOUTH);

		JLabel lblTip = new JLabel("** Segure 'Ctrl' e selecione os itens para somar.");
		lblTip.setFont(lblTip.getFont().deriveFont(Font.ITALIC));
		lblTip.setForeground(Color.GRAY);
		footerPanel.add(lblTip, BorderLayout.WEST);

		JLabel lblItemsSum = new JLabel("Total soma:");
		lblItemsSum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblItemsSum.setFont(lblItemsSum.getFont().deriveFont(Font.BOLD, 16));
		footerPanel.add(lblItemsSum, BorderLayout.CENTER);

		txtItemsSum = new JTextField(10);
		txtItemsSum.setHorizontalAlignment(SwingConstants.RIGHT);
		txtItemsSum.setFont(txtItemsSum.getFont().deriveFont(16f));
		footerPanel.add(txtItemsSum, BorderLayout.EAST);

		return itemsPanel;
	}

	public void addActionsListeners(ActionListener l) {
		btnAddTerm.addActionListener(l);
		btnRemoveTerm.addActionListener(l);
		btnBrowser.addActionListener(l);
		btnUpdate.addActionListener(l);
		btnExportNfCsv.addActionListener(l);
		btnExportNfXlsx.addActionListener(l);
		cbxTermFilter.addActionListener(l);

		termList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && termList.getSelectedIndex() != -1) {
				MainScreenActionCommand command = MainScreenActionCommand.SELECT_LIST_TERM;
				l.actionPerformed(new ActionEvent(termList, ActionEvent.ACTION_PERFORMED, command.name()));
			}
		});

		tableNf.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tableNf.getSelectedRow() != -1) {
				MainScreenActionCommand command = MainScreenActionCommand.SELECT_TABLE_NF;
				l.actionPerformed(new ActionEvent(tableNf, ActionEvent.ACTION_PERFORMED, command.name()));
			}
		});

		tableItems.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tableItems.getSelectedRow() != -1) {
				MainScreenActionCommand command = MainScreenActionCommand.SELECT_TABLE_ITEM;
				l.actionPerformed(new ActionEvent(tableItems, ActionEvent.ACTION_PERFORMED, command.name()));
			}
		});
	}

	public void setPath(String pathName) {
		txtPath.setText(pathName);
	}

	public String getPath() {
		return txtPath.getText().trim();
	}

	public void setNfeSize(String size) {
		lblResult.setText(size);
	}

	public void updateTermList(TermListModel termListModel) {
		termList.setModel(termListModel);
	}

	public void updateNfeTable(NfeTableModel nfeTableModel) {
		tableNf.setModel(nfeTableModel);
		
		int column = 0;
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(80); 
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(240);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(100);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(90);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(90);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(100);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(240);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(240);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(80);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(100);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(100);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(100);
	    tableNf.getColumnModel().getColumn(column++).setPreferredWidth(80);
	}

	public void setEnabledBtnExport(boolean b) {
		btnExportNfCsv.setEnabled(b);
		btnExportNfXlsx.setEnabled(b);
	}

	public void setEnabledBtnRemoveTerm(boolean b) {
		btnRemoveTerm.setEnabled(b);
	}

	public int getTableNfeSelectedRow() {
		return tableNf.getSelectedRow();
	}

	public void updateNfeItemTable(NfeItemTableModel nfeItemTableModel) {
		tableItems.setModel(nfeItemTableModel);
		
		int column = 0;
		tableItems.getColumnModel().getColumn(column++).setPreferredWidth(80); 
		tableItems.getColumnModel().getColumn(column++).setPreferredWidth(240); 
		tableItems.getColumnModel().getColumn(column++).setPreferredWidth(80); 
		tableItems.getColumnModel().getColumn(column++).setPreferredWidth(80); 
		tableItems.getColumnModel().getColumn(column++).setPreferredWidth(80); 
	}

	public int[] getTableNfeItemsSelectedRows() {
		return tableItems.getSelectedRows();
	}

	public void setItemsSelectedTotal(String total) {
		txtItemsSum.setText(total);
	}

	public String getTermText() {
		return txtTerm.getText().trim();
	}

	public int getListTermSelectedIndex() {
		return termList.getSelectedIndex();
	}

	public boolean isSelectedTermFilter() {
		return cbxTermFilter.isSelected();
	}

	public void setSelectedTermFilter(boolean b) {
		cbxTermFilter.setSelected(b);
	}

}
