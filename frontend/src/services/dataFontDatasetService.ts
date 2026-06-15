import type { DataFontDataset, DataFontDatasetPost } from "../util/DTO";
import { api } from "./api";

export const getDataFontsDatasetByDataset = async (datasetId: number): Promise<DataFontDataset[]> => {
    const response = await api.post("fonteDadosDataset/listarPorDataset", {datasetId});
    return response.data;
}

export const getDataFontsDatasetByDataFont = async (dataFontId: number): Promise<DataFontDataset[]> => {
    const response = await api.post("fonteDadosDataset/listarPorDataFont", {dataFontId});
    return response.data;
}

export const createRelationDataFontDataset = async (dataFont: DataFontDatasetPost) => {
    await api.post("fonteDadosDataset/criar", dataFont);
}