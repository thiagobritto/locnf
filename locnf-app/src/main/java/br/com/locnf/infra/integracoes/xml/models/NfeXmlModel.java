package br.com.locnf.infra.integracoes.xml.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.locnf.domain.entities.Nfe;
import br.com.locnf.domain.entities.NfeItem;
import br.com.locnf.domain.value_objects.Endereco;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NfeXmlModel {

	private String chave;
	private String numero;
	private String serie;
	private OffsetDateTime data;
	private String nomeEmitente;
	private String cnpjEmitente;
	private BigDecimal totalNota;
	private BigDecimal totalProdutos;
	
	private String nomeDestinatario;
	private String cpfCnpjDestinatario;
	private String logradouroDestinatario;
	private String numeroCasaDestinatario;
	private String bairroDestinatario;
	private String cidadeDestinatario;
	private String estadoDestinatario;
	private String cepDestinatario;

	private List<NfeItemXmlModel> items = new ArrayList<>();

	public void addItem(NfeItemXmlModel item) {
		items.add(item);
	}

	public Nfe toDomainEntity() {
		List<NfeItem> nfeItems = items.stream().map(NfeItemXmlModel::toDomainEntity).collect(Collectors.toList());
		Endereco endereco = new Endereco(logradouroDestinatario, numeroCasaDestinatario, bairroDestinatario, cidadeDestinatario, estadoDestinatario, cepDestinatario);
		
		return new Nfe(chave, numero, serie, data, totalNota, totalProdutos, nomeEmitente, cnpjEmitente, nomeDestinatario, 
				cpfCnpjDestinatario, endereco, nfeItems);
	}

}
