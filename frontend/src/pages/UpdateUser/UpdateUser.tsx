import { CircularProgress, Container, Grid, TextField } from "@mui/material";
import UserForm from "../../components/GenericForm";
import { useEffect, useState } from "react";
import type { User } from "../../util/DTO";
import { getUserByEmail, updateUser } from "../../services/userService";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";
import { DefaultHeader } from "../../components/DefaultHeader";
import { SideMenu } from "../../components/SideMenu";
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

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
            <Container component="main" sx={{ display: "flex", justifyContent: "left", height: "80vh" }} maxWidth={false} >
                <SideMenu />
                <Container component={"section"} disableGutters maxWidth={false} sx={{ display: "flex", justifyContent: "flex-start", alignItems: "center", flexDirection: "column", gap: 1, paddingTop: 2 }}>
                    <UserForm
                        title="Perfil"
                        buttonText="Atualizar"
                        error={error}
                        onSubmit={onSubmit}
                    >
                        <Grid container rowSpacing={2} columnSpacing={2}>
                            <Grid size={6}>
                                <TextField label="CPF: " value={user?.cpf} disabled fullWidth />
                            </Grid>
                            <Grid size={6}>
                                <TextField label="Data de criação: " disabled value={format(user?.createdDate, 'dd/MM/yyyy', { locale: ptBR})} fullWidth/>
                            </Grid>
                            <Grid size={6}>
                                <TextField label="Nome: " value={user?.name} onChange={(e) => setUser(prev =>({...prev, name: e.target.value}) as User)} required fullWidth/>
                            </Grid>
                            <Grid size={6}>
                                <TextField label="Email: " placeholder="ex. exemplo@email.com" required value={user?.email} onChange={(e) => setUser(prev =>({...prev, email: e.target.value} as User))} fullWidth/>
                            </Grid>                            
                        </Grid>
                    </UserForm>
                </Container>
            </Container>
        </>
    )
}