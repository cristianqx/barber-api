package br.com.dornelasit.barber.api.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name="usuario")
public class UsuarioEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="id_tipo_usuario")
	private TipoUsuarioEntity tipo_usuario;
	
	private String email;
	private String senha;
	private String nome;
	private String apelido;
	private String celular;
	private Boolean flagAtivo;
	
	@ManyToOne
	@JoinColumn(name="id_estabelecimento")
	private EstabelecimentoEntity estabelecimento_preferencia;

}
