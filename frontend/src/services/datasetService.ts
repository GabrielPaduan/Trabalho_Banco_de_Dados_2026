import type { Dataset, DatasetPost } from "../util/DTO";
import { api } from "./api"

export const getDatasets = async (userEmail: string): Promise<Dataset[]> => {
    const response = await api.post("datasets/listar", {userEmail});
    return response.data;
}

export const getDatasetById = async (datasetId: number): Promise<Dataset> => {
    const response = await api.post("datasets/listarNome", {datasetId})
    return response.data;
}

export const createDataset = async (dataset: DatasetPost): Promise<Dataset> => {
    const response = await api.post("datasets/inserir", dataset);
    return response.data;
}

export const deleteDatasetById = async (id: number) => {
    await api.delete("datasets/excluir", {data: {id}});
}

export const updateDataset = async (dataset: Dataset): Promise<Dataset> => {
    const response = await api.put("datasets/atualizar", dataset);
    return response.data;
}