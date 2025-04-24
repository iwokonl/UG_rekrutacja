import { Injectable } from '@angular/core';
import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

@Injectable({
  providedIn: 'root'
})
export class AxiosService {
  private axiosInstance: AxiosInstance;

  constructor() {
    this.axiosInstance = axios.create({
      baseURL: 'http://localhost:8080/',
      timeout: 5000
    });
  }

  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    try {
      const response = await this.axiosInstance.get<T>(url, config);
      return response.data;
    } catch (error) {
      console.error('Błąd podczas wykonywania GET:', error);
      throw error;
    }
  }
  async getWithParams<T>(url: string, params?: Record<string, any>, config?: AxiosRequestConfig): Promise<T> {
    try {
      const response = await this.axiosInstance.get<T>(url, { ...config, params });
      return response.data;
    } catch (error) {
      console.error('Błąd podczas wykonywania GET z parametrami:', error);
      throw error;
    }
  }
  async post<T>(url: string, data: any, config?: AxiosRequestConfig): Promise<T> {
    try {
      const response = await this.axiosInstance.post<T>(url, data, config);
      return response.data;
    } catch (error) {
      console.error('Błąd podczas wykonywania POST:', error);
      throw error;
    }
  }

  async put<T>(url: string, data: any, config?: AxiosRequestConfig): Promise<T> {
    try {
      const response = await this.axiosInstance.put<T>(url, data, config);
      return response.data;
    } catch (error) {
      console.error('Błąd podczas wykonywania PUT:', error);
      throw error;
    }
  }

  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    try {
      const response = await this.axiosInstance.delete<T>(url, config);
      return response.data;
    } catch (error) {
      console.error('Błąd podczas wykonywania DELETE:', error);
      throw error;
    }
  }
}
