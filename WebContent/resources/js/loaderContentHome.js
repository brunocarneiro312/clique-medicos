"use strict"

function iniciaLoader(z,y,x){
    let loader = document.getElementById(z);
    let conteudo = document.getElementById(y);
    let botao = document.getElementById(x);
    let botaoLoader = document.getElementById(x +'Loader');
    if(isNullUndef(loader))
        return;

    let parent = document.getElementById(z).parentElement;
    if(isNullUndef(parent))
        return;

    let ultimaAltura = parent.style.height;


        if(!loader.classList.contains("spinner-border")){
            loader.classList.add("spinner-border");
        }
        if(!conteudo.classList.contains("esconde")){
            conteudo.classList.add("esconde");
        }
        if(!isNullUndef(botaoLoader) && !botaoLoader.classList.contains("spinner-border")){
            botaoLoader.classList.add("spinner-border");
            botao.disabled = true;
        }

}

function encerraLoader(z,y,x){
    let loader = document.getElementById(z);
    let conteudo = document.getElementById(y);
    let botao = document.getElementById(x);    
    let botaoLoader = document.getElementById(x +'Loader');
    if(isNullUndef(loader))
        return;
    
    	if(loader.classList.contains("spinner-border")){
            loader.classList.remove("spinner-border");
        }
    	if(conteudo.classList.contains("esconde")){
            conteudo.classList.remove("esconde");
        }
    	if(!isNullUndef(botaoLoader) && botaoLoader.classList.contains("spinner-border")){
            botaoLoader.classList.remove("spinner-border");
            botao.disabled = false;
        }

}