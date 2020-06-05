package br.com.dornelasit.barber.api.entity;

import java.io.Serializable;

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
import java.sql.Time;
import java.util.Calendar;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name="horario_funcionamento")
public class HorarioFuncionamentoEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_horario_funcionamento;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dia;
		
	private Boolean flagDisponivel;
	
	private Time horario;
	
	@ManyToOne
	@JoinColumn(name="id_estabelecimento")
	private EstabelecimentoEntity estabelecimento;
	
	@ManyToOne
	@JoinColumn(name="id_profissional")
	private ProfissionalEntity profissional;
}
