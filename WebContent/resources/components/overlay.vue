<template>
    <div class="hepta-overlay"></div>
</template>

<script>
    "use strict";
    module.exports = {
        data: function() {
            return {
                isOverlay: false
            }
        },
        methods: {

            applyOverlay() {
                this.isOverlay = true;
            },

            removeOverlay() {
                this.isOverlay = false;
            },

            toggleOverlay() {
                this.isOverlay = !this.isOverlay;
            }

        },
        created: function() {

        },
        mounted: {

        }
    }

</script>

<style scoped>

    .hepta-overlay {
        position: absolute;
        top: 0;
        left: 0;
        background: rgba(0, 0, 0, .80);
        z-index: 9999;
    }

</style>
