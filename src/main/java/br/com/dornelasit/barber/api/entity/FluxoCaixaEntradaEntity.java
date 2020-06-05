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
@Table(name="fluxo_caixa_entrada")
public class FluxoCaixaEntradaEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_fluxo_caixa_entrada;
	
	private Double valor;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_entrada")
	private Calendar dataEntrada;
	
	private Time horario;
	
	@ManyToOne
	@JoinColumn(name="id_estabelecimento")
	private EstabelecimentoEntity estabelecimento;
	
	@ManyToOne
	@JoinColumn(name="id_agenda")
	private AgendaEntity agenda;

}
