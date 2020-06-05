package br.com.dornelasit.barber.api.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dornelasit.barber.api.entity.FormaPagamentoEntity;
import br.com.dornelasit.barber.api.model.FormasPagamentoResource;
import br.com.dornelasit.barber.api.repositories.FormasPagamentoRepository;

@Service
public class FormasPagamentoService {

	@Autowired
	private FormasPagamentoRepository formasPagamentoRepository;
	
	public void manterFormaPagamento(FormasPagamentoResource formasPagamentoResource) {
		
		FormaPagamentoEntity formaPagamento = new FormaPagamentoEntity();
		formaPagamento.setDescricao(formasPagamentoResource.getDescricao());
		formaPagamento.setId_forma_pagamento(formasPagamentoResource.getId());
		formasPagamentoRepository.saveAndFlush(formaPagamento);
		
	}
	
	public List<FormasPagamentoResource> listarFormasPagamento() {
		
		final List<FormasPagamentoResource> formaPagamentoResource = new ArrayList<FormasPagamentoResource>();
		final List<FormaPagamentoEntity> formaPagamentoEntity = this.formasPagamentoRepository.findAll();
		
		if(formaPagamentoEntity != null) {
			for(int i = 0; i < formaPagamentoEntity.size(); i++) {
				FormasPagamentoResource objeto = new FormasPagamentoResource();
				objeto.setDescricao(formaPagamentoEntity.get(i).getDescricao());
				objeto.setId(formaPagamentoEntity.get(i).getId_forma_pagamento());
				formaPagamentoResource.add(objeto);
			}

		}
		return formaPagamentoResource;
	}
}
