import { API_PATH } from '../utils';
import { ITrafficLightDto } from '../components/LightForm/ITrafficLightDto';

class ApiService {
    private static instance: ApiService;
    private baseUrl: string;

    private constructor() {
        this.baseUrl = API_PATH;
    }

    public static getInstance(): ApiService {
        if (!ApiService.instance) {
            ApiService.instance = new ApiService();
        }
        return ApiService.instance;
    }

    private async request<T>(
        endpoint: string,
        options: RequestInit = {}
    ): Promise<T> {
        const url = `${this.baseUrl}${endpoint}`;
        const response = await fetch(url, {
            ...options,
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
        });

        // Handle successful responses (200-299)
        if (response.ok) {
            // For responses with no content (204) or created (201) without body
            if (response.status === 204 || response.status === 201) {
                return {} as T;
            }
            // For responses with JSON body
            return response.json();
        }

        throw new Error(`HTTP error! status: ${response.status}`);
    }

    // GET all lights
    public async getAllLights<T>(): Promise<T> {
        return this.request<T>('');
    }

    // POST new light
    public async createLight(lightData: ITrafficLightDto): Promise<Response> {
        return this.request<Response>('', {
            method: 'POST',
            body: JSON.stringify(lightData),
        });
    }

    // DELETE light
    public async deleteLight(id: string | number): Promise<void> {
        return this.request<void>(`/${id}`, {
            method: 'DELETE',
        });
    }
}

export const apiService = ApiService.getInstance(); 