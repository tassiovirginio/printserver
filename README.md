# PrintServer

Video Demonstrando o Sistema:

[![Everything Is AWESOME](https://img.youtube.com/vi/uZycKj1_ZIU/0.jpg)](https://www.youtube.com/embed/uZycKj1_ZIU?rel=0 "Everything Is AWESOME")

Pre-requisitos do sistema
 - Java 1.8 
 - Maven 3
 
 Atualmente o sistema utilizar um bando de dados gerado assim que iniciar o sistema (HSQLDB), sendo possível a utilização de outros SGDB (Postgres e MySQL). Para configurar o Banco de Dados acesse o arquivo application.properties
 
 LDAP: para quem utiliza um servidor de dominio ou ldap pode fazer a configuração dentro do arquivo application.proprerties.
 
 localização do application.properties -> printserver/src/main/resources/application.properties
 
 Instalação:

 ```shell
 git clone https://github.com/tassiovirginio/printserver.git
 cd printserver
 mvn jetty:run
```
 
 acesse: http://127.0.0.1:8083/
 
 Para cadastrar as impressoras click em "Impressoras".
 
 login: admin
 senha: admin
 
 ** O login e senha podem ser alterados no application.properties.
  
 O sistema acessa impressoras atravez do protocolo IPP. No cadastro da impressora coloque o nome da impressora, descrição e a url do serviço de impressão dela pelo ipp, e ative a mesma.
 
 Se estiver utilizando uma distribuição linux, pode utilizar o "screen" para levantar o sistema, podendo sair do terminal sem delisgar o sistema.
 
 no Ubuntu instale o "screen":
 
```shell
sudo apt install screen
```
 após a instalação execute o sistema com o comando:
 
```shell
screen -S printserver mvn jetty:run
``` 

depois que o sistema iniciar precione: Ctrl + A + D   , o terminal vai ser fechado, mas o processo vai continuar rodando.
 
para acessar novamente o terminal digite:

```shell
screen -R 
```