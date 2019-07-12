"use strict";
const RODANDO = 'rodando';
const SUCESSO = 'sucesso';
const FALHOU = 'falhou';
const ESPERANDO = 'esperando';

const RODANDO_COR = '#3c79ae';
const SUCESSO_COR = '#3cae6f';
const FALHOU_COR = '#ED553B';
const ESPERANDO_COR = '#F6D55C';

var testesVue = new Vue({
    el : '#testes',
    components: {
        'compra-cartao': httpVueLoader('../resources/components/compra-cartao.vue'),
        'header-cm': httpVueLoader('../resources/components/header-cm.vue'),
        'footer-cm': httpVueLoader('../resources/components/footer-cm.vue'),
        'calendario': httpVueLoader('../resources/components/calendario.vue'),
        'cadastro': httpVueLoader('../resources/components/cadastro.vue'),
        'login': httpVueLoader('../resources/components/login.vue'),
    },
    data : {
        //NÃO MEXA NESSES
        sucessosTotal:0,
        falhasTotal:0,
        total:0,
        listaArquivos:[],
        testesJaForamRodados:false,
        rodar:false,
        //PODE MEXER A PARTIR DAQUI

        //VARIAVEIS DOS COMPONENTES
        calendario : {
            listaDatas         : [],
            cssDiaInicial      : "cor-dia-vermelho texto-dia-branco dia-hoje",
            cssDiaComHorario   : "cor-dia-azul texto-dia-branco",
            cssDiaPadrao       : "cor-dia-branco texto-dia-preto",
            estiloVisualizacao : "ano",
            acessoAoPassado    : false,
            loaderLigado       : false,
            mudouAno           : function (){},
            mudouMes           : function (){},
            apertouHoje        : function (){},
            selecionaDia       : function (){},
        },
        gpCompra : {
            pagarCartao        : function (){},
        },
        login : {

        },
        cadastro : {

        },
        headerCm : {
            caminhoRelativo    : "../",
            paginaPrivada      : "false",
        },
    },
    mounted: function(){
    },
    methods: {
        //
        //NÃO ALTERE ESSA FUNÇÃO
        //
        async rodarTestes(){
            const vm = this;
            await rodarTestesVue(vm);
        },

        //
        // CRIE SEUS TESTES A PARTIR DAQUI
        //
        async testes_testeExemplo1(){
            const vm = this;
            return assert(true, "teste");
        },

        //LOGIN.VUE e CADASTRO.VUE
        async login1_testeLoginECadastro(){
            const vm = this; let arrayErros = [];
            const componenteLogin = retornaInstanciaComponenteVue("login");
            const componenteCadastro = retornaInstanciaComponenteVue("cadastro");

            let objLogin = {
                cpf       : "463.710.288-04",
                senha     : "teste",
            };
            let objLembrar = {
                email : "cadastroteste@teste.com.br",
                cpf   : "463.710.288-04",
            };
            componenteLogin.login = objLogin;
            componenteLogin.lembrar = objLembrar;
            await componenteLogin.$nextTick();

            let objUsuario = {
                str_nome     : "teste",
                str_senha    : "teste",
                str_cpf      : "463.710.288-04",
                str_email    : "cadastroteste@teste.com.br",
                str_telefone : "(61)99999-9999",
                str_endereco : "testeEndereco",
                str_cep      : "99999-999"
            };
            let objSeguranca = {
                senha     : 'teste',
                validacao : 'teste'
            };
            componenteCadastro.usuario = objUsuario;
            componenteCadastro.seguranca = objSeguranca;
            await componenteCadastro.$nextTick();

            let resultado1 = await componenteLogin.validaLogin(objLogin);
            if(resultado1)
                arrayErros.push("Conseguiu logar com conta que não existe");

            let resultado2 = await componenteCadastro.cadastrarUsuarioRequest(objUsuario);
            if(!resultado2)
                arrayErros.push("Não conseguiu cadastrar com dados válidos");

            let resultado3 = await componenteLogin.validaLogin(objLogin);
            if(!resultado3)
                arrayErros.push("Não conseguiu logar com conta que existe");
            
            return assert(arrayErros.length === 0,arrayErros);
        },

        //ASSOCIADOS.JS
        async associados_testeMudarData(){
            const vm = this;
            //testando se a data é inicializada corretamente
            let auxData = new Date().addDays(1);
            if(!auxData.compare(associadosVue.dataAtual,"dmy") === 0)
                return fail("Data atual em associadosVue não é inicializada como a data atual. DataAux: "+auxData +" DataAssociado: "+associadosVue.dataAtual);

            //testando se mudarData faz algo errado quando somamos 0 dias
            associadosVue.mudarData(0);
            if(!auxData.compare(associadosVue.dataAtual,"dmy") === 0)
                return fail("Data atual em associadosVue é mudada quando é adicionado 0 dias. DataAux: "+auxData +" DataAssociado: "+associadosVue.dataAtual);

            //testando quando adicionamos um dia
            associadosVue.mudarData(1);
            if(auxData.compare(associadosVue.dataAtual,"dmy") === 0)
                return fail("Data atual em associadosVue não é mudada quando é adicionado 1 dias. DataAux: "+auxData +" DataAssociado: "+associadosVue.dataAtual);

            //testando quandoa dicionamos 23 dias
            let resultado = associadosVue.mudarData(23);
            auxData.addDays(24);
            if(!auxData.compare(associadosVue.dataAtual,"dmy") === 0)
                return fail("Data atual em associadosVue após adicionar 24 dias não é igual a auxData após adicionar 24 dias. DataAux: "+auxData +" DataAssociado: "+associadosVue.dataAtual);

            //testando se é escrita corretamente
            return assert(resultado === auxData.parsePTBR(), "Data atual em associadosVue não é escrita corretamente");
        },
        async associados_testeBuscaLocalidades(){
            const vm = this; let arrayErros = [];

            let especialidades = await indexVue.buscaEspecialidades();
            if(especialidades.length <= 0)
                return fail("Não foi possível buscar especialidades, todos os outros testes falham.");
            
            let localidades = await indexVue.buscaLocalidades(especialidades[1].id);
            if(localidades.length <= 0)
                return fail("Não foi possível buscar localidades dessa especialidade, todos os outros testes falham.");
            
            let res = associadosVue.buscaLocalidades(null,[]);
            if(!isNullUndef(res) && res.length > 0)
                arrayErros.push("Retornou localidades sem ter um array de ids");
            res = associadosVue.buscaLocalidades(localidades[0]);
            if(res.length <= 0)
                arrayErros.push("Não retornou localidades ao dar uma id");
            res = associadosVue.buscaLocalidades([localidades[0],localidades[1]]);
            if(res.length <= 0)
                arrayErros.push("Não retornou localidades ao dar um array de id");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async associados_testeBuscaAssociados(){
            const vm = this; let arrayErros =[];

            let dataAtual = new Date();
            dataAtual.addDays(1);

            let especialidades = await indexVue.buscaEspecialidades();
            if(especialidades.length <= 0)
                return fail("Não foi possível buscar especialidades, todos os outros testes falham.");
        
            let idEspecialidade = especialidades[15].id;

            //teste com tudo vazio
            let res = await associadosVue.buscaAssociados(0,"",dataAtual);
            if(res.length > 0)
                arrayErros.push("Retornou associados sem uma especialidade");

            //teste com filtro de especialidade
            res = await associadosVue.buscaAssociados(idEspecialidade,"",dataAtual);
            if(res.length <= 0)
                arrayErros.push("Não retornou associados com especialidade");

            //teste com filtro de especialidade e nome
            let aux = await associadosVue.buscaAssociados(idEspecialidade,"",dataAtual);
            if(aux.length >0){
                res = await associadosVue.buscaAssociados(idEspecialidade,aux[0].nomePrestador,dataAtual);
                if(res.length <= 0)
                    arrayErros.push("Não retornou associados com especialidade e nome do médico");
            } else 
                arrayErros.push("Não retornou associados com especialidade e nome do médico pois não foi encontrado nada dessa especialidade");

            //teste com filtro de especialidade, duas localidades e nome
            aux = await associadosVue.buscaAssociados(idEspecialidade,"",dataAtual);
            if(aux.length >0){
                res = await associadosVue.buscaAssociados(idEspecialidade,aux[0].nomePrestador,dataAtual);
                if(res.length <= 0)
                    arrayErros.push("Não retornou associados com especialidade, data e nome do médico");
            } else 
                arrayErros.push("Não retornou associados com especialidade, data e nome do médico pois não foi encontrado nada dessa especialidade");
            
            return assert(arrayErros.length === 0, arrayErros);
        },

        //INDEX.JS
        async index_testeListaEspecialidade(){
            let especilidadeAMHPTISS = await indexVue.buscaEspecialidades();
            //chama api do AMHPTIIS
            if(isNullUndef(especilidadeAMHPTISS)
            || especilidadeAMHPTISS.length == 0){
                return fail("A lista de especialidades do AMHPTISS está vazia, provavelmente a API não está retornando nenhum valor");
            }
            else if(!especilidadeAMHPTISS[0].hasOwnProperty("id") 
            && !especilidadeAMHPTISS[0].hasOwnProperty("descricao")){
                return fail("Os objetos da lista de especialidades do AMHPTISS não são os esperados: " + especilidadeAMHPTISS[0]);
            }
            else if(indexVue.listaEspecialidades == especilidadeAMHPTISS ){
                return assert(true, "OK");
            }else{
                return fail("A lista de especilidades é diferente da lista de especialidades do AMHPTISS");
            }
        },
        async index_testeListaLocalidade(){
            const vm = this; let arrayErros = [];
            let especialidades = await indexVue.buscaEspecialidades();
            if(especialidades.length > 0){
                let localidadeAMHPTISS = await indexVue.buscaLocalidades(especialidades[1].id);
                //chama api do AMHPTIIS
                if(isNullUndef(localidadeAMHPTISS)
                || localidadeAMHPTISS.length == 0){
                    arrayErros.push("A lista de localidades do AMHPTISS está vazia, provavelmente a API não está retornando nenhum valor");
                } else if(!localidadeAMHPTISS[0].hasOwnProperty("id")
                && !localidadeAMHPTISS[0].hasOwnProperty("descricao")){
                    arrayErros.push("Os objetos da lista de localidades do AMHPTISS não são os esperados: " + localidadeAMHPTISS[0]);
                }
            } else 
                arrayErros.push("Não foi possível buscar especialidades do AMHPTISS");
            
            return assert(arrayErros.length === 0, arrayErros);
        },

        //COMPRA.JS
        async compra_testePagar(){
            const vm = this; let arrayErros = [];
            let dadosCliente = {
                CardholderName              : "",
                EmailAddress                : "",
                BillingAddressLine1         : "",
                BillingAddressPostalCode    : "",
                str_cpf_cliente             : "",
                str_telefone_cliente        : "",
            };
            compraVue.data = new Date();
            compraVue.idPrestador = "";
            compraVue.idCredenciado = "";
            compraVue.idEsp = "";
            compraVue.associadoEnderecoHorario = "";
            compraVue.precoConsulta = "";
            let resultado1 = await compraVue.pagarCartao(dadosCliente);

            dadosCliente = {
                CardholderName              : "Teste CardholderName",
                EmailAddress                : "Teste EmailAddress",
                BillingAddressLine1         : "Teste BillingAddressLine1",
                BillingAddressPostalCode    : "99999-999",
                str_cpf_cliente             : "999.999.999.99",
                str_telefone_cliente        : "(99)99999-9999",
            };
            compraVue.data = new Date();
            compraVue.idPrestador = -77857;
            compraVue.idCredenciado = -7686464;
            compraVue.idEsp = -887585;
            compraVue.associadoEnderecoHorario = "Teste pessoa endereço";  // Campo para diferenciar a localidade que o medico atende no AMHP
            compraVue.precoConsulta = 999.99;
            let resultado2 = await compraVue.pagarCartao(dadosCliente);
            if(resultado1)
                arrayErros.push("Pagamento com campos vazios");
            
            if(!resultado2)
                arrayErros.push("Pagamento não foi feito com campos preenchidos corretamente");
            
            
            return assert(arrayErros.length === 0,arrayErros);
        },
        async compra_testeBaixarBoleto(){
            const vm = this; let arrayErros = [];
            //let resultado = await compraVue.baixarBoleto();
            return fail("Não implementado");
        },
        async compra_testeCopiarCodigoBarra(){
            const vm = this; let arrayErros = [];
            //let resultado = await compraVue.copiarCodigoBarra();
            return fail("Não implementado");
        },
        async compra_testeBuscaAssociado(){
            const vm = this; let arrayErros = [];
            //let resultado = await compraVue.buscaAssociado();
            return fail("Não implementado");
        },

        //HISTORICO.JS
        async historico_testeBuscaHistorico(){
            const vm = this; let arrayErros = [];

            let resultado = await historicoVue.buscaHistorico();
            if(resultado.isEmpty())
                arrayErros.push("Não foi possível buscar historico dessa conta, array vazio");
            return assert(arrayErros.length === 0, arrayErros);
        },

        //AGENDAMENTOS.JS
        async agendamento_testeBuscaAgendamentos(){
            const vm = this; let arrayErros = [];
            let resultado = await agendamentoVue.buscaAgendamentos();
            if(resultado.isEmpty())
                arrayErros.push("Não foi possível buscar agendamentos dessa conta, array vazio");
            return assert(arrayErros.length === 0, arrayErros);
        },

        //COMPONENTES VUE
        //CALENDARIO.VUE
        async calendario_testeGerarDia(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let teste = new Date(2019, 4, 22);
            let obj = componenteCalendario.gerarDia(22,4,2019);
            if(obj.diaAno !== teste.getFullYear()
            && obj.diaMes !== teste.getMonth()
            && obj.diaSemana !== teste.getDate()
            && obj.diaSemanaTxt !== "Quarta"
            && obj.diaSemanaTxtMini !== "Qua"
            && obj.cssClass !== componenteCalendario.cssClass + " cursor-invalido"
            && !obj.selecionado
            && obj.invalido
            && obj.txtTooltip !== "")
                arrayErros.push("Dia foi mal-formado");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeGerarMes(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let teste = new Date(2019, 4, 22);
            let obj = componenteCalendario.gerarMes(4,2019);
            if(obj.mes !== teste.getMonth()
            && obj.nome !== "Maio"
            && obj.dias.length === 0)
                arrayErros.push("Mês foi mal-formado");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeGerarAno(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let teste = new Date(2019, 4, 22);
            let obj = componenteCalendario.gerarAno(2019);
            if(obj.ano !== teste.getFullYear()
            && obj.meses.length === 0)
                arrayErros.push("Ano foi mal-formado");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeCalcularDiasNoMes(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let diasNoMes = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

            for(let i = 0; i < 12; i++){
                if(diasNoMes[i] !== componenteCalendario.calcularDiasNoMes(2019,i)){
                    arrayErros.push("Numero de dias foi calculado errado para o mês: "+i);
                    break;
                }
            }

            for(let i = 0; i < 12; i++){
                if(i == 2 && 29 !== componenteCalendario.calcularDiasNoMes(2016,i)){
                    arrayErros.push("Numero de dias foi calculado errado para o mês: "+i);
                    break;
                }
            }

            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeLimparDiasAnos(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");
            let dataHoje = new Date();
            let classes = "classeTeste";
            let txtTooltip = "tooltipTeste";
            await componenteCalendario.inserirConteudoDia(dataHoje,classes,txtTooltip);
            await componenteCalendario.limparDiasAnos();
            let achou = false;
            for(let i = 0; i < componenteCalendario.listaAnos.length; i++){
                let ano = componenteCalendario.listaAnos[i];
                if(ano.ano === dataHoje.getFullYear()){
                    for(let j = 0; j < ano.meses.length; j++){
                        let mes = ano.meses[j];
                        if(mes.mes === dataHoje.getMonth()){
                            for(let k = 0; k < mes.dias.length; k++){
                                let dia = mes.dias[k];
                                if(dia.diaMes === dataHoje.getDate()){
                                    if(dia.cssClass === classes
                                    && dia.txtTooltip === txtTooltip)
                                        arrayErros.push("Achou o dia, mas os dados não foram apagados");
                                    achou = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(!achou)
                arrayErros.push("Não achou a data no calendario");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeSelecionaDia(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            testesVue.calendario.acessoAoPassado = false;
            await componenteCalendario.$nextTick();
            let mes = componenteCalendario.gerarMes(4,2019);
            let dia = componenteCalendario.gerarMes(22,4,2019);
            if(componenteCalendario.selecionaDia(dia, mes))
                arrayErros.push("Selecionou dia no passado.");

            testesVue.calendario.acessoAoPassado = true;
            await componenteCalendario.$nextTick();
            if(!componenteCalendario.selecionaDia(dia, mes))
                arrayErros.push("Não conseguiu selecionar dia no passado.");

            let data = new Date();
            let mes2 = componenteCalendario.gerarMes(data.getMonth(), data.getFullYear());
            let dia2 = componenteCalendario.gerarMes(data.getDate(), data.getMonth(), data.getFullYear());
            if(!componenteCalendario.selecionaDia(dia2, mes2))
                arrayErros.push("Não conseguiu selecionar dia no futuro.");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeInserirConteudoDia(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");
            let dataHoje = new Date();
            let dataFuturo = new Date().addYears(20);
            let classes = "classeTeste";
            let txtTooltip = "tooltipTeste";

            await componenteCalendario.inserirConteudoDia(dataHoje,classes,txtTooltip);
            let achou = false;
            for(let i = 0; i < componenteCalendario.listaAnos.length; i++){
                let ano = componenteCalendario.listaAnos[i];
                if(ano.ano === dataHoje.getFullYear()){
                    for(let j = 0; j < ano.meses.length; j++){
                        let mes = ano.meses[j];
                        if(mes.mes === dataHoje.getMonth()){
                            for(let k = 0; k < mes.dias.length; k++){
                                let dia = mes.dias[k];
                                if(dia.diaMes === dataHoje.getDate()){
                                    if(dia.cssClass !== classes
                                    && dia.txtTooltip !== txtTooltip)
                                        arrayErros.push("Achou o dia, mas os dados não foram inseridos");
                                    achou = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(!achou)
                arrayErros.push("Não achou a data no calendario");

            await componenteCalendario.inserirConteudoDia(dataFuturo,classes,txtTooltip);
            achou = false;
            for(let i = 0; i < componenteCalendario.listaAnos.length; i++){
                let ano = componenteCalendario.listaAnos[i];
                if(ano.ano === dataFuturo.getFullYear()){
                    for(let j = 0; j < ano.meses.length; j++){
                        let mes = ano.meses[j];
                        if(mes.mes === dataFuturo.getMonth()){
                            for(let k = 0; k < mes.dias.length; k++){
                                let dia = mes.dias[k];
                                if(dia.diaMes === dataFuturo.getDate()){
                                    if(dia.cssClass !== classes
                                    && dia.txtTooltip !== txtTooltip)
                                        arrayErros.push("Achou o dia futuro, mas os dados não foram inseridos");
                                    achou = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(!achou)
                arrayErros.push("Não achou a data futura no calendario");
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeAdicionarDiasHorarios(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");
            let classeCor = "corClass";
            let dataAtual = new Date();
            dataAtual.addDays(1);

            let especialidades = await indexVue.buscaEspecialidades();
            if(especialidades.length <= 0)
                return fail("Não foi possível buscar especialidades, todos os outros testes falham.");
            let idEspecialidade = especialidades[15].id;
            let res = await associadosVue.buscaAssociados(idEspecialidade,"",dataAtual);
            if(res.length <= 0)
                return fail("Não foi possível buscar associados, todos os outros testes falham.");

            await componenteCalendario.adicionarDiasHorarios(res,classeCor);
            await componenteCalendario.$nextTick();
            let achou = false;
            for(let i = 0; i < componenteCalendario.listaAnos.length; i++){
                let ano = componenteCalendario.listaAnos[i];
                for(let j = 0; j < ano.meses.length; j++){
                    let mes = ano.meses[j];
                    for(let k = 0; k < mes.dias.length; k++){
                        let dia = mes.dias[k];
                        if(dia.invalido === false)
                            achou = true;
                        break;
                    }
                }
            }
            if(!achou)
                arrayErros.push("Nenhum dia foi modificado no calendário.");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeMudaAno(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let ano = componenteCalendario.anoSelecionado.ano;
            let mes = componenteCalendario.mesSelecionado.mes;
            let diaMes = componenteCalendario.diaSelecionado.diaMes;

            await componenteCalendario.mudaAno(1);
            if(ano === componenteCalendario.anoSelecionado.ano
            && mes === componenteCalendario.mesSelecionado.mes
            && diaMes === componenteCalendario.diaSelecionado.diaMes)
                arrayErros.push("Mudou de ano, mas não mudou os valores de ano, mes e dia selecionado.");

            await componenteCalendario.mudaAno(20);
            if(ano === componenteCalendario.anoSelecionado.ano
            && mes === componenteCalendario.mesSelecionado.mes
            && diaMes === componenteCalendario.diaSelecionado.diaMes)
                arrayErros.push("Mudou de ano no futuro distante, mas não mudou os valores de ano, mes e dia selecionado.");
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeMudaMes(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let ano = componenteCalendario.anoSelecionado.ano;
            let mes = componenteCalendario.mesSelecionado.mes;
            let diaMes = componenteCalendario.diaSelecionado.diaMes;

            await componenteCalendario.mudaMes(1);
            if(ano !== componenteCalendario.anoSelecionado.ano
            && mes === componenteCalendario.mesSelecionado.mes
            && diaMes === componenteCalendario.diaSelecionado.diaMes)
                arrayErros.push("Mudou de mes, mas mudou os valores de ano, ou não mudou os de mes e dia selecionado.");

            await componenteCalendario.mudaMes(13);
            if(ano === componenteCalendario.anoSelecionado.ano
            && mes === componenteCalendario.mesSelecionado.mes
            && diaMes === componenteCalendario.diaSelecionado.diaMes)
                arrayErros.push("Mudou 13 meses, mas não mudou os valores de ano, mes e dia selecionado.");
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeMudaHoje(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");
            await componenteCalendario.mudaHoje();
            let ano = componenteCalendario.anoSelecionado.ano;
            let mes = componenteCalendario.mesSelecionado.mes;
            let diaMes = componenteCalendario.diaSelecionado.diaMes;

            let hoje = new Date();
            
            if(ano !== hoje.getFullYear()
            && mes !== hoje.getMonth()
            && diaMes !== hoje.getDate())
                arrayErros.push("Mudou de mes, voltou pra hoje, mas não voltou.");
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeAnoComparator(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");
            let a = 1;
            let b = 2;
            if(componenteCalendario.anoComparator(a,b) !== -1)
                arrayErros.push(" 1 > 2");
            if(componenteCalendario.anoComparator(b,a) !== 1)
                arrayErros.push(" 2 < 1");
            if(componenteCalendario.anoComparator(a,a) !== 0)
                arrayErros.push(" 1 != 1");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeDiaDoMes(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");

            let teste1 = new Date(2019,4,1);
            let teste2 = new Date(2019,4,31);
            let obj = componenteCalendario.gerarMes(4,2019);
            for(let i = 0; i < 6; i++){
                for(let j = 0; j < 7; j++){
                    let teste = componenteCalendario.diaDoMes(obj,i,j);
                    if(i === 0){
                        if(j < 3 && teste.diaMes !== -1)
                            fail("Renderização de filler de mês na primeira linha inválida");
                        else if(teste.diaMes === -1)
                            fail("Renderização dos dias do mês na primeira linha inválida");
                    } else if(i > 0 && i < 5){
                        if(teste.diaMes === -1)
                            fail("Renderização dos dias do mês nas linhas do meio inválida");
                    } else if(i === 5){
                        if(j > 5 && teste.diaMes !== -1)
                            fail("Renderização de filler de mês na última linha inválida");
                        else if(teste.diaMes === -1)
                            fail("Renderização dos dias do mês na última linha inválida");
                    }
                }
            }

            return assert(arrayErros.length === 0, arrayErros);
        },
        async calendario_testeIniciarCalendario(){
            const vm = this; let arrayErros = [];
            const componenteCalendario = retornaInstanciaComponenteVue("calendario");
            let resultado = componenteCalendario.iniciarCalendario();
            for(let i = 0; i < resultado.length; i++){
                let ano = resultado[i];
                if(ano.ano === undefined
                && ano.meses === undefined
                && ano.meses.length === 0){
                    fail("Ano malformado i: " + i);
                } else {
                    for(let j = 0; j < resultado[i].meses.length; j++){
                        let mes = resultado[i].meses[j];
                        if(mes.mes === undefined
                        && mes.nome === undefined
                        && mes.dias === undefined
                        && mes.dias.length === 0){
                            fail("Mês malformado j: " + j);
                        } else {
                            for(let k = 0; k < resultado[i].meses[j].dias.length; k++){
                                let dia = resultado[i].meses[j].dias[k];
                                if(dia.diaAno == undefined
                                && dia.diaMes == undefined
                                && dia.diaSemana == undefined
                                && dia.diaSemanaTxt == undefined
                                && dia.diaSemanaTxtMini == undefined
                                && dia.cssClass == undefined
                                && dia.selecionado == undefined
                                && dia.invalido == undefined
                                && dia.txtTooltip == undefined){
                                    fail("Dia malformado k: " + k);
                                }
                            }
                        }
                    }
                }
            }
            return assert(arrayErros.length === 0, arrayErros);
        },

        //LOGIN.JS
        async login2_testeLogout(){
            const vm = this; let arrayErros = [];
            let resultado = false;//await loginVue.logout();
            return assert(resultado, "Não foi possível fazer logout.");
        },

        //UTILS.JS
        //testa a função shuffle que randomiza um array.
        async utils_testeArrayAleatorio(){
            const vm = this;
            //preparando array
            let arrayTeste = [];
            for (let i = 0; i < 10000; i++) {
                arrayTeste.push(i);
            }
            //copiando array original e randomizando
            let arrayShuffle = arrayTeste.slice();
            arrayShuffle = arrayShuffle.shuffle();

            //comparando
            let tipo1 =Object.prototype.toString.call(arrayTeste);
            let tipo2 =Object.prototype.toString.call(arrayShuffle);
            if(tipo1 !== tipo2)
                return fail("Arrays nao tem o mesmo tipo depois de fazer shuffle");
            else if(arrayShuffle.length !== arrayTeste.length)
                return fail("Arrays nao tem tamanho iguais depois de fazer shuffle");
            else{
                let achouDiferente = false
                for (let i = 0; i < arrayTeste.length; i++) {
                    if(arrayTeste[i] !== arrayShuffle[i]){
                        achouDiferente = true;
                    }
                }
                return assert(achouDiferente, "Arrays são iguais depois de fazer shuffle, ou você tem muita sorte, ou tem algo errado.");
            }
        },
        async utils_testeIsEmptyArray(){
            const vm = this; let arrayErros = [];
            let teste = [];
            if(!teste.isEmpty())
                arrayErros.push("Array vazio foi visto como não vazio.")

            teste.push("a");
            if(teste.isEmpty())
                arrayErros.push("Array não vazio foi visto como vazio.")
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeisNullUndef(){
            const vm = this; let arrayErros = [];
            let test1 = undefined;
            let test2 = null;
            let test3 = 0;
            
            if(!isNullUndef(test1))
                arrayErros.push("Variável undefined não reconhecida como nula ou undefined");
            if(!isNullUndef(test2))
                arrayErros.push("Variável nula não reconhecida como nula ou undefined");
            if(isNullUndef(test3))
                arrayErros.push("Variável com valor reconhecida como nula ou undefined");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeIsEmpty(){
            const vm = this; let arrayErros = [];

            let obj1 = {};
            let obj2 = {
                test:"test"
            };
            if(!isEmpty(obj1))
                arrayErros.push("Objeto vazio não foi reconhecido como vazio");
            if(isEmpty(obj2))
                arrayErros.push("Objeto com parametros foi reconhecido como vazio");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeIsValid(){
            const vm = this; let arrayErros = [];

            let teste = new Date();
            if(!teste.isValid())
                arrayErros.push("Data valida foi reconhecida como invalida");
            teste.setTime("a");
            if(teste.isValid())
                arrayErros.push("Data invalida foi reconhecida como valida");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeToISOLocal(){
            const vm = this; let arrayErros = [];

            let teste = new Date(2019,4,30,15,16,37);
            let resultadoEsperado = "2019-05-30T15:16:37";
            if(teste.toISOLocal() !== resultadoEsperado)
                arrayErros.push("Data não foi escrita corretamente");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeFromLocalDateTime(){
            const vm = this; let arrayErros = [];
            let input = {
                chronology: {id: "ISO", calendarType: "iso8601"},
                calendarType: "iso8601",
                id: "ISO",
                dayOfMonth: 3,
                dayOfWeek: "FRIDAY",
                dayOfYear: 123,
                hour: 14,
                minute: 30,
                month: "MAY",
                monthValue: 5,
                nano: 0,
                second: 0,
                year: 2019,
            }
            let teste1 = new Date(2019,4,3,14,30,0,0);
            let teste2 = new Date().fromLocalDateTime(input);
            if(teste1.getFullYear() !== teste2.getFullYear()
            || teste1.getMonth() !== teste2.getMonth()
            || teste1.getDate() !== teste2.getDate()
            || teste1.getHours() !== teste2.getHours()
            || teste1.getMinutes() !== teste2.getMinutes()
            || teste1.getSeconds() !== teste2.getSeconds()
            || teste1.getMilliseconds() !== teste2.getMilliseconds())
                arrayErros.push("Não houve conversão correta");

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeFormatDate(){
            const vm = this; let arrayErros = [];

            let data1 = new Date(1999, 10, 10, 12, 13, 14);
            let resultadoEsperado1 = "10/11/1999 12:13:14";
            let resultado1 = data1.formatDate("dmyHMS");

            let data2 = new Date(1999, 1, 3, 3, 4, 5);
            let resultadoEsperado2 = "03/02/1999 03:04:05";
            let resultado2 = data2.formatDate("dmyHMS");

            let data3 = new Date(1999, 10, 12, 12, 13);
            let resultadoEsperado3 = "12/11/1999 12:13";
            let resultado3 = data3.formatDate("dmyHM");

            let data4 = new Date(1999, 10, 10);
            let resultadoEsperado4 = "10/11/1999";
            let resultado4 = data4.formatDate("dmy");

            let data5 = new Date(1999, 10, 10, 12, 13, 14);
            let resultadoEsperado5 = "12:13:14";
            let resultado5 = data5.formatDate("HMS");

            let data6 = new Date(1999, 10, 10, 12, 13, 14);
            let resultadoEsperado6 = "12:13";
            let resultado6 = data6.formatDate("HM");

            let data7 = new Date(1999, 10, 10, 23, 13, 14);
            let resultadoEsperado7 = "23:13";
            let resultado7 = data7.formatDate("HM");

            let data8 = new Date(1999, 10, 10, 23, 13, 14);
            let resultadoEsperado8 = "23:13:14";
            let resultado8 = data8.formatDate("HMS");

            if(resultado1 !== resultadoEsperado1)
                arrayErros.push(resultadoEsperado1 + " !== " +resultado1);
            if(resultado2 !== resultadoEsperado2)
                arrayErros.push(resultadoEsperado2 + " !== " +resultado2);
            if(resultado3 !== resultadoEsperado3)
                arrayErros.push(resultadoEsperado3 + " !== " +resultado3);
            if(resultado4 !== resultadoEsperado4)
                arrayErros.push(resultadoEsperado4 + " !== " +resultado4);
            if(resultado5 !== resultadoEsperado5)
                arrayErros.push(resultadoEsperado5 + " !== " +resultado5);
            if(resultado6 !== resultadoEsperado6)
                arrayErros.push(resultadoEsperado6 + " !== " +resultado6);
            if(resultado7 !== resultadoEsperado7)
                arrayErros.push(resultadoEsperado7 + " !== " +resultado7);
            if(resultado8 !== resultadoEsperado8)
                arrayErros.push(resultadoEsperado8 + " !== " +resultado8);

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddMillis(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addMillis(1);
            if(comparar.getMilliseconds() === teste1.getMilliseconds())
                arrayErros.push("Adicionei um milli e os milli estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());   
            if(comparar.getMilliseconds() > teste1.getMilliseconds())
                arrayErros.push("Adicionei um milli e o milli de comparação é maior que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            teste1.addMillis(-2);
            if(comparar.getMilliseconds() === teste1.getMilliseconds())
                arrayErros.push("Subtrai dois milli e os milli estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getMilliseconds() < teste1.getMilliseconds())
                arrayErros.push("Subtrai dois milli e o milli de comparação é menor que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddSeconds(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addSeconds(1);
            if(comparar.getSeconds() === teste1.getSeconds())
                arrayErros.push("Adicionei um segundo e os segundos estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());   
            if(comparar.getSeconds() > teste1.getSeconds())
                arrayErros.push("Adicionei um segundo e o segundo de comparação é maior que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            teste1.addSeconds(-2);
            if(comparar.getSeconds() === teste1.getSeconds())
                arrayErros.push("Subtrai dois segundos e os segundos estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getSeconds() < teste1.getSeconds())
                arrayErros.push("Subtrai dois segundos e o segundo de comparação é menor que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddMinutes(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addMinutes(1);
            if(comparar.getMinutes() === teste1.getMinutes())
                arrayErros.push("Adicionei um minuto e os minutos estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());   
            if(comparar.getMinutes() > teste1.getMinutes())
                arrayErros.push("Adicionei um minuto e o minuto de comparação é maior que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            teste1.addMinutes(-2);
            if(comparar.getMinutes() === teste1.getMinutes())
                arrayErros.push("Subtrai dois minutos e os minutos estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getMinutes() < teste1.getMinutes())
                arrayErros.push("Subtrai dois minutos e o minuto de comparação é menor que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddHours(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addHours(1);
            if(comparar.getHours() === teste1.getHours())
                arrayErros.push("Adicionei uma hora e as horas estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());   
            if(comparar.getHours() > teste1.getHours())
                arrayErros.push("Adicionei uma hora e a hora de comparação é maior que a teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            teste1.addHours(-2);
            if(comparar.getHours() === teste1.getHours())
                arrayErros.push("Subtrai duas horas e as horas estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getHours() < teste1.getHours())
                arrayErros.push("Subtrai duas horas e a hora de comparação é menor que a teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddDays(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addDays(1);
            if(comparar.getDate() === teste1.getDate())
                arrayErros.push("Adicionei um dia e os dias estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getDate() > teste1.getDate())
                arrayErros.push("Adicionei um dia e o dia de comparação é maior que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            teste1.addDays(-2);
            if(comparar.getDate() === teste1.getDate())
                arrayErros.push("Subtrai dois dias e os dias estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getDate() < teste1.getDate())
                arrayErros.push("Subtrai dois dias e o dia de comparação é menor que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddMonths(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addMonths(1);
            if(comparar.getMonth() === teste1.getMonth())
                arrayErros.push("Adicionei um mes e os meses estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getMonth() > teste1.getMonth())
                arrayErros.push("Adicionei um mes e o mes de comparação é maior que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            teste1.addMonths(-2);
            if(comparar.getMonth() === teste1.getMonth())
                arrayErros.push("Subtrai dois meses e os meses estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getMonth() < teste1.getMonth())
                arrayErros.push("Subtrai dois meses e o mes de comparação é menor que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeAddYears(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            teste1.addYears(1);
            if(comparar.getFullYear() === teste1.getFullYear())
                arrayErros.push("Adicionei um ano e os anos estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getFullYear() > teste1.getFullYear())
                arrayErros.push("Adicionei um ano e o ano de comparação é maior que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());

            teste1.addYears(-2);
            if(comparar.getFullYear() === teste1.getFullYear())
                arrayErros.push("Subtrai dois anos e os anos estão iguais" + comparar.toISOString() + ' - ' +teste1.toISOString());
            if(comparar.getFullYear() < teste1.getFullYear())
                arrayErros.push("Subtrai dois anos e o ano de comparação é menor que o teste" + comparar.toISOString() + ' - ' +teste1.toISOString());
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompare(){
            const vm = this; let arrayErros = [];
            let data1 = new Date(2000,2,13,14,15,16,171);
            let data2 = new Date(2001,3,11,12,13,14,151);
            if(!data1.compare(data1,"dmyHMSL") === 0)
                arrayErros.push("datas iguais foram vistas como diferentes")
            if(!data1.compare(data1,"dyHS") === 0)
                arrayErros.push("datas iguais foram vistas como diferentes")
            if(!data1.compare(data1,"mML") === 0)
                arrayErros.push("datas iguais foram vistas como diferentes")
            if(data1.compare(data2,"dmyHMSL") === 0)
                arrayErros.push("datas diferentes foram vistas como iguais")
            if(data1.compare(data2,"dyHS") === 0)
                arrayErros.push("datas diferentes foram vistas como iguais")
            if(data1.compare(data2,"mML") === 0)
                arrayErros.push("datas diferentes foram vistas como iguais")

            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareYear(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareYear(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareYear(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareYear(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareDate(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareDate(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareDate(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareDate(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareMonth(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareMonth(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareMonth(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareMonth(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareHour(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareHour(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareHour(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareHour(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareMinute(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareMinute(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareMinute(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareMinute(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareSecond(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareSecond(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareSecond(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareSecond(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeCompareMilli(){
            const vm = this; let arrayErros = [];
            let comparar = new Date();
            let teste1 = new Date();
            let teste2 = new Date(1999, 5, 1, 10, 11, 12, 134);
            let teste3 = new Date(2000, 6, 2, 11, 12, 13, 145);
            let teste4 = new Date(2001, 7, 3, 12, 13, 14, 156);
            if(!comparar.compareMilli(teste1) === 0)
                arrayErros.push("Data iguais não foram vistas como iguas");
            if(!teste2.compareMilli(teste3) < 0)
                arrayErros.push("Data menor não foi vista como menor");
            if(!teste4.compareMilli(teste3) > 0)
                arrayErros.push("Data maior não foi vista como maior");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeIsLeapYear(){
            const vm = this; let arrayErros = [];
            let leapYearList = [1804,1808,1812,1816,1820,1824,1828,1832,1836,1840,1844,1848,1852,1856,1860,1864,1868,1872,1876,1880,1884,1888,1892,1896,1904,1908,1912,1916,1920,1924,1928,1932,1936,1940,1944,1948,1952,1956,1960,1964,1968,1972,1976,1980,1984,1988,1992,1996,2000,2004,2008,2012,2016,2020,2024,2028,2032,2036,2040,2044,2048,2052,2056,2060,2064,2068,2072,2076,2080,2084,2088,2092,2096,2104,2108,2112,2116,2120,2124,2128,2132,2136,2140,2144,2148,2152,2156,2160,2164,2168,2172,2176,2180,2184,2188,2192,2196,2204,2208,2212,2216,2220,2224,2228,2232,2236,2240,2244,2248,2252,2256,2260,2264,2268,2272,2276,2280,2284,2288,2292,2296,2304,2308,2312,2316,2320,2324,2328,2332,2336,2340,2344,2348,2352,2356,2360,2364,2368,2372,2376,2380,2384,2388,2392,2396,2400];
            for(let i = 0; i < leapYearList.length; i++){
                let teste = new Date(leapYearList[i],1,1,1,1,1);
                if(!teste.isLeapYear()){
                    return fail("Ano bissexto não foi considerado como bissexto:" + teste.getFullYear());
                }
            }
            for(let i = 0; i < leapYearList.length; i++){
                let teste = new Date(leapYearList[i] - 1,1,1,1,1,1);
                if(teste.isLeapYear()){
                    return fail("Ano não bissexto foi considerado como bissexto:" + teste.getFullYear());
                }
            }
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeGetDOY(){
            const vm = this; let arrayErros = [];
            let diaTeste =  new Date(2000,0,1,0,0,0,1);
            for(let i = 0; i < 365; i++){
                diaTeste.setDate(diaTeste.getDate()+1);
                if(diaTeste.getDOY() !== i+1)
                    fail("Dia do ano calculado incorretamente.");
            }
            
            return assert(true, "");
        },
        async utils_testeCompareIgnoreCase(){
            const vm = this; let arrayErros = [];
            let teste1 = "AaAaAa5";
            let teste2 = "aaaaaa5";
            let teste3 = "aAaAaa5";
            let teste4 = "bbbbbb3";
            if(teste1.compareIgnoreCase(teste4)===0)
                arrayErros.push("Strings que não são iguais foram vistas como iguais.");
            if(!teste1.compareIgnoreCase(teste2)===0)
                arrayErros.push("Não ignorou o case sensitive em teste1");
            if(!teste1.compareIgnoreCase(teste3)===0)
                arrayErros.push("Não ignorou o case sensitive em teste3");
            if(!teste2.compareIgnoreCase(teste2)===0)
                arrayErros.push("Não conseguiu mostrar que duas string iguais são iguais");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeIncludesIgnoreCase(){
            const vm = this; let arrayErros = [];
            let teste1 = "AbRaCaDaBrA66";
            let teste2 = "cada";
            let teste3 = "CaDa";
            let teste4 = "bbbbbb3";
            if(teste1.includesIgnoreCase(teste4))
                arrayErros.push("String do teste1, que não inclui a string do teste4, disse que incluia.");
            if(!teste1.includesIgnoreCase(teste2))
                arrayErros.push("Não ignorou o case sensitive do teste1");
            if(!teste1.includesIgnoreCase(teste3))
                arrayErros.push("Não ignorou o case sensitive do teste3");
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeReplaceAll(){
            const vm = this; let arrayErros = [];
            let teste1 = "000.000.000-00";
            let result = teste1.replaceAll(".","");
            if(result !== "000000000-00")
                arrayErros.push("Não conseguiu dar replace corretamente: " + teste1 + " - " + result);

            teste1 = result;
            result = teste1.replaceAll("-","");
            if(result !== "00000000000")
                arrayErros.push("Não conseguiu dar replace corretamente: " + teste1 + " - " + result);

            let teste2 = "000.000.000-00";
            result = teste2.replaceAll(".","x");
            if(result !== "000x000x000-00")
                arrayErros.push("Não conseguiu dar replace corretamente: " + teste2 + " - " + result);

            teste2 = result;
            result = teste2.replaceAll("-","j");
            if(result !== "000x000x000j00")
                arrayErros.push("Não conseguiu dar replace corretamente: " + teste2 + " - " + result);
            
            return assert(arrayErros.length === 0, arrayErros);
        },
        async utils_testeChecarCPF(){
            const vm = this; let arrayErros = [];
            let testeValidos = ["983.334.927-70","98333492770","950.049.878-23","95004987823","463.710.288-04","46371028804","332.982.841-22","33298284122","904.320.117-06","90432011706","953.056.814-21","95305681421","961.537.444-00","96153744400","161.038.720-14","16103872014","131.351.885-90","13135188590","229.375.756-09","22937575609","203.013.269-16","20301326916","557.730.192-05","55773019205","250.303.501-95","25030350195","184.090.365-17","18409036517","166.767.120-08","16676712008","249.900.866-00","24990086600","042.800.952-21","04280095221","117.399.520-06","11739952006","628.054.207-64","62805420764","367.638.574-89","36763857489","852.442.056-17","85244205617","758.739.604-70","75873960470","773.145.375-85","77314537585","978.165.675-10","97816567510","227.038.899-25","22703889925","910.370.118-21","91037011821","570.502.544-01","57050254401","877.236.032-15","87723603215","413.926.628-77","41392662877","521.235.687-32","52123568732"];
            let testeInvalidos = ["a","","vwucm8wmf7dsf6y98734","aaaaaaaaaaa","00000000000","11111111111","22222222222","33333333333","44444444444","55555555555","66666666666","77777777777","88888888888","99999999999","!@#$%¨&*()_-+=","!@#$%¨&*()_"]
            for(let i = 0; i < testeValidos.length; i++){
                if(!checarCPF(testeValidos[i]))
                    arrayErros.push("Valido foi visto como invalido " + testeValidos[i]);
            }

            for(let i = 0; i < testeInvalidos.length; i++){
                if(checarCPF(testeInvalidos[i]))
                    arrayErros.push("Invalido foi visto como valido " + testeInvalidos[i]);
            }
            
            
            return assert(arrayErros.length === 0, arrayErros);
        },
    },
    //
    //NÃO ALTERE ESSA FUNÇÃO
    //
    created: async function() {
        const vm = this;
        await criarTestesVue(vm);
    }
});