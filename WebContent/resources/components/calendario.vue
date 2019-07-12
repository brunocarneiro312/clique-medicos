<template>
<div>
    <div id="calendario">
       <div class="text-right"><small>
                Legenda: <i class="btn btn-date disabled cor-dia-azul texto-dia-branco dia-hoje" style="width: 15px; height: 15px;border-radius: 10px;padding: 0.25rem 0.25rem;"></i> 
                Disponível </small></div>
        <div v-if="estiloVisualizacao === 'ano'" class="container calendario-ano">
            <div class="row row-header-anos">
                <b-button class="col" @click="mudaHoje()"> Próximo dia disponível </b-button>
            </div>
            <div class="row row-header-anos">
                <b-button class="col-2" @click="mudaAno(-1)"> &lsaquo;&lsaquo; </b-button>
                <p class="col-8">{{anoSelecionado.ano}}</p>
                <b-button class="col-2" @click="mudaAno(+1)"> &rsaquo;&rsaquo;</b-button>
            </div>
            <div class="meses row">
                <div class="col-6" v-for="mes in anoSelecionado.meses" :key="mes.nome+'-'+anoSelecionado.ano">
                    <div class="row mes-header">
                        <p class="col">{{mes.nome}}</p>
                    </div>
                    <div class="row-weekdays-header row">
                        <div v-for="j in 7" class="weekdays col" :key="j">{{nomeDiasMini[j-1]}}</div>
                    </div>
                    <div    v-for="i in 6"
                            class="row row-dias-mes"   
                            :key="i">
                        <div    v-for="j in 7" :key="j"
                                class="day col" v-b-tooltip.hover 
                                :title="diaDoMes(mes,i,j).txtTooltip">
                            <b-button   v-if="diaDoMes(mesSelecionado,i,j).diaMes !== -1" 
                                        @click="selecionaDia(diaDoMes(mesSelecionado,i,j),mes)"
                                        :class="diaDoMes(mesSelecionado,i,j).cssClass"
                                        variant="date">
                                    {{diaDoMes(mesSelecionado,i,j).diaMes}}
                            </b-button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-else class="calendario-mes">
           <div class="container">
                <div class="row row-header-anos">
                    <b-button variant="date-primary" class="col-12" @click="mudaHoje()"> Hoje </b-button>
                </div>
                <div class="row row-header-anos " v-if="mesSelecionado.nome == 'Dezembro'">
                    <b-button class="col-2" @click="mudaAno(-1)">
                        <svg width="26px" height="26px" viewBox="0 -1 16 34" class="vc-svg-icon vc-arrow vc-cursor-pointer vc-select-none vc-text-gray-600 hover:vc-opacity-50" data-v-03bd482a=""><path d="M11.196 10c0 0.143-0.071 0.304-0.179 0.411l-7.018 7.018 7.018 7.018c0.107 0.107 0.179 0.268 0.179 0.411s-0.071 0.304-0.179 0.411l-0.893 0.893c-0.107 0.107-0.268 0.179-0.411 0.179s-0.304-0.071-0.411-0.179l-8.321-8.321c-0.107-0.107-0.179-0.268-0.179-0.411s0.071-0.304 0.179-0.411l8.321-8.321c0.107-0.107 0.268-0.179 0.411-0.179s0.304 0.071 0.411 0.179l0.893 0.893c0.107 0.107 0.179 0.25 0.179 0.411z" data-v-03bd482a=""></path></svg>
                     </b-button>
                    <p class="col-8">{{anoSelecionado.ano}}</p>
                    <b-button class="col-2" @click="mudaAno(+1)">
                         <svg width="26px" height="26px" viewBox="-5 -1 16 34" class="vc-svg-icon vc-arrow vc-cursor-pointer vc-select-none vc-text-gray-600 hover:vc-opacity-50" data-v-03bd482a=""><path d="M10.625 17.429c0 0.143-0.071 0.304-0.179 0.411l-8.321 8.321c-0.107 0.107-0.268 0.179-0.411 0.179s-0.304-0.071-0.411-0.179l-0.893-0.893c-0.107-0.107-0.179-0.25-0.179-0.411 0-0.143 0.071-0.304 0.179-0.411l7.018-7.018-7.018-7.018c-0.107-0.107-0.179-0.268-0.179-0.411s0.071-0.304 0.179-0.411l0.893-0.893c0.107-0.107 0.268-0.179 0.411-0.179s0.304 0.071 0.411 0.179l8.321 8.321c0.107 0.107 0.179 0.268 0.179 0.411z" data-v-03bd482a=""></path></svg>
                    </b-button>
                </div>
                <b-row class="row-header-meses"  align-h="center" align-v="center">
                    <b-col>
                        <b-button style="float:left" class="btn-prev" @click="mudaMes(-1)"  variant="date-primary">
                            <svg width="26px" height="26px" viewBox="0 -1 16 34" class="vc-svg-icon vc-arrow vc-cursor-pointer vc-select-none vc-text-gray-600 hover:vc-opacity-50" data-v-03bd482a=""><path d="M11.196 10c0 0.143-0.071 0.304-0.179 0.411l-7.018 7.018 7.018 7.018c0.107 0.107 0.179 0.268 0.179 0.411s-0.071 0.304-0.179 0.411l-0.893 0.893c-0.107 0.107-0.268 0.179-0.411 0.179s-0.304-0.071-0.411-0.179l-8.321-8.321c-0.107-0.107-0.179-0.268-0.179-0.411s0.071-0.304 0.179-0.411l8.321-8.321c0.107-0.107 0.268-0.179 0.411-0.179s0.304 0.071 0.411 0.179l0.893 0.893c0.107 0.107 0.179 0.25 0.179 0.411z" data-v-03bd482a=""></path></svg>
                        </b-button>
                    </b-col>
                    <b-col cols="text-center">                    
                        <h5 class="date-title">{{mesSelecionado.nome}} {{anoSelecionado.ano}} </h5>
                    </b-col>
                    <b-col>        
                        <b-button style="float:right" class="btn-next" @click="mudaMes(+1)" variant="date-primary">
                            <svg width="26px" height="26px" viewBox="-5 -1 16 34" class="vc-svg-icon vc-arrow vc-cursor-pointer vc-select-none vc-text-gray-600 hover:vc-opacity-50" data-v-03bd482a=""><path d="M10.625 17.429c0 0.143-0.071 0.304-0.179 0.411l-8.321 8.321c-0.107 0.107-0.268 0.179-0.411 0.179s-0.304-0.071-0.411-0.179l-0.893-0.893c-0.107-0.107-0.179-0.25-0.179-0.411 0-0.143 0.071-0.304 0.179-0.411l7.018-7.018-7.018-7.018c-0.107-0.107-0.179-0.268-0.179-0.411s0.071-0.304 0.179-0.411l0.893-0.893c0.107-0.107 0.268-0.179 0.411-0.179s0.304 0.071 0.411 0.179l8.321 8.321c0.107 0.107 0.179 0.268 0.179 0.411z" data-v-03bd482a=""></path></svg>
                        </b-button>
                    </b-col>
                </b-row>
                <div class="row-weekdays-header row">
                    <div v-for="j in 7" class="weekdays col" :key="j">{{nomeDiasMini[j-1]}}</div>
                </div>
                <div v-if="loaderLigado" class="row" style="min-height:125px">
                    <div class="loader-position col-12">
                        <div class="spinner-border">
                        </div>
                    </div>
                </div>
                <div    v-else 
                        v-for="i in 6" :key="i"
                        class="row row-dias-mes">
                    <div    v-for="j in 7" :key="j"
                            class="day col" 
                            v-b-tooltip.hover 
                            :title="diaDoMes(mesSelecionado,i,j).txtTooltip">
                        <b-button   v-if="diaDoMes(mesSelecionado,i,j).diaMes !== -1" 
                                    @click="selecionaDia(diaDoMes(mesSelecionado,i,j),mesSelecionado)"
                                    :class="diaDoMes(mesSelecionado,i,j).cssClass"
                                    variant="date"
                                    :disabled="diaDoMes(mesSelecionado,i,j).invalido">
                                    {{diaDoMes(mesSelecionado,i,j).diaMes}}
                        </b-button>
                    </div>
                </div>
            </div>   
        </div>
    </div>
