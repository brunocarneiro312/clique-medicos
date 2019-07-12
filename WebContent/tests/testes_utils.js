"use strict";
function assert(test, msgErro){
    if(msgErro === undefined)
        return [test, test ? "Sucesso" : "Assert falhou."];
    else
        return [test, test ? "Sucesso" : msgErro];
}

function fail(msg){
    return [false,msg];
}

function pegaNomeArquivo(nomeFuncao){
    let fim = nomeFuncao.search("_teste");
    return nomeFuncao.slice(0,fim) +".js";
}

function trocaIcone(tipo,arq,index){
    document.getElementById("res-"+arq+"-"+index).style.background="white";
    switch(tipo){
        case SUCESSO:
            document.getElementById("res-"+arq+"-"+index).style.background=SUCESSO_COR;
        break;
        case FALHOU:
            document.getElementById("res-"+arq+"-"+index).style.background=FALHOU_COR;
        break;
        case RODANDO:
            document.getElementById("res-"+arq+"-"+index).style.background=RODANDO_COR;
        break;
        case ESPERANDO:
            document.getElementById("res-"+arq+"-"+index).style.background=ESPERANDO_COR;
        break;
    }
}

function trocaIconeHeader(tipo,arq){
    document.getElementById("res-head-"+arq).children[0].style.background="white";
    switch(tipo){
        case SUCESSO:
            document.getElementById("res-head-"+arq).children[0].style.background=SUCESSO_COR;
        break;
        case FALHOU:
            document.getElementById("res-head-"+arq).children[0].style.background=FALHOU_COR;
        break;
        case RODANDO:
            document.getElementById("res-head-"+arq).children[0].style.background=RODANDO_COR;
        break;
        case ESPERANDO:
            document.getElementById("res-head-"+arq).children[0].style.background=ESPERANDO_COR;
        break;
    }
}

async function rodarTestesVue(vm){
    document.getElementById("resultadoTestes").style.background = RODANDO_COR;
    
    if(vm.testesJaForamRodados){
        if(!window.location.href.includes("rodar="))
            window.location = window.location.href + "?rodar=" + true;
        else
            window.location = window.location.href;
        return;
    } else
        vm.testesJaForamRodados = true;

    vm.sucessosTotal = 0;
    vm.falhasTotal = 0;

    for (let i = 0; i < vm.listaArquivos.length; i++) {
        trocaIconeHeader(RODANDO, vm.listaArquivos[i].nome, i);
        vm.listaArquivos[i].estado = RODANDO;

        let sucessos = 0;
        let falhas = 0;

        for(let j = 0; j < vm.listaArquivos[i].listaTestes.length; j++){
            let teste = vm.listaArquivos[i].listaTestes[j];

            trocaIcone(RODANDO, teste.nomeTeste, j);
            let resultado = await vm.$options.methods[teste.nomeTeste]();
            if(resultado[0]) {
                trocaIcone(SUCESSO, teste.nomeTeste, j);
                teste.estado = SUCESSO;
                sucessos++;
            } else {
                teste.erro = resultado[1];
                trocaIcone(FALHOU, teste.nomeTeste, j);
                teste.estado = FALHOU;
                falhas++;
            }
        }
        if(falhas > 0){
            vm.listaArquivos[i].estado = FALHOU;
            trocaIconeHeader(FALHOU, vm.listaArquivos[i].nome, i);
        } else {
            vm.listaArquivos[i].estado = SUCESSO;
            trocaIconeHeader(SUCESSO, vm.listaArquivos[i].nome, i);
        }

        vm.listaArquivos[i].sucessos = sucessos;
        vm.listaArquivos[i].falhas = falhas;

        vm.sucessosTotal += sucessos;
        vm.falhasTotal += falhas;
    }
    if(vm.falhasTotal>0){
        document.getElementById("resultadoTestes").style.background = FALHOU_COR;
    } else {
        document.getElementById("resultadoTestes").style.background = SUCESSO_COR;
    }

    //ordenando por falhas e numero de falhas
    vm.listaArquivos.sort(sortErrosPrimeiro)
}

function sortErrosPrimeiro(a,b){
    if(a.estado === FALHOU && b.estado === FALHOU){
        if(a.falhas > b.falhas)
            return -1;//a
        else if(a.falhas > b.falhas)
            return 1;//b
        else
            return 0;
    } else if(a.estado === FALHOU && b.estado !== FALHOU)
        return -1;//a
    else if(a.estado !== FALHOU && b.estado === FALHOU)
        return 1;//b
    else
        return 0;
}

function criarTestesVue(vm){
    let aux = queryParameters(document.location.search);
    if(aux.rodar !== undefined){
        vm.rodar = aux.rodar[0];
    }

    let listaFuncoes = Object.getOwnPropertyNames(vm.$options.methods);

    vm.listaArquivos = [];

    let arquivo = {
        nome:           pegaNomeArquivo(listaFuncoes[1]),
        estado:         ESPERANDO,
        listaTestes:    [],
        sucessos:       0,
        falhas:         0,
    };

    for (let i = 1; i < listaFuncoes.length; i++) {
        let aux = pegaNomeArquivo(listaFuncoes[i]);
        if(arquivo.nome === aux){
            let objTest = {
                nomeTeste:      listaFuncoes[i],
                estado:         ESPERANDO,
                erro:           "",
            };
            arquivo.listaTestes.push(objTest);
            if(i === listaFuncoes.length - 1){
                let objArq = {
                    nome:           arquivo.nome,
                    estado:         arquivo.estado,
                    listaTestes:    arquivo.listaTestes.slice(),
                    sucessos:       arquivo.sucessos,
                    falhas:         arquivo.falhas,
                };
                vm.listaArquivos.push(objArq);   
                vm.total += arquivo.listaTestes.length;                 
            }
        } else {
            let objArq = {
                nome:           arquivo.nome,
                estado:         arquivo.estado,
                listaTestes:    arquivo.listaTestes.slice(),
                sucessos:       arquivo.sucessos,
                falhas:         arquivo.falhas,
            };
            vm.listaArquivos.push(objArq);
            vm.total += arquivo.listaTestes.length;

            arquivo.nome            = aux;
            arquivo.estado          = ESPERANDO;
            arquivo.listaTestes     = [];
            
            let obj = {
                nomeTeste:      listaFuncoes[i],
                estado:         ESPERANDO,
                erro:           "",
            };
            arquivo.listaTestes.push(obj);
            if(i === listaFuncoes.length - 1){
                let objArq = {
                    nome:           arquivo.nome,
                    estado:         arquivo.estado,
                    listaTestes:    arquivo.listaTestes.slice(),
                    sucessos:       arquivo.sucessos,
                    falhas:         arquivo.falhas,
                };
                vm.listaArquivos.push(objArq);   
                vm.total += arquivo.listaTestes.length;                 
            }
        }
    }
    

    if(vm.rodar == "true"){
        Vue.nextTick(function () {
            vm.rodarTestes();
        });
    }
}

function retornaInstanciaComponenteVue(nomeComponente){
    for (let i = 0; i < testesVue.$children.length; i++) {
        if(testesVue.$children[i].$options._componentTag === nomeComponente)
            return testesVue.$children[i];
    }
    return null;
}