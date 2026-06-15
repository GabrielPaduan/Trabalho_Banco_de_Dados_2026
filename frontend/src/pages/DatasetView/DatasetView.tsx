import { Box, Button, Container, Tab, Typography } from "@mui/material";
import { TabContext, TabList, TabPanel } from '@mui/lab';
import { useParams } from "react-router-dom";
import { SideMenu } from "../../components/SideMenu";
import { DefaultHeader } from "../../components/DefaultHeader";
import { useEffect, useState } from "react";
import type { Dataset, User } from "../../util/DTO";
import { getDatasetByName } from "../../services/datasetService";
import { useAuth } from "../../hooks/useAuth";
import { getUserByEmail } from "../../services/userService";
import { format } from "date-fns";

export function DatasetView() {
    const { name } = useParams();
    const [dataset, setDataset] = useState<Dataset>();
    const [pageValue, setPageValue] = useState("1");
    const [user, setUser] = useState<User>();
    const { loggedUser } = useAuth();

    function handleChangeTab(event: React.SyntheticEvent, newValue: string) {
        setPageValue(newValue);
    }

    useEffect(() => {
        const fetchDataset = async () => {
            try {
                if (name == undefined) {
                    console.log("Nome está inválido!");
                    return;
                }
                if (loggedUser?.sub == undefined) {
                    console.log("Usuário está inválido!");
                    return;
                }
                const [responseDataset, responseUser ] = await Promise.all([
                    getDatasetByName(name),
                    getUserByEmail(loggedUser?.sub)
                ]) 
                setDataset(responseDataset);
                setUser(responseUser);
            } catch (err: any) {
                if (err.response && err.response.data) {
                    console.log("Err: " + err.response.data);
                } else {
                    console.log("Erro na comunicação com o servidor!");
                }
            }
        }
        fetchDataset();
    }, []);

    return (
        <>
            <DefaultHeader />
            <Container component="main"  sx={{ display: "flex", justifyContent: "left", height: "80vh" }} maxWidth={false}>
                <SideMenu />
                <Container component="section" disableGutters sx={{ width: "85%", padding: 2, display: "flex", flexDirection: "column", gap: 2 }} maxWidth={false}>
                    <Typography component={"h2"} variant="h6">Visualizar Dataset</Typography>
                    {dataset && dataset !== undefined ? (
                        <>
                            <Container component={"article"} sx={{ width: "100%", padding: 2, border: "1px solid lightgray", borderRadius: "10px" }}>
                                {/* Parte de cima */}
                                <Box sx={{ borderBottom: "1px solid lightgray", width: "100%", display: "flex", justifyContent: "space-between", alignContent: "center", gap: 2, paddingBottom: 2 }}>
                                    <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                                        <Box sx={{ display: "flex", gap: 2, alignContent: "center"}}>
                                            <Typography component="h2" variant="h6">{dataset?.name}</Typography>
                                            <Typography component="span" variant="h6" sx={{ backgroundColor: "#B3FFF3", borderRadius: "10%", padding: "5px 10px"}}>{dataset?.active ? "Ativo" : "Inativo"}</Typography>
                                        </Box>
                                        <Box>
                                            <Typography>{dataset?.description}</Typography>
                                        </Box>
                                    </Box>
                                    <Button variant="contained" onClick={() => {}}>
                                        Criar Nova Versão
                                    </Button>
                                </Box>
                                {/* Parte de baixo */}
                                <Box sx={{ width: "100%", display: "flex", gap: 4, paddingTop: 2 }}>
                                    <Box>
                                        <Typography>Criador</Typography>
                                        <Typography>{user?.name}</Typography>
                                    </Box>
                                    <Box>
                                        <Typography>Data de Criação</Typography>
                                        <Typography>{format(dataset?.createdDate, "dd/MM/yyyy")}</Typography>
                                    </Box>
                                    <Box>
                                        <Typography>Tamanho Total</Typography>
                                        <Typography>4.8TB</Typography>
                                    </Box>
                                    
                                </Box>
                            </Container>
                            <Container component={"article"} disableGutters sx={{ width: "100%", padding: 2, border: "1px solid lightgray", borderRadius: "10px" }} maxWidth={false}>
                                <TabContext value={pageValue}>
                                    <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
                                        <TabList
                                            onChange={handleChangeTab}
                                            
                                        >
                                            <Tab label="Linhagens e versões" value="1" />
                                            <Tab label="Configurações" value="2" />
                                        </TabList>
                                    </Box>
                                    <TabPanel value={"1"}>Tab 1</TabPanel>
                                    <TabPanel value={"2"}>Tab 2</TabPanel>
                                </TabContext>
                            </Container>
                        </>
                    ) : (
                         <Box sx={{ width: "85%", padding: 2 }}>
                            <Typography component={"h2"} variant="h6">Dataset não encontrado!</Typography>
                         </Box>
                    )} 
                </Container>
            </Container>

        </>
    )
}