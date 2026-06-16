import type { Feature, FeaturePost } from "../util/DTO";
import { api } from "./api";

export const createFeature = async (featureData: FeaturePost): Promise<Feature> => {
    const response = await api.post("/features", featureData);
    return response.data; 
}

export const getFeaturesByVersionId = async (versionId: number): Promise<Feature[]> => {
    const response = await api.get(`/features/versao/${versionId}`);
    return response.data; 
}

export const getFeatureById = async (id: number): Promise<Feature> => {
    const response = await api.get(`/features/${id}`);
    return response.data; 
}

export const updateFeature = async (id: number, featureData: Feature): Promise<Feature> => {
    const response = await api.put(`/features/${id}`, featureData);
    return response.data; 
}

export const deleteFeature = async (id: number): Promise<string> => {
    const response = await api.delete(`/features/${id}`);
    return response.data; 
}