package br.com.dishup.administrador.bean;

public class ProposalItemBean {
	
	private int id;
	private String CNPJ;
	private String razaoSocial;
	private String dataInclusao;
	private String status;
	
	public ProposalItemBean(int id, String cNPJ, String razaoSocial,
			String dataInclusao, String status) {
		this.id = id;
		CNPJ = cNPJ;
		this.razaoSocial = razaoSocial;
		this.dataInclusao = dataInclusao;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getCNPJ() {
		return CNPJ;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getDataInclusao() {
		return dataInclusao;
	}

	public String getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return "ID("+id+") CNPJ("+CNPJ+") RAZAO SOCIAL("+razaoSocial+") DATA INCLUSAO("+dataInclusao+") STATUS("+status+")";
	}
}
