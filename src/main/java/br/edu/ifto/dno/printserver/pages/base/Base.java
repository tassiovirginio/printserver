package br.edu.ifto.dno.printserver.pages.base;

import br.edu.ifto.dno.printserver.pages.HomePage;
import br.edu.ifto.dno.printserver.pages.TotalPorIP;
import br.edu.ifto.dno.printserver.pages.TotalPorUsuario;
import br.edu.ifto.dno.printserver.pages.UltimasImpressoes;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class Base extends WebPage {
    private static final long serialVersionUID = 1L;


    public Base(final PageParameters parameters) {
        super(parameters);

        add(new Link("lkEnviar") {
            @Override
            public void onClick() {
                Base.this.setResponsePage(HomePage.class);
            }
        });

        add(new Link("lkUltImpressoes") {
            @Override
            public void onClick() {
                Base.this.setResponsePage(UltimasImpressoes.class);
            }
        });


        add(new Link("lkTotalPorIP") {
            @Override
            public void onClick() {
                Base.this.setResponsePage(TotalPorIP.class);
            }
        });

        add(new Link("lkTotalPorUsuario") {
            @Override
            public void onClick() {
                Base.this.setResponsePage(TotalPorUsuario.class);
            }
        });

    }

}
