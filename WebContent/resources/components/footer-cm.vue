<template>
    <div class="container">
        <div class="row">
            <div class="col-12 col-lg-4 mb-2">
            <div class="mapaSite">
                <b-container class="">
                    <h3>Mapa do Site</h3>
                    <ul style="padding-top: 20px;margin-left: -30px;">
                        <li>Quem Somos</li>
                            <ul>
                                <li>Missão</li>
                                <li>Visão</li>
                            </ul>
                        <li>Política de Privacidade</li>
                        <li>Termo de Uso</li>
                    </ul>
                </b-container>
            </div>	            	
            </div>
            <div class="col-12 col-lg-4 mb-2">
                <h3>Condições de Pagamento</h3>
                <b-container class="">
                    <h4>Cartões de Créditos</h4>
                        <b-row>
                            <b-col cols="3" class="mb-1"><i class="pagamentos pagamentos-visa"></i></b-col>
                            <b-col cols="3" class="mb-1"><i class="pagamentos pagamentos-mastercard"></i></b-col>
                            <b-col cols="3" class="mb-1"><i class="pagamentos pagamentos-elo"></i></b-col>
                            <b-col cols="3" class="mb-1"><i class="pagamentos pagamentos-amex"></i></b-col>
                            <b-col cols="3" class="mb-1"><i class="pagamentos pagamentos-hiper"></i></b-col>
                        </b-row>
                        <h4>Boleto Bancário</h4>
                        <b-row align-v="start">
                            <b-col><i class="pagamentos pagamentos-barcode"></i></b-col>
                        </b-row>
                </b-container>
            </div>
            <div class="col-12 col-lg-4 mb-2"><h3>Fale Conosco</h3>
                <b-container class="">
                    <h4>Redes Sociais</h4>
                        <b-row>
                        <b-col cols="3" class="mb-1"><i class="redesSociais redesSociais-facebook"></i></b-col>
                        <b-col cols="3" class="mb-1"><i class="redesSociais redesSociais-instagram"></i></b-col>
                        <b-col cols="3" class="mb-1"><i class="redesSociais redesSociais-youtube"></i></b-col>
                        </b-row>
                    <h4>E-mail</h4>
                        <b-row>
                        </b-row>
                </b-container>           
            </div>            
        </div>
    </div>
</template>
<script>
    "use strict";
    module.exports = {
        data: function() {
            return {
                logado: false
            }
        }
    }
</script>

<style>
@import url('../css/iconeSistemas.css');
@import url('../css/pagamento.css');
@import url('../css/redesSociais.css');
#rodape {
    background: url(../images/backgroundFooter.png) bottom left;
    background-color: #BBD2D9 !important;
    background-repeat: no-repeat;
    background-size: auto;
    bottom: 0px;
    margin: 30px 0 0;/* */
}

#rodape h4 {
	font-size: 1.0rem;
	color: #008288;
	text-align: left;
	margin: 15px 0 5px;
}
#rodape h3 {
    font-size: 1.5rem;
	font-weight: 600;
	color: #008288;
	text-align: left;
	margin: 20px 0 0;
}
.mapaSite {
    text-align: left;
}
.mapaSite ul>li {
    list-style-type: none;
    margin: 5px 0;
}

@media ( max-width : 576px) {
	.mapaSite ul>li {
		list-style-type: none;
		margin: 10px 0;
	}
}
</style>
