import { defineStore } from 'pinia';
import { ref } from 'vue';

export interface NotificationData {
  id: string;
  message: string;
  timestamp: string;
}

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref<NotificationData[]>([]);

  function addNotification(data: NotificationData) {
    notifications.value.unshift(data);
  }

  function clearNotifications() {
    notifications.value = [];
  }

  return { notifications, addNotification, clearNotifications };
});