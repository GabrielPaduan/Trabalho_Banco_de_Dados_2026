import { useContext } from "react";
import { AuthContext } from "../contexts/AuthContext";

export function useAuth() {
    const context = useContext(AuthContext);
    if (context == undefined) {
        throw new Error("Necessário haver auth provider nas rotas");
    }

    return context;
}