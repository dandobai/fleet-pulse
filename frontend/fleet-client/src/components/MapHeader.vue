<template>
    <header
        class="h-14 border border-green-500 flex items-center justify-between px-3 bg-black shadow-[0_0_10px_rgba(0,255,65,0.1)]">
        <div class="flex items-center gap-4">
            <h1 class="tracking-widest text-green-400">fleet_pulse</h1>
            <SystemPulse ref="pulseComp" />
        </div>

        <button @click="$emit('toggle')" class="border border-green-500 px-4 py-1 hover:bg-green-900 transition-all">
            {{ isVisible ? '[HIDE_SIDEBAR]' : '[SHOW_SIDEBAR]' }}
        </button>
    </header>
</template>

<script setup lang="ts">
import SystemPulse from './SystemPulse.vue';

defineProps<{ isVisible: boolean }>();
defineEmits(['toggle']);

</script>