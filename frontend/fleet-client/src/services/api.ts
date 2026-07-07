import axios from 'axios';

const api = axios.create({
  baseURL: __API_URL__,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;