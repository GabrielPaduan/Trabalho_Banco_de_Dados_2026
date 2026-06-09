import { jwtDecode } from "jwt-decode";
import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { api } from "../services/api";
import { doLogin } from "../services/userService";

interface TokenInterface {
    sub: string;
    name: string;
    exp: number;
}

interface AuthContextInterface {
    authenticated: boolean;
    loggedUser: TokenInterface | null;
    login: (cpf: string, password: string) => void;
    logout: () => void;
}
 
export const AuthContext = createContext<AuthContextInterface>({} as AuthContextInterface);

export function AuthProvider({children}: {children: ReactNode}) {
    const [loggedUser, setLoggedUser] = useState<TokenInterface | null>(null);

    useEffect(() => {
        const token = localStorage.getItem("@FeatureStore:token");

        if (token) {
            try {
                // Tenta decodificar o token
                const decodedToken = jwtDecode<TokenInterface>(token);
                const dataAtual = Date.now() / 1000; // converte de milissegundos para segundos
                if (decodedToken.exp < dataAtual) {
                    throw new Error("Token expirado! Deslogando...");
                }

                api.defaults.headers.common['Authorization'] = `Bearer: ${token}`;
                setLoggedUser(decodedToken);
            } catch (err: any) {
                logout();
            }
        }
    }, []);

    const login = async (cpf: string, password: string) => {
        try {
            const response = await doLogin(cpf, password);
            const token = response.data;

            localStorage.setItem("@FeatureStore:token", token);
            api.defaults.headers.common['Authorization'] = `Bearer: ${token}`;
            const decodedToken = jwtDecode<TokenInterface>(token)
            setLoggedUser(decodedToken);
        } catch (err: any) {
            throw new Error(err);
        }
    }

    const logout = async () => {
        localStorage.removeItem("@FeatureStore:token");
        delete api.defaults.headers.common["Authorization"];
        setLoggedUser(null);
    };

    return (
        <AuthContext.Provider
            value={{
                authenticated: !!loggedUser,
                loggedUser,
                login,
                logout
            }}
        >
            {children}
        </AuthContext.Provider>
    )
}