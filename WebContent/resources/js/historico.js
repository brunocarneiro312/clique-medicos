
var historicoVue = new Vue({
    el : '#historico',
    components: {
    },
    data: {
        historico : [],
    },
    methods: {
        buscaHistorico(){
            const vm = this;
            iniciaLoader("loaderHistorico", "cardsHistorico", null);
            return axios.get(getCaminhoTestes() + "../rs/compras/private/historico")
                .then(function (response) {
                    vm.historico = response.data;
                    for(let i = 0; i < vm.historico.length; i++){
                        let auxConsultaDate = new Date().fromLocalDateTime(vm.historico[i].date_consulta);
                        let auxRegistroDate = new Date().fromLocalDateTime(vm.historico[i].date_registro);
                        vm.historico[i].dataConsultaString = auxConsultaDate.formatDate("dmyHM");
                        vm.historico[i].dataRegistroString = auxRegistroDate.formatDate("dmyHM");
                    }
                    encerraLoader("loaderHistorico", "cardsHistorico", null); 
                    return response.data;
                })
                .catch(function (error) {
                    if(error.response.status ==  404)
                        changeLocation("../registro.html");
                    encerraLoader("loaderHistorico", "cardsHistorico", null);    
                    return [];
                });
        },
    },
    created: function() {
        const vm = this;
        vm.buscaHistorico();
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


