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
        dataAtualStringExtra        : "",
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
            for(let j = 0; j < vm.localidadesSelecionadas.length; j++){
                for(let i = 0; i < vm.listaLocalidades.length; i++){
                    if(vm.localidadesSelecionadas[j] == vm.listaLocalidades[i].id){
                        vm.filtro.localidades.push(vm.listaLocalidades[i]);
                    }
                }
            }
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
        buscaOuFiltraAssociados(dataHoje,ultimaData){
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
        },


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
                });
        },
        buscaAssociados(filtro){
            const vm = this;
            let dataAux = new Date(filtro.dataAtual.getTime());
            let localidadeAux = filtro.localidades;//variavel temporaria pra localidades
            dataAux.setDate(1);
            let msg = {
                especialidadeId : filtro.especialidade.id,
                bairroId        : [],
                data            : dataAux.toISOString(),
                nome            : "",
            };
            iniciaLoader("loaderAssociados");
            vm.loadingAssociados = true;
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
                });
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
                mostrarAssociadoCal      : associado.mostrarAssociadoCal,
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
            let resultado = [];
            let idHorario = 0;
            for(let i = 0; i < associados.length; i++){
                //fazendo uma cópia dos associados, exceto a agendaApontamentos
                let obj = {
                    agendaApontamentos       : [],
                    iniciosOrdemChegada      : {},
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
                    mostrarAssociadoCal      : false,
                };

                let agenda = associados[i].agendaApontamentos;
                let inicioChegada;
                for(let j = 0; j < agenda.length; j++){
                    let dataInicial;
                    let dataFinal;

                    if(isNumber(agenda[j].inicio))
                        dataInicial = new Date(agenda[j].inicio);
                    else
                        dataInicial = new Date().fromLocalDateTime(agenda[j].inicio);

                    if(isNumber(agenda[j].fim))
                        dataFinal = new Date(agenda[j].fim);
                    else
                        dataFinal = new Date().fromLocalDateTime(agenda[j].fim);

                    //preparando inicioChegada e fimChegada para o caso de ordem de chegada
                    let aux = obj.iniciosOrdemChegada[""+dataInicial.getDate()];
                    if(aux === undefined || aux.getTime() > dataInicial.getTime()){
                        obj.iniciosOrdemChegada[""+dataInicial.getDate()] = dataInicial;
                    }

                    //preparando o objeto horario da agenda
                    let horario = {
                        id           : idHorario,
                        inicioString : dataInicial.formatDate("HM"),
                        fimString    : dataFinal.formatDate("HM"),
                        inicio       : dataInicial.getTime(),
                        fim          : dataFinal.getTime(),
                        mostrar      : false,
                        mostrarCal   : false,
                    };
                    obj.agendaApontamentos.push(horario);
                    idHorario++;
                }
                resultado.push(obj);
            }
            return resultado;
        },
        filtrarAssociados(associados,filtro){
            const vm = this;
            let totalAssociadosFiltrados = 0;
            let houveMudanca = false;

            for(let i = 0; i < associados.length; i++){
                let obj = associados[i];
                obj.mostrarAssociado = false;
                obj.mostrarAssociadoCal = false;
                //filtrando nome
                if(filtro.nomeProfissional.length > 0
                    && !obj.nomePrestador.includesIgnoreCase(filtro.nomeProfissional)){
                    continue;//se deu errado, já vamos pro próximo
                }

                //filtrando localidade
                let achou = false;
                for(let i = 0; i < filtro.localidades.length; i++){
                    if(obj.bairrodesc.compareIgnoreCase(filtro.localidades[i].descricao) === 0){
                        achou = true;
                        break;
                    }
                }
                if(!achou && filtro.localidades.length > 0){
                    continue;//se deu errado, já vamos pro próximo
                }

                let dataMinima = new Date();
                dataMinima.addDays(1);

                let agenda = associados[i].agendaApontamentos;
                for(let j = 0; j < agenda.length; j++){
                    let horario = agenda[j];
                    horario.mostrar = false;
                    horario.mostrarCal = false;

                    let dataMinimaAux = new Date(dataMinima);
                    dataMinimaAux.setHours(0,0,0,1);
                    let horarioAgenda = new Date(horario.inicio);
                    if (horarioAgenda.compare(dataMinimaAux,"dmy") >= 0){
                        if(!isNullUndef(filtro.dataFiltro) && filtro.dataFiltro !== ""){
                            let hora = parseInt(filtro.dataFiltro.split(':')[0]);
                            let minuto = parseInt(filtro.dataFiltro.split(':')[1]);
                            if (hora < horarioAgenda.getHours()
                                || (hora === horarioAgenda.getHours() && minuto < horarioAgenda.getMinutes())
                                || (hora === horarioAgenda.getHours() && minuto === horarioAgenda.getMinutes())){
                                if(horarioAgenda.getDate() === filtro.dataAtual.getDate()){
                                    horario.mostrar = true;
                                    obj.mostrarAssociado = true;
                                }
                                horario.mostrarCal = true;
                                obj.mostrarAssociadoCal = true;
                                houveMudanca = true;
                            } else {
                                if(horarioAgenda.getDate() === filtro.dataAtual.getDate())
                                    horario.mostrar = false;

                                horario.mostrarCal = false;
                                houveMudanca = true;
                            }
                        } else {
                            if(horarioAgenda.getDate() === filtro.dataAtual.getDate()){
                                horario.mostrar = true;
                                obj.mostrarAssociado = true;
                            }
                            horario.mostrarCal = true;
                            obj.mostrarAssociadoCal = true;
                            houveMudanca = true;
                        }
                    }
                }
                let aux = obj.iniciosOrdemChegada[""+filtro.dataAtual.getDate()];
                if(aux !== undefined)
                    obj.ordemChegadaInicio = aux.formatDate("HM");
                if(obj.mostrarAssociado)
                    totalAssociadosFiltrados++;
            }
            if(houveMudanca){
                vm.associadosCalendario = vm.resultadoAssociados.slice(0);
            }
            vm.numeroAssociadosEncontrados = totalAssociadosFiltrados;
            return totalAssociadosFiltrados;
        },
        mudarFiltro(filtro,localidadesSelecionadas){
            const vm = this;
            vm.dataAtualStringExtra = filtro.dataFiltro;
            vm.filtrarAssociados(vm.resultadoAssociados,filtro);
        },
        onClose() {
            const vm = this;
            vm.popoverShow = false;
        },

        /**
         *
         * ----------------------------------------
         * Adiciona N dias à variável vm.dataAtual.
         * ----------------------------------------
         *
         * @param quantidadeDeDias
         *      Quantidade de dias a serem adicionados.
         *      - Obrigatoriamente deve ser um número maior que 0.
         *      - SEMPRE adiciona a quantidade de dias em relação a data de HOJE (new Date()).
         */
        adicionarDias(dataInicial, quantidadeDeDias) {
            return dataInicial.addDays(quantidadeDeDias);
        },

        /**
         *
         * ----------------------------------------
         * Subtrai N dias da variável vm.dataAtual.
         * ----------------------------------------
         *
         * @param quantidadeDeDias
         *      Quantidade de dias a serem subtraidos.
         *      - Obrigatoriamente deve ser um número menor que 0.
         */
        subtrairDias(dataInicial, quantidadeDeDias) {
            return dataInicial.addDays(-quantidadeDeDias);
        },

        /**
         * ----------------------
         * Vai para o próximo dia
         * ----------------------
         */
        irParaODiaSeguinte() {
            const vm = this;
            this.dataAtual       = this.adicionarDias(this.dataAtual,1);
            this.dataAtualString = this.dataAtual.parsePTBR();
        },

        /**
         * -----------------------
         * Vai para o dia anterior
         * -----------------------
         */
        irParaODiaAnterior() {
            const vm = this;
            // Não pode voltar dias anteriores à data de hoje
            if (this.dataAtual.getTime() <= new Date().getTime()) {
                return;
            }

            this.dataAtual       = this.subtrairDias(this.dataAtual, 1);
            this.dataAtualString = this.dataAtual.parsePTBR();
        },
    },
    /* ON LOAD */
    created: function() {
        const vm = this;
        window.scroll(0,0);

        let filtro = loadLocalStorage("filtroAssociados");
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
            vm.filtro = localStorage.dataFiltro;
        }

        vm.filtro.dataAtual = vm.dataAtual;

        vm.dataAtualString = vm.dataAtual.parsePTBR();
        vm.buscaPrecoConsulta(vm.filtro.especialidade);
        vm.buscaLocalidades(vm.filtro.especialidade);
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