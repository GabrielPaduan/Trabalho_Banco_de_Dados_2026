import type { User } from "../util/DTO";
import { api } from "./api";

export const doLogin = async (email: string, password: string) => {
    const response = await api.post("/usuarios/login", {email, password});
    return response;
}

export const createUser = async (user: User) => {
    const response = await api.post("/usuarios/cadastrar", user);
    return response;
}