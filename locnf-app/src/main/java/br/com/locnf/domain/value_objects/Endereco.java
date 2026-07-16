package br.com.locnf.domain.value_objects;

import lombok.Value;

@Value
public class Endereco {

	private String logradouro;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;
	
}