</div>
</template>
<style scoped>
    .loader-position{
        text-align:center;
        padding-top: 3em;
    }
    .row-dias-mes .day button{
        width:25px;
        height:25px;
        background-color: white;
        border: 0px;
        color: black;
        padding: 0.175rem 0.15rem;
    }

    .day, .weekdays{
        padding-right: 0px;
        padding-left: 0px;
    }
    .card {
        max-width: 300px;
    }
    .btn-next, .btn-prev{
        color: #333;
        background-color:none;
        border-color: none;
    }
    .date-title{
        font-size: 1.125rem;
        cursor: pointer;
        white-space: nowrap;
        line-height: 1.5;
        border-width: 0;
        border-style: solid;
        box-sizing: border-box;
    }
    .weekdays{
        color: #a0aec0;
        justify-content: center;
        align-items: center;
        font-size: .875rem;
    }
    .btn-date {
        font-size: .875rem;
        font-weight: 500;
        cursor: pointer;
        border-radius: 15px;
    }
    .btn-date:hover {
        background-color: #BBD2D9!important;
    }
    .cor-dia-verde{
        background-color: green!important;
    }
    .cor-dia-azul{
        background-color: #0093ff38 !important;
    }
    .cor-dia-amarelo{
        background-color: yellow!important;
    }
    .cor-dia-vermelho{
        background-color: red!important;
    }
    .cor-dia-preto{
        background-color: black!important;
    }
    .cor-dia-branco{
        background-color: white!important;
    }
    .cor-dia-roxo{
        background-color: purple!important;
    }
    .cor-dia-cinza{
        background-color: grey!important;
    }
    .texto-dia-branco{
        color: white!important;
    }
    .texto-dia-preto{
        color: black!important;   
    }
    .cursor-invalido{
        cursor:not-allowed!important;
    }
