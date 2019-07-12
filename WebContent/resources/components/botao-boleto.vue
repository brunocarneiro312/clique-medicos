<template>
    <div>

        <!-- Informações adicionais sobre o pagamento com boleto -->
        <div class="boleto-texto text-center">
            <b-alert variant="primary" show>
                <h3>Atenção!</h3>
                <span>A solicitação da consulta somente será confirmado após a compensação do boleto.</span>

            </b-alert>
        </div>

        <!-- Contém o botão para gerar o boleto -->
        <div class="form-group-text-center botao-gerar-container">
            <b-row v-if="!boleto.url">
                <b-col cols="6" offset="3">
                    <button class="btn btn-success btn-lg btn-block" @click="gerarBoleto()">
                        Gerar Boleto <i class="far fa-barcode"></i>
                    </button>
                </b-col>
            </b-row>
        </div>

        <div v-if="iniciaGerarBoleto">

            <!-- Caso o boleto tenha sido gerado com sucesso, então apresenta a div -->
            <div class="text-center" v-if="boleto.url">
                <h3>Boleto gerado com sucesso <i class="far fa-check fa-fw"></i></h3>
                <h5>Verique o envio do boleto em seu e-mail!</h5>
            </div>

            <!-- Caso contrário, apresenta o texto de loading -->
            <div v-else class="text-center">

                <h3>
                    O boleto está sendo gerado... 
                </h3>
            </div>

            <div class="form-group text-center" v-if="boleto.codigoDeBarras">
                <label for="input-codigo-barras">Número do boleto</label>

                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text" id="basic-addon1">
                            <button style="padding: 0; border: 0; color: rgba(0,0,0, .7); background: transparent" @click="copiarNumeroDoBoleto">
                                <i class="far fa-copy fa-fw"></i>
                            </button>
                        </span>
                    </div>
                    <input id="input-codigo-barras"
                           type="text"
                           class="form-control input-lg"
                           :value="boleto.codigoDeBarras"
                           @focus="selectText"
                           style="background: white"
                           readonly>
                </div>

                <div :hidden="!isCopied" class="div-alert-copy">
                    copiado com sucesso!
                </div>
            </div>
        </div>

        <div class="form-group">
            <b-row v-if="boleto.codigoDeBarras">
                <b-col cols="6" offset="3" class="text-center">
                    <a :href="boleto.url || '#'" target="_blank" class="btn btn-primary btn-sm btn-block">
                        <i class="far fa-file-import fa-fw"></i> Visualizar Boleto
                    </a>
                </b-col>
            </b-row>
        </div>
    </div>
</template>
<style scoped>
    input {
        border-radius: 0;
        text-align: center;
        font-size: 1.1em;
        outline: none;
        box-shadow: inset 0 -1px 0 #ddd;
    }
    input:disabled {
        background: white;
    }
    input::selection {
        background: #d2e0f7;
    }
    label {
        font-size: 1.3em;
    }
    .div-alert-copy {
        font-size: 0.8em;
        font-weight: bold;
        color: #3b6e12;
        padding-top:10px;
    }
    .boleto-texto {
        /* padding: 15px 15px;
        background: #f5f5f5;
        text-align: center;
        font-size: 1.2em; */
    }
    .botao-gerar-container {
        margin: 15px 0;
        padding: 15px 0;
    }
    .botao-gerar-container btn {
        font-weight: bold;
    }
</style>
<script type="text/javascript">
	module.exports = {
        props: {
            'compra': {

            }
        },
        data: function() {
            return {
                boleto: {
                    url: undefined,
                    codigoDeBarras: undefined
                },
                isCopied: false,
                iniciaGerarBoleto: false
            }
		},
		methods: {
			copyToClipboard(){
				const vm = this;
				navigator.clipboard.writeText(vm.numBoleto)
                .then(() => {
                    console.log('Text copied to clipboard');
                })
                .catch(err => {
                    // This can happen if the user denies clipboard permissions:
                    console.error('Could not copy text: ', err);
                });
		    },

            /**
             * Realiza a geração do boleto
             */
            gerarBoleto() {

                const vm = this;

                vm.iniciaGerarBoleto = true;

                iniciaLoader("loaderCompra","dadoPessoaisCompra", null);
                
                return axios.post(getCaminhoTestes() + "../rs/pagamento/private/boleto", vm.compra)
                    .then(function (response) {

                        // Seta dados do boleto
                        vm.boleto.url            = response.data.boleto.str_url;
                        vm.boleto.codigoDeBarras = response.data.boleto.str_codigo_barras;

                        // Formatando número do boleto
                        vm.boleto.codigoDeBarras = vm.boleto.codigoDeBarras.replace(/\./g, '');
                        vm.boleto.codigoDeBarras = vm.boleto.codigoDeBarras.replace(/\s/g, '');
                        vm.boleto.codigoDeBarras = vm.boleto.codigoDeBarras.trim();

                        encerraLoader("loaderCompra","dadoPessoaisCompra", null);

                        return true;
                    })
                    .catch(function (error) {
                        encerraLoader("loaderCompra","dadoPessoaisCompra", null);
                        return false;
                    });
            },

            /**
             * Seleciona todo o texto do componente informado como parâmetro
             */
            selectText($event) {
                $event.target.select()
            },

            /**
             * Copia texto
             */
            copiarNumeroDoBoleto() {

                var copyText = document.getElementById("input-codigo-barras");

                // Sanitizando o texto
                copyText.value = copyText.value.replace(/\./g, '');
                copyText.value = copyText.value.replace(/\s/g, '');
                copyText.value = copyText.value.trim();

                copyText.select();
                document.execCommand("copy");

                this.isCopied = true;
            },
        },
        beforeCreate: function() {
            this.$emit('get-boleto', undefined);
        },
		/* ON LOAD */
    	created: function() {
    	   this.iniciaGerarBoleto = false;
		}
	}
</script>