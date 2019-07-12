"use strict"
const URLBLOG = 'http://3.130.32.18/wp-json/wp/v2/';
// const URLBLOG = 'https://www.cliquemedicos.com.br/blog/wp-json/wp/v2/';
var menuSuperior = new Vue({
    el : '#menuSuperior',
    components: {
        'header-cm': httpVueLoader('resources/components/header-cm.vue')
    },
    data: {
    }
});
var newsLetter = new Vue({
    el : '#newsLetter',
    data: {
    }
});
var rodape = new Vue({
    el : '#rodape',
    components: {
        'footer-cm': httpVueLoader('resources/components/footer-cm.vue')
    },
    data: {
    },
    methods: {
        scrollToTop(scrollDuration) {
            console.log(scrollDuration);
            var scrollStep = -window.scrollY / (scrollDuration / 15),
                scrollInterval = setInterval(function(){
                if ( window.scrollY != 0 ) {
                    window.scrollBy( 0, scrollStep );
                }
                else clearInterval(scrollInterval); 
            },15);
        }

    }
});

Vue.component('v-select', VueSelect.VueSelect)
var indexVue = new Vue({
    el : '#appIndex',
    components: {
        
    },
    data : {
        nomeProfissional            : '',
        especialidadeSelecionada    : '',
        localidadeSelecionada       : [],
        listaEspecialidades         : [],
        listaLocalidades            : [],
        dismissCountDown            : 0,
        dismissSecs                 : 10,
        showDismissibleAlert        : false,
        erro                        : false,
        naoPodeBuscar               : true,
        msgErro                     : '',
        placeholder: {
            type: String,
            default: "Escolha o serviço"
        },
        mockServicos: [
            {
              "id": 39,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Alergia e Imunologia Pediátrica",
              "valor": 126,
              "especialidade": -864068,
              "nomeEspecialidade": "Alergia e Imunologia Pediátrica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 38,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Neurologia Pediátrica",
              "valor": 126,
              "especialidade": -864164,
              "nomeEspecialidade": "Neurologia Pediátrica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 37,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Pediatria",
              "valor": 126,
              "especialidade": -864184,
              "nomeEspecialidade": "Pediatria",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 36,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Ortopedia e Traumatologia",
              "valor": 150,
              "especialidade": -864176,
              "nomeEspecialidade": "Ortopedia e Traumatologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 35,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Ginecologia e Obstetrícia",
              "valor": 150,
              "especialidade": -864134,
              "nomeEspecialidade": "Ginecologia e Obstetrícia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 34,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Gastroenterologia Pediátrica",
              "valor": 126,
              "especialidade": -864130,
              "nomeEspecialidade": "Gastroenterologia Pediátrica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 33,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Urologia",
              "valor": 150,
              "especialidade": -864231,
              "nomeEspecialidade": "Urologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 32,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Nutrologia",
              "valor": 150,
              "especialidade": -864168,
              "nomeEspecialidade": "Nutrologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 31,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Infectologia",
              "valor": 150,
              "especialidade": -864144,
              "nomeEspecialidade": "Infectologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 30,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Psiquiatria",
              "valor": 150,
              "especialidade": -864213,
              "nomeEspecialidade": "Psiquiatria",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 29,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Cardiologia",
              "valor": 150,
              "especialidade": -864078,
              "nomeEspecialidade": "Cardiologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 28,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Endocrinologia Pediátrica",
              "valor": 126,
              "especialidade": -864115,
              "nomeEspecialidade": "Endocrinologia Pediátrica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 27,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Angiologia e Cirurgia Vascular",
              "valor": 150,
              "especialidade": -864073,
              "nomeEspecialidade": "Angiologia e Cirurgia Vascular",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 26,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Oftalmologia",
              "valor": 150,
              "especialidade": -864170,
              "nomeEspecialidade": "Oftalmologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 25,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Otorrinolaringologia",
              "valor": 150,
              "especialidade": -864181,
              "nomeEspecialidade": "Otorrinolaringologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 24,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Endocrinologia",
              "valor": 150,
              "especialidade": -864114,
              "nomeEspecialidade": "Endocrinologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 23,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Clínica Médica",
              "valor": 150,
              "especialidade": -864101,
              "nomeEspecialidade": "Clínica Médica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 22,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Neurologia",
              "valor": 150,
              "especialidade": -864163,
              "nomeEspecialidade": "Neurologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 21,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Reumatologia",
              "valor": 150,
              "especialidade": -864219,
              "nomeEspecialidade": "Reumatologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 20,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Fonoaudiologia",
              "valor": 80,
              "especialidade": -864126,
              "nomeEspecialidade": "Fonoaudiologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 19,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Neurocirurgia",
              "valor": 150,
              "especialidade": -864160,
              "nomeEspecialidade": "Neurocirurgia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 18,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Mastologia",
              "valor": 150,
              "especialidade": -864148,
              "nomeEspecialidade": "Mastologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 17,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Alergia E Imunologia",
              "valor": 150,
              "especialidade": -864067,
              "nomeEspecialidade": "Alergia E Imunologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 16,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Dermatologia",
              "valor": 150,
              "especialidade": -864105,
              "nomeEspecialidade": "Dermatologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 15,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Pneumologia Pediátrica",
              "valor": 126,
              "especialidade": -864246,
              "nomeEspecialidade": "Pneumologia Pediátrica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 14,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Acupuntura",
              "valor": 150,
              "especialidade": -864066,
              "nomeEspecialidade": "Acupuntura",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 13,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Cardiologia Pediátrica",
              "valor": 126,
              "especialidade": -864079,
              "nomeEspecialidade": "Cardiologia Pediátrica",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 12,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Gastroenterologia",
              "valor": 150,
              "especialidade": -864129,
              "nomeEspecialidade": "Gastroenterologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 11,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Pneumologia",
              "valor": 150,
              "especialidade": -864185,
              "nomeEspecialidade": "Pneumologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 10,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Psicologia",
              "valor": 150,
              "especialidade": -864190,
              "nomeEspecialidade": "Psicologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 9,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Coloproctologia",
              "valor": 150,
              "especialidade": -864186,
              "nomeEspecialidade": "Coloproctologia",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 8,
              "codigo": "10101012",
              "descricao": "Consulta em Consultório com Nutrição",
              "valor": 80,
              "especialidade": -864166,
              "nomeEspecialidade": "Nutrição",
              "tipo": "Consulta",
              "podeAgendar": true
            },
            {
              "id": 7,
              "codigo": "40901483",
              "descricao": "Doppler Colorido Arterial ou Venoso de Membro Inferior - Bilateral",
              "valor": 450,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 6,
              "codigo": "40901475",
              "descricao": "Doppler Colorido Arterial ou Venoso de Membro Inferior - Unilateral",
              "valor": 250,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 5,
              "codigo": "40201082",
              "descricao": "Colonoscopia (inclui Material, Medicamento, Biópsia, Pinça e Anestesista)",
              "valor": 850,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 4,
              "codigo": "20104065",
              "descricao": "Remoção de Cerume (bilateral)",
              "valor": 18,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 3,
              "codigo": "40201210",
              "descricao": "Vídeo Naso-sinusal ou Faringo-laringoscopia - Flexível",
              "valor": 180,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 2,
              "codigo": "20102011",
              "descricao": "Holter ou Mapa de 24 Horas",
              "valor": 178,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 1,
              "codigo": "40101037",
              "descricao": "Teste Ergométrico",
              "valor": 142,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            },
            {
              "id": 0,
              "codigo": "30907063",
              "descricao": "Escleroterapia de Veias - Por Sessão",
              "valor": 150,
              "especialidade": null,
              "nomeEspecialidade": null,
              "tipo": "Exame",
              "podeAgendar": false
            }
          ]
        
            
    },
    mounted: function(){},
    methods: {

        onVSelectChange(event) {
            this.buscaLocalidades(event.especialidade);
        },

        buscaLocalidades(idEspecialidade){
            console.log(idEspecialidade);
            const vm = this;
            vm.listaLocalidades = [];
            if(!isNullUndef(idEspecialidade)){          
        
            let select = document.getElementById("localidade");
            if(!isNullUndef(select)){
                select.disabled = true;
                select.children[0].innerHTML = "Escolha a localidade.";
            }

            iniciaLoader("loaderLocalidade", "localidade", "botaoBusca");
            return axios.get(getCaminhoTestes() + "rs/filtro/localidade/"+idEspecialidade)
                .then(function (response) {
                    if(typeof response.data === "string"){
                        vm.listaLocalidades = JSON.parse(response.data);
                    } else {
                        vm.listaLocalidades = response.data;
                    }
                    let select = document.getElementById("localidade");
                    if(!isNullUndef(select))
                        select.disabled = false;
                    vm.listaLocalidades = vm.listaLocalidades.sort(sortLexDescricao)
                    vm.naoPodeBuscar = false;
                    encerraLoader("loaderLocalidade", "localidade", "botaoBusca");
                    return vm.listaLocalidades;
                })
                .catch(function (error) {
                    console.log(error);
                    let select = document.getElementById("localidade");
                    if(!isNullUndef(select))
                        select.children[0].innerHTML = "Nenhum local para esta especialidade.";
                    vm.naoPodeBuscar = true;
                    encerraLoader("loaderLocalidade", "localidade", "botaoBusca");
                    return [];
                });
            } else {
                vm.listaLocalidades = [{descricao:"Todas", id:0}];
                vm.naoPodeBuscar = false;
                return vm.listaLocalidades;
            }
        },
        buscaEspecialidades(){
            const vm = this;
            iniciaLoader("loaderEspecialidade", "especialidade", "botaoBusca");
            return axios.get(getCaminhoTestes() + "rs/filtro/especialidade")
                .then(function (response) {
                    if(typeof response.data === "string"){
                        vm.listaEspecialidades = JSON.parse(response.data);
                    } else {
                        vm.listaEspecialidades = response.data;
                    }
                    vm.naoPodeBuscar = true;
                    vm.listaEspecialidades = vm.listaEspecialidades.sort(sortLexDescricao);
                    encerraLoader("loaderEspecialidade", "especialidade", "botaoBusca");
                    return vm.listaEspecialidades;
                })
                .catch(function (error) {
                    console.log(error);
                    vm.showAlert("Ops! Algo deu errado",10);
                    vm.naoPodeBuscar = true;
                    vm.erro = true;
                    encerraLoader("loaderEspecialidade", "especialidade", "botaoBusca");
                    return [];
                });
        },
        buscaMedico(){
            const vm = this;
            let esp = {};
            let loc = {};
            //if pode agendar E especialidade =! null
            if (vm.especialidadeSelecionada.podeAgendar){
                    //susbtitui o for por um parser do objeto servicos para especialidade 
                    // esp = { nome: servico.label id = servico.especialidade}
                // for(let i = 0; i < vm.listaEspecialidades.length; i++){
                //     if(vm.listaEspecialidades[i].id === vm.especialidadeSelecionada){
                //         esp = vm.listaEspecialidades[i];
                //         break;
                //     }
                // }             
                esp =  {'descricao':vm.especialidadeSelecionada.nomeEspecialidade, 'id':vm.especialidadeSelecionada.especialidade};
                let resultadoLocalidades = [];
                let arrayAux = vm.localidadeSelecionada;
                for (let i = 0, j = 0; i < vm.listaLocalidades.length; i++) {
                    if(vm.listaLocalidades[i].id === parseInt(arrayAux[j])){
                        resultadoLocalidades.push(vm.listaLocalidades[i]);
                        j++;
                    }
                    if(j >= arrayAux.length)
                        break;
                }
                //TODO refatorar o metedo para deixar ele mais generico
                vm.addFiltroLocalStorage(esp, resultadoLocalidades, vm.nomeProfissional);
                changeLocation(getCaminhoTestes() + "associados.html");
                //else podeAgendar = false

            }else{
                vm.addFiltroSemAgendaToLocalStorage(vm.especialidadeSelecionada, [], vm.nomeProfissional);
                changeLocation(getCaminhoTestes() + "associados-sem-agenda.html");
            }
 
        },
        countDownChanged(dismissCountDown) {
            const vm = this;
            vm.dismissCountDown = dismissCountDown;
            if(dismissCountDown === 0){
                vm.erro = false;
                vm.buscaEspecialidades();
            }
        },
        showAlert(msgErro,dismissSecs) {
            const vm = this;
            vm.dismissSecs = dismissSecs;
            vm.msgErro = msgErro;
            if(vm.dismissCountDown === 0 )
                vm.dismissCountDown = vm.dismissSecs;
        },
        clearStorage(){
            localStorage.clear();
        },
        addFiltroLocalStorage(especialidade,localidades,nomeProfissional){
            let filtroAssociados = {
                especialidade : {
                    nome : especialidade.descricao,
                    id : especialidade.id,
                },
                localidades : localidades,
                nomeProfissional : nomeProfissional,
            };
            saveLocalStorage("filtroAssociados",filtroAssociados);
            saveLocalStorage("ultimaPagina","index.html");
        },
        addFiltroSemAgendaToLocalStorage(servico, localidades, nomeProfissional){
          let filtroAssociadosSemAgenda = {
              servico : {
                "id": servico.id,
                "codigo": servico.codigo,
                "descricao": servico.descricao,
                "valor": servico.valor,
                "especialidade": servico.especialidade,
                "nomeEspecialidade": servico.nomeEspecialidade,
                "tipo": servico.tipo,
                "podeAgendar": servico.podeAgendar
              },
              localidades : localidades,
              nomeProfissional : nomeProfissional,
          };
          saveLocalStorage("filtroAssociadosSemAgenda", filtroAssociadosSemAgenda);
          saveLocalStorage("ultimaPagina","index.html");
      },
    },
    
    /* ON LOAD */
    created: function() {
        const vm = this;
        vm.buscaEspecialidades();
        vm.clearStorage();
    }
});

var artigosBlog= new Vue({
    el : '#artigosBlog',
    data: {
        posts:[],
        imagesPosts: [],
        MAX_POSTS: 3,
        imagem:'',
    },
    methods:{
        buscaArtigos(){
            const vm = this;
            iniciaLoader("loaderArtigosBlog", "cardsArtigosBlog", null);
            return axios.get(URLBLOG + 'posts?_embed').then(function (response) {
                    vm.posts = response.data.slice(0, vm.MAX_POSTS);
                    encerraLoader("loaderArtigosBlog", "cardsArtigosBlog", null);
                    return response.data;
                })
                .catch(function (error) {
                    encerraLoader("loaderArtigosBlog", "cardsArtigosBlog", null);  
                                          
                });                                      
        }
    },
    created: function() {
        const vm = this;
        vm.buscaArtigos();
    }   
});


function sortLexDescricao(a,b){
    if(isNullUndef(a) || isNullUndef(b))
        return 0;
    return a.descricao.localeCompare(b.descricao);
}

