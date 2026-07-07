import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import tailwindcss from '@tailwindcss/vite';
import path from 'path';

export default defineConfig(({ mode }) => {
  const envDir = path.resolve(__dirname, '../../');
  
  const env = loadEnv(mode, envDir, '');

  return {
    envDir: envDir,
    
    define: {
      __API_URL__: JSON.stringify(env.BACKEND_URL),
      __WS_URL__: JSON.stringify(env.WEBSOCKET_URL),
    },
    
    plugins: [
      vue(),
      tailwindcss(),
    ],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src'),
      },
    },
  };
});