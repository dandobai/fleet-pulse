import { describe, it, expect, vi, beforeEach } from 'vitest';
import { PositionHistoryService } from '../../services/positionHistoryService';
import api from '../../services/api';

vi.mock('../../services/api', () => ({
  default: {
    get: vi.fn(),
  },
}));

describe('PositionHistoryService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should correctly map API response to PositionData format', async () => {
    const mockData = {
      history: [
        { latitude: 47.1, longitude: 19.2, timestamp: '2026-07-14T10:00:00Z' }
      ]
    };

    vi.mocked(api.get).mockResolvedValue({ data: mockData });

    const result = await PositionHistoryService.fetchHistory('v123');

    expect(api.get).toHaveBeenCalledWith('api/v1/history/v123');
    expect(result).toEqual([{
      lat: 47.1,
      lng: 19.2,
      timestamp: '2026-07-14T10:00:00Z'
    }]);
  });
});