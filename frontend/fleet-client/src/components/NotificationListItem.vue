<template>
    <li class="p-2 border border-green-900/50 bg-black/40 text-xs font-mono text-green-400">
        <div class="text-[9px] opacity-70 mb-1">
            {{ new Date(notification.timestamp).toLocaleTimeString() }}
        </div>
        <div class="text-green-300">{{ notification.message }}</div>
    </li>
</template>

<script setup lang="ts">
import type { NotificationData } from '@/stores/notificationStore';
defineProps<{ notification: NotificationData }>();
</script>