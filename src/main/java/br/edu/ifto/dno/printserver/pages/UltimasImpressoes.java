package br.edu.ifto.dno.printserver.pages;

import br.edu.ifto.dno.printserver.business.ImpressaoBusiness;
import br.edu.ifto.dno.printserver.entities.Impressao;
import br.edu.ifto.dno.printserver.pages.base.Base;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;

import java.util.List;

public class UltimasImpressoes extends Base {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private ImpressaoBusiness impressaoBusiness;

    public UltimasImpressoes(final PageParameters parameters) {
        super(parameters);

        List<Impressao> listaImpressao = impressaoBusiness.getUltimasImpressoes(10);

        ListView<Impressao> listView = new ListView<Impressao>("listaImpressao",listaImpressao) {
            @Override
            protected void populateItem(ListItem<Impressao> listItem) {
                Impressao impressao = listItem.getModelObject();
                listItem.add(new Label("impressora",impressao.getImpressora().getNome()));
                listItem.add(new Label("usuario",impressao.getUsuario()));
                listItem.add(new Label("data",new LocalDateTime(impressao.getData()).toString("HH:mm dd/MM/yyyy")));
                listItem.add(new Label("ip",impressao.getIpOrigem()));
                listItem.add(new Label("total",impressao.getPaginasTotal()));
            }
        };

        add(listView);

    }


}
