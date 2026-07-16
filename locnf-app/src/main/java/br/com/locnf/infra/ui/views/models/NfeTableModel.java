package br.com.locnf.infra.ui.views.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.locnf.domain.entities.Nfe;
import br.com.locnf.infra.ui.utils.MoneyFormat;

public class NfeTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private final String[] columns = new String[] { "NF", "Emissor", "Data de Emissão", "Total da Nota", "Total de Produtos",
			"Cliente CPF/CNPJ", "Cliente Nome", "Cliente Logradouro", "Cliente Casa", "Cliente Bairro",
			"Cliente Cidade", "Cliente Estado", "Cliente CEP" };
	
	private final List<Nfe> nfeList;

	public NfeTableModel() {
		this.nfeList = new ArrayList<>();
	}

	public NfeTableModel(List<Nfe> nfeList) {
		this.nfeList = nfeList;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Nfe nfe = nfeList.get(rowIndex);
		int index = 0;

		if (columnIndex == index++) {
			return nfe.getNumero();
		} else if (columnIndex == index++) {
			return nfe.getNomeEmitente();
		} else if (columnIndex == index++) {
			return nfe.getDateFormat();
		} else if (columnIndex == index++) {
			return MoneyFormat.format(nfe.getTotalNota());
		} else if (columnIndex == index++) {
			return MoneyFormat.format(nfe.getTotalProdutos());
		} else if (columnIndex == index++) {
			return nfe.getCpfCnpjDestinatario();
		} else if (columnIndex == index++) {
			return nfe.getNomeDestinatario();
		} else if (columnIndex == index++) {
			return nfe.getEnderecoDestinatario().getLogradouro();
		} else if (columnIndex == index++) {
			return nfe.getEnderecoDestinatario().getNumero();
		} else if (columnIndex == index++) {
			return nfe.getEnderecoDestinatario().getBairro();
		} else if (columnIndex == index++) {
			return nfe.getEnderecoDestinatario().getCidade();
		} else if (columnIndex == index++) {
			return nfe.getEnderecoDestinatario().getEstado();
		} else if (columnIndex == index++) {
			return nfe.getEnderecoDestinatario().getCep();
		} else {
			return null;
		}
	}

	@Override
	public int getRowCount() {
		return nfeList.size();
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
