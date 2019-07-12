<template>
<section id="appRegistro" v-cloak>
		<b-container>

			<h2 v-if="cadastroCompleto">Sua conta foi cadastrada com sucesso!</h2>
			<h2 v-else>Registre-se e marque sua consulta de forma rápida, segura e moderna</h2>
			<b-row v-if="cadastroCompleto" align-h="center">
				<span>
					Falta só mais uma coisa:
				</span>
				<span>
					Te mandamos um e-mail para verificar a sua conta, por favor acesse o link que está dentro do e-mail para completar o processo de cadastro!
				</span>
			</b-row>
            <b-row v-else align-h="center">
				<b-col cols="12" md="8">  
					<b-card no-body class="mb-1" name="pagamento">
						<b-card-header header-tag="header" class="p-2" role="tab">
							Cadastro
						</b-card-header>
						<b-card-body>
							<b-alert 
							:show="alertaRegistro" 
							:variant="tipoAlertaRegistro" 
							:dismissible="fecharAlertaRegistro">
									{{mensagemAlertaRegistro}}
							</b-alert>
						</b-card-body>	
						<b-card-body>
							<div style="text-align:center">
								<div id="loaderCadastro" role="status" class="aqui"></div>
							</div>
							<div id="cadastroCampos">
								<div class="">
									<span><h4>Informe os dados de Acesso</h4></span>
									<hr>
								</div>
								<b-row align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraCPF" >CPF</label>
									</b-col>
									<b-col sm="10">
										<input 
											type="tel" maxlength="100"
											class="form-control form-control form-control-sm" 
											id="registraCPF"
											size="sm"
											placeholder="Informe o seu CPF"
											v-model="usuario.str_cpf"
											required
											v-mask="'###.###.###-##'"
											@blur="testeCampo('registraCPF')"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											{{erroCPF}}
										</b-form-invalid-feedback>    
									</b-col>
								</b-row>
								<b-row align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraSenha">Senha </label>
									</b-col>
									<b-col sm="10">
										<input  
											type="password" maxlength="100"
											class="form-control" 
											id="registraSenha"
											size="sm"
											placeholder="Informe sua senha"
											required
											v-model="seguranca.senha"
											@blur="testeSenha1('registraSenha')"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											{{erroSenha}}
										</b-form-invalid-feedback>  
									</b-col>
									<b-col sm="2" class="label-form">
										<label for="registraValidacao">Verifica senha </label>
									</b-col>
									<b-col sm="10">
										<input  
											type="password" maxlength="100"
											class="form-control" 
											id="registraValidacao"
											size="sm"
											placeholder="Informe sua senha"
											required
											v-model="seguranca.validacao"
											@blur="testeSenha2('registraValidacao')"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											{{erroSenha2}}
										</b-form-invalid-feedback>    
									</b-col>
								</b-row>
								<div class="">
									<span><h4>Informe os dados Pessoais</h4></span>
									<hr>
								</div>

								<!-- ==== -->
								<!-- NOME -->
								<!-- ==== -->
								<b-row  align-v="center"  class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraNome">
											Nome
										</label>
									</b-col>
									<b-col sm="10">
										<input   
											type="text" maxlength="100"
											class="form-control" 
											id="registraNome"
											size="sm"
											placeholder="Informe o seu nome" 
											required
											v-model="usuario.str_nome"
											@blur="testeCampo('registraNome')"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Informe seu nome
										</b-form-invalid-feedback>  
									</b-col>
								</b-row>

								<!-- ===== -->
								<!-- EMAIL -->
								<!-- ===== -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEmail">E-mail </label>
									</b-col>
									<b-col sm="10">
										<input   
											type="text" maxlength="100"
											class="form-control" 
											id="registraEmail"
											size="sm"
											placeholder="Informe o seu e-mail" 
											v-model="usuario.str_email"
											required
											@blur="testeEmail('registraEmail')"> 
										</input>                    
										<b-form-invalid-feedback id="input-live-feedback">
											Insira seu e-mail válido
										</b-form-invalid-feedback> 
									</b-col>
								</b-row>

								<!-- ======== -->
								<!-- TELEFONE -->
								<!-- ======== -->
								<b-row  align-v="center"  class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraTelefone">
											Telefone
										</label>
									</b-col>
									<b-col sm="10">
										<input 
											type="tel" maxlength="100"
											class="form-control" 
											id="registraTelefone"
											size="sm"
											placeholder="Informe o seu telefone (com DDD)" 
											v-model="usuario.str_telefone"
											required
											v-mask="'(##)#####-####'"
											@blur="testeCampo('registraTelefone')"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira seu telefone válido
										</b-form-invalid-feedback> 
									</b-col>
								</b-row>

								<!-- ================== -->
								<!-- DATA DE NASCIMENTO -->
								<!-- ================== -->
								<b-row  align-v="center"  class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="data_nascimento">
											Data nascimento
										</label>
									</b-col>
									<b-col sm="10">
										<input 
											type="date"
											class="form-control" 
											id="data_nascimento"
											size="sm"
											placeholder="dd/mm/aaaa" 
											v-model="usuario.dataNascimentoString"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira sua data de nascimento
										</b-form-invalid-feedback> 
									</b-col>
								</b-row>

								<!-- === -->
								<!-- CEP -->
								<!-- === -->
								<b-row  align-v="center"  class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="cep">
											CEP
										</label>
									</b-col>
									<b-col sm="10">
										<input 
											type="tel" maxlength="100"
											class="form-control" 
											id="cep"
											size="sm"
											placeholder="Informe o seu CEP" 
											v-model="usuario.str_cep"
											v-mask="'#####-###'"
											@blur="viacep(usuario.str_cep)">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira seu CEP
										</b-form-invalid-feedback> 
									</b-col>
								</b-row>

								<!-- ========== -->
								<!-- LOGRADOURO -->
								<!-- ========== -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEnderecoLogradouro">Logradouro</label>
									</b-col>
									<b-col sm="10">
										<input
												type="text" maxlength="100"
												class="form-control"
												id="registraEnderecoLogradouro"
												size="sm"
												placeholder="-"
												v-model="usuario.str_logradouro">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira o logradouro
										</b-form-invalid-feedback>
									</b-col>
								</b-row>

								<!-- =========== -->
								<!-- COMPLEMENTO -->
								<!-- =========== -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEnderecoComplemento">Complemento</label>
									</b-col>
									<b-col sm="10">
										<input
												type="text" maxlength="100"
												class="form-control"
												id="registraEnderecoComplemento"
												size="sm"
												placeholder="-"
												v-model="usuario.str_complemento">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											<!-- O complemento não é obrigatório -->
										</b-form-invalid-feedback>
									</b-col>
								</b-row>

								<!-- ====== -->
								<!-- BAIRRO -->
								<!-- ====== -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEnderecoBairro">Bairro</label>
									</b-col>
									<b-col sm="10">
										<input
												type="text" maxlength="100"
												class="form-control"
												id="registraEnderecoBairro"
												size="sm"
												placeholder="-"
												v-model="usuario.str_bairro">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira o bairro
										</b-form-invalid-feedback>
									</b-col>
								</b-row>

								<!-- ====== -->
								<!-- CIDADE -->
								<!-- ====== -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEnderecoLocalidade">Localidade</label>
									</b-col>
									<b-col sm="10">
										<input
												type="text" maxlength="100"
												class="form-control"
												id="registraEnderecoLocalidade"
												size="sm"
												placeholder="-"
												v-model="usuario.str_localidade">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira a localidade
										</b-form-invalid-feedback>
									</b-col>
								</b-row>

								<!-- == -->
								<!-- UF -->
								<!-- == -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEnderecoUf">UF</label>
									</b-col>
									<b-col sm="10">
										<input
												type="text" maxlength="100"
												class="form-control"
												id="registraEnderecoUf"
												size="sm"
												placeholder="-"
												v-model="usuario.str_uf">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira a UF
										</b-form-invalid-feedback>
									</b-col>
								</b-row>

								<!-- ======= -->
								<!-- UNIDADE -->
								<!-- ======= -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="registraEnderecoUnidade">Unidade</label>
									</b-col>
									<b-col sm="10">
										<input
												type="text" maxlength="100"
												class="form-control"
												id="registraEnderecoUnidade"
												size="sm"
												placeholder="-"
												v-model="usuario.str_unidade">
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											<!-- A unidade não é obrigatória -->
										</b-form-invalid-feedback>
									</b-col>
								</b-row>

								<!-- ==================== -->
								<!-- BOTÃO PARA REGISTRAR -->
								<!-- ==================== -->
								<b-row  align-v="center" class="mb-2">
									<b-col sm="12">
										<b-button   id="botaoRegistro" 
											role="status" aria-hidden="true"
											type="button" aria-label="Add" 
											block 
											class="btn btn-success btn-sm registro" 
											v-on:click="cadastrarUsuario(usuario)"
											style="margin-top: 20px;">
											REGISTRAR
										</b-button>
									</b-col>
								</b-row>

								<b-row align-v="center" class="mb-2">
									<b-col class=" text-center">	
										<hr>
										<h4>Já tem uma conta? 
	                          				<span @click="toggleCadastro()" 
	                          				style="cursor:pointer;text-decoration:underline">
	                          					Faça login!
	                          				</span>
	                        			</h4>
									</b-col>
								</b-row>
							</div>
						</b-card-body>
					</b-card>	
				</b-col>	
			</b-row>
		</b-container>
	</section>
