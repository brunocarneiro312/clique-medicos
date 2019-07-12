
var registro = new Vue({
    el : '#registro',
    components: {
    },
    data: {
        estado : 0,
    },
    methods: {
    },
    created: function() {
        const vm = this;
        let aux = queryParameters(document.location.search);
        if (aux.estado !== undefined){
            vm.estado = parseInt(aux.estado[0]);
        }
    }
});

var menuSuperior = new Vue({
    el : '#menuSuperior',
    components: {
        'header-cm': httpVueLoader('resources/components/header-cm.vue')
    },
    data: {

    }
});
var rodape = new Vue({
    el : '#rodape',
    components: {
        'footer-cm': httpVueLoader('resources/components/footer-cm.vue')
    },
    data: {
    }
});