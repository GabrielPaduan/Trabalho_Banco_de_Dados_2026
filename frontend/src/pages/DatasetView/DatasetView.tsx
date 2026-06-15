import { Box, Button, Container, Grid, Tab, TextField, Typography } from "@mui/material";
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
import GenericForm from "../../components/GenericForm";
import { Table } from "../../components/Table";

export function DatasetView() {
    const { name } = useParams();
    const [dataset, setDataset] = useState<Dataset>();
    const [datasetUpdates, setDatasetUpdates] = useState<Dataset>();
    const [pageValue, setPageValue] = useState("1");
    const [user, setUser] = useState<User>();
    const { loggedUser } = useAuth();
    const [error, setError] = useState("");

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
                setDatasetUpdates(responseDataset);
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
                            { pageValue.localeCompare("2") ? (
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
                                            <Typography>Implementando...</Typography>
                                        </Box>
                                    </Box>
                                </Container>
                            ) : (
                                <Container component={"article"} sx={{ width: "100%", padding: 2, border: "1px solid lightgray", borderRadius: "10px" }}>
                                    <Box sx={{ borderBottom: "1px solid lightgray", width: "100%", display: "flex", justifyContent: "space-between", alignItens: "center", gap: 2, paddingBottom: 2 }}>
                                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                                            <Box sx={{ display: "flex", gap: 2, alignItens: "center"}}>
                                                <Typography component="h2" variant="h6">{dataset?.name}</Typography>
                                            </Box>
                                        </Box>
                                        <Box sx={{ display: "flex", gap: "5px", alignItens: "center", justifyContent: "center" }}>
                                            <Typography component={"h6"} variant="h6">Última versão: </Typography>
                                            <Typography component={"span"}>Implementando...</Typography>
                                        </Box>
                                    </Box>
                                </Container>
                            )}
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
                                    <TabPanel value={"2"} sx={{ paddingTop: 2 }}>
                                        <Container component={"section"} disableGutters sx={{ display: "flex", justifyContent: "space-between", padding: 1, border: "1px solid lightgray", borderRadius: "10px" }} maxWidth={false}>
                                            <Container component={"article"} sx={{ borderRight: "1px solid lightgray", width: "40%" }}>
                                                <GenericForm
                                                    title="Informações Gerais"
                                                    buttonText="Salvar Alterações"
                                                    error={error}
                                                    onSubmit={() => {}}
                                                >
                                                    <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
                                                        <TextField label="Nome: " value={datasetUpdates?.name} onChange={(e) => setDataset(prev =>({...prev, name: e.target.value}) as Dataset)} required fullWidth/>
                                                    
                                                        <TextField slotProps={{
                                                            htmlInput: { maxLength: 255 }
                                                            }} multiline rows={4} helperText={`${datasetUpdates?.description?.length || 0}/255 caracteres`} label="Descrição: " value={datasetUpdates?.description} onChange={(e) => setDatasetUpdates(prev =>({...prev, description: e.target.value}) as Dataset)} required fullWidth
                                                        />
                                                    </Box>
                                                </GenericForm>
                                            </Container>
                                            <Container component={"article"} sx={{ width: "60%" }}>
                                                <Table
                                                    title="Schema de Metadados"
                                                />
                                            </Container>
                                        </Container>
                                    </TabPanel>
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