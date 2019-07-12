<template>
	<b-form class="form-signin" @keydown.enter.prevent.self>
<div role="tablist" id="infoPagamentos">
    <b-card no-body class="mb-1 mt-3">
      <b-card-header header-tag="header" role="tab">
        <b-row>
            <b-col cols="12">
                <b-button block href="#" v-b-toggle.beneficiario variant="info" class="text-left mt-2" >                    
                        <span class="when-info-opened"><h5><i class="far fa-minus-square"></i> Informações do Beneficiário</h5></span>
                        <span class="when-info-closed"><h5><i class="far fa-plus-square"></i> Informações do Beneficiário</h5></span>                     
                </b-button>              
            </b-col>
        </b-row>        
      </b-card-header>
      <b-collapse id="beneficiario" visible accordion="beneficiario" role="tabpanel">
        <b-card-body>
        <b-container>
            <!-- <b-row class="mt-3">
                <b-col sm="3"></b-col>
                <b-col sm="9">
                    <h5>Informações do Beneficiário</h5>
                </b-col>
            </b-row> -->
            <b-row align-v="center" class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="nome_paciente" >Nome</label>
                </b-col>
                <b-col sm="9">
                    <b-form-input 
                        type="text"   
                        automplete="cc-name" maxlength="100"
                        class="form-control"
                        id="nome_paciente"
                        placeholder="Insira o nome do paciente"
                        v-model="paciente.nome"
                        required                         
                        autofocus> 
                    </b-form-input> 
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira o nome do beneficiario
                    </b-form-invalid-feedback>    
                </b-col>
            </b-row>
            <b-row  align-v="center" class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="email_paciente">Email</label>
                </b-col>
                <b-col sm="9">
                    <b-form-input  
                        type="text" maxlength="100"
                        class="form-control" 
                        id="email_paciente"
                        placeholder="Informe o Email do paciente"
                        required
                        @blur="testeEmail()"
                        v-model="paciente.email"
                       > 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira o email do paciente
                    </b-form-invalid-feedback>  
                </b-col>
            </b-row>
            <b-row  align-v="center"  class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="str_telefone_paciente">
                        Telefone
                    </label>
                </b-col>
                <b-col sm="9">
                    <b-form-input   
                        type="text" maxlength="100"
                        class="form-control" 
                        id="str_telefone_paciente"
                        placeholder="Informe o telefone do paciente"
                        v-mask="'(##)#####-####'"
                        required
                        v-model="paciente.telefone"> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira o telefone do paciente
                    </b-form-invalid-feedback>  
                </b-col>
            </b-row>
            <!-- <b-row  align-v="center" class="mb-2">
                <b-button   id="botaoCartao" 
                            role="status" aria-hidden="true"
                            type="button" aria-label="Add" 
                            class="btn btn-success btn-sm col-6 pagar" 
                            v-on:click="mostrarAreaCartao()"
                            style="margin-top: 20px;">
                    PAGAR COM CARTÃO
                </b-button>
                <b-button   id="botaoBoleto" 
                            role="status" aria-hidden="true"
                            type="button" aria-label="Add" 
                            class="btn btn-success btn-sm col-6 pagar" 
                            v-on:click="mostrarAreaBoleto()"
                            style="margin-top: 20px;">
                    PAGAR COM BOLETO
                </b-button>
            </b-row> -->
        </b-container>
        </b-card-body>
      </b-collapse>
    </b-card>
    <b-card no-body class="mb-1 mt-3">
      <b-card-header header-tag="header" role="tab">
            <b-row class="">
                <b-col cols="12">
                        <b-button block href="#" v-b-toggle.pagamento variant="info" class="text-left" >
                        <span class="when-dados-opened"><h5><i class="far fa-minus-square"></i> Dados do Cartão de Crédito</h5></span>
                        <span class="when-dados-closed"><h5><i class="far fa-plus-square "></i > Dados do Cartão de Crédito</h5></span>
                    </b-button>
                    <!-- <b-button block href="#" @click="iconMinus=!iconMinus" v-b-toggle.pagamento variant="info" class="text-right" ><i class="far fa-minus-square"></i></b-button> -->
                </b-col>
            </b-row>
      </b-card-header>
      <b-collapse id="pagamento" visible  accordion="pagamento" role="tabpanel">
        <b-card-body>
        <!-- <b-container v-if="mostrarCartao"> -->           
        <b-container>
            <b-row align-v="center" class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="CardholderName" >Nome no Cartão</label>
                </b-col>
                <b-col sm="9">
                    <b-form-input 
                        type="text"   
                        automplete="cc-name" maxlength="100"
                        class="form-control" 
                        id="CardholderName"
                        placeholder="Insira o nome escrito no cartão"
                        v-model="payment.creditCard.holder.name"
                        required> 
                    </b-form-input> 
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira o nome do titular
                    </b-form-invalid-feedback>
                </b-col>
            </b-row>
            <b-row  align-v="center" class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="CardNumber" > Número do Cartão</label>
                </b-col>
                <b-col sm="9">
                    <b-form-input   
                        type="text" 
                        inputmode="numeric" 
                        autocomplete="cc-number" maxlength="100"
                        class="form-control" 
                        id="CardNumber"
                        placeholder="Informe o número do cartão" 
                        v-mask="'#### #### #### ####'"
                        v-model="cardNumber"
                        required> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira o número do cartão
                    </b-form-invalid-feedback>  
                </b-col>
            </b-row>
            <b-row  align-v="center" class="mb-2" >
                <b-col sm="3" class="label-form">
                    <label for="Expiry">Validade</label>
                </b-col>
                <b-col sm="3 mb-2">
                    <b-form-input
                        type="text"
                        autocomplete="cc-exp" maxlength="100"
                        class="form-control" 
                        id="Expiry"
                        placeholder="MM/AAAA"
                        v-mask="'##/####'"
                        v-model="expiry"
                        required>
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira a validade
                    </b-form-invalid-feedback> 
                </b-col>
                <b-col sm="3" class="label-form">
                    <label for="SecurityCode">
                        CVC
                    </label>
                </b-col>
                <b-col sm="2 mb-2">
                    <b-form-input   
                        type="text"
                        autocomplete="cc-csc" maxlength="100"
                        class="form-control" 
                        id="SecurityCode"
                        placeholder="000"
                        v-mask="'###'"
                        v-model="securityCode"
                        required> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira o código CVC
                    </b-form-invalid-feedback> 
                </b-col>
            </b-row>
            <b-row align-v="center" class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="str_cpf_cliente">CPF do titular</label>
                </b-col>
                <b-col sm="9">
                    <b-form-input  
                        type="text" maxlength="100"
                        class="form-control" 
                        id="str_cpf_cliente"
                        placeholder="Informe o CPF do titular"
                        required
                        v-mask="'###.###.###-##'"
                        v-model="payment.creditCard.holder.documents[0].value"> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Informe o CPF válido do titular
                    </b-form-invalid-feedback>
                </b-col>
            </b-row>
            <b-row align-v="center"  class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="birthdate_holder">
                        Data de Nascimento
                    </label>
                </b-col>
                <b-col sm="9">
                    <b-form-input   
                        type="date" maxlength="100"
                        class="form-control" 
                        id="birthdate_holder"
                        placeholder="dd/mm/aaaa" 
                        required
                        v-model="holderBirthDate"> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira a data de nascimento do titular
                    </b-form-invalid-feedback> 
                </b-col>
            </b-row>
            <b-row  align-v="center"  class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="str_telefone_cliente">
                        Telefone do titular
                    </label>
                </b-col>
                <b-col sm="9">
                    <b-form-input   
                        type="text" maxlength="100"
                        class="form-control" 
                        id="str_telefone_cliente"
                        placeholder="Informe o telefone do titular"
                        required
                        v-mask="'(##)#####-####'"
                        v-model="holderPhone"> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Informe o telefone do titular
                    </b-form-invalid-feedback>
                </b-col>
            </b-row>
            <b-row  align-v="center"  class="mb-2">
                <b-col sm="3" class="label-form">
                    <label for="BillingAddressPostalCode">
                        CEP
                    </label>
                </b-col>
                <b-col sm="9">
                    <b-form-input 
                        type="text" maxlength="100"
                        class="form-control" 
                        id="BillingAddressPostalCode"
                        placeholder="Informe o seu CEP" 
                        v-model="payment.creditCard.billingAddress.postalCode"
                        required
                        @blur.native="buscaEndereco(payment.creditCard.billingAddress.postalCode)" 
                        autofocus> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira seu CEP
                    </b-form-invalid-feedback> 
                </b-col>
            </b-row>
            <b-row  align-v="center" class="mb-2">                
                <b-col sm="3" class="label-form">
                    <label for="BillingAddressLine1">Endereço</label>
                </b-col>
                <b-col sm="9">
                    <b-form-input   
                        type="text" maxlength="100"
                        class="form-control" 
                        id="BillingAddressLine1"
                        placeholder="Informe o seu endereço"
                        required
                        v-model="payment.creditCard.billingAddress.street"> 
                    </b-form-input>
                    <b-form-invalid-feedback id="input-live-feedback">
                        Insira seu endereço
                    </b-form-invalid-feedback> 
                </b-col>
            </b-row>

        </b-container>
        </b-card-body>
      </b-collapse>
        <b-container>
            <b-row  align-v="center" class="">
                <b-button   id="botaoCompra" 
                            role="status" aria-hidden="true"
                            type="button" aria-label="Add" 
                            class="btn btn-success btn-sm col-12 pagar" 
                            v-on:click="validaCartao()"
                            style="margin-top: 20px;">
                    Pagar com Cartão
                </b-button>
            </b-row>
        </b-container>
    </b-card>
  </div>
    </b-form>
	<!-- <input type="submit" id="payButtonId" value="Checkout Now" />
    <br />
    <iframe id="targetIframe" style="display:none;"></iframe> -->
