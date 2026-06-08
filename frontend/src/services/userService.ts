import { api } from "./api";

export const doLogin = async (cpf: string, password: string) => {
    const response = await api.post("/usuarios/login", {cpf, password});
    return response;
}