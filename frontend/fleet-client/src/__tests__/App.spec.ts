import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia } from 'pinia'
import router from '../router/index.ts'
import App from '../App.vue'

// 1. Mockold a Leaflet-et a tesztfájl elején
vi.mock('leaflet', () => ({
  default: {
    map: vi.fn(() => ({
      setView: vi.fn(),
      addLayer: vi.fn(),
      remove: vi.fn(),
    })),
    tileLayer: vi.fn(() => ({
      addTo: vi.fn(),
    })),
  },
}))

describe('App', () => {
  it('mounts the app container correctly', async () => {
    router.push('/')
    await router.isReady()

    const wrapper = mount(App, {
      global: {
        plugins: [createPinia(), router],
        // 2. Ha a MapContainer-ben van globális komponens használat, 
        // azt itt is ki kell zárni (stubs):
        stubs: {
          MapContainer: true 
        }
      }
    })
    
    expect(wrapper.find('main').exists()).toBe(true)
  })
})