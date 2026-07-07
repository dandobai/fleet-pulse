import api from './api';
import type { PositionData } from '@/stores/positionHistoryStore';

export const PositionHistoryService = {
  async fetchHistory(vehicleId: string): Promise<PositionData[]> {
    const response = await api.get<{ history: any[] }>(`api/v1/history/${vehicleId}`);

    return response.data.history.map((dto: any) => ({
      lat: dto.latitude,
      lng: dto.longitude,
      timestamp: dto.timestamp
    }));
  }
};