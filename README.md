# printserver

Pre-requisitos do sistema
 - Java 1.8 
 - Maven 3
 
 Atualmente o sistema utilizar um bando de dados gerado assim que iniciar o sistema (HSQLDB), sendo possível a utilização de outros SGDB (Postgres e MySQL). Para configurar o Banco de Dados acesse o arquivo application.properties
 
 LDAP: para quem utiliza um servidor de dominio ou ldap pode fazer a configuração dentro do arquivo application.proprerties.
 
 localização do application.properties -> printserver/src/main/resources/application.properties
 
 Instalação:
 
 git clone https://github.com/tassiovirginio/printserver.git
 
 cd printserver
 
 mvn jetty:run
 
 acesse: http://127.0.0.1:8083/
 
 para cadastrar as impressoras acesse: 
 
 http://127.0.0.1:8083/impressoras
 
 