import { Box, Button, Container, TextField, Typography } from "@mui/material";
import { useAuth } from "../hooks/useAuth";
import { DatasetCard } from "./DatasetCard";
import { useEffect, useState } from "react";
import { type DataFontPost, type DataFont, type Dataset, type DatasetPost } from "../util/DTO";
import { createDataset, getDatasets, deleteDatasetById } from "../services/datasetService";
import { ConfirmModal } from "./Modal";
import { getUserByEmail } from "../services/userService";
import { useNavigate } from "react-router-dom";
import { createDataFont, getDataFonts } from "../services/dataFontService";
import MultipleSelectChip from "./MultipleSelectChip";
import { createRelationDataFontDataset } from "../services/dataFontDatasetService";
import { ChildModal } from "./ChildModal";

export function DatasetCards() {
    const [datasets, setDatasets] = useState<Dataset[]>();
    const [dataset, setDataset] = useState<DatasetPost>({
        name: "",
        description: "",
        userCPF: "",
    });
    const { loggedUser } = useAuth();
    const [ createModalStatus, setCreateModalStatus ] = useState<boolean>(false);
    const [ deleteModalStatus, setDeleteModalStatus ] = useState<boolean>(false);
    const [selectedId, setSelectedId] = useState<number>();
    const [dataFont, setDataFont] = useState<DataFont[]>([]);
    const [selectedFontsIds, setSelectedFontsIds] = useState<number[]>([]);
    const [fontDataName, setFontDataName] = useState<DataFontPost>();

    const navigate = useNavigate();

    const handleNavigate = (id: number) => {
        navigate(`/dataset/${id.toString()}`);
    }

    const handleOpenCreateModal = () => {
        setCreateModalStatus(true);
    }

    const handleCloseCreateModal = () => {
        setCreateModalStatus(false);
    }

    const handleOpenDeleteModal = (id: number) => {
        setDeleteModalStatus(true);
        setSelectedId(id)
    }

    const handleCloseDeleteModal = () => {
        setDeleteModalStatus(false);
    }

    useEffect(() => {
        const fetchDatasets = async() => {
            if (!loggedUser) {
                return;
            }

            try {
                if(loggedUser?.sub !== undefined) {
                    const [responseDataset, responseUser, responseDataFont] = await Promise.all([
                        getDatasets(loggedUser?.sub),
                        getUserByEmail(loggedUser?.sub),
                        getDataFonts()
                    ]) 
                    if (responseDataset !== undefined){ 
                        setDatasets(responseDataset);
                    }

                    if (responseUser?.cpf !== undefined) {
                        setDataset(prev => ({...prev, userCPF: responseUser.cpf}));
                    }

                    if (responseDataFont !== undefined) {
                        setDataFont(responseDataFont);
                    }
                }
            } catch (err: any) {
                if (err.response && err.response.data) {
                    alert("Erro" + err.response.data);
                }
            }
        }
        fetchDatasets();
    }, [loggedUser?.sub])

    const createNewDataset = async (data: DatasetPost) => {
        try {
            if (data !== null) {
                const newDataset = await createDataset(data);
                setDatasets(datasets => [...(datasets || []), newDataset]);
                
                await Promise.all(selectedFontsIds.map((fontId) => {
                        const payload = ({datasetId: newDataset.id, dataFontId: fontId});
                        return createRelationDataFontDataset(payload);  
                    })
                )
                handleCloseCreateModal();
                setDataset({ name: "", description: "", userCPF: data.userCPF });
                setSelectedFontsIds([]);
            } 
        } catch (err: any) {
            if (err.response?.data) {
                console.log(err.response.data)
            } else {
                console.log("Erro na comunicação com o servidor!")
            }
        }
    };

    const deleteDataset = async (id: number) => {
        try {
            await deleteDatasetById(id);
            handleCloseDeleteModal();
            const newList = datasets?.filter(dataset => dataset.id !== id);
            setDatasets(newList);
        } catch (err: any) {
            if (err.response && err.response.data) {
                console.log(err.response.data);
            } else {
                console.log("Erro na comunicação com o servidor!")
            }
        }
    }

    const handleCreateNewDataFont = async () => {
        try {
            const fontDataResponse = await createDataFont(fontDataName as DataFontPost);
            setDataFont((prev) => [...prev, fontDataResponse]);
            setFontDataName({name: ""});
        } catch (err: any) {
            if (err?.response) {
                console.log("Erro: ", err?.response.data);
            } else {
                console.log("Erro interno no servidor!");
            }
        }
    };

    return (
        <Container component="section" sx={{ display: "flex", flexDirection: "column", gap: 3}}>
            <ConfirmModal
                title= "Cadastrar Dataset"
                buttonText= "Criar"
                fetchFunc = {createNewDataset}
                data = {dataset ? dataset : null}
                modalStatus = {createModalStatus}
                handleClose = {handleCloseCreateModal}
            >
                <TextField label="Titulo: " value={dataset?.name}onChange={(e) => setDataset(prevDataset => ({...prevDataset, name: e.target.value}))} fullWidth />
                <TextField label="Descrição: " value={dataset?.description} onChange={(e) => setDataset(prevDataset => ({...prevDataset, description: e.target.value}))} fullWidth />
                <MultipleSelectChip 
                    dataFont={dataFont ?? []}
                    selectedFontsIds={selectedFontsIds}
                    onChange={setSelectedFontsIds}
                />
                <ChildModal
                    createFunc={handleCreateNewDataFont}
                >
                    <TextField
                        label="Fonte de Dado"
                        value={fontDataName?.name ?? ""}
                        onChange={(e) => setFontDataName((prev) => ({ ...(prev ?? {}), name: e.target.value }))}
                        fullWidth
                    />
                </ChildModal>
            </ConfirmModal>

            <ConfirmModal
                title= "Excluir Dataset"
                buttonText= "Deletar"
                fetchFunc = {deleteDataset}
                data = {selectedId ? selectedId : null}
                modalStatus = {deleteModalStatus}
                handleClose = {handleCloseDeleteModal}
            >
                <Typography>Você realmente deseja excluir esse dataset?</Typography>
            </ConfirmModal>
            
            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
                    <Typography component="h1" variant="h2" sx={{ fontSize: "26px" }}>Datasets</Typography>
                    <Typography component="h2" variant="h6" sx={{ fontSize: "16px" }}>Catálogo de datasets criados por você!</Typography>
                </Box>
                <Box>
                    <Button variant="contained" color="primary" onClick={handleOpenCreateModal}>Novo Dataset</Button>
                </Box>
            </Box>
            <Box sx={{ display: "flex", gap: 2,  flexWrap: "wrap"}}>
                {
                    datasets && datasets.length > 0 ? (
                        datasets.map((dataset) => (
                            <DatasetCard 
                                title={dataset.name}
                                desc={dataset.description}
                                createdDate={dataset.createdDate.toString()}
                                active={dataset.active}
                                deleteModal={() => handleOpenDeleteModal(dataset.id)}
                                key={dataset.id}
                                navigate={() => handleNavigate(dataset.id)}
                            />
                        )) 
                    ) : ( 
                        <Typography>Que tal começar criando o seu primeiro dataset?</Typography>
                    )
                }
            </Box>
        </Container>
    )
}