import type { User } from "../util/DTO";
import { api } from "./api";

export const doLogin = async (email: string, password: string) => {
    const response = await api.post("/usuarios/login", {email, password});
    return response;
}

export const createUser = async (user: User) => {
    const response = await api.post("/usuarios", user);
    return response;
}

export const updateUser = async (user: User) => {
    const response = await api.put("/usuarios", user);
    return response;
}

export const getUserByEmail = async (email: string): Promise<User> => {
    const response = await api.get(`/usuarios/${email}`);
    return response.data;
}