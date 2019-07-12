<template>

<div> 
	<b-container class="">
		<b-row>
			<b-col cols="12">
				<b-navbar toggleable="sm" type="light" variant="info"  class="fixed-top bg-cliqueMedicos shadow-sm">
					<!-- icone do menu -->
					<b-navbar-toggle target="nav-collapse" id="menuClique" class=" mt-3" ></b-navbar-toggle>

					<!-- Logo Clique Médicos -->
					<b-navbar-brand :href="caminhoRelativo + 'index.html'" class="ml-3">
						<img :src="caminhoRelativo + 'resources/images/logo_Cliquemedicos_verde.png'" id="logo_Cliquemedicos_nav" alt="logo Clique Médicos" />
					</b-navbar-brand>

					<!-- menu login não logado -->
					<div class="ml-auto d-block d-sm-none" v-if="!logado">
						<b-nav-item-dropdown right v-if="lerUsuario">
							<template slot="button-content">
								<img :src="caminhoRelativo + 'resources/images/icones/icones-user.png'" class="d-block d-sm-none" id="userM"/>
							</template>
							<b-dropdown-item :href="caminhoRelativo + 'registro.html'">Entre</b-dropdown-item>
							<b-dropdown-item :href="caminhoRelativo + 'registro.html?cadastrando=true'">Cadastro</b-dropdown-item>
						</b-nav-item-dropdown>
					</div>

					<b-collapse id="nav-collapse" class="fundoMenu" is-nav>
						<!-- Menu logado mobile-->
						<template v-if="logado">
							<template v-if="lerUsuario">
							<b-navbar-nav class="ml-auto d-block d-sm-none login" id="itensUsuario">					
								<b-nav-item class="mt-2" href="#" id="nomeUsuario">
									<img :src="caminhoRelativo + 'resources/images/icones/icones-user-white.png'" style="margin-top: -10px;"/>
									<span> Bem vindo, {{usuario}}</span>
								</b-nav-item>
								<b-nav-item :href="caminhoRelativo + 'pages/dados.html'">Meus Dados</b-nav-item>
								<b-nav-item :href="caminhoRelativo + 'pages/agendamentos.html'">Meus Agendamentos</b-nav-item>
								<b-nav-item :href="caminhoRelativo + 'pages/historico.html'">Meus Historicos</b-nav-item>
								<b-nav-item @click="logout()">Sair</b-nav-item>
							</b-navbar-nav>
							<div class="separador"></div>
							</template>
						</template>

						<b-navbar-nav  id="itensEmpresa">
							<b-nav-item :href="caminhoRelativo + 'index.html'" left>Início</b-nav-item>
							<b-nav-item-dropdown left>
								<template slot="button-content">A Empresa</template>
								<b-dropdown-item :href="caminhoRelativo + ''">Quem Somos</b-dropdown-item>
								<b-dropdown-item :href="caminhoRelativo + ''">Missão</b-dropdown-item>
								<b-dropdown-item :href="caminhoRelativo + ''">Visão</b-dropdown-item>
								<b-dropdown-item :href="caminhoRelativo + ''">Política de Privacidade</b-dropdown-item>
								<b-dropdown-item :href="caminhoRelativo + ''">Termo de Uso</b-dropdown-item>
							</b-nav-item-dropdown>
						</b-navbar-nav>
						<template v-if="lerUsuario">	
							<b-navbar-nav class="ml-auto d-none d-sm-block">
								<b-nav-item-dropdown right v-if="!logado">									
										<template slot="button-content"><img :src="caminhoRelativo + 'resources/images/icones/icones-user.png'"  />
											<span class="ml-2">Entrar</span>
										</template>
										<b-dropdown-item :href="caminhoRelativo + 'registro.html'">Entre</b-dropdown-item>
										<b-dropdown-item :href="caminhoRelativo + 'registro.html?cadastrando=true'">Cadastre-se</b-dropdown-item>								
								</b-nav-item-dropdown>
								<b-nav-item-dropdown right v-else>
									<template  v-if="lerUsuario">
										<template slot="button-content"><img :src="caminhoRelativo+'resources/images/icones/icones-user.png'"/><span class="ml-2">Bem vindo, {{usuario}}</span></template>
										<b-dropdown-item :href="caminhoRelativo + 'pages/dados.html'">Meus Dados</b-dropdown-item>
										<b-dropdown-item :href="caminhoRelativo + 'pages/agendamentos.html'">Meus Agendamentos</b-dropdown-item>
										<b-dropdown-item :href="caminhoRelativo + 'pages/historico.html'">Meus Historicos</b-dropdown-item>
										<b-dropdown-item @click="logout()">Sair</b-dropdown-item>
									</template>
								</b-nav-item-dropdown>
							</b-navbar-nav>
						</template>
					</b-collapse>	
				</b-navbar>
			</b-col>
		</b-row>
</b-container>
</div>
</template>

