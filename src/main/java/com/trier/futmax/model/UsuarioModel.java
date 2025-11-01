package com.trier.futmax.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBUSUARIO")

public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDUSUARIO")
    private Long cdUsuario;

    @Column(name = "NMUSUARIO")
    private String nmUsuario;

    @Column(name = "NMEMAIL", unique = true)
    private String nmEmail;

    @Column(name = "NMCPF", unique = true)
    private String nmCpf;

    @Column(name = "NMSENHA")
    @JsonIgnore
    private String nmSenha;

    @Column(name = "NMTELEFONE", unique = true)
    private String nmTelefone;

    @Column(name = "NMENDERECO")
    private String nmEndereco;

    @Column(name = "DSENDERECO")
    private String dsEndereco;

    @Column(name = "FLATIVO")
    private Boolean flAtivo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TBUSUARIOROLES",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roleModels = new HashSet<>();

}