</template>
<style >
    .collapsed > .when-info-opened,:not(.collapsed) > .when-info-closed {
        display: none;
    }
    .collapsed > .when-info-closed,:not(.collapsed) > .when-info-opened {
        display: block;
    }

    .collapsed > .when-dados-opened,:not(.collapsed) > .when-dados-closed {
        display: none;
    }
    .collapsed > .when-dados-closed,:not(.collapsed) > .when-dados-opened {
        display: block;
    }
    .card-header{
        padding: .05rem 0.05rem !important;
    }

    .label-form {
        text-align: right !important;
    }
    @media ( max-width : 576px) {
    .label-form {
        text-align: left !important;
        } 
    }
    #infoPagamentos .btn-info {
        color: #117a8b !important;
        background:none !important;
        border: none !important;
    }
    #infoPagamentos .btn-info:hover {
        color: #117a8b !important;
        background:none !important;
        border: none !important;
    }

    #infoPagamentos .btn-info.focus, #infoPagamentos .btn-info:focus {
        box-shadow: 0 0 0 0.2rem rgba(58,176,195,.5) !important;
    }
    #infoPagamentos .card-header, #infoPagamentos .card-footer {
        background:none !important;
        border: none !important;    
        font-size: 1.5rem;
        line-height: 1.0;
        font-weight: 600;
        text-align: left;
        color: #117a8b !important;
    }

    #infoPagamentos .card{
        /* border: none !important; */
        border-bottom: 1px solid #ccc;
    }

