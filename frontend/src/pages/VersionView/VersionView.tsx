import { Box, Button, Container, Grid, Icon, Tab, TextField, Typography } from "@mui/material";
import { DefaultHeader } from "../../components/DefaultHeader";
import { SideMenu } from "../../components/SideMenu";
import { ConfirmModal } from "../../components/Modal";
import type { Dataset, Feature, FeaturePost, User, Version } from "../../util/DTO";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useAuth } from "../../hooks/useAuth";
import { downloadVersionFile, getVersionById } from "../../services/versionsService";
import { getDatasetById } from "../../services/datasetService";
import { getUserByEmail } from "../../services/userService";
import { format } from "date-fns";
import DownloadIcon from '@mui/icons-material/Download';
import { createFeature, getFeaturesByVersionId, updateFeature } from "../../services/FeatureService";
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';

export function VersionView() {
    const { id } = useParams();
    const { loggedUser } = useAuth();
    const [version, setVersion] = useState<Version>();
    const [dataset, setDataset] = useState<Dataset>();
    const [user, setUser] = useState<User>();
    const [baseVersion, setBaseVersion] = useState<Version>();
    const [modalStatusCreateFeature, setModalStatusCreateFeature] = useState<boolean>(false);
    const [feature, setFeature] = useState<Feature>();
    const [listFeature, setListFeature] = useState<Feature[]>();
    const navigate = useNavigate();

    const handleOpenCreateFeatureModal = () => {
        setModalStatusCreateFeature(true);
    }

    const handleCloseCreateFeatureModal = () => {
        setModalStatusCreateFeature(false);
    }

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (id == undefined) {
                    console.error("Id é inválido!");
                    return;
                }

                if (loggedUser?.sub == undefined) {
                    console.error("Id é inválido!");
                    return;
                }
                const idNumber = parseInt(id);
                const [versionResponse, userResponse, featureResponse] = await Promise.all([
                    getVersionById(idNumber),
                    getUserByEmail(loggedUser?.sub),
                    getFeaturesByVersionId(idNumber)
                ]);
                setVersion(versionResponse);
                setUser(userResponse);
                setListFeature(featureResponse);

                const datasetResponse = await getDatasetById(versionResponse.datasetId);
                setDataset(datasetResponse);

                const baseVersionResponse = await getVersionById(versionResponse.baseVersionId);
                console.log(baseVersionResponse);
                setBaseVersion(baseVersionResponse);
                

            } catch (err: any) {
                if (err?.response) {
                    console.error(err?.response.data);
                } else {
                    console.error("Erro na comunicação com o servidor!");
                }
            }
        }

        fetchData();
    }, [ id, loggedUser?.sub]);

    const handleDownload = async (versionId: number, numVersion: string) => {
        try {
            const blob = await downloadVersionFile(versionId);

            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            
            link.setAttribute('download', `versao_${numVersion}.csv`); 
            
            document.body.appendChild(link);
            link.click();
            
            link.remove();
            window.URL.revokeObjectURL(url);

        } catch (error) {
            console.error("Erro ao baixar o arquivo", error);
            alert("Não foi possível baixar o arquivo. Verifique o console.");
        }
    };

    const handleUpdateFeature = async (e: React.FormEvent<HTMLFormElement>, featureId: number | undefined, id_versao: number) => {
        e.preventDefault(); // Evita que a página recarregue
        
        if (!featureId) return;

        // Captura todos os inputs de dentro do UserForm disparado
        const formData = new FormData(e.currentTarget);
        
        const updatedFeature: Feature = {
            id: featureId,
            name: formData.get("name") as string,
            dataType: formData.get("dataType") as string,
            description: formData.get("description") as string,
            versionId: id_versao
        };

        try {
            await updateFeature(featureId, updatedFeature);
            alert("Feature atualizada com sucesso!");
        } catch (error) {
            console.error("Erro ao atualizar a feature", error);
            alert("Não foi possível atualizar a feature. Tente novamente.");
        }
    };

    const handleCreateFeature = async () => {
        try {
            if (feature == undefined) {
                console.error("Feature vazia!");
                return;
            } 
            const featurePost: FeaturePost = {
                name: feature.name,
                dataType: feature.dataType,
                description: feature.description,
                versionId: Number(id)
            }
            const newFeature = await createFeature(featurePost);
            setListFeature((prev) => ([...(prev || []), newFeature]));
            handleCloseCreateFeatureModal();
        } catch (err: any) {
            if (err?.response) {
                console.error(err?.response.data);
            } else {
                console.error("Erro na comunicação com o servidor!")
            }
        }
    }

    return (
        <>
            <DefaultHeader />
            <Container component="main"  sx={{ display: "flex", justifyContent: "left", maxHeight: "80vh" }} maxWidth={false}>
                <SideMenu />
                <Container component="section" disableGutters sx={{ width: "85%", padding: 2, display: "flex", flexDirection: "column", gap: 1 }} maxWidth={false}>
                    <Box sx={{ display: "flex", justifyContent: "left", alignContent: "center", height: "10%" }}>
                        <Button onClick={() => navigate(-1)}><ArrowBackIosIcon></ArrowBackIosIcon></Button>
                        <Typography component={"h2"} variant="h6" sx={{ padding: "6px 8px" }}>Visualizar Versão</Typography>
                    </Box>
                    {version && version !== undefined ? (
                        <>
                            <Container component={"article"} sx={{ width: "100%", padding: 2, border: "1px solid lightgray", borderRadius: "10px", height: "40%" }}>
                                {/* Parte de cima */}
                                <ConfirmModal
                                    title= "Criar Feature"
                                    buttonText= "Criar"
                                    fetchFunc = {handleCreateFeature}
                                    data = {feature || ({} as Feature)}
                                    modalStatus = {modalStatusCreateFeature as boolean}
                                    handleClose ={handleCloseCreateFeatureModal}
                                >
                                    <TextField 
                                        name="name" 
                                        label="Nome da Feature" 
                                        defaultValue={feature?.name} 
                                        onChange={(e) => setFeature((prev) => ({...prev, name: e.target.value}) as Feature)}
                                        size="small"
                                        sx={{ flex: 1 }}
                                    />
                                    
                                    <TextField 
                                        name="dataType" 
                                        label="Tipo de Dado" 
                                        defaultValue={feature?.dataType} 
                                        onChange={(e) => setFeature((prev) => ({...prev, dataType: e.target.value}) as Feature)}
                                        size="small"
                                        sx={{ width: "150px" }} 
                                    />
                                    
                                    <TextField 
                                        name="description" 
                                        label="Descrição / Significado" 
                                        defaultValue={feature?.description}
                                        onChange={(e) => setFeature((prev) => ({...prev, description: e.target.value}) as Feature)} 
                                        size="small"
                                        sx={{ flex: 2 }}
                                    />
                                </ConfirmModal>

                                <Box sx={{ borderBottom: "1px solid lightgray", width: "100%", display: "flex", justifyContent: "space-between", alignContent: "center", gap: 2, paddingBottom: 2 }}>
                                    <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                                        <Box sx={{ display: "flex", gap: 0.5, alignContent: "center", flexDirection: "column", justifyContent: "center"}}>
                                            <Box sx={{ display: "flex", gap: 2, alignContent: "center" }}>
                                                <Typography component="h2" variant="h6" sx={{ borderRight: "1px solid black", paddingRight: 2 }}>Versão Selecionada: {version?.numVersion}</Typography>
                                                <Typography component="span" variant="h6">Versão Base: {baseVersion?.numVersion}</Typography>
                                            </Box>
                                            <Box sx={{ display: "flex", gap: 2, alignItens: "center"}}>
                                                <Typography component="h2" variant="h6" sx={{ fontSize: "14px"}}>Dataset: {dataset?.name}</Typography>
                                            </Box>
                                        </Box>
                                    </Box>
                                    <Button variant="contained" onClick={handleOpenCreateFeatureModal}>
                                        Criar Nova Feature
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
                                        <Typography>
                                            {version?.createdDate 
                                            ? format(new Date(version?.createdDate), "dd/MM/yyyy") 
                                            : "Data indisponível"}
                                        </Typography>
                                    </Box>
                                    <Box>
                                        <Typography>Tamanho da Versão</Typography>
                                        <Typography>{version?.size ? (version?.size / 1024).toFixed(2) + " MB" : "Não determinado!"}</Typography>
                                    </Box>
                                    <Box>
                                        <Typography>Download</Typography>
                                        <Button onClick={() => handleDownload(version.id, version.numVersion as string)}><DownloadIcon /></Button>
                                    </Box>
                                </Box>
                            </Container>
                            <Container component={"article"} disableGutters sx={{ width: "100%", padding: 1.5, border: "1px solid lightgray", borderRadius: "10px", minHeight: 0, flex: 1, overflow: "hidden" }} maxWidth={false}>
                                <Typography component="h3" variant="h5">
                                    Schema de Dados (Features)
                                </Typography>

                                {listFeature && listFeature.length > 0 ? (
                                    <Box sx={{ overflow: 'auto', height: "100%" }}>
                                        {listFeature.map((feat) => (
                                            <Box 
                                                key={feat.id} 
                                                component="form" 
                                                onSubmit={(e) => handleUpdateFeature(e, feat.id, feat.versionId)}
                                                sx={{ 
                                                    display: "flex", 
                                                    flexDirection: "row", // Coloca tudo na mesma linha
                                                    alignItems: "center", // Centraliza verticalmente
                                                    gap: 2, // Espaço entre os campos
                                                    padding: 2, 
                                                    borderBottom: "1px solid #e0e0e0", // Apenas uma linha divisória embaixo, em vez de uma caixa fechada
                                                    "&:hover": { backgroundColor: "#f9f9f9" } // Efeito visual sutil ao passar o mouse
                                                }}
                                            >
                                                <TextField 
                                                    name="name" 
                                                    label="Nome da Feature" 
                                                    defaultValue={feat.name} 
                                                    size="small"
                                                    sx={{ flex: 1 }} // Ocupa uma parte do espaço
                                                />
                                                
                                                <TextField 
                                                    name="dataType" 
                                                    label="Tipo de Dado" 
                                                    defaultValue={feat.dataType} 
                                                    size="small"
                                                    sx={{ width: "150px" }} // Tamanho fixo menor, já que o tipo (TEXT, INT) é curto
                                                />
                                                
                                                <TextField 
                                                    name="description" 
                                                    label="Descrição / Significado" 
                                                    defaultValue={feat.description} 
                                                    size="small"
                                                    sx={{ flex: 2 }}
                                                />
                                                
                                                <Button 
                                                    type="submit" 
                                                    variant="contained" 
                                                    size="small" 
                                                    sx={{ whiteSpace: "nowrap" }}
                                                >
                                                    Atualizar
                                                </Button>
                                            </Box>
                                        ))}
                                    </Box>
                                ) : (
                                    <Typography color="text.secondary">Nenhuma feature extraída ou cadastrada para esta versão.</Typography>
                                )}
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