import type { Dataset, DatasetPost } from "../util/DTO";
import { api } from "./api"

export const getDatasets = async (userEmail: string): Promise<Dataset[]> => {
    const response = await api.get(`/datasets/usuario/${userEmail}`);
    return response.data;
}

export const getDatasetById = async (datasetId: number): Promise<Dataset> => {
    const response = await api.get(`/datasets/${datasetId}`);
    return response.data;
}

export const createDataset = async (dataset: DatasetPost): Promise<Dataset> => {
    const response = await api.post("/datasets", dataset);
    return response.data;
}

export const deleteDatasetById = async (id: number) => {
    await api.delete(`/datasets/${id}`);
}

export const updateDataset = async (dataset: Dataset): Promise<Dataset> => {
    const response = await api.put("/datasets", dataset);
    return response.data;
}