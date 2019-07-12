<template>
    <div class="multiselect">
        <div :class="['multiselect-caixa row',{'square-border-bottom' : aberto}]">
            <div class="multiselect-texto-caixa no-padding-lr col-11" 
                @click="toggleEspecial()"
                :style="[aberto ? {'cursor':'default'}:{'cursor':'pointer'}]">
                <span v-if="selecionados.length === 0">
                    {{placeholder.length === 0 ? 'Multi-Select' : placeholder}}
                </span>
                <span v-else>
                    {{selecionados.length}} valores selecionados.
                </span>
            </div>
            <div class="multiselect-seta no-padding-lr col-1" @click="toggle()">V</div>
        </div>
        <div v-if="aberto" 
            :class="['multiselect-lista row',{'square-border-top' : aberto}]"
            :style="{'max-height':maxHeight}">
            <div v-for="val in valoresAux" 
                @click="selecionarOuCancelar(val)"
                :class="['multiselect-item col-12', {'selecionado' : val.selecionado}]">
                {{val.valor}}
            </div>
        </div>
    </div>
</template>
<style>
    .multiselect{
        box-sizing: content-box;
        display: block;
        position: relative;
        width: 100%;
        min-height: 40px;
        text-align: left;
        color: #35495e;
    }
    .multiselect-caixa{
        border: 1px solid #bfbfbf;
        border-radius: 5px;
        padding: 0.2em 1em 0.2em 1em;
        font-size: 1.2em;
    }
    .square-border-bottom{
        border-bottom-left-radius: 0px!important;
        border-bottom-right-radius: 0px!important;
    }
    .square-border-top{
        border-top-left-radius: 0px!important;
        border-top-right-radius: 0px!important;
    }
    .multiselect-seta{
        text-align: right;
        cursor:pointer;
    }
    .multiselect-lista{
        border: 1px solid #bfbfbf;
        border-radius: 5px;
        overflow-y: scroll; 
    }
    .multiselect-item{
        padding: 0em 1em 0em 1em;
        font-size: 1.2em;
        cursor:pointer;
    }
    .selecionado{
        background-color:#4ea54e;
    }
    .no-padding-lr{
        padding-left: 0px!important;
        padding-right: 0px!important;
    }
</style>
<script type="text/javascript">
	module.exports = {
        props: [
            'valores',
            'selecionados',
            'placeholder',
            'maxHeight',
        ],
        data: function() {
            return {
                aberto : false,
                valoresAux : [],
            }
		},
		methods: {
            toggle(){
                const vm = this;
                vm.aberto = !vm.aberto;
            },
            toggleEspecial(){
                const vm = this;
                if(!vm.aberto)
                    vm.toggle();
            },
            selecionarOuCancelar(val){
                const vm = this;
                val.selecionado = true;
                let obj = {
                    id    : val.id,
                    valor : val.valor,
                };
                for(let i = 0; i < vm.selecionados.length; i++){
                    if(vm.selecionados[i].id === obj.id){
                        val.selecionado = false;
                        vm.selecionados.splice(i,1);
                        return;
                    }
                }
                vm.selecionados.push(obj);
            },
		},
		/* ON LOAD */
    	created: function() {
            console.log("MULTI-SELECT");
            const vm = this;
            vm.valoresAux = vm.valores.slice(0);
		}
	}
</script>