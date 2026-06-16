import type { Version } from "../util/DTO";
import { api } from "./api";

export const getVersionByDatasetId = async (datasetId: string): Promise<Version[]> => {
    const response = await api.get(`/versoes/dataset/${datasetId}`);
    return response.data;
}

export const getVersionById = async (id: number): Promise<Version> => {
    const response = await api.get(`/versoes/${id}`);
    return response.data;
}

export const createVersion = async (formData: FormData): Promise<Version> => {
    const response = await api.post("/versoes", formData, {
        headers: {
            "Content-Type": "multipart/form-data", 
        },
    });
    return response.data
}

export const getBaseVersion = async (id: number): Promise<Version> => {
    const response = await api.get(`/versoes/baseVersion/${id}`);
    return response.data;
}

export const downloadVersionFile = async (versionId: number): Promise<Blob> => {
    const response = await api.get(`/versoes/download/${versionId}`, {
        responseType: 'blob' 
    });
    return response.data;
}