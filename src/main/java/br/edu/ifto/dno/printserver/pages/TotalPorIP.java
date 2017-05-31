package br.edu.ifto.dno.printserver.pages;

import br.edu.ifto.dno.printserver.business.ImpressaoBusiness;
import br.edu.ifto.dno.printserver.dtos.ImpressoesIPTotalMes;
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

public class TotalPorIP extends Base {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private ImpressaoBusiness impressaoBusiness;

    public TotalPorIP(final PageParameters parameters) {
        super(parameters);

        List<ImpressoesIPTotalMes> listaImpressao = impressaoBusiness.getImpressoesPorMesIP(LocalDate.now().getMonthValue(),LocalDate.now().getYear());

        ListView<ImpressoesIPTotalMes> listView = new ListView<ImpressoesIPTotalMes>("listaImpressao",listaImpressao) {
            @Override
            protected void populateItem(ListItem<ImpressoesIPTotalMes> listItem) {
                ImpressoesIPTotalMes impressao = listItem.getModelObject();
                listItem.add(new Label("ip",impressao.getIp()));
                listItem.add(new Label("qtd",impressao.getQtd()));
            }
        };

        add(listView);

    }


}
