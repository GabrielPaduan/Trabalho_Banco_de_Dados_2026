import type { DataFont } from "../util/DTO";
import { api } from "./api";

export const getDataFonts = async (): Promise<DataFont[]> => {
    const response = await api.get("fonteDados/listar");
    return response.data;
}

export const createDataFont = async (dataFont: DataFont): Promise<DataFont> => {
    const response = await api.post("datasets/inserir", dataFont);
    return response.data;
}