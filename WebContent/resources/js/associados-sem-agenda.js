"use strict";
var associadosVue = new Vue({
    el : '#appAssociados',
    components: {
        'calendario': httpVueLoader('resources/components/calendario.vue')
    },
    data : {
        filtro                      : null,
        profissional                : '',
        listaLocalidades            : [],
        dataAtual                   : new Date(),
        dataAtualString             : "",
        //dataAtualStringExtra        : "",
        resultadoAssociados         : [],
        numeroAssociadosTotal       : 0,
        numeroAssociadosEncontrados : 0,
        especialidade               : {},
        precoConsulta               : "150,00",
        associado: {
            id                      : -1,
            nome                    : "Medico",
            endereco                : "endereco"
        },
        loadingAssociados           : true,
        popoverShow                 : false,
        show                        : false,
        localidadesSelecionadas     : [],
        associadosCalendario        : [],
        idCard                      : null,
    },
    watch:{
        localidadesSelecionadas(){
            const vm = this;
            vm.filtro.localidades = [];
            Array.prototype.push.apply(vm.filtro.localidades, vm.localidadesSelecionadas);
           /* for(let j = 0; j < vm.localidadesSelecionadas.length; j++){
                for(let i = 0; i < vm.listaLocalidades.length; i++){
                    if(vm.localidadesSelecionadas[j].compareIgnoreCase(vm.listaLocalidades[i])){
                        vm.filtro.localidades.push(vm.listaLocalidades[i]);
                    }
                }
            }*/
            vm.mudarFiltro(vm.filtro);
        },
    },
    mounted: function(){

    },
    computed: {
        // uma função "getter" computada (computed getter)
        dataAtualDMY: function () {
            const vm = this;
            return vm.dataAtual.formatDate("dmy");
        }
    },
    methods: {
        //funcoes que são chamadas pelo calendário
        /*buscaOuFiltraAssociados(dataHoje,ultimaData){
            const vm = this;
            if(dataHoje.getMonth() !== ultimaData.getMonth()){
                vm.buscaAssociados(vm.filtro);
            } else {
                vm.filtrarAssociados(vm.resultadoAssociados, vm.filtro);
            }
        },
        mudouAno(qtd){
            const vm = this;
            if(isNullUndef(qtd))
                return;
            let ultimaData = new Date(vm.dataAtual.getTime());
            vm.dataAtual.setFullYear(vm.dataAtual.getFullYear()+qtd, 0, 1);
            vm.dataAtualString = vm.dataAtual.parsePTBR();
            vm.buscaOuFiltraAssociados(vm.dataAtual,ultimaData);
        },
        mudouMes(qtd){
            const vm = this;
            if(isNullUndef(qtd))
                return;
            let ultimaData = new Date(vm.dataAtual.getTime());
            let mesNovo = vm.dataAtual.getMonth()+qtd;
            let amanha = new Date();
            amanha.addDays(1);
            if(mesNovo <= new Date().getMonth()){
                vm.dataAtual.setMonth(vm.dataAtual.getMonth()+qtd, amanha.getDate());
            } else {
                vm.dataAtual.setMonth(vm.dataAtual.getMonth()+qtd, 1);
            }

            vm.dataAtualString = vm.dataAtual.parsePTBR();
            vm.buscaOuFiltraAssociados(vm.dataAtual,ultimaData);
        },
        apertouHoje(){
            const vm = this;
            let ultimaData = new Date(vm.dataAtual.getTime());
            vm.dataAtual = new Date().addDays(1);
            vm.dataAtualString = vm.dataAtual.parsePTBR();
            vm.buscaOuFiltraAssociados(vm.dataAtual,ultimaData);
        },
        selecionaDia(mes,dia){
            const vm = this;

            if(isNullUndef(dia) && isNullUndef(mes))
                return;

            if(isNullUndef(mes)){
                vm.dataAtual.setDate(dia);
            } else {
                vm.dataAtual.setMonth(mes, dia);
            }
            vm.dataAtualString = vm.dataAtual.parsePTBR();
            vm.filtrarAssociados(vm.resultadoAssociados, vm.filtro);
            vm.popoverShow = false;
        },
        async mudarData(numDias){
            const vm = this;
            let limiteMinimo = new Date().addDays(1);
            let ultimaData = new Date(vm.dataAtual.getTime());
            vm.dataAtual.addDays(numDias);

            //não tem como fazer cosulta no passado
            if(vm.dataAtual.getTime() < limiteMinimo.getTime()){
                vm.dataAtual = new Date().addDays(1);
                vm.dataAtualString = vm.dataAtual.parsePTBR();
                return;
            } else {
                vm.dataAtualString =vm.dataAtual.parsePTBR();
                if(vm.dataAtual.getMonth() !== ultimaData.getMonth()){
                    await vm.buscaAssociados(vm.filtro);
                    if(vm.numeroAssociadosEncontrados === 0)
                        vm.mudarData(numDias);
                } else {
                    let filtrados = vm.filtrarAssociados(vm.resultadoAssociados,vm.filtro);
                    if(filtrados === 0)
                        vm.mudarData(numDias);
                }
            }
            return vm.dataAtualString;
        },*/


        buscaPrecoConsulta(especialidade){
            const vm = this;
            return axios.get(getCaminhoTestes() + "rs/filtro/especialidadeValor/"+especialidade.id)
                .then(function (response) {
                    vm.precoConsulta = response.data;
                    return vm.precoConsulta;
                })
                .catch(function (error) {
                    console.log(error);
                    return null;
                });
        },
        buscaLocalidades(especialidade){
            return [];
            /*
            const vm = this;
            if(isNullUndef(especialidade)){
                return [];
            }
            iniciaLoader("loaderAssociados");
            return axios.get(getCaminhoTestes() + "rs/filtro/localidade/"+especialidade.id)
                .then(function (response) {
                    vm.listaLocalidades = response.data;
                    encerraLoader("loaderAssociados");
                    return vm.listaLocalidades;
                })
                .catch(function (error) {
                    console.log(error);
                    encerraLoader("loaderAssociados");
                    return [];
                });*/
        },
        buscaAssociados(filtro){
            const vm = this;
            let dataAux = new Date(filtro.dataAtual.getTime());
            let localidadeAux = filtro.localidades;//variavel temporaria pra localidades
            dataAux.setDate(1);
           
            iniciaLoader("loaderAssociados");
            vm.loadingAssociados = true;
           /* let msg = {
                especialidadeId : filtro.especialidade.id,
                bairroId        : [],
                data            : dataAux.toISOString(),
                nome            : "",
            };
            return axios.post(getCaminhoTestes() + "rs/associados/busca",msg)
                .then(function (response) {
                    vm.filtro.localidades = localidadeAux;
                    vm.resultadoAssociados = vm.prepararAssociados(response.data);
                    vm.filtrarAssociados(vm.resultadoAssociados, vm.filtro);
                    vm.numeroAssociadosTotal = vm.resultadoAssociados.length;
                    vm.loadingAssociados = false;
                    if(!isNullUndef(vm.idCard)){
                        vm.$nextTick(function(){
                            vm.$root.$emit('bv::toggle::collapse', vm.idCard);
                            let card = document.getElementById(vm.idCard);
                            if(!isNullUndef(card)){
                                let top = card.documentOffsetTop() - ( window.innerHeight / 2 );
                                window.scrollTo( 0, top );
                            }
                        });
                    }
                    encerraLoader("loaderAssociados");
                    return vm.resultadoAssociados;
                }).catch(function (error) {
                    console.log(error);
                    vm.loadingAssociados = false;
                    encerraLoader("loaderAssociados");
                    return [];
                });*/
                let associados = [{"pessoaEnderecoHorario": null, "endereco": "SHIS QI 09 BLOCO E SALA 312 CENTRO CLÍNICO DO LAGO", "bairrodesc": "Setor de Habitações Individuais Sul", "uf": "DF", "credenciado": 13092132, "ordemdeChegada": false, "nomeCredenciado": "Hospital Sos Check Up de Brasilia S/C Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"endereco": "SHLS 716 BLOCO E SALA 302 CENTRO MÉDICO DE BRASÍLIA", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 14908631, "ordemdeChegada": false, "nomeCredenciado": "Salus Medicina Integrada Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLS QD 716 BLOCO E SALA 401 CENTRO MÉDICO DE BRASÍLIA", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 21156, "ordemdeChegada": true, "nomeCredenciado": "Jm Assistencia Médica S/S Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "Qse 11 Setor e Área Espec 1/17 - Centro Clínico Sala 10 - Hospital Santa Marta", "bairrodesc": "Taguatinga Sul", "uf": "DF", "credenciado": 23398, "ordemdeChegada": true, "nomeCredenciado": "R & Z Prestaçao de Serviços Medicos S/S", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QNM 17 CONJUNTO F LOTE 04 AV. HÉLIO PRATES", "bairrodesc": "Ceilândia Norte", "uf": "DF", "credenciado": 22664, "ordemdeChegada": false, "nomeCredenciado": "Equipe Saude Médica Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SGAS QD 910 CONJ B BL D SALAS 34/36 TÉRREO ED MIX PARK", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 22160, "ordemdeChegada": false, "nomeCredenciado": "Clínica Gemelli S/C Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QUADRA 02 LOTE 07 TÉRREO SETOR COMERCIAL SUL", "bairrodesc": "Setor Sul", "uf": "DF", "credenciado": 981895, "ordemdeChegada": false, "nomeCredenciado": "Medsul Serviços Médicos e Laboratoriais Ltda. Me", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SEPS EQ 705/905 CONJ B SALA T 20 TÉRREO  ED. EMPRESARIAL", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 22690, "ordemdeChegada": false, "nomeCredenciado": "Oncotek Clínica Espec em Pesquisa e Trat de Cancer Ltda.", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLS 716 BLOCO F SALA 607 EDIFÍCIO OSVALDO CRUZ", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 34087243, "ordemdeChegada": false, "nomeCredenciado": "Cbc Centro Brasiliense de Cardiologia Ltda. Me", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SDS BLOCO P No36 SALAS 213/214 ED VENÂNCIO III", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 20424, "ordemdeChegada": false, "nomeCredenciado": "Silva Oliveira & Cia Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QNA 29 LOTE 08 SALA 103 E 401 ED BRASÍLIA PLAZA CENTER", "bairrodesc": "Taguatinga Norte", "uf": "DF", "credenciado": 23042, "ordemdeChegada": true, "nomeCredenciado": "Cisse Clinica de Psicologia Ltda. - Me", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "RUA DAS FIGUEIRAS LOTE 07 SALA 1101", "bairrodesc": "Norte (Águas Claras)", "uf": "DF", "credenciado": 23568411, "ordemdeChegada": false, "nomeCredenciado": "Cardiologistas Associados de Águas Claras Ltda.", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QS 408 CONJ B BLOCO A  SALA 302", "bairrodesc": "Samambaia Norte", "uf": "DF", "credenciado": 13039289, "ordemdeChegada": false, "nomeCredenciado": "Cliped Medicina Integrada Ltda. Me", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QNM 17 CONJ H LOTE 52", "bairrodesc": "Ceilândia Sul", "uf": "DF", "credenciado": 434691, "ordemdeChegada": true, "nomeCredenciado": "F. I Teles de Souza Clínica Médica Me", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SGAS 610 CONJ F BL 2 SALAS 237/238 ED CEN MÉDICO  LÚCIO COSTA", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 18552896, "ordemdeChegada": false, "nomeCredenciado": "Primantis Dermatologia e Saúde", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLS 716 BLOCO F SALA 209 CENTRO MÉDICO DE BRASÍLIA", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 21052, "ordemdeChegada": false, "nomeCredenciado": "Vidal Instituto de Medicina Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QD 01 CONJ I LOTE 415 CONS 103 SETOR NORTE", "bairrodesc": "Setor Norte", "uf": "DF", "credenciado": 22536, "ordemdeChegada": false, "nomeCredenciado": "Cardiobrasília Centro de Atençao Integ. de Cardiologia Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLS QD.716, CONJ.L BL. 01 TORRE 1 SL. 115 CENTRO CLÍNICO SUL", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 18658050, "ordemdeChegada": false, "nomeCredenciado": "Pro Med Medicos Associados Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SGAS 610, CONJUNTO F, BLOCO 02, SALA 11", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 4758443, "ordemdeChegada": false, "nomeCredenciado": "Maxicor Clínica de Especialidades Médicas Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SGAS 910 CONJUNTO B, BLOCO A SALA 213 - ED. MIX PARK SUL", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 35294033, "ordemdeChegada": false, "nomeCredenciado": "Clinica de Pneumologia Dra. Maria Auxiliadora Eireli Me", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QNA 29 CASA 5", "bairrodesc": "Taguatinga Norte", "uf": "DF", "credenciado": 20726, "ordemdeChegada": false, "nomeCredenciado": "Clínica Santa Maria Auxiliadora Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QND 13 CASA 11", "bairrodesc": "Taguatinga Norte", "uf": "DF", "credenciado": 22718, "ordemdeChegada": false, "nomeCredenciado": "Anima Clínica Médica, Acupuntura e Fisioterapia Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "QNM 17 CONJ H LOTE 58 ED CENTER ONE", "bairrodesc": "Ceilândia Sul", "uf": "DF", "credenciado": 20170, "ordemdeChegada": false, "nomeCredenciado": "Clínica de Reabilitaçao Física de Ceilândia Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLN BL L No 43  SALA 103 ED DE CLÍNICAS II", "bairrodesc": "Asa Norte", "uf": "DF", "credenciado": 3789168, "ordemdeChegada": false, "nomeCredenciado": "Centro de Especialidades Médicas M Caparelli Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SGAS QUADRA 915, CONJUNTO O, LOTE 68A, SALA 16, 2 SS, CENTRO CLÍNICO ADVANCE", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 31421323, "ordemdeChegada": false, "nomeCredenciado": "Innvita Consultorios Medicos Ltda. - Me", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "CSB 02 LOTES 01/02 TORRE A, 10o ANDAR - COBERTURA", "bairrodesc": "Taguatinga Sul", "uf": "DF", "credenciado": 23155094, "ordemdeChegada": false, "nomeCredenciado": "Centro de Especialidades Médicas Bem Viver Ltda. Me", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "Qse 11 Setor e Área Espec 1/17 - Centro Clínico Sala 10 - Hospital Santa Marta", "bairrodesc": "Taguatinga Sul", "uf": "DF", "credenciado": 5529538, "ordemdeChegada": true, "nomeCredenciado": "Clínica Rjs Serviços Medicos Ambulatoriais Eireli Epp", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLN 516, BLOCO J, SALAS 201 E 2014", "bairrodesc": "Asa Norte", "uf": "DF", "credenciado": 39042259, "ordemdeChegada": false, "nomeCredenciado": "Clínica de Multiespecialidades Arrais Ltda.", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SGAS 614 CONJ C SALA 201/202 CENTRO CLÍNICO VITRIUM", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 24301062, "ordemdeChegada": false, "nomeCredenciado": "Nery Clínica de Dermatologia Eireli Me", "prestador": null, "nomePrestador": null, "sexo": "F", "agendaApontamentos": [] }, {"pessoaEnderecoHorario": null, "endereco": "SHLS 716 CJ L CENTRO CLÍNICO SUL TORRE I SL L-307", "bairrodesc": "Asa Sul", "uf": "DF", "credenciado": 20902, "ordemdeChegada": false, "nomeCredenciado": "Clínica de Endocrinologia Dr José Alves Ltda.", "prestador": null, "nomePrestador": null, "sexo": "M", "agendaApontamentos": [] } ]
                vm.filtro.localidades = localidadeAux;
                vm.resultadoAssociados = vm.prepararAssociados(associados);
                vm.filtrarAssociados(vm.resultadoAssociados, vm.filtro);
                vm.numeroAssociadosTotal = vm.resultadoAssociados.length;
                vm.loadingAssociados = false;
                if(!isNullUndef(vm.idCard)){
                    vm.$nextTick(function(){
                        vm.$root.$emit('bv::toggle::collapse', vm.idCard);
                        let card = document.getElementById(vm.idCard);
                        if(!isNullUndef(card)){
                            let top = card.documentOffsetTop() - ( window.innerHeight / 2 );
                            window.scrollTo( 0, top );
                        }
                    });
                }
                encerraLoader("loaderAssociados");
                return vm.resultadoAssociados;
        },
        agendarConsulta(associado, data){
            const vm = this;
            let dataAgendamento;
            let agendamento = [];
            if(!associado.ordemdeChegada){
                associado.ordemChegadaInicio =  null;
                associado.ordemChegadaFim =  null;
                dataAgendamento = data.inicio;
            } else {
                dataAgendamento = data.getTime();
                agendamento = vm.retiraCamposAgendaApontamento(associado.agendaApontamentos, dataAgendamento);
            }
            vm.salvaPesquisaLocalStorage(associado, vm.filtro.especialidade, agendamento,dataAgendamento);
            changeLocation(getCaminhoTestes() + "pages/compra.html");
        },
        checkLocalStorage(){
            const vm = this;

        },
        salvaPesquisaLocalStorage(associado, especialidade, agendamento,dataAgendamento){
            const vm = this;
            localStorage.clear();
            //filtro
            saveLocalStorage("filtroAssociados",vm.filtro);
            //associado
            let associadoAux = {
                agendaApontamentos       : associado.agendaApontamentos,
                associadoEnderecoHorario : associado.associadoEnderecoHorario,
                bairrodesc               : associado.bairrodesc,
                contato                  : associado.contato,
                endereco                 : associado.endereco,
                iniciosOrdemChegada      : associado.iniciosOrdemChegada,
                credenciado              : associado.credenciado,
                mostrarAssociado         : associado.mostrarAssociado,
                //mostrarAssociadoCal      : associado.mostrarAssociadoCal,
                nomeCredenciado          : associado.nomeCredenciado,
                nomePrestador            : associado.nomePrestador,
                ordemChegadaFim          : associado.ordemChegadaFim,
                ordemChegadaInicio       : associado.ordemChegadaInicio,
                ordemdeChegada           : associado.ordemdeChegada,
                dataConsulta             : dataAgendamento,
                prestador                : associado.prestador,
                uf                       : associado.uf,
                idCard                   : associado.prestador+'_'+associado.credenciado,
            };
            saveLocalStorage("associado",associadoAux);
            saveLocalStorage("ultimaPagina","associados.html");
        },
        retiraCamposAgendaApontamento(agenda, dataAgendamento){
            let agendaApontamentos = [];
            let dataRef = new Date(dataAgendamento);
            for(let i = 0; i< agenda.length; i++){
                let inicio = new Date(agenda[i].inicio)
                if(inicio.getDate() == dataRef.getDate()
                    && inicio.getDate() == dataRef.getDate()
                    && inicio.getDate() == dataRef.getDate()){
                    let agendaAux = {};
                    agendaAux.fim = new Date(agenda[i].fim).toISOLocal();
                    agendaAux.inicio = new Date(agenda[i].inicio).toISOLocal();
                    agendaApontamentos.push(agendaAux);
                }
            }
            return agendaApontamentos;
        },
        prepararAssociados(associados){
            const vm = this;
            let resultado = [];
            let idHorario = 0;
            for(let i = 0; i < associados.length; i++){
                //fazendo uma cópia dos associados, exceto a agendaApontamentos
                let obj = {
                    agendaApontamentos       : [],
                    //iniciosOrdemChegada      : {},
                    bairrodesc               : associados[i].bairrodesc,
                    contato                  : associados[i].contato,
                    endereco                 : associados[i].endereco,
                    credenciado              : associados[i].credenciado,
                    prestador                : associados[i].prestador,
                    nomeCredenciado          : associados[i].nomeCredenciado,
                    nomePrestador            : associados[i].nomePrestador,
                    ordemdeChegada           : associados[i].ordemdeChegada,
                    associadoEnderecoHorario : associados[i].pessoaEnderecoHorario,
                    uf                       : associados[i].uf,
                    mostrarAssociado         : false,
                    //mostrarAssociadoCal      : false,
                };
                // Ja que nao existe a funcao de localidades, salva as localidades existentes no array
                if (vm.listaLocalidades.indexOf(associados[i].bairrodesc) == -1) 
                    vm.listaLocalidades.push(associados[i].bairrodesc);

                resultado.push(obj);
            }
            vm.filtro.localidades =  vm.listaLocalidades;
            return resultado;
        },
        filtrarAssociados(associados, filtro){
            const vm = this;
            let totalAssociadosFiltrados = 0;

            for(let i = 0; i < associados.length; i++){
                let obj = associados[i];
                obj.mostrarAssociado = false;
                obj.mostrarAssociadoCal = false;
                //filtrando nome
                if(filtro.nomeProfissional.length > 0
                    && !obj.nomeCredenciado.includesIgnoreCase(filtro.nomeProfissional)){
                    continue;//se deu errado, já vamos pro próximo
                }

                //filtrando localidade
                let achou = false;
                for(let i = 0; i < filtro.localidades.length; i++){
                    if(obj.bairrodesc.compareIgnoreCase(filtro.localidades[i]) === 0){
                        achou = true;
                        break;
                    }
                }
                if(!achou && filtro.localidades.length > 0)
                    continue;//se deu errado, já vamos pro próximo
                
                obj.mostrarAssociado = true;       
                totalAssociadosFiltrados++;
            }
            vm.numeroAssociadosEncontrados = totalAssociadosFiltrados;
            return totalAssociadosFiltrados;
        },
        mudarFiltro(filtro, localidadesSelecionadas){
            const vm = this;
            //vm.dataAtualStringExtra = filtro.dataFiltro;
            vm.filtrarAssociados(vm.resultadoAssociados, filtro);
        },
        onClose() {
            const vm = this;
            vm.popoverShow = false;
        },
    },
    /* ON LOAD */
    created: function() {
        const vm = this;
        window.scroll(0,0);

        let filtro = loadLocalStorage("filtroAssociadosSemAgenda");
        if(filtro !== null){
            vm.filtro = filtro;
            let auxLocalidades = [];
            for (let i = 0; i < vm.filtro.localidades.length; i++) {
                auxLocalidades.push(vm.filtro.localidades[i].id);
            }
            vm.localidadesSelecionadas = auxLocalidades;
        }

        vm.dataAtual = new Date();
        vm.dataAtual.setHours(0,0,0,0);
        vm.dataAtual.addDays(1);

        if(vm.checkLocalStorage()){
            vm.associado = localStorage.associado;
            //vm.filtro = localStorage.dataFiltro;
        }

        vm.filtro.dataAtual = vm.dataAtual;
        vm.dataAtualString = vm.dataAtual.parsePTBR();

        vm.precoConsulta = vm.filtro.servico.valor;
        //vm.buscaPrecoConsulta(vm.filtro.especialidade);
        vm.buscaLocalidades(); // TODO implementar quando funcao de localidade existir
        vm.$nextTick();
        vm.buscaAssociados(vm.filtro);
    }
});

Element.prototype.documentOffsetTop = function () {
    return this.offsetTop + ( this.offsetParent ? this.offsetParent.documentOffsetTop() : 0 );
};

var menuSuperior = new Vue({
    el : '#menuSuperior',
    components: {
        'header-cm': httpVueLoader('resources/components/header-cm.vue')
    },
    data: {}
});
var rodape = new Vue({
    el : '#rodape',
    components: {
        'footer-cm': httpVueLoader('resources/components/footer-cm.vue')
    },
    data: {}
});