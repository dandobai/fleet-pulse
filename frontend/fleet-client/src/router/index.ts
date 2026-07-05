import { createRouter, createWebHistory } from 'vue-router';
import FleetMapView from '../views/FleetMapView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'map',
      component: FleetMapView
    }
  ]
});

export default router;