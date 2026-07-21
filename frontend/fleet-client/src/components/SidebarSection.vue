<template>
    <div class="flex flex-col flex-1 min-h-0 border border-green-500 bg-black p-3">
        <h2 class="mb-2 border-b border-green-500 pb-2 uppercase tracking-widest text-green-500">
            {{ title }} [{{ count }}]
        </h2>
        <ul class="space-y-2 overflow-y-auto custom-scrollbar custom-green-scrollbar flex-1 h-full" @scroll="handleScroll">
            <slot :items="visibleItems" />
        </ul>
    </div>
</template>

<script setup lang="ts">
import { toRef } from 'vue';
import { usePaginationScroll } from '@/composables/usePaginationScroll';

const props = defineProps<{
    title: string,
    count: number,
    items: any[]
}>();

const itemsRef = toRef(props, 'items');
const { visibleItems, handleScroll } = usePaginationScroll(itemsRef);
</script>