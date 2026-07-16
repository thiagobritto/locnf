package br.com.locnf.domain.repositories;

import java.io.File;

import br.com.locnf.domain.entities.Nfe;

public interface NfeReaderGateway {

	Nfe readFile(File file);
	
}
