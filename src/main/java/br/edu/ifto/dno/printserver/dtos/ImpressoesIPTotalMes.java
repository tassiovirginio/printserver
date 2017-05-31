package br.edu.ifto.dno.printserver.dtos;

import java.io.Serializable;

/**
 * Created by tassio on 14/09/16.
 */
public class ImpressoesIPTotalMes implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ip;

    private Integer qtd;

    public ImpressoesIPTotalMes(String ip, Integer qtd) {
        this.ip = ip;
        this.qtd = qtd;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

        ImpressoesIPTotalMes that = (ImpressoesIPTotalMes) o;

        return this.ip != null ? this.ip.equals(that.ip) : that.ip == null;

    }

    @Override
    public int hashCode() {
        return this.ip != null ? this.ip.hashCode() : 0;
    }
}
