import { Link, Container, TextField, Box } from "@mui/material";
import FormularioUser from "../../components/GenericForm";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

export default function UserLogin() {
    const { login, authenticated } = useAuth();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        if (authenticated) {
            navigate("/dashboard");
        }
    }, [authenticated])

    const submitForm = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            login(email, password);
        } catch (err: any) {
            if (err.response && err.response.data) {
                setError(err.response.data);
            } else {
                setError("Erro na conexão com o servidor!");
            }
        }
    }

    return (
        <Container component="main" maxWidth="xs" sx={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column", height: '100vh'}}>
            <FormularioUser
                title="Acesse a Plataforma"
                buttonText="Acessar"
                error={error}
                onSubmit={submitForm}
            >
                <TextField label="Email: " placeholder="ex. exemplo@email.com" required value={email} onChange={(e) => setEmail(e.target.value)} fullWidth/>
                <TextField label="Senha: " type="password" value={password} onChange={(e) => setPassword(e.target.value)} required fullWidth/>
            </FormularioUser>
            <Box sx={{ width: "70%", display: "flex", justifyContent: "space-between", flexDirection: "column", gap: 1}}>
                <Link component={"button"} variant="body2" onClick={() => navigate("/usuario/cadastrar")} sx={{ width: "100%" }}>
                    Criar Conta
                </Link>
            </Box>
        </Container>
    )
}