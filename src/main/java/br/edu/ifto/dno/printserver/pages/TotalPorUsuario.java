package br.edu.ifto.dno.printserver.pages;

import br.edu.ifto.dno.printserver.business.ImpressaoBusiness;
import br.edu.ifto.dno.printserver.dtos.ImpressoesUsuariosTotalMes;
import br.edu.ifto.dno.printserver.entities.Impressao;
import br.edu.ifto.dno.printserver.pages.base.Base;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TotalPorUsuario extends Base {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private ImpressaoBusiness impressaoBusiness;

    public TotalPorUsuario(final PageParameters parameters) {
        super(parameters);

        List<ImpressoesUsuariosTotalMes> listaImpressao = impressaoBusiness.getImpressoesPorMesTodosUsuarios(LocalDate.now().getMonthValue(),LocalDate.now().getYear());
        ListView<ImpressoesUsuariosTotalMes> listView = new ListView<ImpressoesUsuariosTotalMes>("listaImpressao",listaImpressao) {
            @Override
            protected void populateItem(ListItem<ImpressoesUsuariosTotalMes> listItem) {
                ImpressoesUsuariosTotalMes impressao = listItem.getModelObject();
                listItem.add(new Label("usuario",impressao.getUsuario()));
                listItem.add(new Label("total",impressao.getQtd()));
            }
        };

        add(listView);

    }


}
