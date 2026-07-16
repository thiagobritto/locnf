package br.com.locnf.infra.ui.views.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.locnf.domain.entities.NfeItem;
import br.com.locnf.infra.ui.utils.MoneyFormat;

public class NfeItemTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final String[] columns = new String[] { "Código", "Descrição", "Quantidade", "Preço", "Total" };
	private final List<NfeItem> nfeItemList;

	public NfeItemTableModel() {
		this.nfeItemList = new ArrayList<>();
	}

	public NfeItemTableModel(List<NfeItem> nfeItemList) {
		this.nfeItemList = nfeItemList;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		NfeItem item = nfeItemList.get(rowIndex);
		int index = 0;

		if (columnIndex == index++) {
			return item.getCode();
		} else if (columnIndex == index++) {
			return item.getDescription();
		} else if (columnIndex == index++) {
			return item.getQuantity();
		} else if (columnIndex == index++) {
			return MoneyFormat.format(item.getPrice());
		} else if (columnIndex == index++) {
			return MoneyFormat.format(item.getTotal());
		} else {
			return null;
		}
	}

	@Override
	public int getRowCount() {
		return nfeItemList.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

}
