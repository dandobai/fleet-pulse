<script setup lang="ts">
import { RouterView } from 'vue-router';
import { useFleetSocket } from '@/composables/useFleetSocket';

useFleetSocket();
</script>

<template>
  <RouterView />
</template>