package br.com.locnf.domain.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NfeItem {

	private String code;
	private String description;
	private BigDecimal quantity;
	private BigDecimal price;
	private BigDecimal total;

}
