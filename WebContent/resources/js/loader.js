"use strict"
var loaderSemaforo = 0;

function iniciaLoader(s){
    let loader = document.getElementById(s);
    if(isNullUndef(loader))
        return;

    let parent = document.getElementById(s).parentElement;
    if(isNullUndef(parent))
        return;

    let ultimaAltura = parent.style.height;

    if(loaderSemaforo === 0){
        if(!loader.classList.contains("loaderManual")){
            loader.classList.add("loaderManual");
        }
    }

    parent.style.height = ultimaAltura;
    loaderSemaforo++;
}

function encerraLoader(s){
    let loader = document.getElementById(s);
    if(isNullUndef(loader))
        return;
    
    if(loaderSemaforo > 0){
    	loaderSemaforo--;
    }
    
    if(loaderSemaforo === 0){
    	if(loader.classList.contains("loaderManual")){
            loader.classList.remove("loaderManual");
        }
    }
}