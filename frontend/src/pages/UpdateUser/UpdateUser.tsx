import { Button, CircularProgress, Container, TextField } from "@mui/material";
import UserForm from "../../components/UserForm";
import { useEffect, useState } from "react";
import type { User } from "../../util/DTO";
import { getUserByEmail, updateUser } from "../../services/userService";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import { DefaultHeader } from "../../components/DefaultHeader";

export function UpdateUser() {
    const [error, setError] = useState("");
    const [user, setUser] = useState<User | null>(null);
    const navigate = useNavigate();
    const { loggedUser } = useAuth();

    useEffect(() =>  {
        const userEmail = loggedUser?.sub;
        const getUser = async () => {
            try {
                if (userEmail !== undefined) {
                    const user = await getUserByEmail(userEmail);
                    setUser(user);
                }
            } catch (err: any) {
                if (err.response && err.response.data) {
                    setError(err.response.data);
                } else {
                    setError("Erro na comunicação com o servidor!");
                }
            } 
        }
        getUser();  
    }, []);

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            if (user !== null) {
                await updateUser(user);
                navigate("/dashboard");
            }
        } catch (err: any) {
            if (err.response && err.response.data) {
                setError(err.response.data);
            }
        }
    }

    if (!user) {
        return (
            <Container sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <>
            <DefaultHeader />
            <Container component={"main"} sx={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column", gap: 1, paddingTop: 10 }}>
                <UserForm
                    title="Editar Perfil"
                    buttonText="Atualizar"
                    error={error}
                    onSubmit={onSubmit}
                >
                    <TextField label="Nome: " value={user?.name} onChange={(e) => setUser(prev =>({...prev, name: e.target.value}) as User)} required fullWidth/>
                    <TextField label="Email: " placeholder="ex. exemplo@email.com" required value={user?.email} onChange={(e) => setUser(prev =>({...prev, email: e.target.value} as User))} fullWidth/>
                </UserForm>
                <Button variant="contained" onClick={() => navigate(-1)}>Voltar</Button>
            </Container>
        </>
    )
}