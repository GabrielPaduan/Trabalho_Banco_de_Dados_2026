import { Link, Container, TextField, Box } from "@mui/material";
import FormularioUser from "../../components/FormularioUser";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { doLogin } from "../../services/userService";

export default function UserLogin() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const submitForm = async (e: React.SubmitEvent) => {
        e.preventDefault();
        try {
            const response = await doLogin(email, password);
            localStorage.setItem('@FeatureStore:token', response.data);
            navigate('/');
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
                <Link component={"button"} variant="body2" onClick={() => navigate("/redefinir-senha")} sx={{ width: "100%" }}>
                    Esqueci minha senha!
                </Link>
                <Link component={"button"} variant="body2" onClick={() => navigate("/usuario/criar")} sx={{ width: "100%" }}>
                    Criar Conta
                </Link>
            </Box>
        </Container>
    )
}