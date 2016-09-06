package br.edu.ifto.dno.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tassio on 27/02/16.
 */
@Entity
public class Impressao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String usuario;

    private String ipOrigem;

    private String impressora;

    private Integer copias;

    private Integer paginasTotal;

    private Integer paginasDocumento;

    private Date data;

    private String nomeArquivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPaginasDocumento() {
        return paginasDocumento;
    }

    public void setPaginasDocumento(Integer paginasDocumento) {
        this.paginasDocumento = paginasDocumento;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIpOrigem() {
        return ipOrigem;
    }

    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
    }

    public String getImpressora() {
        return impressora;
    }

    public void setImpressora(String impressora) {
        this.impressora = impressora;
    }


    public Integer getCopias() {
        return copias;
    }

    public void setCopias(Integer copias) {
        this.copias = copias;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getPaginasTotal() {
        return paginasTotal;
    }

    public void setPaginasTotal(Integer paginasTotal) {
        this.paginasTotal = paginasTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Impressao that = (Impressao) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
