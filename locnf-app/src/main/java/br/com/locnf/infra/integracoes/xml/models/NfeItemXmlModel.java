package br.com.locnf.infra.integracoes.xml.models;

import java.math.BigDecimal;

import br.com.locnf.domain.entities.NfeItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NfeItemXmlModel {

	private String code;
	private String description;
	private BigDecimal quantity;
	private BigDecimal price;
	private BigDecimal total;
	
	public NfeItem toDomainEntity() {
		return new NfeItem(code, description, quantity, price, total);
	}
}
