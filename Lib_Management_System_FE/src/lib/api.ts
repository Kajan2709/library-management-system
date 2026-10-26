/** Shared API client configuration. */
import { API_BASE_URL } from "../utils/apiEndpoint";

export const apiClient = {
  baseUrl: API_BASE_URL,

  async request<T>(path: string, options?: RequestInit): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...options?.headers,
      },
    });

    if (!response.ok) {
  const errorData = await response.json().catch(() => null);

  const errorMessage =
    errorData?.message ||
    errorData?.error ||
    `API request failed: ${response.status}`;

  throw new Error(errorMessage);
}

    if (response.status === 204) return undefined as T;
    return response.json() as Promise<T>;
  },
};
