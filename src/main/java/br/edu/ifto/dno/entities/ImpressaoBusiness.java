package br.edu.ifto.dno.entities;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;

/**
 * Created by tassio on 27/02/16.
 */
@Component
@Transactional
public class ImpressaoBusiness extends BusinessGeneric<ImpressaoDAO, Impressao> {

    @Autowired
    private ImpressaoDAO impressaoDAO;


}
