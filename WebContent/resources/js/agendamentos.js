
var agendamentoVue = new Vue({
    el : '#agendamento',
    components: {
    },
    data: {
        agendamentos : [],
    },
    methods: {
        buscaAgendamentos(){
            const vm = this;
            iniciaLoader("loaderAgendamento", "cardsAgendamento", null);
            return axios.get(getCaminhoTestes() + "../rs/compras/private/agendamentos")
                .then(function (response) {
                    vm.agendamentos = response.data;
                    for(let i = 0; i < vm.agendamentos.length; i++){
                        let auxConsultaDate = new Date().fromLocalDateTime(vm.agendamentos[i].date_consulta);
                        let auxRegistroDate = new Date().fromLocalDateTime(vm.agendamentos[i].date_registro);
                        vm.agendamentos[i].dataConsultaString = auxConsultaDate.formatDate("dmyHM");
                        vm.agendamentos[i].dataRegistroString = auxRegistroDate.formatDate("dmyHM");
                    }
                    encerraLoader("loaderAgendamento", "cardsAgendamento", null);
                    return response.data;

                })
                .catch(function (error) {
                    if(error.response.status ==  404)
                        changeLocation("../registro.html");
                    encerraLoader("loaderAgendamento", "cardsAgendamento", null);    
                    return [];
                    
                });
        },
    },
    created: function() {
        const vm = this;
        vm.buscaAgendamentos();
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


