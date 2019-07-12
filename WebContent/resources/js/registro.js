var registro = new Vue({
    el : '#registro',
    components: {
        'login': httpVueLoader('resources/components/login.vue'), 
        'cadastro': httpVueLoader('resources/components/cadastro.vue'), 
    },
    data: {
        teste: "TESTE",
        cadastrando : false,
    },
    methods: {
        toggleCadastro(obj){
            const vm = this;
            vm.cadastrando = obj;
        },
    },
    created: function() {
        const vm = this;
        let aux = queryParameters(document.location.search);
        if (aux.cadastrando !== undefined){
            vm.cadastrando = aux.cadastrando[0] === 'true' ? true : false;
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