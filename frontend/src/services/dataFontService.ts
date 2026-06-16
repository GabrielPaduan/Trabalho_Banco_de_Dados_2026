import type { DataFont, DataFontPost } from "../util/DTO";
import { api } from "./api";

export const getDataFonts = async (): Promise<DataFont[]> => {
    const response = await api.get("fonteDados");
    return response.data;
}

export const createDataFont = async (dataFont: DataFontPost): Promise<DataFont> => {
    const response = await api.post("fonteDados", dataFont);
    return response.data;
}