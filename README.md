# CliqueMédicos
[![pipeline status](https://gitlab.com/desenvolvimento-hepta/i7/badges/master/pipeline.svg)](https://gitlab.com/desenvolvimento-hepta/i7/commits/master)
[![coverage report](https://gitlab.com/desenvolvimento-hepta/i7/badges/master/coverage.svg)](https://gitlab.com/desenvolvimento-hepta/i7/commits/master)

:white_check_mark: [Homologação](http://heptatomcattest-env.vjj5nwbvdn.us-east-2.elasticbeanstalk.com/)

:x:[Produção](https://dev.hepta.com.br:8443/i7/)

## Sobre CliqueMédicos

O produto Clique Médicos tem por objetivo a venda de serviços dos associados da 
AMHP-DF – consultas, exames clínicos e laboratoriais, e procedimentos – sem a 
necessidade de contratação de um plano de saúde para tal. O foco inicial é a 
fatia da população classificada economicamente como “classe C” de acordo com o 
IBGE, de forma a fornecer serviços de saúde para esta fatia da população de forma 
mais acessível do que o modelo de contratação de plano de saúde atualmente em
voga no mercado.


A visão do produto é um canal Internet de compras, funcional tanto em computadores 
desktop quanto em dispositivos móveis, que permita a aquisição de um serviço 
fornecido por um associado da AMHP-DF, e sua execução e processamento posterior 
como se o serviço tivesse sido agendado pelos canais já existentes para tal. 
Para os processos internos da AMHP-DF, as compras realizadas pelo produto irão 
ser como compras de um novo plano de saúde, de forma a que a parte de “retaguarda” 
dos processos – execução da consulta e repasse dos valores devidos à Associação e 
ao associado – se encaixem nos processos já existentes, diminuindo o esforço de 
implementação e reutilizando processos e procedimentos que já funcionam hoje.

## Funcionalidades

O paciente pode pesquisar associados por especialidades, localidades e nome. 
Pode ver a disponibilidade de horários para servico para a data atual. 
Pode refinar o resultado da pesquisa para datas diferentes.

O paciente ainda não poderá realizar o pagamento online. Ao fechar a compra, 
informa nome, e-mail e telefone. Estas informações estão disponíveis em um 
relatório de compras por período para que a equipe do Clique Médicos entre em 
contato com cada cliente, solicite os dados de pagamento e realize o pagamento no 
lugar do cliente; informe ao cliente e ao associado sobre a consulta comprada 
via e-mail; e acrescente uma entrada na agenda do associado no AMHPTIS.


* Listar especialidades

* Listar localidades pela especialidade selecionada

* Pesquisar associados pela especialidade e localidades selecionadas, para a data atual

* Pesquisar associados pelo nome, para a data atual

* Refinar resultado da pesquisa de associados por período

* Resumir compra

* Gerar número de voucher para consulta

* Registrar compra

* Relatório de compras realizadas em um período

## Como instalar e executar

* Clone o projeto no seu computador
* Abra no Eclipse
* Dê update maven
* Execute no eclipse
 
Para que a planilha em anexo funcione são necessários os seguintes requisitos:
1.       Deve ser usado o Excel versão 32 bits;
2.       Instalar na máquina o driver ODBC de 32 bits do MySQL (https://dev.mysql.com/downloads/connector/odbc/3.51.html);
3.       Escreva no iniciar "ODBC", escolha 32 ou 64 bits dependendo do seu Excel
4.       Criar uma DSN chamada “CM32”, com a seguinte configuração:
    1.       Server: heptadb.c2a5wwusa0me.us-east-2.rds.amazonaws.com
    2.       Port: 3306
    3.       User: hepta
    4.       Passw: .H3pt4.H3pt4.
    5.       Database: cliquemedicos

Scripts de criação das VIEW (para localhost)

CREATE ALGORITHM=UNDEFINED DEFINER=`hepta`@`%` SQL SECURITY DEFINER VIEW `cliquemedicos`.`RelatorioAgendamentos` 
AS select `cliquemedicos`.`Agendamento`.`id_agendamento` AS `id_agendamento`,
`cliquemedicos`.`Agendamento`.`id_amhp` AS `id_amhp`,
`cliquemedicos`.`Agendamento`.`str_credenciado` AS `id_credenciado`,
`cliquemedicos`.`Agendamento`.`str_nome_beneficiario` AS `nome_beneficiario`,
`cliquemedicos`.`Agendamento`.`str_cpf_beneficiario` AS `cpf_beneficiario`,
`cliquemedicos`.`Agendamento`.`str_email_beneficiario` AS `email_beneficiario`,
`cliquemedicos`.`Agendamento`.`str_pessoa_endereco` AS `endereco_beneficiario`,
`cliquemedicos`.`Agendamento`.`date_inicio` AS `date_inicio`,
`cliquemedicos`.`Agendamento`.`str_especialidade` AS `especialidade`,
`cliquemedicos`.`Agendamento`.`str_prestador` AS `nome_prestador`,
`cliquemedicos`.`Agendamento`.`str_telefone_beneficiario` AS `tel_beneficiario`
from `cliquemedicos`.`Agendamento`;

CREATE ALGORITHM=UNDEFINED DEFINER=`hepta`@`%` SQL SECURITY DEFINER VIEW `cliquemedicos`.`RelatorioCompras` 
AS select `cliquemedicos`.`Compra`.`id_compra` AS `id_compra`,
`cliquemedicos`.`Compra`.`bool_pago` AS `bool_pago`,
`cliquemedicos`.`Compra`.`date_consulta` AS `data_consulta`,
`cliquemedicos`.`Compra`.`date_registro` AS `data_registro`,
`cliquemedicos`.`Compra`.`dec_valor_consulta` AS `valor_consulta`,
`cliquemedicos`.`Compra`.`long_voucher` AS `num_voucher`,
`cliquemedicos`.`Compra`.`str_nome_cliente` AS `nome_cliente`,
`cliquemedicos`.`Compra`.`str_cpf_cliente` AS `cpf_cliente`,
`cliquemedicos`.`Compra`.`str_email_cliente` AS `email_cliente`,
`cliquemedicos`.`Compra`.`str_endereco_cliente` AS `endereco_cliente`,
`cliquemedicos`.`Compra`.`str_pessoa_endereco` AS `cep_cliente`,
`cliquemedicos`.`Compra`.`str_telefone_cliente` AS `tel_cliente`,
`cliquemedicos`.`Compra`.`str_especialidade` AS `especialidade`,
`cliquemedicos`.`Compra`.`str_matricula_credenciado` AS `matricula_credenciado`,
`cliquemedicos`.`Compra`.`str_matricula_prestador` AS `matricula_prestador`,
`cliquemedicos`.`Compra`.`str_nome_associado` AS `nome_associado`
from `cliquemedicos`.`Compra`;

Globalpayments API token do ambiente de testes: qwertyasdf0123456789



