import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createPinia } from 'pinia'
import router from '../router/index.ts'
import App from '../App.vue'

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

vi.mock('@/composables/useFleetSocket', () => ({
  useFleetSocket: vi.fn(), 
}))

describe('App', () => {
  it('mounts the app container correctly', async () => {
    router.push('/')
    await router.isReady()

    const wrapper = mount(App, {
      global: {
        plugins: [createPinia(), router],
        stubs: {
          MapContainer: true 
        }
      }
    })
    
    expect(wrapper.find('main').exists()).toBe(true)
  })
})