</template>
<style>
hr {
    margin-top: 1rem;
    margin-bottom: 1rem;
    border: 0;
    border-top: 1px solid #6c757d !important;
}
.label-form {
    text-align: right !important;
}
@media ( max-width : 576px) {
   .label-form {
    text-align: left !important;
	} 
		.separador {
		width: 100% !important;
		margin: 0 auto !important;
		border-bottom: 1px solid #1e6a82 !important;
		padding-bottom: 0px !important;
		margin-bottom: 20px !important;
	}
}
</style>

<script type="text/javascript">
	"use strict";
	module.exports = {
        data: function() {
            return {
                usuario: {
                    str_nome             : "",
                    str_senha            : "",
                    str_cpf              : "",
                    str_email            : "",
                    str_telefone         : "",
                    str_endereco         : "",
					str_logradouro       : "",
					str_complemento      : "",
					str_bairro           : "",
					str_localidade       : "",
					str_uf               : "",
					str_unidade          : "",
                    str_cep              : "",
                    data_nascimento      : "",
                    dataNascimentoString : "",
                },
                seguranca: {
                    senha  	  : '',
                    validacao : ''
                },
                alertaRegistro : false,
                mensagemAlertaRegistro : 'Alert! Mensagem para alertas.',
                tipoAlertaRegistro : 'info',
                fecharAlertaRegistro : true,
                cadastroCompleto : false,
                erroCPF : "Insira seu CPF",
                erroSenha : "Informe uma senha segura",
                erroSenha2 : "Repita a senha",
            }
		},
		methods: {
			toggleCadastro(){
				const vm = this;
				window.scroll(0,0);
				vm.$emit("toggle-cadastro",false);
			},
			testeCampo(campo){
				let elemento = document.getElementById(campo);
				if(isNullUndef(elemento))
					return null;
				if(elemento.value === ""){
					elemento.classList.add("is-invalid");
					return null;
				} else {
					elemento.classList.remove("is-invalid");
					return elemento;
				}
			},
			testeCPF(campo){
				const vm = this;
				let elemento = vm.testeCampo(campo);
				if(isNullUndef(elemento))
					return;
				if(checarCPF(elemento.value)){
					elemento.classList.remove("is-invalid");
	                vm.erroCPF = "Insira seu CPF";
	                return true;
				} else {
	                elemento.classList.add("is-invalid");
	                vm.erroCPF = "CPF inválido";
	                return false;
				}
			},
			testeSenha1(campo){
				const vm = this;
				let elemento = vm.testeCampo(campo);
				if(isNullUndef(elemento))
					return;
				//TODO: outras coisas de senha, como tamanho e símbolo
			},
			testeSenha2(campo){
				const vm = this;
				let elemento = vm.testeCampo(campo);
				if(isNullUndef(elemento))
					return;
				if(vm.seguranca.senha === vm.seguranca.validacao){
					elemento.classList.remove("is-invalid");
	                vm.erroSenha = "Informe uma senha segura";
	                vm.erroSenha2 = "Repita a senha";
	                return true;
				} else {
					elemento.classList.add("is-invalid");
	                vm.erroSenha = "As senhas não são iguais";
	                vm.erroSenha2 = "As senhas não são iguais";
	                return false;
				}
			},
			testeEmail(campo){
				const vm = this;
				let elemento = vm.testeCampo(campo);
				if(isNullUndef(elemento))
					return;
				elemento.value = elemento.value.replaceAll(" ","");
				if(vm.checkEmail(elemento.value)){
					elemento.classList.remove("is-invalid");
					return true;
				} else {
					elemento.classList.add("is-invalid");
					return false;
				}
			},
            checkEmail(email) {
                const vm = this;
                let valido = "";
                let reg = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
                valido = reg.test(email);
                return valido;
            },    
			cadastrarUsuario(usuario){

	            const vm = this;
	            let cont = 0;
	            let camposAlvo = document.getElementsByClassName("form-control");
                for (let i=0; i < camposAlvo.length; i++){
                	let elem = camposAlvo[i];
                	if(!elem.hasAttribute("required"))
                		continue;
                	if(elem.value === ""){
                    	elem.classList.add("is-invalid");
                    	cont++;
                	} else if(elem.id === "registraEmail"){
						if(vm.checkEmail(elem.value)){
							elem.classList.remove("is-invalid");
						} else {
                    		elem.classList.add("is-invalid");
							cont++;
                    	}
                	} else {
                			elem.classList.remove("is-invalid");
                	}
                }
                window.scroll(0,0);
                if(cont === 0){
                	if(vm.seguranca.senha !== vm.seguranca.validacao){
                		vm.alertaRegistro = true;
		                vm.mensagemAlertaRegistro = 'As senhas não são iguais.';
		                vm.tipoAlertaRegistro = 'danger';
		                return false;
                	}
                	vm.usuario.str_senha = vm.seguranca.senha;
		            return vm.cadastrarUsuarioRequest(usuario);
		        }
		        return false;
	        },
	        cadastrarUsuarioRequest(usuario){
	        	const vm = this;
				iniciaLoader("loaderCadastro","cadastroCampos");
				vm.alertaRegistro = false;
				usuario.data_nascimento = createDateFromSQLString(usuario.dataNascimentoString);
				let obj = {
					str_nome        : usuario.str_nome,
                    str_senha       : usuario.str_senha,
                    str_cpf         : usuario.str_cpf,
                    str_email       : usuario.str_email,
                    str_telefone    : usuario.str_telefone,
                    str_cep         : usuario.str_cep,
					str_logradouro  : usuario.str_logradouro,
					str_endereco    : usuario.str_logradouro,
					str_complemento : usuario.str_complemento,
					str_bairro      : usuario.str_bairro,
					str_localidade  : usuario.str_localidade,
					str_uf          : usuario.str_uf,
					str_unidade     : usuario.str_unidade,
					data_nascimento : createDateFromSQLString(usuario.dataNascimentoString),
				};
	        	return axios.post(getCaminhoTestes() + "rs/cadastro/cadastrar", obj)
	                .then(function (response) {
						console.log("TOP");
						vm.cadastroCompleto = true;
						vm.alertaRegistro = false;
						encerraLoader("loaderCadastro","cadastroCampos");
	                    return true;
	                })
	                .catch(function (error) {
						console.log("POT");
						vm.alertaRegistro = true;
						vm.mensagemAlertaRegistro = error.response.data;
						vm.tipoAlertaRegistro = 'danger';
						if(isNullUndef(vm.mensagemAlertaRegistro)){
							vm.mensagemAlertaRegistro =  "Não foi possível finalizar seu cadastro. Entre em contato com clique@amhp.com.br"
						}
						encerraLoader("loaderCadastro","cadastroCampos");
	                    return false;
	                });
	        },
	        buscaEndereco(cep){
                const vm = this;
                if(cep.length != 9)
                	return false;
                return axios.get("https://viacep.com.br/ws/"+ cep + "/json/")
		            .then(function (response) {
		            	let dados = response.data;
		            	vm.usuario.str_endereco = 
		            		response.data.logradouro + ", " +
		            		response.data.bairro + ", " +
		            		response.data.localidade + ", " +
		            		response.data.uf;
                        return true;
		            }).catch(function (error) {
		                console.log(error);
		                return false;
                    });
            },
	        scrollTop(scrollDuration) {
	            var scrollStep = -window.scrollY / (scrollDuration / 15),
	                scrollInterval = setInterval(function(){
	                if ( window.scrollY != 0 ) {
	                    window.scrollBy( 0, scrollStep );
	                }
	                else clearInterval(scrollInterval); 
	            },15);
	        },

			/**
			 *
			 * Dispara uma requisição ao ViaCep informando o cep para preencher o endereço
			 *
			 * @param cep
			 */
			viacep(cep) {

				const URI_TEMPLATE = "https://viacep.com.br/ws/${cep}/json/";
				const REQ_URL      = URI_TEMPLATE.replace("${cep}", cep);

				const vm = this;

				axios
					.get(REQ_URL)
					.then(function(response) {
						vm.usuario.str_logradouro  = response.data["logradouro"];
						vm.usuario.str_complemento = response.data["complemento"];
						vm.usuario.str_bairro      = response.data["bairro"];
						vm.usuario.str_localidade  = response.data["localidade"];
						vm.usuario.str_uf          = response.data["uf"];
						vm.usuario.str_unidade     = response.data["unidade"];
					})
					.catch(function(error) {
						console.log(error.response.data);
					});
			}
        },
        created: function() {
            const vm = this;
        }
	}
</script>
