<template>
	<section id="appLogin" v-cloak>
		<b-container> 
			<b-row v-if="mostraLogin" align-h="center">
				<b-col cols="12" md="8">  
					<h2>Entre e marque sua consulta de forma rápida, segura e moderna</h2>
					<b-card no-body class="mb-1" name="pagamento">
						<b-card-header header-tag="header" class="p-2" role="tab">
							Login
						</b-card-header>
						<b-card-body>
							<b-alert 
								:show="alertaLogin" 
								:variant="tipoAlertaLogin" 
								:dismissible='fecharAlertaLogin'>
								<span>{{mensagemAlertaLogin}}</span>
							</b-alert>
						</b-card-body>
						<b-card-body>
							<div style="text-align:center">
								<div id="loaderLogin" role="status" class="aqui"></div>
							</div>
							<div id="loginCampos">
								<b-row align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="cpf">CPF</label>
									</b-col>
									<b-col sm="10">
										<input  
											type="tel" maxlength="100"
											class="form-control form-control form-control-sm" 
											id="cpf"
											placeholder="Informe o seu CPF"
											v-model="login.cpf"
											required
											v-mask="'###.###.###-##'" 
											on-blur="cpfValido('cpf',login.cpf)" autofocus> 
										</input> 
										<b-form-invalid-feedback id="input-live-feedback">
											{{erroCPF}}
										</b-form-invalid-feedback>    
									</b-col>
								</b-row>
								<b-row align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">
										<label for="senha">Senha</label>
									</b-col>
									<b-col sm="10">
										<input  
											type="password" maxlength="100"
											class="form-control" 
											id="senha"
											size="sm"
											placeholder="Informe sua senha"
											v-model="login.senha"> 
										</input>
										<b-form-invalid-feedback id="input-live-feedback">
											Insira sua senha
										</b-form-invalid-feedback>  
									</b-col>
								</b-row>
								<b-row align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">										
									</b-col>
									<b-col sm="10">
										<b-form-checkbox v-model="checked" name="check-button" switch>
											Salvar senha
										</b-form-checkbox>
									</b-col>
								</b-row>
								<b-row align-v="center" class="mb-2">
									<b-col sm="2" class="label-form">											
									</b-col>
									<b-col sm="10">
										<b-button size="sm" variant="primary" @click="validaLogin(login)">
											Entrar
										</b-button>
										<b-link href="./esqueceuSenha.html" class="btn btn-outline-info btn-sm">Esqueci a Senha</b-link>
									</b-col>
								</b-row>
								
								<b-row align-v="center" class="mb-2">
									<b-col class=" text-center">	
										<hr>
										<h4>
											Não tem uma conta? 
											<span @click="toggleCadastro()" 
											style="cursor:pointer;text-decoration:underline">
												Cadastre-se!
											</span>
										</h4>
									</b-col>
								</b-row>
							</div>
						</b-card-body>
					</b-card>
				</b-col>	
			</b-row>
			<b-row v-else align-h="center">
				<b-col cols="12" md="8">  
					<h2>Recupere a senha usando CPF ou e-mail cadastrado.</h2>
					<b-card no-body class="mb-1" name="pagamento">
						<b-card-header header-tag="header" class="p-2" role="tab">
							Lembrar senha
						</b-card-header>
						<b-card-body>
							<b-alert 
								:show="alerta_lembrar" 
								:variant="tipoAlerta_lembrar" 
								dismissible >
								{{mensagemAlerta_lembrar}}
							</b-alert>
						</b-card-body>	
						<b-card-body>
							<b-row align-v="center" class="mb-2">
								<b-col sm="2" class="label-form">
									<label for="lembrarCPF">CPF</label>
								</b-col>
								<b-col sm="10">
									<b-form-input
										type="tel" maxlength="100"
										class="form-control form-control form-control-sm" 
										id="lembrarCPF"
										placeholder="Informe o seu CPF"
										v-model="lembrar.cpf"
										v-mask="'###.###.###-##'" 
										@blur="cpfValido('lembrarCPF',lembrar.cpf)"> 
									</b-form-input>
									<b-form-invalid-feedback id="input-live-feedback">
										{{erroCPF}}
									</b-form-invalid-feedback>    
								</b-col>
							</b-row>
							<b-row align-v="center" class="mb-2">
								<b-col sm="2" class="label-form">
									<label for="lembrarE-mail">E-mail</label>
								</b-col>
								<b-col sm="10">
									<b-form-input  
										type="text" maxlength="100"
										class="form-control form-control form-control-sm" 
										id="lembrarE-mail"
										placeholder="Informe seu e-mail"
										v-model="lembrar.email"> 
									</b-form-input>
									<b-form-invalid-feedback id="input-live-feedback">
										Insira seu e-mail
									</b-form-invalid-feedback>    
								</b-col>
							</b-row>
							<b-row align-v="center" class="mb-2">
								<b-col sm="2" class="label-form">
								</b-col>
								<b-col sm="10">
									<b-button @click="validaEsqueciSenha()" size="sm" variant="outline-info">
										Lembrar a Senha
									</b-button>
								</b-col>
							</b-row>
														
							<b-row align-v="center" class="mb-2">
								<b-col class=" text-center">	
									<hr>
									<h4>Se lembrou da sua senha? 
                                	<span @click="toggleLogin()"  style="cursor:pointer;text-decoration:underline">
                                	Faça login!</span>
									</h4>
								</b-col>
							</b-row>
						</b-card-body>
					</b-card>
				</b-col>
			</b-row>
		</b-container>
	</section>
