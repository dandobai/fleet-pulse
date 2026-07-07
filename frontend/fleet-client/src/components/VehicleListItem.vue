<template>
  <li @click="$emit('toggle', vehicle.id)"
      :class="isSelected ? 'border-green-400 bg-green-950/20' : 'border-green-800'"
      class="p-3 bg-gray-900 border cursor-pointer transition-all">

    <div class="flex justify-between items-center mb-2">
      <span class="font-bold text-green-300 uppercase">{{ vehicle.id.slice(0, 8) }}</span>

      <button @click.stop="$emit('toggle', vehicle.id)"
              :class="isShowing ? 'bg-green-600 text-black' : 'border-green-500 text-green-500'"
              class="border px-2 py-0.5 text-[10px] hover:bg-green-500 hover:text-black transition-all">
        {{ isShowing ? '[SHOWING]' : '[HIDDEN]' }}
      </button>
    </div>

    <div v-if="isDetailsVisible"
         class="text-[10px] text-green-400 py-2 border-y border-green-900/30 mb-2 bg-black/30">
      <div>FULL ID: {{ vehicle.id }}</div>
      <div>LAT: {{ vehicle.lat.toFixed(4) }}</div>
      <div>LNG: {{ vehicle.lng.toFixed(4) }}</div>
      <div>LAST UPDATE: {{ formatTime(vehicle.timestamp) }}</div>
    </div>

    <div @click.stop class="flex gap-2 py-2 border-y border-green-900/50">
      <button @click="handleAction('zoom', vehicle.id)" class="text-[9px] text-green-500 hover:text-green-300">
        [ZOOM_TO]
      </button>
      <button @click="handleAction('follow', vehicle.id)" class="text-[9px] text-green-500 hover:text-green-300">
        {{ isFollowing ? '[FOLLOWING]' : '[FOLLOW]' }}
      </button>
      <button @click="toggleDetails" class="text-[9px] text-green-500 hover:text-green-300">
        {{ isDetailsVisible ? '[HIDE_DETAILS]' : '[DETAILS]' }}
      </button>
      <button @click="toggleHistory" class="text-[9px] text-yellow-500 hover:text-yellow-300">
        {{ isHistoryActive ? '[HIDE_HISTORY]' : '[HISTORY]' }}
      </button>
    </div>

    <div v-if="isHistoryActive" @click.stop
         class="mt-2 p-2 bg-yellow-950/20 border border-yellow-900/30 text-[9px] text-yellow-500">
      <div v-if="historyStore.loading.get(vehicle.id)">Loading history...</div>
      <div v-else class="custom-scrollbar custom-yellow-scrollbar max-h-32 overflow-y-auto">
        <div v-for="(point, index) in historyData" :key="index"
             class="flex justify-between border-b border-yellow-900/20 py-0.5">
          <span>{{ formatTime(point.timestamp) }}</span>
          <span>{{ point.lat.toFixed(4) }}, {{ point.lng.toFixed(4) }}</span>
        </div>
      </div>
    </div>
  </li>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { usePositionHistoryStore } from '@/stores/positionHistoryStore';
import { PositionHistoryService } from '@/services/positionHistoryService';

const props = defineProps<{
  vehicle: any,
  selectedVehicles: Set<string>,
  isFollowing: boolean
}>();

const emit = defineEmits(['toggle', 'sub-action']);
const historyStore = usePositionHistoryStore();

const isDetailsVisible = ref(false);

const isHistoryActive = computed(() => historyStore.isHistoryActive(props.vehicle.id));
const historyData = computed(() => historyStore.getHistories(props.vehicle.id) || []);

const isSelected = computed(() => props.selectedVehicles.has(props.vehicle.id));
const isShowing = computed(() => props.selectedVehicles.size === 0 || isSelected.value);

const toggleDetails = () => {
  isDetailsVisible.value = !isDetailsVisible.value;
};

const toggleHistory = async () => {
  if (isHistoryActive.value) {
    historyStore.deletePositionHistory(props.vehicle.id);
  } else {
    try {
      const data = await PositionHistoryService.fetchHistory(props.vehicle.id);
      historyStore.setHistory(props.vehicle.id, data);
    } catch (err) {
      console.error("Hiba a history lekérésekor:", err);
    }
  }
};

const handleAction = (action: string, id: string) => {
  emit('sub-action', { action, id });
};

const formatTime = (ts: any) => 
  ts ? new Date(ts).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }) : '--:--';
</script>