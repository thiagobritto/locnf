package br.com.locnf.domain.repositories;

import java.util.List;

import br.com.locnf.domain.entities.Term;

public interface TermRepository {

	void save(Term term);
	void delete(Long id);
	Term findById(Long id);
	Term findByName(String termName);
	List<Term> findAll();
	
}
