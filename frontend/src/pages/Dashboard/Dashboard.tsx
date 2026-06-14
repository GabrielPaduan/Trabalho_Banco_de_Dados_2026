import { Container, Grid, Typography } from "@mui/material";
import { DefaultHeader } from "../../components/DefaultHeader";
import { SideMenu } from "../../components/SideMenu";
import { DatasetCards } from "../../components/DatasetCards";

export function Dashboard() {
    return (
        <>
            <DefaultHeader />
            <Container component="main" sx={{ display: "flex", justifyContent: "left", height: "80vh" }} maxWidth={false}>
                <SideMenu />
                <Container component="section" disableGutters sx={{ width: "85%", padding: 2 }} maxWidth={false}>
                    <DatasetCards />
                </Container>
    
            </Container>
        </>
    )
}