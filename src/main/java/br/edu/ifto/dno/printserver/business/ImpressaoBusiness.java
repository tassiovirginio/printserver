package br.edu.ifto.dno.printserver.business;

import br.edu.ifto.dno.printserver.dtos.ImpressoesUsuariosTotalMes;
import br.edu.ifto.dno.printserver.entities.Impressao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.criterion.Restrictions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by tassio on 27/02/16.
 */
@Component
@Transactional
public class ImpressaoBusiness extends BusinessGeneric<ImpressaoDAO, Impressao> {

    @Autowired
    private ImpressaoDAO impressaoDAO;

    public List<Impressao> getUltimasImpressoes(int qtd) {
        return impressaoDAO.findByHQL(Order.desc("data"), 0, qtd);
    }

    public List<Impressao> getImpressoesPorMes(int mes, int ano) {
        LocalDate initial = LocalDate.of(ano, mes, 1);
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        return impressaoDAO.findByCriteriaReturnList(between("data", start, end));
    }

    public List<Impressao> getImpressoesPorMesUsuario(int mes, int ano, String usuario) {
        LocalDate initial = LocalDate.of(ano, mes, 1);
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(initial.lengthOfMonth());
        List<Impressao> lista = impressaoDAO.findByCriteriaReturnList(
                between("data", start, end)
                , eq("usuario", usuario.trim())
        );
        return lista;
    }


    public List<ImpressoesUsuariosTotalMes> getImpressoesPorMesTodosUsuarios(int mes, int ano) {
        LocalDate initial = LocalDate.of(ano, mes, 1);

        LocalDate startLocalDate = initial.withDayOfMonth(1);
        java.util.Date start = java.sql.Date.valueOf(startLocalDate);

        LocalDate endLocalDate = initial.withDayOfMonth(initial.lengthOfMonth());
        java.util.Date end = java.sql.Date.valueOf(endLocalDate);

        List<Impressao> lista = impressaoDAO.findByCriteriaReturnList(
                between("data", start, end)
        );

        Map<String, Integer> listaUsuarios = new HashMap<String, Integer>();
        List<ImpressoesUsuariosTotalMes> impressoesUsuariosTotalMes = new ArrayList<ImpressoesUsuariosTotalMes>();

        for (Impressao i : lista) {
            if (listaUsuarios.containsKey(i.getUsuario())) {
                int valor = listaUsuarios.get(i.getUsuario());
                listaUsuarios.put(i.getUsuario(), i.getPaginasTotal() + valor);
            } else {
                listaUsuarios.put(i.getUsuario(), i.getPaginasTotal());
            }
        }

        Iterator iteTotalUsuarios = listaUsuarios.entrySet().iterator();

        while (iteTotalUsuarios.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry) iteTotalUsuarios.next();
            impressoesUsuariosTotalMes.add(new ImpressoesUsuariosTotalMes(pair.getKey(),pair.getValue()));
            iteTotalUsuarios.remove();
        }


        return impressoesUsuariosTotalMes;
    }

}
