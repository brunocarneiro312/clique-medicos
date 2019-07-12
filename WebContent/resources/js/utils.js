"use strict"
var _testesRodando;
function isBoolean(o){
    return typeof o === 'boolean';
}
function isNumber(o){
    return typeof o === 'number';
}
function isArray(o){
    return Array.isArray(o);
}
function isObject(o){
    return typeof o === 'object';
}
function isDate(o){
    return Object.prototype.toString.call(o) === '[object Date]';
}
function isString(o){
    return typeof o === 'string';
}
//vê se o objeto não tem nenhum campo dentro dele
function isEmpty(obj) {
    for(let key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}
//vê se o array é vazio
Array.prototype.isEmpty= function(){
    return this.length === 0;
}
//knuth shuffle
Array.prototype.shuffle = function() {
    let currentIndex = this.length, temporaryValue, randomIndex;
    // While there remain elements to shuffle...
    while (0 !== currentIndex) {
        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;
        // And swap it with the current element.
        temporaryValue = this[currentIndex];
        this[currentIndex] = this[randomIndex];
        this[randomIndex] = temporaryValue;
    }
    return this;
}
//adiciona millisegundos numa data
Date.prototype.addMillis= function(m){
    this.setMilliseconds(this.getMilliseconds()+m);
    return this;
}
//adiciona segundos numa data
Date.prototype.addSeconds= function(s){
    this.setSeconds(this.getSeconds()+s);
    return this;
}
//adiciona minutos numa data
Date.prototype.addMinutes= function(m){
    this.setMinutes(this.getMinutes()+m);
    return this;
}
//adiciona horas numa data
Date.prototype.addHours= function(h){
    this.setHours(this.getHours()+h);
    return this;
}
//adiciona dias em uma data
Date.prototype.addDays= function(d){
    this.setHours(this.getHours()+(24*d));
    return this;
}
//adiciona meses em uma data
Date.prototype.addMonths= function(d){
    this.setMonth(this.getMonth()+d);
    return this;
}
//adiciona anos em uma data
Date.prototype.addYears= function(d){
    this.setFullYear(this.getFullYear()+d);
    return this;
}
//mega função de comparação de data, fields pode ser "dmyHML" pra testar todos os campos
Date.prototype.compare = function(data, fields) {
    let ano = this.compareYear(data);
    let anoField = fields.includes('y');
    if(anoField && ano < 0)
        return -1;
    else if(!anoField || ano == 0) {
        let mes = this.compareMonth(data);
        let mesField = fields.includes('m');
        if(mesField && mes < 0)
            return -1;
        else if(!mesField || mes == 0) {
            let dia = this.compareDate(data);
            let diaField = fields.includes('d');
            if(diaField && dia < 0)
                return -1;
            else if(!diaField || dia == 0) {
                let hora = this.compareHour(data);
                let horaField = fields.includes('H');
                if(horaField && hora < 0)
                    return -1;
                else if(!horaField || hora == 0) {
                    let minuto = this.compareMinute(data);
                    let minField = fields.includes('M');
                    if(minField && minuto < 0)
                        return -1;
                    else if(!minField || minuto == 0) {
                        let segundo = this.compareSecond(data);
                        let segField = fields.includes('S');
                        if(segField && segundo < 0)
                            return -1;
                        else if(!segField || segundo == 0) {
                            let milli = this.compareMilli(data);
                            let milliField = fields.includes('L');
                            if(milliField && milli < 0)
                                return -1;
                            else if(!milliField || milli == 0) {
                                return 0;
                            }   
                        }    
                    }    
                }    
            }
        }
    }
    return 1;
};
//funções de comparação de data
Date.prototype.compareYear = function(data) {
    return this.getFullYear() < data.getFullYear() ?
    -1 : (this.getFullYear() > data.getFullYear() ? 1 : 0);
}
Date.prototype.compareDate = function(data) {
    return this.getDate() < data.getDate() ? 
    -1 : (this.getDate() > data.getDate() ? 1 : 0);
}
Date.prototype.compareMonth = function(data) {
    return this.getMonth() < data.getMonth() ? 
    -1 : (this.getMonth() > data.getMonth() ? 1 : 0);
}
Date.prototype.compareHour = function(data) {
    return this.getHours() < data.getHours() ?
    -1 : (this.getHours() > data.getHours() ? 1 : 0);
}
Date.prototype.compareMinute = function(data) {
    return this.getMinutes() < data.getMinutes() ? 
    -1 : (this.getMinutes() > data.getMinutes() ? 1 : 0);
}
Date.prototype.compareSecond = function(data) {
    return this.getSeconds() < data.getSeconds() ? 
    -1 : (this.getSeconds() > data.getSeconds() ? 1 : 0);
}
Date.prototype.compareMilli = function(data) {
    return this.getMilliseconds() < data.getMilliseconds() ? 
    -1 : (this.getMilliseconds() > data.getMilliseconds() ? 1 : 0);
}
//ano bissexto
Date.prototype.isLeapYear = function() {
    let year = this.getFullYear();
    if((year & 3) != 0) 
        return false;
    return ((year % 100) != 0 || (year % 400) == 0);
};
//Dia do ano
Date.prototype.getDOY = function() {
    let dayCount = [0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334];
    let mn = this.getMonth();
    let dn = this.getDate();
    let dayOfYear = dayCount[mn] + dn;
    if(mn > 1 && this.isLeapYear()) 
        dayOfYear++;
    return dayOfYear;
};
// retorna uma data com nome do dia da semana e mês em extenso
Date.prototype.parsePTBR = function(){    
    let options = {month:'long', day:'numeric', weekday:'long'};
    return this.toLocaleDateString("pt-BR",options).replace("-feira","");
}
//vê se a data é válida
Date.prototype.isValid = function() {
    return this instanceof Date && !isNaN(this);
}
//cria uma data a partir de uma data no formato sql "00-00-00"
function createDateFromSQLString(s){
    let data = s.split("-");
    return new Date(parseInt(data[0]), parseInt(data[1]) - 1, parseInt(data[2]));
}
Date.prototype.toInputDate = function() {
    return this.toISOString().split('T')[0];
}
//formata data dependendo do valor enviado em "format"
//ex: format = "dmyHMS" cria data desse tipo:
//dd/mm/yyyy hh:mm:ss
Date.prototype.formatDate = function(format,separatorDate,separatorTime) {
    if(isNullUndef(this))
        return "";
    separatorDate = isNullUndef(separatorDate) ? '/' : separatorDate;
    separatorTime = isNullUndef(separatorTime) ? ':' : separatorTime;
    let result = "";
    let month, day, year, hour, minute, second;

    if(format.includes('d')){
        day = '' + this.getDate();
        if (day.length < 2)
            day = '0' + day;
        result = result.concat(day + separatorDate);
    }
    if(format.includes('m')){
        month = '' + (this.getMonth() + 1);
        if (month.length < 2)
            month = '0' + month;
        result = result.concat(month + separatorDate);
    }
    if(format.includes('y')){
        year = '' + this.getFullYear();
        result = result.concat(year);
    }
    if(format.includes('H')){
        hour = '' + this.getHours();
        if (hour.length < 2)
            hour = '0' + hour;
        if(result.length > 0)
            result = result.concat(" " + hour + separatorTime);
        else
            result = result.concat(hour + separatorTime);
    }
    if(format.includes('M')){
        minute = '' + this.getMinutes();
        if (minute.length < 2)
            minute = '0' + minute;
        result = result.concat(minute);
    }
    if(format.includes('S')){
        second = '' + this.getSeconds();
        if (second.length < 2)
            second = '0' + second;
        result = result.concat(separatorTime + second);
    }
    return result
}
Date.prototype.toISOLocal = function() {
    const offsetMs = this.getTimezoneOffset() * 60 * 1000;
    const msLocal =  this.getTime() - offsetMs;
    const dateLocal = new Date(msLocal);
    const iso = dateLocal.toISOString();
    const isoLocal = iso.slice(0, 19);
    return isoLocal;
}
//converte localDateTime do Java.time para Date do javascript
Date.prototype.fromLocalDateTime = function(t){
    return new Date(t.year, t.monthValue - 1, t.dayOfMonth, t.hour, t.minute, t.second, t.nano);
}
//compara a string argumento com a string ignorando case
String.prototype.compareIgnoreCase = function (str) {
    return this.toLowerCase().localeCompare(str.toLowerCase());
}
//vê se a string argumento existe dentro da string ignorando case
String.prototype.includesIgnoreCase = function (str) {
    return this.toLowerCase().includes(str.toLowerCase());
}
String.prototype.replaceAll = function (target, replacement) {
    return this.split(target).join(replacement);
}

function saveLocalStorage(nome, objeto){
    localStorage.setItem(nome, JSON.stringify(objeto));
}
function loadLocalStorage(nome){
    if(checkLocalStorage(nome))
        return JSON.parse(localStorage[nome]);
    else
        return null;
}
function checkLocalStorage(nome){
    return !isNullUndef(localStorage[nome]);
}

//função foi criada para fazer o redirect caso os testes não estejam rodando
function changeLocation(location){
    if(_testesRodando === undefined)
        window.location = location;
}

//função foi criada para retornar o caminho correto caso os testes não estejam rodando
function getCaminhoTestes(caminho){
    if(_testesRodando === undefined){
        if(isNullUndef(caminho))
            return "";
        else
            return caminho;
    } else
        return "/CliqueMedicos/";
}

//returna true se o objeto for nulo ou undefined
function isNullUndef(a){
    return (a === null || a === undefined);
}

//returna true se o objeto for nulo, undefined ou empty
function isNUE(a){
    return (a === null || a === undefined || a.length === 0);
}

function hasChild(parent, childEL){
    if(isNullUndef(parent))
        return -1;
    if(isNullUndef(parent.$children))
        return -1;
    if(parent.$children.length === 0)
        return -1;
    let children = parent.$children;
    for(let i=0;i<children.length;++i){
        if(!isNullUndef(children[i].$el)
        && !isNullUndef(children[i].$el.id)){
            if(children[i].$el.id === childEL)
                return i;
        }
    }
    return -1;
}

function checarCPF(cpf){
    cpf = cpf.replaceAll('.','');
    cpf = cpf.replaceAll('-','');
    if(cpf.length !== 11)
        return false;
    let aux = ["00000000000","11111111111","22222222222","33333333333","44444444444","55555555555","66666666666","77777777777","88888888888","99999999999"];
    for(let i = 0; i < 10; i++){
        if(cpf === aux[i])
            return false;
    }

    let soma = 0;
    let peso = 10;
    for(let i = 0; i < 9; i++, peso--){
        soma += cpf[i] * peso;
    }
    let resto = soma % 11;
    let digito1 = (soma % 11) < 2 ? 0 : 11 - resto;

    soma = 0;
    peso = 11;
    for(let i = 0; i < 10; i++, peso--){
        soma += cpf[i] * peso;
    }
    resto = soma % 11;
    let digito2 = (soma % 11) < 2 ? 0 : 11 - resto;

    if(digito1 === parseInt(cpf[9]) 
    && digito2 === parseInt(cpf[10]))
        return true;
    else
        return false;
}

function todosCamposRequiredForamPreenchidos(form){
    let campos = document.getElementById(form).querySelectorAll("[required]");
    let tam = campos.length;
    let formValido = true;
    
    for(let i = 0; i < tam; i++){
        let x = campos[i];
        x.classList.remove("was-validated");
        x.classList.remove("is-invalid");
        
        x.classList.add('was-validated');
        let valido = true;
        if(x.tagName === "SELECT"){
            if(parseInt(x.value) == -1){
                valido = false;
            }
        } else if(x.tagName === "INPUT"){
            if(x.type == "text" || x.type == "date" || x.type == "number"){
                if(x.value === "") {
                    valido = false;
                }
            }
        } else if(x.tagName === "TEXTAREA"){
            if(x.value === "") {
                valido = false;
            }
        }
        if(!valido){
            x.classList.add('is-invalid');
            formValido = false;
        }
    }
    
    return formValido;
}

//retorna o valor do cookie
function getCookie(cname) {
    let name = cname + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
//insere um cookie
function setCookie(cname, cvalue, exdays) {
    let d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

//usada para pegar parâmetros no header da página
function queryParameters(query) {
    let keyValuePairs = query.split(/[&?]/g);
    let params = {};
    for (let i = 0, n = keyValuePairs.length; i < n; ++i) {
        let m = keyValuePairs[i].match(/^([^=]+)(?:=([\s\S]*))?/);
        if (m) {
            let key = decodeURIComponent(m[1]);
            (params[key] || (params[key] = [])).push(decodeURIComponent(m[2]));
        }
    }
    return params;
}

function mostraAlertaErro(vue,erro,str){
    let childIndex = hasChild(vue,"alerta");
    if(childIndex !== -1)
        vue.$children[childIndex].mostraErro(erro,str);  
}
function mostraAlertaMensagem(vue,str){
    let childIndex = hasChild(vue,"alerta");
    if(childIndex !== -1)
        vue.$children[childIndex].mostraMsg(str);
}
function mostraAlertaErroSimples(vue, erro, str) {
    let childIndex = hasChild(vue, "alerta");
    if (childIndex !== -1)
        vue.$children[childIndex].mostraErroSimples(erro, str);
}

function checkError(error, location){
    if(!isNullUndef(error)
    && !isNullUndef(error.response)) {
        if(error.response.status === 403)
            window.location = location;
        else
            return error;
    }
    else
        return null;
}

function mostraAlertaErroBackEnd(vue,erro,str){
    if(isNullUndef(erro))
        mostraAlertaErro(vue, "erro", str);
    else if(isNullUndef(erro.response))
        mostraAlertaErro(vue, erro, str);
    else if(erro.response.data !== null
    || erro.response.data !== undefined)
        mostraAlertaErro(vue, erro, erro.response.data);
    else
        mostraAlertaErro(vue, erro, str);
}