<script>
	"use strict";
    module.exports = {
		props: ["caminhoRelativo","paginaPrivada"],
        data: function() {
            return {
				logado : false,
				usuario : "Nome",
				lerUsuario: false
            }
		},
		methods: {				
			userOff(campo, classe){
				const vm = this;
				vm.lerUsuario = false;			
			},
			userOn(campo, classe){
				const vm = this;
				vm.lerUsuario = true;			
			},
			logout(){
                const vm = this;
                return axios.get(getCaminhoTestes(vm.caminhoRelativo) + "rs/auth/private/logout")
                    .then(function (response) {
                    	vm.logado = false;
                    	if(!isBoolean(vm.paginaPrivada)){
                    		vm.paginaPrivada = vm.paginaPrivada === 'true' ? true:false;	
                    	}
                    	
                    	if(vm.paginaPrivada)
                    		changeLocation("../index.html");
                        return true;
                    })
                    .catch(function (error) {
                        return false;
                    });
            },
            buscarNome(){
                const vm = this;
				vm.userOff();
                return axios.get(getCaminhoTestes(vm.caminhoRelativo) + "rs/cadastro/private/buscarNome")
                    .then(function (response) {
                    	if(isString(response.data)){							
							vm.userOn();
                    		return false;
						}else{
                    		vm.usuario = response.data.str_nome;
                    		vm.logado = true;
							vm.userOn();
                        	return true;
						}
                    })
                    .catch(function (error) {
						vm.userOn();
                        return false;
					});
				vm.userOn();
            }
		},
		/* ON LOAD */
    	created: function() {
			const vm = this;
			Vue.nextTick(function () {
				vm.buscarNome();				
			});
		}
	}
	
</script>

<style scoped>

@import '../css/iconeSistemas.css';

body {
    margin-top: 70px;
}
li { 
	list-style: none;
}
.registro {
    color: rgb(0, 137, 143);
    font-weight: bold;
}

#logo_Cliquemedicos_nav {
	height: 55px;
	margin-top: -5px;
}
.navbar-toggler-icon {
    display: inline-block;
    width: 2em;
    height: 2em;
    vertical-align: middle;
    content: "";
    /* background: no-repeat center center;
    background-size: 100% 100%; */
}
.navbar-toggler {
    padding: 0;
    font-size: 1rem;
    line-height: 1;
    background-color: transparent;
    border: 1px solid transparent;
    border-radius: 0.25rem;
}
.navbar-light .navbar-toggler-icon {
    background-image: url('../images/icones/menu.svg');
}
/*Personalização do bootstrap*/
.bg-cliqueMedicos {
	background-color: #BBD2D9 !important;
}
.navbar {
	padding: 0.5rem;
}
#loginMobile{
	list-style: none;
}
.login{
	/* display: none; */
}
.esconde{
	display: none !important;
}
.mostra{
	display: block !important;
}

@media ( max-width : 576px) {
	.navbar-collapse {
		background:	#008288;
		/* background: url(../images/detalhe_caracteristicas.png)
					#008288 right top/25%
					no-repeat; */
		margin: 10px -16px -8px;
		padding: 0px 0px 50px;
	}
	.navbar-nav .nav-link {
		font-size: 1.7rem;
		text-align: left;
		padding-left: 20px;
		line-height: 2rem;
		font-weight: 800;
	}
	.navbar-light .navbar-nav .nav-link {
    color: #FFF !important;
    font-weight: bold;
	}
	#loginMobile .nav-link {
    padding: 0.60rem 0.0rem 0;
	}
	#loginMobile .dropdown-toggle::after {
    vertical-align: 0.5em;
	}
	a {
    color: #1c6a82;
	}

	
.nomeUsuario{
    word-break: break-word;
}

.itensUsuarios{
	margin-left:20px;
}
#itensEmpresa{
    margin-left: 20px;
	font-style: italic;
}
#itensEmpresa ul li {
    font-size: 1.6rem;
}
#itensEmpresa  .dropdown-menu {
    background: none;
	border:none;
	color: #fff;
}
.navbar-light .navbar-nav .show > .nav-link, .navbar-light .navbar-nav .active > .nav-link, .navbar-light .navbar-nav .nav-link.show, .navbar-light .navbar-nav .nav-link.active {
    color: #fff;
}
.fundoMenu .dropdown-item {
	    color: #FFF !important;;
    font-size: 1.5rem !important;;
}

.fundoMenu .dropdown-item:hover,.fundoMenu  .dropdown-item:focus {
    color: #1e6a82;
    text-decoration: none;
    background-color: #BBD2D9;
}

.fundoMenu {
    max-height: 500px !important;
	overflow-y: scroll !important;
	padding-bottom: 20px !important;
	border-bottom: 8px solid #1e6a82;
}
.separador{
	width: 80%;
	margin: 0 auto;
	border-bottom: 2px solid #1e6a82;
    padding-bottom: 20px;
    margin-bottom: 20px;
}

}

</style>