</template>
<style scoped>
	@media ( min-width : 669px) {
		.label-form {
			text-align: right !important;
		}
	}
	@media ( max-width : 668px) {
	   .label-form {
	        text-align: left !important;
		} 
		.separador {
		width: 100% !important;
		margin: 0 auto !important;
		border-bottom: 1px solid #1e6a82 !important;
		/* padding-bottom: 20px; */
		margin-bottom: 20px !important;
		}
	}
</style>

<script type="text/javascript">
	"use strict";
	module.exports = {
        data: function() {
            return {
                login: {
                    senha: '',
                    cpf: ''
                },
                lembrar: {
                    email: '',
                    cpf: ''
                },
                checked : false,        
                alertaLogin : false,
                alerta_lembrar : false,
                tipoAlertaLogin : 'success',
                tipoAlerta_lembrar : 'warning',
                mensagemAlertaLogin :'Alert! Mensagem para alertas.',
                mensagemAlerta_lembrar :'Alert! Mensagem para alertas.',
                fecharAlertaLogin : true,
                fecharAlerta_lembrar: true,
                mostraLogin : true,
                erroCPF : "Insira seu CPF",
            }
        },
		methods: {
			cpfValido(prop,cpf){
				const vm = this;
				let campoalvo = document.getElementById(prop);
				if(isNullUndef(campoalvo))
                	return;
				if(checarCPF(campoalvo.value)){
					campoalvo.classList.remove("is-invalid");
	                vm.erroCPF = "Insira seu CPF";
	                return true;
				} else {
	                campoalvo.classList.add("is-invalid");
	                vm.erroCPF = "CPF inválido";
	                return false;
				}
			},
            toggleLogin(){
                const vm = this;
                window.scroll(0,0);
                vm.mostraLogin = !vm.mostraLogin;
            },
            toggleCadastro(){
                const vm = this;
                window.scroll(0,0);
                vm.$emit("toggle-cadastro", true);
            },
            validaLogin(dados) {
                const vm = this;
                let semErro = true;
                if(!vm.cpfValido('cpf',vm.login.cpf)){
                	semErro = false;
                }
                let campoalvo = document.getElementById("senha");
                if(isNullUndef(campoalvo))
                	return;
                if(vm.login.senha === ""){
	                campoalvo.classList.add("is-invalid");
	                semErro = false;
                } else {
	                campoalvo.classList.remove("is-invalid");
                }
                if(semErro)
                	 vm.loginSistema(vm.login);
            },
            validaEsqueciSenha(dados) {
            	const vm = this;
                let semErro = true;
                let campoalvo = document.getElementById("lembrarE-mail");
                if(isNullUndef(campoalvo))
                	return;
                if(!vm.cpfValido('lembrarCPF',vm.lembrar.cpf)){
                	semErro = false;
                }
                if(vm.lembrar.email === ""){
					campoalvo.classList.add("is-invalid");
	            	semErro = false;
                } else {
                	campoalvo.classList.remove("is-invalid");
                }
                //if(semErro)
                	 //vm.loginSistema(vm.login);
            },
            //rest
            loginSistema(login){
                const vm = this;
                let usuario = {
                    str_senha : login.senha,
                    str_cpf : login.cpf,
                };
				iniciaLoader("loaderLogin","loginCampos");
				vm.alertaLogin = false;
                return axios.post(getCaminhoTestes() + "rs/auth/login", usuario)
                    .then(function (response) {
                    	let ultimaPagina = loadLocalStorage("ultimaPagina");
                    	if(ultimaPagina !== null){
                    		if(ultimaPagina === "associados.html")
								changeLocation(getCaminhoTestes() + "pages/compra.html")
							else
								changeLocation(getCaminhoTestes() + "index.html")
                    	} else {
                    		changeLocation(getCaminhoTestes() + "index.html");	
                    	}
                        window.scroll(0,0);
                        vm.alertaLoginDismissed = true;
                        vm.alerta_login = false;
                        return true;
                    })
                    .catch(function (error) {
                        vm.alertaLogin = true;
                        vm.tipoAlertaLogin = "danger";
                        vm.mensagemAlertaLogin = error.response.data;
                        window.scroll(0,0);
                        encerraLoader("loaderLogin","loginCampos");
                        return false;
                    });
            },
	        scrollTop(scrollDuration) {
	            let scrollStep = -window.scrollY / (scrollDuration / 15),
	                scrollInterval = setInterval(function(){
	                if ( window.scrollY != 0 ) {
	                    window.scrollBy( 0, scrollStep );
	                }
	                else clearInterval(scrollInterval); 
	            },15);
	        }
        },
        created: function() {
            const vm = this;
            console.log("LOGIN");
        }
	};
</script>
