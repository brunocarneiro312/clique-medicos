<template>
    <b-container>
        <b-row>
            <b-col>

                <!-- Info message -->
                <div>
                    <b-alert variant="primary" show v-if="showInfoMessage">
                        <b-row class="mt-0">
                            <b-col cols="8" class="mt-0">
                                <h4 class="font-weight-bold">Número do Vale Compra</h4>
                                <span>Insira o número do Vale Compra para pagar a consulta.</span>
                            </b-col>
                            <b-col class="mt-0 text-right">
                                <i class="far fa-keyboard fa-fw"></i>
                            </b-col>
                        </b-row>
                    </b-alert>
                </div>

                <!-- Error message -->
                <div>
                    <b-alert variant="danger" show v-if="hasError && !loading">
                        <b-row class="mt-0">
                            <b-col cols="8" class="mt-0">
                                <h4 class="font-weight-bold">Erro</h4>
                                <span>{{ message }}</span>
                            </b-col>
                            <b-col class="mt-0 text-right">
                                <i class="far fa-exclamation-triangle fa-fw"></i>
                            </b-col>
                        </b-row>
                    </b-alert>
                </div>

                <!-- Success message -->
                <div>
                    <b-alert variant="success" show v-if="compraAtualizada">
                        <b-row class="mt-0">
                            <b-col cols="8" class="mt-0">
                                <h4 class="font-weight-bold">Sucesso</h4>
                                <span>{{ message }}</span>
                            </b-col>
                            <b-col class="mt-0 text-right">
                                <i class="far fa-check fa-fw"></i>
                            </b-col>
                        </b-row>
                    </b-alert>
                </div>

                <!-- Loading -->
                <div>
                    <b-alert variant="secondary" show v-if="loading">
                        <b-row class="mt-0">
                            <b-col cols="8" class="mt-0">
                                <h4>Aguarde...</h4>
                                <span>Aguarde enquanto processamento a compra.</span>
                            </b-col>
                            <b-col class="mt-0 text-right">
                                <i class="far fa-hourglass-half fa-fw rotating"></i>
                            </b-col>
                        </b-row>
                    </b-alert>
                </div>
            </b-col>
        </b-row>
        <b-row>
            <b-col>
                <b-form-group>
                    <b-form-input
                            placeholder="0000000000000000000"
                            size="lg"
                            class="text-center watermark"
                            v-model="nroVoucher"
                            @keypress="permitOnlyNumbers()"
                            maxlength="19"
                            trim>
                    </b-form-input>
                </b-form-group>
                <div class="text-muted">
                    <span class="description" v-if="!nroVoucher">
                        <i class="far fa-exclamation-triangle fa-fw"></i>
                        Insira o número do <i><b>Vale-Compra</b></i>
                    </span>
                </div>
                <br>
            </b-col>
        </b-row>
        <b-row>
            <b-col class="text-center">
                <b-btn
                        variant="success"
                        size="lg"
                        @click="comprarComVoucher"
                        :disabled="!nroVoucher || loading"
                        block>

                    Comprar com Vale-Compra&nbsp;
                    <i class="far fa-ticket-alt fa-fw rotate-45deg"></i>
                </b-btn>
            </b-col>
        </b-row>
    </b-container>
</template>
<style scoped>
    input {
        border-radius: 0;
        box-shadow: inset 0 2px 3px #ddd;
    }
    input[type="text"]:focus,
    input[type="text"]:active {
        outline:0px !important;
        box-shadow: inset 0 2px 3px #ddd;
    }
    .btn {
        border-radius: 0;
    }
    .alert {
        border-radius: 0;
        box-shadow: 0 1px 5px #ddd;
    }
    .rotating {
        transition: all .5s;
        -webkit-animation: rotating 1s infinite;
        -o-animation: rotating 1s infinite;
        animation: rotating 1s infinite;
    }
    .rotate-45deg {
        transform: rotate(45deg);
    }
    .watermark::placeholder {
        color: #dadada;
    }
    .description {
        font-size: 0.8em;
    }
    @-webkit-keyframes rotating {

    }
    @keyframes rotating{
        from {
            transform: rotate(0deg);
        }
        to {
            transform: rotate(180deg);
        }
    }
</style>
<script type="text/javascript">
	module.exports = {
        props: [
            'compra',
        ],
        data: function() {
            return {
                nroVoucher: undefined,
                compraAtualizada: undefined,
                message: undefined,
                hasError: undefined,
                loading: undefined,
            }
		},
        computed: {
            showInfoMessage() {
                return !this.compraAtualizada
                    && !this.hasError
                    && !this.loading;
            }
        },
		methods: {

            /**
             * Solicita compra com o voucher para a API
             */
            comprarComVoucher() {

                const vm = this;

                // Endpoint para pagamento com vale compra
                const REST_ENDPOINT = getCaminhoTestes() + "../rs/pagamento/private/voucher";

                // Atribuindo o numero do voucher à comrpra
                this.compra.long_voucher = this.nroVoucher;

                this.loading = true;
                this.message = "Aguarde enquanto o pagamento é processado.";

                // Disparando requisição para o endpoint
                axios
                    .post(REST_ENDPOINT, this.compra)
                    .then(function(response) {
                        vm.compraAtualizada = response.data;
                        vm.message = "Compra com vale finalizada com sucesso!";
                    })
                    .catch(function(err) {
                        vm.message = err.response.data;
                        vm.hasError = true;
                    })
                    .finally(function() {
                        vm.loading = false;
                    });
            },

            /**
             * Permite apenas a entrada de valores numéricos
             * @param $event
             */
            permitOnlyNumbers: function(evt) {

                console.log('permitOnlyNumbers');

                var charCode = (evt.which) ? evt.which : evt.keyCode;
                if ((charCode > 31 && (charCode < 48 || charCode > 57)) && charCode !== 46) {
                    evt.preventDefault();
                } else {
                    return true;
                }
            }

        },
        beforeCreate: function() {
            
        },
    	created: function() {

		}
	}
</script>