import type { Dataset, DatasetPost } from "../util/DTO";
import { api } from "./api"

export const getDatasets = async (userEmail: string): Promise<Dataset[]> => {
    const response = await api.post("datasets/listar", {userEmail});
    return response.data;
}

export const getDatasetByName = async (datasetName: string): Promise<Dataset> => {
    const response = await api.post("datasets/listarNome", {datasetName})
    return response.data;
}

export const createDataset = async (dataset: DatasetPost): Promise<Dataset> => {
    const response = await api.post("datasets/inserir", dataset);
    return response.data;
}

export const deleteDatasetById = async (id: number) => {
    await api.delete("datasets/excluir", {data: {id}});
}