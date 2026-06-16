import type { DataFontDataset, DataFontDatasetPost } from "../util/DTO";
import { api } from "./api";

export const getDataFontsDatasetByDataset = async (datasetId: number): Promise<DataFontDataset[]> => {
    const response = await api.get(`/fonteDadosDataset/dataset/${datasetId}`);
    return response.data;
}

export const getDataFontsDatasetByDataFont = async (dataFontId: number): Promise<DataFontDataset[]> => {
    const response = await api.get(`/fonteDadosDataset/fonte/${dataFontId}`);
    return response.data;
}

export const createRelationDataFontDataset = async (dataFont: DataFontDatasetPost) => {
    await api.post("/fonteDadosDataset", dataFont);
}