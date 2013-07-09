package br.com.dishup.administrador.bean;

public class ProposalBean {
	
	private int id;
	private String dataInclusao;
	private String status;
	private String usuarioAprovador;
	private String dataAprovacao;
	private String emailRestaurante;
	private String CNPJ;
	private String razaoSocial;
	private String CEP;
	private String logradouro;
	private String numeroEndereco;
	private String complementoEndereco;
	private String bairro;
	private String cidade;
	private String estado;
	private String pais;
	private String DDDtelefoneRestaurante;
	private String telefoneRestaurante;
	private String siteRestaurante;
	private String tipoCulinaria;
	private String CPFresponsavel;
	private String nomeResponsavel;
	private String cargoResponsavel;
	private String DDDtelefoneResponsavel;
	private String telefoneResponsavel;
	
	public ProposalBean(int id, String dataInclusao, String status,
			String usuarioAprovador, String dataAprovacao,
			String emailRestaurante, String cNPJ, String razaoSocial,
			String cEP, String logradouro, String numeroEndereco,
			String complementoEndereco, String bairro, String cidade,
			String estado, String pais, String dDDtelefoneRestaurante,
			String telefoneRestaurante, String siteRestaurante,
			String tipoCulinaria, String cPFresponsavel,
			String nomeResponsavel, String cargoResponsavel,
			String dDDtelefoneResponsavel, String telefoneResponsavel) {
		this.id = id;
		this.dataInclusao = dataInclusao;
		this.status = status;
		this.usuarioAprovador = usuarioAprovador;
		this.dataAprovacao = dataAprovacao;
		this.emailRestaurante = emailRestaurante;
		CNPJ = cNPJ;
		this.razaoSocial = razaoSocial;
		CEP = cEP;
		this.logradouro = logradouro;
		this.numeroEndereco = numeroEndereco;
		this.complementoEndereco = complementoEndereco;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.pais = pais;
		DDDtelefoneRestaurante = dDDtelefoneRestaurante;
		this.telefoneRestaurante = telefoneRestaurante;
		this.siteRestaurante = siteRestaurante;
		this.tipoCulinaria = tipoCulinaria;
		CPFresponsavel = cPFresponsavel;
		this.nomeResponsavel = nomeResponsavel;
		this.cargoResponsavel = cargoResponsavel;
		DDDtelefoneResponsavel = dDDtelefoneResponsavel;
		this.telefoneResponsavel = telefoneResponsavel;
	}

	public int getId() {
		return id;
	}

	public String getDataInclusao() {
		return dataInclusao;
	}

	public String getStatus() {
		return status;
	}

	public String getUsuarioAprovador() {
		return usuarioAprovador;
	}

	public String getDataAprovacao() {
		return dataAprovacao;
	}

	public String getEmailRestaurante() {
		return emailRestaurante;
	}

	public String getCNPJ() {
		return CNPJ;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getCEP() {
		return CEP;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getNumeroEndereco() {
		return numeroEndereco;
	}

	public String getComplementoEndereco() {
		return complementoEndereco;
	}

	public String getBairro() {
		return bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public String getEstado() {
		return estado;
	}

	public String getPais() {
		return pais;
	}

	public String getDDDtelefoneRestaurante() {
		return DDDtelefoneRestaurante;
	}

	public String getTelefoneRestaurante() {
		return telefoneRestaurante;
	}

	public String getSiteRestaurante() {
		return siteRestaurante;
	}

	public String getTipoCulinaria() {
		return tipoCulinaria;
	}

	public String getCPFresponsavel() {
		return CPFresponsavel;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public String getCargoResponsavel() {
		return cargoResponsavel;
	}

	public String getDDDtelefoneResponsavel() {
		return DDDtelefoneResponsavel;
	}

	public String getTelefoneResponsavel() {
		return telefoneResponsavel;
	}

	@Override
	public String toString() {
		return 
				"PROPOSTA: ID("+id+") STATUS("+status+") DATA INCLUSAO("+dataInclusao+") \n" +
				"APROVACAO: USUARIO("+usuarioAprovador+") DATA("+dataAprovacao+") \n" +
				"RESTAURANTE: EMAIL("+emailRestaurante+") CNPJ("+CNPJ+") RAZAO SOCIAL("+razaoSocial+") \n" +
				"ENDERECO RESTAURANTE: CEP("+CEP+") LOGRADOURO("+logradouro+") NUMERO("+numeroEndereco+") COMPLEMENTO("+complementoEndereco+") " +
						"BAIRRO("+bairro+") CIDADE("+cidade+") ESTADO("+estado+") PAIS("+pais+") \n" +
				"CONTATO RESTAURANTE: DDD("+DDDtelefoneRestaurante+") TELEFONE("+telefoneRestaurante+") " +
						"SITE("+siteRestaurante+") TIPO CULINARIA("+tipoCulinaria+") \n" +
				"RESPONSAVEL: CPF("+CPFresponsavel+") NOME("+nomeResponsavel+") CARGO("+cargoResponsavel+") " +
						"DDD("+DDDtelefoneResponsavel+") TELEFONE("+telefoneResponsavel+")";
	}
}
