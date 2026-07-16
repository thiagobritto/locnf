package br.com.locnf.domain.entities;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.com.locnf.domain.value_objects.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Nfe {

	private String chave;
	private String numero;
	private String serie;
	private OffsetDateTime data;
	private BigDecimal totalNota;
	private BigDecimal totalProdutos;
	
	private String nomeEmitente;
	private String cnpjEmitente;
	
	private String nomeDestinatario;
	private String cpfCnpjDestinatario;
	private Endereco enderecoDestinatario;
	
	private List<NfeItem> products = new ArrayList<>();
	
	public void addProduct(NfeItem product) {
		this.products.add(product);
	}
	
	public String getDateFormat() {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	public String getDateTimeFormat() {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"));
	}
	
}
