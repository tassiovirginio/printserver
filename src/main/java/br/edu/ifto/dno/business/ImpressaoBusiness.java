package br.edu.ifto.dno.business;

import br.edu.ifto.dno.entities.Impressao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tassio on 27/02/16.
 */
@Component
@Transactional
public class ImpressaoBusiness extends BusinessGeneric<ImpressaoDAO, Impressao> {

    @Autowired
    private ImpressaoDAO impressaoDAO;


}
