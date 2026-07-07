export interface WebSocketPayload<T = any> {
  type: string;
  data: T;
}