import { Container, Typography } from "@mui/material";
import { DefaultHeader } from "../../components/DefaultHeader";

export function Dashboard() {
    return (
        <>
            <DefaultHeader />
            <Container component="main">
                <Typography component="h1" variant="h2">Tela em Desenvolvimento!</Typography>
            </Container>
        </>
    )
}