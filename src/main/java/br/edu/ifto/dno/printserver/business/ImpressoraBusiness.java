package br.edu.ifto.dno.printserver.business;

import br.edu.ifto.dno.printserver.dtos.ImpressoesUsuariosTotalMes;
import br.edu.ifto.dno.printserver.entities.Impressao;
import br.edu.ifto.dno.printserver.entities.Impressora;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.hibernate.criterion.Restrictions.between;
import static org.hibernate.criterion.Restrictions.eq;

/**
 * Created by tassio on 27/02/16.
 */
@Component
@Transactional
public class ImpressoraBusiness extends BusinessGeneric<ImpressoraDAO, Impressora> {

    @Autowired
    private ImpressoraDAO impressoraDAO;


}