</style>
<script type="text/javascript">
    "use strict";
    module.exports = {
        data: function () {
            return {
                iconMinus: false,
                mostrarCartao : false,
                mostrarBoleto : false,
                payButtonId:'payButtonId',
                loadBoleto:'',
                paciente : {
                    nome        : "",
                    email       : "",
                    telefone    : "",
                    cep         : "",
                    cpf         : "",
                    endereco    : "",
                    bairro      : "",
                    complemento : "",
                    localidade  : "",
                    logradouro  : "",
                    uf          : "",
                },
                payment: {
                    sender: {
                        hash: '',
                    },
                    creditCard: {
                        token: '',
                        installment: {
                            quantity: '1',
                            value: '',
                            noInterestInstallmentQuantity: ''
                        },
                        holder: {
                            name: '',
                            documents: [{
                                type: 'CPF',
                                value: '',
                            }],
                            birthDate: '',
                            phone: {
                                areaCode: "",
                                number: "",
                            },
                        },
                        billingAddress: {
                            number: "",
                            street: "",
                            district: "",
                            city: "",
                            state: "",
                            country: "",
                            postalCode: "",
                            complement: "",
                        }
                    }
                },
                validacao: {
                    CardNumber: "",
                    Expiry: "",
                    SecurityCode: "",
                    CardholderName: "",
                    BillingAddressLine1: "",
                    BillingAddressPostalCode: "",
                    str_cpf_cliente: "",
                    str_telefone_cliente: "",
                },
                cliente: {
                    CardNumber: "",
                    Expiry: "",
                    SecurityCode: "",
                    CardholderName: "",
                    EmailAddress: "",
                    BillingAddressLine1: "",
                    BillingAddressPostalCode: "",
                    str_cpf_cliente: "",
                    str_telefone_cliente: "",
                    BillingAddressCountry: "",
                    Timestamp: "",
                    MerchantID: "",
                    OrderId: "",
                    Amount: "",
                    Currency: "",
                    AutoSettleFlag: "",
                    HPPVersion: "",
                    Sha1hash: "",
                },
                cardNumber: '', // Número do cartão de crédito
                securityCode: '', // CVV do cartão
                expiry: '',
                expirationMonth: '', // Mês da expiração do cartão
                expirationYear: '',
                sessionId: '',
                holderPhone: '',
                holderBirthDate: ''
            }  
		},
		methods: {  
		        mostrarAreaCartao() {
		            const vm = this;
		            vm.mostrarCartao = true;
		            vm.mostrarBoleto = false;
		        },
		        mostrarAreaBoleto() {
		            const vm = this;
		            vm.mostrarCartao = false;
		            vm.mostrarBoleto = true;
		        },
		        buscaEndereco(cep) {
		            const vm = this;
		            if (cep.length == 8) {
		                return axios.get("https://viacep.com.br/ws/" + cep + "/json/")
                            .then(function (response) {
                                vm.payment.creditCard.billingAddress.number = "0",
                                vm.payment.creditCard.billingAddress.street = response.data.logradouro;
                                vm.payment.creditCard.billingAddress.district = response.data.bairro;
                                vm.payment.creditCard.billingAddress.city = response.data.localidade;
                                vm.payment.creditCard.billingAddress.state = response.data.uf;
                                vm.payment.creditCard.billingAddress.country = "Brasil"; // nao ha cep em outro pais
                                vm.payment.creditCard.billingAddress.complement = "0";
                            }).catch(function (error) {
                                console.log(error);
                            });
		            }
		        },
		        buscaDadosUsuario() {
		            const vm = this;
		            return axios.get(getCaminhoTestes() + "../rs/cadastro/private/buscar")
		                .then(function (response) {

		                    let usuario = response.data;

		                    vm.paciente.nome        = usuario.str_nome;
		                    vm.paciente.telefone    = usuario.str_telefone;
                            vm.paciente.email       = usuario.str_email;
                            vm.paciente.cpf         = usuario.str_cpf;
		                    vm.paciente.cep         = usuario.str_cep;
		                    vm.paciente.endereco    = usuario.str_endereco;
                            vm.paciente.bairro      = usuario.str_bairro;
                            vm.paciente.complemento = usuario.str_complemento;
                            vm.paciente.localidade  = usuario.str_localidade;
                            vm.paciente.logradouro  = usuario.str_logradouro;
                            vm.paciente.uf          = usuario.str_uf;
                            vm.paciente.unidade     = usuario.str_unidade;

		                    vm.holderPhone = usuario.str_telefone;

		                    vm.payment.creditCard.holder.documents[0].value = usuario.str_cpf;
		                    vm.payment.creditCard.billingAddress.postalCode = usuario.str_cep;
		                    vm.payment.creditCard.billingAddress.street     = usuario.str_endereco;

		                    vm.holderBirthDate = new Date(usuario.data_nascimento).toInputDate();
		                    console.log(response.data);

		                }).catch(function (error) {
		                    return null;
		                });
		        },
		        getSessionId() {
		            const vm = this;
		            return axios.get(getCaminhoTestes() + "../rs/pagamento/private/sessaoPS")
		                .then(function (response) {
		                    vm.sessionId = response.data;
		                    PagSeguroDirectPayment.setSessionId(vm.sessionId);
		                    console.log(response.data);
		                }).catch(function (error) {
		                    return null;
		                });
		        },
		        async createCardToken() {

		            const vm = this;
		            let brand = {}
		            //TODO melhor esse jeito de extrair ddd e telefone
		            vm.payment.creditCard.holder.phone.areaCode = vm.holderPhone.replaceAll(/\W/, "").substring(0, 2);
		            vm.payment.creditCard.holder.phone.number = vm.holderPhone.replaceAll(/\W/, "").substring(2);

		            vm.expirationMonth = vm.expiry.split('/')[0];
		            vm.expirationYear = vm.expiry.split('/')[1];
		            // PagSeguroDirectPayment.setSessionId(vm.sessionId);
		            console.log(vm.sessionId)

                    // Cria hash pagseguro
		            PagSeguroDirectPayment.onSenderHashReady((response) => {

		                if (response.status == 'error') {
		                    console.log(response.message);
		                    return false;
		                }

		                console.log(response);
		                this.payment.sender.hash = response.senderHash; //Hash estará disponível nesta variável.

                        // Pega dados do cartão de crédito
                        PagSeguroDirectPayment.getBrand({
                            cardBin: vm.cardNumber.replaceAll(/\W/, "").substring(0, 6), //busca os primeiros 6 numeros do cartao
                            success: function (response) {

                                brand = response;
                                console.log(vm.cardNumber + " - " + brand.name);
                                PagSeguroDirectPayment.createCardToken({
                                    cardNumber: vm.cardNumber.replaceAll(/\W/, ""), // Número do cartão de crédito
                                    brand: brand.name, // Bandeira do cartão
                                    cvv: vm.securityCode, // CVV do cartão
                                    expirationMonth: vm.expirationMonth, // Mês da expiração do cartão
                                    expirationYear: vm.expirationYear, // Ano da expiração do cartão, é necessário os 4 dígitos.
                                    success: function (response) {
                                        vm.payment.creditCard.token = response.card.token;
                                        vm.payment.creditCard.holder.birthDate = createDateFromSQLString(vm.holderBirthDate).formatDate("dmy");
                                        vm.$emit('pagar-cartao', vm.payment, vm.paciente);
                                        console.log(vm.payment);
                                    },
                                    error: function (response) {
                                        console.log(response);
                                    },
                                    complete: function (response) {
                                        // Callback para todas chamadas.
                                    }
                                });
                            },
                            error: function (error) {
                                console.log(error);
                            },
                            complete: function (response) {
                                //tratamento comum para todas chamadas
                            }
                        });
		            });

		        },
		        pagarCartao() {
                    const vm = this;

                    
		            vm.createCardToken(vm.payment.creditCard);
		        },
		        adicionarClass(campo, classe) {
		            let alvo = document.getElementById(campo);

		            if (!alvo.classList.contains(classe)) {
		                alvo.classList.add(classe);
		            }
		        },
		        removerClass(campo, classe) {
		            let alvo = document.getElementById(campo);

		            if (alvo.classList.contains(classe)) {
		                alvo.classList.remove(classe);
		            }
		        },
		        validaCartao() {

		            const vm = this;
		            let cont = 0;
		            for (let prop in vm.validacao) {
		                // console.log("obj." + prop + " = -->" + vm.validacao[prop]+"<--");
		                let campoalvo = document.getElementById(prop);

		                if (campoalvo.value === "") {
                            vm.adicionarClass(prop, "is-invalid");
                            campoalvo.focus();
		                    cont++;
		                } else {
                            if (prop == "str_cpf_cliente"){
                                console.log("aqui está o CPF"+ campoalvo.value );
                                if(!vm.testaCPF(campoalvo.value)){
                                    vm.adicionarClass(prop, "is-invalid");
                                    campoalvo.focus();
                                    cont++;
                                }else{
                                    vm.removerClass(prop, "is-invalid");
                                };
                            }else{
                                vm.removerClass(prop, "is-invalid");
                            }
		                    
		                }

		                // return mandarEmail(dados);
		            }

		            // let isNomeInformado  = vm.paciente.nome;
		            // let isEmailInformado = vm.paciente.email;
		            // let isTelefone       = vm.paciente.telefone;
		            // let isDataNascimento = vm.holderBirthDate;
                    //
		            // // Campos adicionais a serem validados
		            // if (!isNomeInformado
                    //     || !isEmailInformado
                    //     || !isTelefone
                    //     || !isDataNascimento) {
                    //
		            //     cont ++;
                    //
		            //     if (!isNomeInformado) {
                    //         vm.adicionarClass('nome_paciente', "is-invalid");
                    //     }
		            //     else {
                    //         vm.removerClass('nome_paciente', "is-invalid");
                    //     }
                    //
                    //     if (!isEmailInformado) {
                    //         vm.adicionarClass('email_paciente', "is-invalid");
                    //     }
                    //     else {
                    //         vm.removerClass('email_paciente', "is-invalid");
                    //     }
                    //
                    //     if (!isTelefone) {
                    //         vm.adicionarClass('str_telefone_paciente', "is-invalid");
                    //     }
                    //     else {
                    //         vm.removerClass('str_telefone_paciente', "is-invalid");
                    //     }
                    //
                    //     if (!isDataNascimento) {
                    //         vm.adicionarClass('birthdate_holder', "is-invalid");
                    //     }
                    //     else {
                    //         vm.removerClass('birthdate_holder', "is-invalid");
                    //     }
                    // }

		            console.log("var vm.erroValidacao = " + cont);
		            if (cont === 0) {
                        //
		                //console.log("feito");		     
		                return vm.createCardToken(vm.payment.creditCard);
		            }
                }, 
                testeEmail(campo){
                     console.log("testeEmail"+campo);		
                    // const vm = this;
                    // let elemento = vm.testeCampo(campo);
                    // if(isNullUndef(elemento))
                    //     return;
                    // elemento.value = elemento.value.replaceAll(" ","");
                    // if(vm.checkEmail(elemento.value)){
                    //     elemento.classList.remove("is-invalid");
                    //     return true;
                    // } else {
                    //     elemento.classList.add("is-invalid");
                    //     return false;
                    // }
                },
                checkEmail(email) {
                    const vm = this;
                    let valido = "";
                    let reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
                    valido = reg.test(email);
                    return valido;
                },
                testaCPF(strCPF) {
                        var Soma;
                        var Resto;
                        Soma = 0;
                        strCPF = strCPF.replace(/[^\d]+/g,'');
                        //console.log("testaCPF "+strCPF);
                    if (strCPF == "00000000000") return false;
                        
                    for (let i=1; i<=9; i++){
                        Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (11 - i);
                    }
                    Resto = (Soma * 10) % 11;
                    
                        if ((Resto == 10) || (Resto == 11))  Resto = 0;
                        if (Resto != parseInt(strCPF.substring(9, 10)) ) return false;
                    
                    Soma = 0;
                        for (let i = 1; i <= 10; i++){ 
                            Soma = Soma + parseInt(strCPF.substring(i-1, i)) * (12 - i);
                        }

                        Resto = (Soma * 10) % 11;
                    
                        if ((Resto == 10) || (Resto == 11))  Resto = 0;
                        if (Resto != parseInt(strCPF.substring(10, 11) ) ){
                         //console.log("False " + strCPF);
                            return false;
                        }
                        //console.log("True " + strCPF);
                        return true;
                    }
                
		    },
		    /* ON LOAD */
		    created: function () {
		        const vm = this;
		        console.log("GP");
		        vm.getSessionId();
		        vm.buscaDadosUsuario();
		    }
		}
</script>