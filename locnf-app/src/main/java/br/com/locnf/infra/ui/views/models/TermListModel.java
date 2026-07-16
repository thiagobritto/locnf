package br.com.locnf.infra.ui.views.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import br.com.locnf.domain.entities.Term;

public class TermListModel extends AbstractListModel<String> {

	private static final long serialVersionUID = 1L;
	private final List<Term> termList;
	
	public TermListModel() {
		this.termList = new ArrayList<>();
	}
	
	public TermListModel(List<Term> termList) {
		this.termList = termList;
	}

	@Override
	public int getSize() {
		return termList.size();
	}

	@Override
	public String getElementAt(int index) {
		return termList.get(index).getName();
	}

}
