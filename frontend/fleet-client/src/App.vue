<script setup lang="ts">
import { RouterView } from 'vue-router';
import { useFleetSocket } from '@/composables/useFleetSocket';

useFleetSocket();
</script>

<template>
  <div class="h-screen w-screen flex flex-col bg-black overflow-hidden">
    <main class="flex-1 overflow-hidden">
      <RouterView />
    </main>
  </div>
</template>