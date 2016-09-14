package br.edu.ifto.dno.printserver.dtos;

import java.io.Serializable;

/**
 * Created by tassio on 14/09/16.
 */
public class ImpressoesUsuariosTotalMes implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;

    private Integer qtd;

    public ImpressoesUsuariosTotalMes(String usuario, Integer qtd) {
        this.usuario = usuario;
        this.qtd = qtd;
    }

    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImpressoesUsuariosTotalMes that = (ImpressoesUsuariosTotalMes) o;

        return this.usuario != null ? this.usuario.equals(that.usuario) : that.usuario == null;

    }

    @Override
    public int hashCode() {
        return this.usuario != null ? this.usuario.hashCode() : 0;
    }
}
