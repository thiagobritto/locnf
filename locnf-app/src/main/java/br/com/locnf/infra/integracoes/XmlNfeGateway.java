package br.com.locnf.infra.integracoes;

import java.io.File;

import br.com.locnf.domain.entities.Nfe;
import br.com.locnf.domain.repositories.NfeReaderGateway;
import br.com.locnf.infra.integracoes.xml.models.NfeXmlModel;
import br.com.locnf.infra.integracoes.xml.utils.XmlNfeReader;

public class XmlNfeGateway implements NfeReaderGateway {

	private final XmlNfeReader xmlNfeReader;

	public XmlNfeGateway(XmlNfeReader xmlNfeReader) {
		this.xmlNfeReader = xmlNfeReader;
	}

	@Override
	public Nfe readFile(File file) {
		NfeXmlModel xmlModel = xmlNfeReader.parse(file);
		return xmlModel.toDomainEntity();
	}

}