</style>
<script>
"use strict";
module.exports = {
    props: [
        'diasComHorarios',
        'cssDiaInicial',
        'cssDiaComHorario',
        'cssDiaPadrao',
        'estiloVisualizacao',
        'acessoAoPassado',
        'loaderLigado'
    ],
    data: function() {
        return {
            listaAnos : [],
            mesSelecionado:{
                mes     : 0,
                nome    : "Janeiro",
                dias    : [],
            },
            diaSelecionado:{
                diaAno              : 1,
                diaMes              : 1,
                diaSemana           : 1,
                diaSemanaTxt        : "Domingo",
                diaSemanaTxtMini    : "Dom",
                cssClass            : "cor-dia-branco texto-dia-preto",
                selecionado         : false,
                txtTooltip          : "",
            },
            anoSelecionado:{
                ano     : 1999,
                meses   : [],
            },
            nomeMeses :     ["Janeiro", "Fevereiro", "Março", "Abril", "Maio","Junho",
                             "Julho","Agosto","Setembro","Outubro","Novembro", "Dezembro" ],
            nomeDiasMini :  ["Dom","Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
            nomeDias :      ["Domingo","Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"],
            diasNoMes :     [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
        };
    },
    watch:{
        diasComHorarios(){
            const vm = this;
            vm.limparDiasAnos();
            vm.adicionarDiasHorarios(vm.diasComHorarios, vm.cssDiaComHorario);
        },
    },
    computed:{
        
    },
    methods: {
        //FUÇÕES PARA RENDERIZAR OS OBJETOS
        diaDoMes(mes,i,j){
            let index = ( ( i - 1 ) * 7 ) + ( j - 1 );
            let numFillerIni = mes.dias[0].diaSemana;
            let numFillerFim = 42 - (mes.dias.length + numFillerIni);
            let objFiller = {
                diaMes      : -1,
                cssClass    : "",
                txtTooltip  : "",
            };
            if(index < numFillerIni)
                return objFiller;
            else if(index >= numFillerIni && index < 42 - numFillerFim)
                return mes.dias[index - numFillerIni];
            else
                return objFiller;
        },

        //FUNÇÕES PARA ANDAR NO TEMPO
        anoComparator(a,b){
            if(a < b)
                return -1;//a
            else if(a > b)
                return 1;//b
            else 
                return 0
        },
        mudaAno(qtd){
            const vm = this;
            let novoAno = vm.anoSelecionado.ano + qtd;

            if(!vm.acessoAoPassado && qtd < 0){
                if(novoAno < new Date().getFullYear())
                    return;
            }

            let achou = false;
            for(let i = 0; i < vm.listaAnos.length; i++){
                if(vm.listaAnos[i].ano === novoAno){
                    vm.anoSelecionado = vm.listaAnos[i];
                    achou = true;
                    break;
                }
            }
            if(!achou){
                vm.anoSelecionado = vm.gerarAno(novoAno);
                vm.listaAnos.push(vm.anoSelecionado);
                vm.listaAnos = vm.listaAnos.sort(vm.anoComparator);
            }
            if(qtd < 0){
                vm.mesSelecionado = vm.anoSelecionado.meses[vm.anoSelecionado.meses.length - 1];
                vm.diaSelecionado = vm.mesSelecionado.dias[vm.mesSelecionado.dias.length - 1];
            } else {
                vm.mesSelecionado = vm.anoSelecionado.meses[0];
                vm.diaSelecionado = vm.mesSelecionado.dias[0];
            }
            vm.$emit('mudou-ano',qtd);
        },
        mudaMes(qtd){
            const vm = this;
            let novoMes = vm.mesSelecionado.mes + qtd;

            if(!vm.acessoAoPassado && qtd < 0){
                if(novoMes < new Date().getMonth())
                    return;
            }

            if(novoMes < 0 
            || novoMes > 11){
                vm.mudaAno(qtd);
            } else {
                vm.mesSelecionado = vm.anoSelecionado.meses[novoMes];
                if(qtd < 0){
                    vm.diaSelecionado = vm.mesSelecionado.dias[vm.mesSelecionado.dias.length-1];
                } else {
                    vm.diaSelecionado = vm.mesSelecionado.dias[0];
                }
                vm.$emit('mudou-mes',qtd);
            }
        },
        mudaHoje(){
            const vm = this;
            let hoje = new Date();
            for(let i = 0; i < vm.listaAnos.length; i++){
                if(hoje.getFullYear() === vm.listaAnos[i].ano){
                    vm.anoSelecionado = vm.listaAnos[i];
                    break;
                }
            }
            vm.mesSelecionado = vm.anoSelecionado.meses[hoje.getMonth()];
            vm.diaSelecionado = vm.mesSelecionado.dias[hoje.getDate() - 1];
            vm.$emit('apertou-hoje', hoje);
        },
        adicionarDiasHorarios(diasComHorarios,cor){
            const vm = this;
            vm.diasComHorarios = diasComHorarios;

            if(!isNullUndef(vm.diasComHorarios)){
                for(let i = 0; i < vm.diasComHorarios.length; i++){
                    for(let j = 0; j < vm.diasComHorarios[i].agendaApontamentos.length; j++){
                        let associado = vm.diasComHorarios[i];
                        let servico = associado.agendaApontamentos[j];
                        if(associado.mostrarAssociadoCal && servico.mostrarCal){
                            let data = new Date(servico.inicio);
                            vm.inserirConteudoDia(data, cor, "");
                        }
                    }
                }
            }
        },

        //FUNÇÕES PARA INSERIR OU ALTERAR VALORES NOS DIAS DO MÊS
        inserirConteudoDia(data, classes, txtTooltip){
            const vm = this;
            let anoEncontrado = null;
            let anoProcurado = data.getFullYear();
            for(let i = 0; i < vm.listaAnos.length; i++){
                if(vm.listaAnos[i].ano === anoProcurado){
                    anoEncontrado = vm.listaAnos[i];
                    break;
                }
            }
            if(anoEncontrado !== null){
                let mes = anoEncontrado.meses[data.getMonth()];
                let dia = mes.dias[data.getDate() - 1];
                if(classes !== null)
                    dia.cssClass = classes;
                if(txtTooltip !== null)
                    dia.txtTooltip = txtTooltip;
                dia.invalido = false;
            } else {
                let novoAno = vm.gerarAno(anoProcurado);
                let mes = novoAno.meses[data.getMonth()];
                let dia = mes.dias[data.getDate() - 1];
                if(classes !== null)
                    dia.cssClass = classes;
                if(txtTooltip !== null)
                    dia.txtTooltip = txtTooltip;
                dia.invalido = false;
                vm.listaAnos.push(novoAno);
            }
        },
        selecionaDia(dia,mes){
            const vm = this;
            let aux = new Date();
            let auxMes = isNullUndef(mes) ? null : mes.mes;

            if(!vm.acessoAoPassado){
                if(aux.getMonth() > auxMes)
                    return false;
                if(aux.getMonth() === auxMes && aux.getDate() >= dia.diaMes)
                    return false;
            }
            vm.$emit('seleciona-dia', auxMes, dia.diaMes);
            return true;
        },
        limparDiasAnos(){
            const vm = this;
            let anoAtual = null;
            let mesAtual = vm.mesSelecionado.mes;
            let diaAtual = vm.diaSelecionado.diaMes;
            let hoje = new Date();
            for(let i = 0; i < vm.listaAnos.length; i++){
                for(let j = 0; j < 12; j++){
                    vm.listaAnos[i].meses = [];
                }
            }
            for(let i = 0; i < vm.listaAnos.length; i++){
                for(let j = 0; j < 12; j++){
                    let objMes = vm.gerarMes(j, vm.listaAnos[i].ano);
                    vm.listaAnos[i].meses.push(objMes);
                }
            }
            for(let i = 0; i < vm.listaAnos.length; i++){
                if(vm.listaAnos[i].ano === hoje.getFullYear()){
                    anoAtual = vm.listaAnos[i];
                    break;
                }
            }
            if(anoAtual != null){
                vm.mesSelecionado = vm.anoSelecionado.meses[mesAtual];
                vm.diaSelecionado = vm.mesSelecionado.dias[diaAtual - 1];
                let dia = anoAtual.meses[hoje.getMonth()].dias[hoje.getDate() - 1];
                dia.cssClass = vm.cssDiaInicial;
                dia.txtTooltip = "Hoje";
            }            
        },


        //FUNÇÕES PARA INCIALIZAR O CALENDÁRIO COM VALORES
        calcularDiasNoMes(ano,mes) {
            const vm = this;
            const anoBissexto = (ano % 4 === 0 && ano % 100 !== 0) || ano % 400 === 0;
            if (mes === 2 && anoBissexto) 
                return 29;
            return vm.diasNoMes[mes];
        },
        gerarAno(anoNum){
            const vm = this;
            let objAno = {
                ano     : anoNum,
                meses   : [],
            };
            for (let j = 0; j < 12; j++) {
                let objMes = vm.gerarMes(j, objAno.ano);
                objAno.meses.push(objMes);
            }
            return objAno;
        },
        gerarMes(mesNum, ano){
            const vm = this;
            let objMes = {
                mes     : mesNum,
                nome    : vm.nomeMeses[mesNum],
                dias    : [],
            };
            let diasNoMesReal = vm.calcularDiasNoMes(ano, objMes.mes);
            for (let k = 1; k < diasNoMesReal + 1 ; k++) {
                let objDia = vm.gerarDia(k, objMes.mes, ano);
                objMes.dias.push(objDia);
            }
            return objMes;
        },
        gerarDia(diaNum, mes, ano){
            const vm = this;
            let dataDia = new Date(ano, mes, diaNum)
            let diaSemana = dataDia.getDay();
            let objDia = {
                diaAno              : dataDia.getDOY(),
                diaMes              : diaNum,
                diaSemana           : diaSemana,
                diaSemanaTxt        : vm.nomeDias[diaSemana],
                diaSemanaTxtMini    : vm.nomeDiasMini[diaSemana],
                cssClass            : vm.cssDiaPadrao + " cursor-invalido",
                selecionado         : false,
                invalido            : true,
                txtTooltip          : "",
            };
            return objDia;
        },
        iniciarCalendario(){
            const vm = this;
            let hoje = new Date();

            let listaAnos = [];
            for (let i = -2; i < 3; i++) {
                let objAno = vm.gerarAno(hoje.getFullYear() + i);
                listaAnos.push(objAno);
            }

            vm.anoSelecionado = listaAnos[2];
            vm.mesSelecionado = vm.anoSelecionado.meses[hoje.getMonth()];
            vm.diaSelecionado = vm.mesSelecionado.dias[hoje.getDate() - 1];

            vm.diaSelecionado.cssClass = vm.cssDiaInicial;
            vm.diaSelecionado.txtTooltip = "Hoje";
            vm.dataFiltro = hoje.getHours()+":"+hoje.getMinutes();
            return listaAnos;
        },
    },
    created: function() {
        const vm = this;
        console.log("CALENDARIO DEBUG");
        
        vm.listaAnos = vm.iniciarCalendario();
    },
};

</script>
