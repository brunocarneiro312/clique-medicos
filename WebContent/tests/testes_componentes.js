"use strict";
var testesVue = new Vue({
    el : '#testes',
    components: {
        'multi-select': httpVueLoader('../resources/components/multi-select.vue'),
    },
    data : {
        multiSelect : {
            valores : [],
            selecionados : [],
        },
    },
    mounted: function(){
    },
    methods: {
    },
    created: async function() {
        const vm = this;
        for(let i = 0; i < 10; i++){
            let obj = {id:i,valor:"valor "+i};
            vm.multiSelect.valores.push(obj);
        }
    }
});