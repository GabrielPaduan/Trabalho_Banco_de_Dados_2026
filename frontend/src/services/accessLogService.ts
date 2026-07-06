import { api } from "./api"; 
import type { AccessLog, AccessLogPost } from "../util/DTO";

export const createAccessLog = async (accessLog: AccessLogPost): Promise<void> => {
    await api.post("/logsAcesso", accessLog);
};

export const getAccessLogById = async (id: number): Promise<AccessLog> => {
    const response = await api.get<AccessLog>(`/logsAcesso/${id}`);
    return response.data;
};

export const getAllAccessLogs = async (): Promise<AccessLog[]> => {
    const response = await api.get<AccessLog[]>("/logsAcesso");
    return response.data;
};

export const updateAccessLog = async (id: number, accessLog: AccessLog): Promise<AccessLog> => {
    const response = await api.put<AccessLog>(`/logsAcesso/${id}`, accessLog);
    return response.data;
};

export const deleteAccessLog = async (id: number): Promise<void> => {
    await api.delete(`/logsAcesso/${id}`);
};