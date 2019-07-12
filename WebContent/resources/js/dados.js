var dadosVue = new Vue({
    el : '#dados',
    components: {

    },
    data: {

        // Objeto para agrupar recursos locais
        local: {

            // Armazena informações de deleção
            deleteInfo: {
                deleteModalIsOpen:     false,
                isContaDeletada:       false,
                errorOnDelete:         false,
                checkSenha:            undefined,
                senhaMatch:            false,
                mensagemContaDeletada: undefined
            }
        },
        logado: true,
        usuario: {
            id_usuario           : undefined,
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
        },
        dataNascimentoString : "",
        seguranca: {
            senha  	  : undefined,
            validacao : undefined
        },
        erroCPF : undefined,
        erroCEP : undefined,
        erroSenha : undefined,
        erroSenha2 : undefined,
        erroEmail: undefined,
        erroTelefone: undefined,
        alertaDados: false,
        mensagemAlertaDados : undefined,
        tipoAlertaDados : 'info',
        fecharAlertaDados : true,
        cadastroCompleto : false,
        emailValido: true,
        cepValido: true,
        telefoneValido: true,
        isEmailValidacaoEnviado: false
    },
    methods: {

        /**
         *
         * Solicita a alteração do usuário
         *
         */
        alterar() {
            const vm = this;

            // Realiza a validação dos campos
            if(vm.isFormValido()) {
                // Preenche a senha para alteração, caso ela tenha sido informada
                if (vm.seguranca.senha) {
                    vm.usuario.str_senha = vm.seguranca.senha;
                }
                let obj = {
                    id_usuario            : vm.usuario.id_usuario,
                    str_nome              : vm.usuario.str_nome,
                    str_senha             : vm.usuario.str_senha,
                    str_cpf               : vm.usuario.str_cpf,
                    str_email             : vm.usuario.str_email,
                    str_telefone          : vm.usuario.str_telefone,
                    str_endereco          : vm.usuario.str_endereco,
                    str_cep               : vm.usuario.str_cep,
                    str_logradouro        : vm.usuario.str_logradouro,
                    str_complemento       : vm.usuario.str_complemento,
                    str_bairro            : vm.usuario.str_bairro,
                    str_localidade        : vm.usuario.str_localidade,
                    str_uf                : vm.usuario.str_uf,
                    str_unidade           : vm.usuario.str_unidade,
                    dataNascimentoString  : vm.usuario.dataNascimentoString,
                    str_token_verificacao : vm.usuario.str_token_verificacao,
                    data_nascimento       : createDateFromSQLString(vm.dataNascimentoString),
                };

                return axios.put("../rs/cadastro/private/alterar", obj)
                    .then(function(response) {
                       vm.usuario = response.data;
                       vm.buscar();
                       vm._displayMensagem('Usuário atualizado com sucesso!', 'info');
                       vm.seguranca.senha = undefined;
                       vm.seguranca.validacao = undefined;
                        vm.scrollTop(250);
                       return true;
                    })
                    .catch(function(err) {
                        console.log(err);
                        vm._displayMensagem('Erro durante a alteração do usuário.', 'danger');
                        return false;
                    });
                    vm.scrollTop(150);
            } else {
                return false;
            }
        },

        /**
         *
         * Busca um usuário
         *
         */
        buscar() {
            const vm = this;
            return axios.get('../rs/cadastro/private/buscar')
                .then(function(response) {
                    vm.usuario = response.data;
                    vm.usuario.data_nascimento = new Date(vm.usuario.data_nascimento);
                    vm.dataNascimentoString = vm.usuario.data_nascimento.toInputDate();
                    return vm.usuario;
                })
                .catch(function(err) {
                    console.log(err);
                    vm._displayMensagem("Erro durante a obtenção do usuário logado", "danger");
                    return null;
                });
        },

        /**
         *
         * Verifica se todos os dados do usuario foram informados
         *
         * @return
         *      true: todos os campos foram informados
         *      false: algum dos campos não foi informado
         */
        isCamposInformados() {

            const vm = this;

            let isIdUsuario  = vm.usuario.id_usuario;
            let isNome       = vm.usuario.str_nome;
            let isCpf        = vm.usuario.str_cpf;
            let isEmail      = vm.usuario.str_email;
            let isTelefone   = vm.usuario.str_telefone;
            let isCep        = vm.usuario.str_cep;
            let isLogradouro = vm.usuario.str_logradouro;
            let isBairro     = vm.usuario.str_bairro;
            let isLocalidade = vm.usuario.str_localidade;
            let isUf         = vm.usuario.str_uf;

            if (isIdUsuario
                && isNome
                && isCpf
                && isEmail
                && isTelefone
                && isCep
                && isLogradouro
                && isBairro
                && isLocalidade
                && isUf) {

                return true;
            }

            return false;
        },

        /**
         *
         * Verifica se os campos informados estão válidos
         *
         * @return
         *      true: Todos os campos estão válidos
         *      false: Algum dos campos não está válido
         */
        isCamposValidos() {
            const vm = this;
            return vm.seguranca.senha == vm.seguranca.validacao
                && vm.cepValido
                && vm.emailValido
                && vm.telefoneValido;
        },

        /**
         *
         * Verifica se formulário está válido
         *
         * @return
         *      true: Formulário válido
         *      false: Formulário inválido
         */
        isFormValido() {
            const vm = this;
            return vm.isCamposInformados() && vm.isCamposValidos();
        },

        /**
         *
         * Apresenta mensagem global
         *
         * @param mensagem
         *      Texto da mensagem
         * @param estilo
         *      Estilo da mensagem
         * @private
         */
        _displayMensagem(mensagem, estilo) {
            const vm = this;
            vm.alertaDados         = true;
            vm.mensagemAlertaDados = mensagem;
            vm.tipoAlertaDados     = estilo;
        },

        /**
         *
         * Faz integração com https://viacep.com.br/
         *
         */
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
			},
        /**
         *
         * Realiza validação de campo
         *
         * @param event
         */
        validaCampo(event) {
            if (!event.target.value) {
                event.target.classList.add('is-invalid');
            }
            else {
                event.target.classList.remove('is-invalid');
            }
        },

        /**
         * Realiza a validação do Email
         *
         * @param event
         */
        validaEmail(event) {
            const vm = this;
            let hasError = false;
            let email    = event.target.value;
            let reg      = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

            if (!event.target.value) {
                vm.erroEmail = 'Insira um e-mail';
                hasError = true;
            }

            if (event.target.value && !reg.test(email)) {
                vm.erroEmail = 'Insira um e-mail válido (ex: meu@email.com)';
                hasError = true;
            }

            if (hasError) {
                event.target.classList.add('is-invalid');
                vm.emailValido = false;
            }
            else {
                event.target.classList.remove('is-invalid');
                vm.emailValido = true;
            }
        },

        /**
         *
         * Realiza a validação do CEP
         *
         * @param event
         */
        validaCEP(event) {
            const vm = this;
            let hasError = false;
            let cep      = event.target.value;

            if (!cep) {
                vm.erroCEP = "Informe o CEP";
                hasError = true;
            }

            if (cep && isNaN(cep)) {
                vm.erroCEP = "O CEP deve ser numérico";
                hasError = true;
            }

            if (hasError) {
                event.target.classList.add('is-invalid');
                vm.cepValido = false;
            }
            else {
                event.target.classList.remove('is-invalid');
                vm.cepValido = true;
            }
        },

        /**
         *
         * Realiza a validação da Senha
         *
         * @param event
         */
        validaSenha(event) {
            const vm = this;
            let hasError = false;
            let senha    = event.target.value;

            if (vm.seguranca.senha) {

                if (vm.seguranca.senha && vm.seguranca.validacao) {
                    if (vm.seguranca.senha != vm.seguranca.validacao) {
                        vm.erroSenha = 'As senhas não coincidem';
                        hasError = true;
                    }
                }

                if (hasError) {
                    event.target.classList.add('is-invalid');
                }
                else {
                    event.target.classList.remove('is-invalid');
                    vm.obterEnderecoPeloCep();
                }
            }

        },

        /**
         *
         * Realiza a validação do Telefone
         *
         * @param event
         */
        validaTelefone(event) {
            const vm = this;
            let hasError = false;
            let telefone = event.target.value;

            if (!telefone.length) {
                vm.erroTelefone = 'Insira o telefone';
                hasError = true;
            }

            if (hasError) {
                event.target.classList.add('is-invalid');
                vm.telefoneValido = false;
            }
            else {
                event.target.classList.remove('is-invalid');
                vm.telefoneValido = true;
            }
        },

        /**
         * Solicita o envio de email de validação
         */
        enviarEmailValidacao() {
            const vm = this;
            let obj = {
                    id_usuario            : vm.usuario.id_usuario,
                    str_nome              : vm.usuario.str_nome,
                    str_senha             : vm.usuario.str_senha,
                    str_cpf               : vm.usuario.str_cpf,
                    str_email             : vm.usuario.str_email,
                    str_telefone          : vm.usuario.str_telefone,
                    str_endereco          : vm.usuario.str_endereco,
                    str_cep               : vm.usuario.str_cep,
                    str_token_verificacao : vm.usuario.str_token_verificacao,
                    data_nascimento       : createDateFromSQLString(vm.dataNascimentoString),
            };
            return axios.post("../rs/cadastro/enviar-email", obj)
                .then(function(response) {
                   vm.isEmailValidacaoEnviado = true;
                   return true;
                })
                .catch(function(err) {
                    console.log(err);
                    vm.isEmailValidacaoEnviado = false;
                    return false;
                });
        },

        /**
         * Caso a validação esteja preenchida e a senha não,
         * limpa validação
         */
        checkLimparSenha() {
            const vm = this;
            if (!vm.seguranca.senha.length) {
                vm.seguranca.validacao = '';
            }
        },

        /**
         * ------------
         * Deleta conta
         * ------------
         * Deleta usuário locado na sessão e realiza o logout.
         */
        deletarUsuarioLogado() {

            const vm = this;

            axios
                .post('../rs/cadastro/private/deletar', vm.local.deleteInfo.checkSenha)
                .then(function(response) {

                    // Caso consiga deletar, armazena mensagem de sucesso no localStorage
                    localStorage.isContaDeletada = true;
                    localStorage.mensagemContaDeletada = response.data;

                    // Faz logout
                    axios.get(getCaminhoTestes(vm.caminhoRelativo) + "../rs/auth/private/logout")
                        .then(function (response) {
                            changeLocation("../index.html");
                        })
                        .catch(function (error) {

                        });

                })
                .catch(function(err) {

                    // Caso aconteça algum erro, mostra mensagem ao usuário
                    vm.local.deleteInfo.errorOnDelete = true;
                    vm.local.deleteInfo.isContaDeletada = false;
                    vm.local.deleteInfo.mensagemContaDeletada = err.response.data;
                    vm.local.deleteInfo.deleteModalIsOpen = false;

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
        vm.buscar();
        vm.seguranca.senha = undefined;
        vm.seguranca.validacao = undefined;
    }
});

var menuSuperior = new Vue({
    el : '#menuSuperior',
    components: {
        'header-cm': httpVueLoader('../resources/components/header-cm.vue')
    },
    data: {

    }
});
var rodape = new Vue({
    el : '#rodape',
    components: {
        'footer-cm': httpVueLoader('../resources/components/footer-cm.vue')
    },
    data: {
    }
});


