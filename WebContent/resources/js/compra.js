"use strict"  
var compraVue = new Vue({
    el : '#appCompra',
    components: {
         'compra-cartao': httpVueLoader('../resources/components/compra-cartao.vue'), 
         'compra-boleto': httpVueLoader('../resources/components/botao-boleto.vue'),
         'vale-compra': httpVueLoader('../resources/components/vale-compra.vue')
    },
    data : {
        idAssociado                 : 1,
        dataEscolhida               : "",
        dataEscolhidaString         : "",
        associado                   : null,
        filtro                      : null,

        data                        : "",
        precoConsulta               : "0,00",
        sucessoPagamento            : false,
        aguardePagamento            : false,
        erroPagamento               : false,
        mensagemErro                : '',
        tipoErro                    : '',
        compra                      : undefined,
        tipoCompra                  : undefined,
        paciente                    : {},
        usuario                     : {},
        boleto                      : undefined,
        dia                         : 345600000,
        hoje                        :"",
        D4                          :"",
        limite                      :"",
        valeCompra                  :false
    },
    mounted: function(){
    
    },
    methods: {
        pagarCartao(payment, paciente){
            const vm = this;
            let dataAgendada = vm.data;
            let compra = {
                usuario: {
                    //TODO apagar depois que a API for atualizada
                    str_cep                 : paciente.cep,
                    str_cpf                 : paciente.cpf,
                    str_endereco            : paciente.endereco,
                    //dados do beneficiario
                    str_email               : paciente.email,
                    str_nome                : paciente.nome,
                    str_telefone            : paciente.telefone,
                },
                date_consulta               : dataAgendada.toISOLocal(),
                str_matricula_prestador     : vm.associado.prestador,
                str_matricula_credenciado   : vm.associado.credenciado,
                str_especialidade           : vm.filtro.especialidade.id,
                str_associado_endereco      : vm.associado.associadoEnderecoHorario, // Campo para diferenciar a localidade que o medico atende no AMHP
                payment                     : payment,
                dec_valor_consulta          : parseFloat(vm.precoConsulta),
                ordemDeChegada              : vm.associado.ordemdeChegada   //TODO padronizar nome de ordem de chegada
            };
            console.log(compra);
            vm.erroPagamento = false;
            vm.aguardePagamento = true;
            iniciaLoader("loaderCompra","dadoPessoaisCompra", null);
            vm.scrollTop(250);
            return axios.post(getCaminhoTestes() + "../rs/pagamento/private/compraPS", compra)
                .then(function (response) {
                    vm.sucessoPagamento = true;
                    vm.aguardePagamento = false;
                    localStorage.clear();
                    encerraLoader("loaderCompra","dadoPessoaisCompra", null); 
                    return true;
                }).catch(function (error) {
                    vm.sucessoPagamento = false;                
                    vm.aguardePagamento = false;
                    vm.erroPagamento = true;
                    vm.mensagemErro = error.response.data;
                    vm.tipoErro =  error.response.status;
                    // vm.mensagemErro = "Não foi possível efetuar o pagamento!";
                    if(isNullUndef(vm.mensagemErro))
                        vm.mensagemErro = "Não foi possível efetuar o pagamento!";
                    encerraLoader("loaderCompra","dadoPessoaisCompra", null);
                    return false;

                });
        },
        buscaPrecoConsulta(especialidade){
            const vm = this;
            return axios.get(getCaminhoTestes() + "../rs/filtro/especialidadeValor/"+especialidade.id)
                .then(function (response) {
                    vm.precoConsulta = response.data;
                    return vm.precoConsulta;
                })
                .catch(function (error) {
                    console.log(error);
                    return null;
                });
        },
        voltarIndex(){
            changeLocation(getCaminhoTestes()+ "../index.html");
        },
        baixarBoleto(){
            const vm = this;
        },
        copiarCodigoBarra(){
            const vm = this;
        },
        scrollTop(scrollDuration) {
            let scrollStep = -window.scrollY / (scrollDuration / 15),
                scrollInterval = setInterval(function(){
                if ( window.scrollY != 0 ) {
                    window.scrollBy( 0, scrollStep );
                }
                else clearInterval(scrollInterval); 
            },15);
        },

        /**
         * Monta objeto de compra
         */
        montarCompra() {
            const vm = this;
            // Deletando as propriedades não necessários para o request
            delete vm.usuario.id_usuario;
            delete vm.usuario.data_nascimento;
            delete vm.usuario.data_token;
            delete vm.usuario.data_registro;
            delete vm.usuario.compras;
            delete vm.usuario.bool_verificado;
            delete vm.usuario.str_token_verificacao;
            delete vm.usuario.str_senha;
            
            // Monta objeto de retorno
            vm.compra = {
                usuario                     : vm.usuario,
                date_consulta               : vm.data.toISOLocal(), 
                str_matricula_prestador     : vm.associado.prestador,
                str_matricula_credenciado   : vm.associado.credenciado,
                str_especialidade           : vm.filtro.especialidade.id,
                str_associado_endereco      : vm.associado.associadoEnderecoHorario,
                dec_valor_consulta          : parseFloat(vm.precoConsulta),
                ordemDeChegada              : vm.associado.ordemdeChegada
            };
        },

        /**
         * Busca os dados do usuário logado
         */
        buscaDadosUsuario() {

            const vm = this;

            axios
                .get(getCaminhoTestes() + "../rs/cadastro/private/buscar")
                .then(function (response) {

                    // Seta os dados do usuário
                    vm.usuario = response.data;

                    // Monta o objeto de compra
                    vm.montarCompra();
                })
                .catch(function (error) {

                    console.log(error);
                    return null;
                });
        },

        /**
         * Trata boleto recebido do componente filho
         */
        getBoleto(e) {
            this.boleto = e;
        }
    },
    /* ON LOAD */
    created: function() {
        console.log("teste");
        const vm = this;
        let filtro = loadLocalStorage("filtroAssociados");
        let associado = loadLocalStorage("associado");
        if(associado != null && filtro != null){
            vm.filtro = filtro;
            vm.associado = associado;
            vm.buscaPrecoConsulta(filtro.especialidade);
            vm.data = new Date(associado.dataConsulta);
            if(associado.ordemChegadaInicio !== null){
                let horario = associado.ordemChegadaInicio;
                vm.data.setHours(horario.substr(0,2), horario.substr(3,4));
                
                let options = {weekday:"long",month:'numeric', day:'numeric'};
                vm.dataString = vm.data.toLocaleDateString("pt-BR",options);
                vm.dataString += " por ordem de chegada a partir das " + vm.associado.ordemChegadaInicio;
            } else{
                let options = {weekday:"long",month:'numeric', day:'numeric'};
                vm.dataString = vm.data.toLocaleDateString("pt-BR",options);
                vm.dataString = vm.dataString.concat(" às " + vm.data.formatDate("HM"));
            }
        } else {
            changeLocation(getCaminhoTestes()+ "../index.html");
        }

        this.buscaDadosUsuario();
        this.montarCompra();
        let options = {weekday:"long",month:'numeric', day:'numeric'};
        vm.hoje = new Date().getTime() ;   
        vm.D4 =    vm.hoje + vm.dia;
        vm.limite = new Date( vm.D4);
        vm.limite = vm.limite.toLocaleDateString("pt-BR",options);

    }
});
var menuSuperior = new Vue({
    el : '#menuSuperior',
    components: {
        'header-cm': httpVueLoader('../resources/components/header-cm.vue')
    },
    data: {
    }
});
var rodape = new Vue({
    el : '#rodape',
    components: {
        'footer-cm': httpVueLoader('../resources/components/footer-cm.vue')
    },
    data: {
    }
});


