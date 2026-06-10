import { Container, Link, TextField } from "@mui/material";
import FormularioUser from "../../components/UserForm";
import { useState } from "react";
import type { User } from "../../util/DTO";
import { useNavigate } from "react-router-dom";
import { createUser } from "../../services/userService";
import { DefaultHeader } from "../../components/DefaultHeader";

export default function CreateUser() {
    const [error, setError] = useState('');
    const [user, setUser] = useState<User>();
    const navigate = useNavigate();

    const submitForm = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        try {
            if (user !== undefined) {
                await createUser(user);
                navigate("/login");
            }
        } catch (err: any) {
            if (err.response && err.response.data) {
                setError(err.response.data);
            } else {
                setError("Erro na conexão com o servidor!");
            }
        }
    }

    return (
        <>
            <Container component={"main"} maxWidth="xs" sx={{ display: "flex", alignItems: "center", flexDirection: "column", height: '100vh', paddingTop: 5}}>
                <FormularioUser
                    title="Cadastro"
                    buttonText="Criar"
                    error = {error}
                    onSubmit={submitForm}
                >
                    <TextField label="CPF: " placeholder="ex. 000.000.000-00" required value={user?.cpf} onChange={(e) => setUser(prev => ({...prev, cpf: e.target.value} as User))} fullWidth/>

                    <TextField label="Nome: " value={user?.name} onChange={(e) => setUser(prev =>({...prev, name: e.target.value}) as User)} required fullWidth/>
                    
                    <TextField label="Email: " placeholder="ex. exemplo@email.com" required value={user?.email} onChange={(e) => setUser(prev =>({...prev, email: e.target.value} as User))} fullWidth/>

                    <TextField label="Senha: " type="password" value={user?.password} onChange={(e) => setUser(prev => ({...prev, password: e.target.value}) as User)} required fullWidth/>
                </FormularioUser>
                <Link component={"button"} variant="body2" onClick={() => navigate("/")} sx={{ width: "100%" }}>
                    Já tem uma conta? Entre!
                </Link>
            </Container>
        </>
    )
}