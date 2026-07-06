import { Box, Button, Container, Grid, Tab, TextField, Typography } from "@mui/material";
import { TabContext, TabList, TabPanel } from '@mui/lab';
import { useNavigate, useParams } from "react-router-dom";
import { SideMenu } from "../../components/SideMenu";
import { DefaultHeader } from "../../components/DefaultHeader";
import { useEffect, useState } from "react";
import type { DataFont, Dataset, User, Version } from "../../util/DTO";
import { getDatasetById, updateDataset } from "../../services/datasetService";
import { useAuth } from "../../hooks/useAuth";
import { getUserByEmail } from "../../services/userService";
import { format } from "date-fns";
import GenericForm from "../../components/GenericForm";
import MultipleSelectChip from "../../components/MultipleSelectChip";
import { getDataFonts } from "../../services/dataFontService";
import { createRelationDataFontDataset, getDataFontsDatasetByDataset } from "../../services/dataFontDatasetService";
import GenericTable from "../../components/GenericTable";
import { createVersion, getVersionByDatasetId } from "../../services/versionsService";
import { ConfirmModal } from "../../components/Modal";

export function DatasetView() {
    const { id } = useParams();
    const [dataset, setDataset] = useState<Dataset>();
    const [datasetUpdates, setDatasetUpdates] = useState<Dataset>();
    const [pageValue, setPageValue] = useState("1");
    const [user, setUser] = useState<User>();
    const { loggedUser } = useAuth();
    const [error, setError] = useState("");
    const [dataFont, setDataFont] = useState<DataFont[]>([]);
    const [selectedFontsIds, setSelectedFontsIds] = useState<number[]>([]);
    const [versionsList, setVersionsList] = useState<Version[]>([]);
    const [version, setVersion] = useState<Version>();
    const [modalStatusVersion, setModalStatusVersion] = useState<boolean>();
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const navigate = useNavigate();

    function handleChangeTab(event: React.SyntheticEvent, newValue: string) {
        setPageValue(newValue);
    }

    useEffect(() => {
        const fetchDataset = async () => {
            try {
                if (id == undefined) {
                    console.log("Id está inválido!");
                    return;
                }
                if (loggedUser?.sub == undefined) {
                    console.log("Usuário está inválido!");
                    return;
                }
                const [responseDataset, responseUser, responseDataFont, responseVersions ] = await Promise.all([
                    getDatasetById(parseInt(id)),
                    getUserByEmail(loggedUser?.sub),
                    getDataFonts(),
                    getVersionByDatasetId(id)
                ]) 
                setDataset(responseDataset);
                setDatasetUpdates(responseDataset);
                setUser(responseUser);
                setDataFont(responseDataFont);
                setVersionsList(responseVersions);

                const responseDataFontDataset = await getDataFontsDatasetByDataset(responseDataset.id);
                const idsFontes = responseDataFontDataset.map((relacao) => relacao.dataFontId);
                setSelectedFontsIds(idsFontes);
            } catch (err: any) {
                if (err.response && err.response.data) {
                    console.log("Err: " + err.response.data);
                } else {
                    console.log("Erro na comunicação com o servidor!");
                }
            }
        }
        fetchDataset();
    }, [id, loggedUser?.sub]);

    const createVersionSubmit = async () => {
        try {
            if (!selectedFile) {
                alert("Por favor, selecione um arquivo.");
                return;
            }

            const formData = new FormData();
            formData.append("datasetId", String(id));
            formData.append("archivePath", selectedFile);
            
            const newVersion: Version = await createVersion(formData);
            
            setVersionsList((prev) => [newVersion, ...prev]);
            
            setSelectedFile(null);
            handleCloseVersionModal();
            navigate(`/version/${newVersion.id}`)
        } catch (err: any) {
            if (err?.response?.data) {
                console.error(err?.response.data);
            } else {
                console.error("Erro");
            }
        }
    }

    const updateDatasetInfo = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const datasetReponse = await updateDataset(datasetUpdates as Dataset);
            setDataset(datasetReponse);
            setDatasetUpdates(datasetReponse);
             await Promise.all(selectedFontsIds.map((fontId) => {
                    const payload = ({datasetId: datasetReponse.id, dataFontId: fontId});
                    return createRelationDataFontDataset(payload);  
                })
            )
        } catch (err: any) {
            if (err?.response.data) {
                console.log("Erro: " + err?.response.data);
            } else {
                console.log("Erro na comunicação com o servidor!");
            }
        }
    }

    const handleOpenVersionModal = () => {
        setModalStatusVersion(true);
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                               
    const handleCloseVersionModal = () => {
        setModalStatusVersion(false);
    }

    return (
        <>
            <DefaultHeader />
            <Container component="main"  sx={{ display: "flex", justifyContent: "left", height: "80vh" }} maxWidth={false}>
                <SideMenu />
                <Container component="section" disableGutters sx={{ width: "85%", padding: 2, display: "flex", flexDirection: "column", gap: 1 }} maxWidth={false}>
                    <Typography component={"h2"} variant="h6">Visualizar Dataset</Typography>
                    {dataset && dataset !== undefined ? (
                        <>
                            { pageValue !== "2" && (
                                <Container component={"article"} sx={{ width: "100%", height: "40%", padding: 2, border: "1px solid lightgray", borderRadius: "10px" }}>
                                    {/* Parte de cima */}
                                    <ConfirmModal
                                        title= "Criar Versão"
                                        buttonText= "Criar"
                                        fetchFunc = {createVersionSubmit}
                                        data = {version || ({} as Version)}
                                        modalStatus = {modalStatusVersion as boolean}
                                        handleClose ={handleCloseVersionModal}
                                    >
                                        <TextField 
                                            type="file" 
                                            label="Arquivo: " 
                                            onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                                                if (e.target.files && e.target.files.length > 0) {
                                                    setSelectedFile(e.target.files[0]); 
                                                }
                                            }} 
                                            slotProps={{
                                                inputLabel: { 
                                                    shrink: true 
                                                },
                                                htmlInput: { 
                                                    accept: ".csv, .json, .parquet" 
                                                }
                                            }}
                                            fullWidth 
                                            required
                                        />
                                    </ConfirmModal>

                                    <Box sx={{ borderBottom: "1px solid lightgray", width: "100%", display: "flex", justifyContent: "space-between", alignContent: "center", gap: 1, paddingBottom: 2 }}>
                                        <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                                            <Box sx={{ display: "flex", gap: 2, alignContent: "center"}}>
                                                <Typography component="h2" variant="h6" sx={{ padding: "5px 0" }}>{dataset?.name}</Typography>
                                                <Typography component="span" variant="h6" sx={{ backgroundColor: "#B3FFF3", borderRadius: "10%", padding: "5px 10px"}}>{dataset?.active ? "Ativo" : "Inativo"}</Typography>
                                            </Box>
                                            <Box>
                                                <Typography>{dataset?.description}</Typography>
                                            </Box>
                                        </Box>
                                        <Button variant="contained" onClick={handleOpenVersionModal}>
                                            Criar Nova Versão
                                        </Button>
                                    </Box>
                                    {/* Parte de baixo */}
                                    <Box sx={{ width: "100%", display: "flex", gap: 4, paddingTop: 1 }}>
                                        <Box>
                                            <Typography>Criador</Typography>
                                            <Typography>{user?.name}</Typography>
                                        </Box>
                                        <Box>
                                            <Typography>Data de Criação</Typography>
                                            <Typography>
                                                {dataset?.createdDate 
                                                ? format(new Date(dataset.createdDate), "dd/MM/yyyy") 
                                                : "Data indisponível"}
                                            </Typography>
                                        </Box>
                                        <Box>
                                            <Typography>Tamanho Total</Typography>
                                            <Typography>{ versionsList ? (versionsList.reduce((acumulator, version) => acumulator + version.size, 0) / 1024).toFixed(2) + " MB" : "Não determinado!" }</Typography>
                                        </Box>
                                    </Box>
                                </Container>
                            )}
                            <Container component={"article"} disableGutters sx={{ width: "100%", padding: 2, border: "1px solid lightgray", borderRadius: "10px", display: "flex", flexDirection: "column", overflowY: "hidden" }} maxWidth={false}>
                                <TabContext value={pageValue}>
                                    <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
                                        <TabList
                                            onChange={handleChangeTab}                 
                                        >
                                            <Tab label="Linhagens e versões" value="1" />
                                            <Tab label="Configurações" value="2" />
                                        </TabList>
                                    </Box>
                                    <TabPanel value={"1"} sx={{ height: "100%"}}>
                                        <GenericTable listVersion={versionsList}/>
                                    </TabPanel>
                                    <TabPanel value={"2"}  sx={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                                        <Container component={"article"} sx={{ width: "100%" }}>
                                            <GenericForm
                                                title="Informações Gerais"
                                                buttonText="Salvar Alterações"
                                                error={error}
                                                onSubmit={updateDatasetInfo}
                                            >
                                                <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                                                    <Grid container rowSpacing={2} columnSpacing={2}>
                                                        <Grid size={4}>
                                                            <TextField label="Nome: " value={datasetUpdates?.name} onChange={(e) => setDatasetUpdates(prev =>({...prev, name: e.target.value}) as Dataset)} required fullWidth/>  
                                                        </Grid>
                                                        <Grid size={4}>
                                                            <MultipleSelectChip 
                                                                dataFont={dataFont ?? []}
                                                                selectedFontsIds={selectedFontsIds}
                                                                onChange={setSelectedFontsIds}
                                                            />
                                                        </Grid>
                                                        <Grid size={4}>
                                                            <TextField label="Data de criação: " value={datasetUpdates?.createdDate} disabled required fullWidth/>
                                                        </Grid>
                                                         <Grid size={12}>
                                                            <TextField slotProps={{
                                                                htmlInput: { maxLength: 255 }
                                                                }} multiline rows={2} helperText={`${datasetUpdates?.description?.length || 0}/255 caracteres`} label="Descrição: " value={datasetUpdates?.description} onChange={(e) => setDatasetUpdates(prev =>({...prev, description: e.target.value}) as Dataset)} required fullWidth
                                                            />
                                                        </Grid>
                                                    </Grid>
                                                </Box>
                                            </GenericForm>
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