package br.edu.ifto.dno.printserver.business;

import br.edu.ifto.dno.printserver.entities.Impressao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.criterion.Restrictions.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by tassio on 27/02/16.
 */
@Component
@Transactional
public class ImpressaoBusiness extends BusinessGeneric<ImpressaoDAO, Impressao> {

    @Autowired
    private ImpressaoDAO impressaoDAO;

    public List<Impressao> getUltimasImpressoes(int qtd){
        return impressaoDAO.findByHQL(Order.desc("data"),0,qtd);
    }

    public List<Impressao> getImpressoesPorMes(int mes, int ano){
        LocalDate initial = LocalDate.of(ano, mes, 1);
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        return impressaoDAO.findByCriteriaReturnList(between("data",start, end));
    }

    public List<Impressao> getImpressoesPorMesUsuario(int mes, int ano, String usuario){
        LocalDate initial = LocalDate.of(ano, mes, 1);
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        List<Impressao>  lista = impressaoDAO.findByCriteriaReturnList(
                between("data",start, end)
                ,eq("usuario",usuario.trim())
        );
        return lista;
    }


}
