<template>
    <div :class="isVisible ? 'w-80' : 'w-0'" class="transition-all duration-300 overflow-hidden flex-shrink-0 h-full">
        <div class="w-80 h-full flex flex-col gap-2">

            <SidebarSection title="Vehicles" :count="vehicles.length" class="flex-[2] overflow-hidden flex flex-col">
                <div class="overflow-y-auto">
                    <VehicleListItem v-for="vehicle in vehicles" :key="vehicle.id" :vehicle="vehicle"
                        :selected-vehicles="selectedVehicles" :is-following="followedId === vehicle.id"
                        @sub-action="(p) => $emit('sub-action', p)" @toggle="(id) => $emit('toggle-selection', id)" />
                </div>
            </SidebarSection>

            <SidebarSection title="Notifications" :count="notifications.length"
                class="flex-[1] overflow-hidden flex flex-col">
                <div class="overflow-y-auto">
                    <NotificationListItem v-for="note in notifications" :key="note.id" :notification="note" />
                </div>
            </SidebarSection>

        </div>
    </div>
</template>

<script setup lang="ts">
import SidebarSection from './SidebarSection.vue';
import VehicleListItem from './VehicleListItem.vue';
import NotificationListItem from './NotificationListItem.vue';
import type { NotificationData } from '@/stores/notificationStore';

defineProps<{
    isVisible: boolean,
    vehicles: any[],
    notifications: NotificationData[],
    selectedVehicles: Set<string>,
    followedId: string | null
}>();
defineEmits(['toggle-selection', 'sub-action']);
</script>