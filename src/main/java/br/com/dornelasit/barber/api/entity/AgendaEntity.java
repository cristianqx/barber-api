package br.com.dornelasit.barber.api.entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name="agenda")
public class AgendaEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_agenda;
	
	private String observacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_agendada")
	private Calendar dataAgendada;
	
	private Time horaAgendada;
	
	private Time horaFinalizada;
		
	@ManyToOne
	@JoinColumn(name="id_estabelecimento")
	private EstabelecimentoEntity estabelecimento;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private UsuarioEntity usuario;
	
	@ManyToOne
	@JoinColumn(name="id_servico")
	private ServicoEntity servico;
	
	@ManyToOne
	@JoinColumn(name="id_status")
	private StatusAgendamentoEntity status_agendamento;
	
	@ManyToOne
	@JoinColumn(name="id_forma_pagamento")
	private FormaPagamentoEntity forma_pagamento;
		
	@ManyToOne
	@JoinColumn(name="id_profissional")
	private ProfissionalEntity profissional;
		
}
