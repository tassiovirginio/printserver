package br.edu.ifto.dno.printserver.pages;

import br.edu.ifto.dno.printserver.business.ImpressaoBusiness;
import br.edu.ifto.dno.printserver.business.ImpressoraBusiness;
import br.edu.ifto.dno.printserver.entities.Impressao;
import br.edu.ifto.dno.printserver.entities.Impressora;
import br.edu.ifto.dno.printserver.pages.base.Base;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDateTime;

import java.util.List;

public class Impressoras extends Base {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private ImpressoraBusiness impressoraBusiness;

    @SpringBean(name = "admin.login")
    private String adminLogin;

    @SpringBean(name = "admin.senha")
    private String adminSenha;

    private String login;

    private String senha;

    private Impressora impressoraNova;

    public Impressoras(){
        this(null,false);
    }

    public Impressoras(Impressora impressoraEditar) {
        this(impressoraEditar,false);
    }

    public Impressoras(Impressora impressoraEditar, Boolean admin) {
        super(null);

        if(impressoraEditar != null) {
            impressoraNova = impressoraEditar;
        }else{
            impressoraNova = new Impressora();
        }

        Form form = new Form("form"){
            @Override
            protected void onSubmit() {
                impressoraBusiness.save(impressoraNova);
                Impressoras.this.setResponsePage(Impressoras.class);
            }
        };

        form.add(new TextField<>("nome",new PropertyModel<String>(impressoraNova,"nome")));
        form.add(new TextField<>("descricao",new PropertyModel<String>(impressoraNova,"descricao")));
        form.add(new TextField<>("url",new PropertyModel<String>(impressoraNova,"url")));
        form.add(new CheckBox("ativa",new PropertyModel<Boolean>(impressoraNova,"ativa")));

        form.setVisible(admin);

        add(form);


        Form formLogin = new Form("formLogin"){
            @Override
            protected void onSubmit() {
                if(adminLogin.equals(login) && adminSenha.equals(senha)){
                    setResponsePage(new Impressoras(impressoraEditar,true));
                }else{
                    setResponsePage(new Impressoras(impressoraEditar,false));
                }
            }
        };
        formLogin.add(new TextField<>("login",new PropertyModel<String>(this,"login")));
        formLogin.add(new PasswordTextField("senha",new PropertyModel<String>(this,"senha")));
        formLogin.setVisible(!admin);

        add(formLogin);


        List<Impressora> listaImpressao = impressoraBusiness.listAll();
        ListView<Impressora> listView = new ListView<Impressora>("listaImpressoras",listaImpressao) {
            @Override
            protected void populateItem(ListItem<Impressora> listItem) {
                Impressora impressora = listItem.getModelObject();
                listItem.add(new Label("nome",impressora.getNome()));
                listItem.add(new Label("descricao",impressora.getDescricao()));
                listItem.add(new Label("url",impressora.getUrl()));
                listItem.add(new Label("ativa",impressora.getAtiva()));
                listItem.add(new Link("editar") {
                    @Override
                    public void onClick() {
                        setResponsePage(new Impressoras(impressora,admin));
                    }
                }.setVisible(admin));
                listItem.add(new Link("deletar") {
                    @Override
                    public void onClick() {
                        impressoraBusiness.delete(impressora);
                        setResponsePage(new Impressoras(null,admin));
                    }
                }.setVisible(admin));
            }
        };

        add(listView);

    }


}
