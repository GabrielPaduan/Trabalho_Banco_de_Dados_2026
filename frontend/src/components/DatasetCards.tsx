import { Box, Button, CircularProgress, Container, TextField, Typography } from "@mui/material";
import { useAuth } from "../hooks/useAuth";
import { DatasetCard } from "./DatasetCard";
import { useEffect, useState } from "react";
import type { Dataset, DatasetPost } from "../util/DTO";
import { createDataset, getDatasets, deleteDatasetById } from "../services/datasetService";
import { ConfirmModal } from "./Modal";
import { getUserByEmail } from "../services/userService";

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
                    const [responseDataset, responseUser] = await Promise.all([
                        getDatasets(loggedUser?.sub),
                        getUserByEmail(loggedUser?.sub)
                    ]) 
                    if (responseDataset !== undefined){ 
                        setDatasets(responseDataset);
                    }

                    if (responseUser?.cpf !== undefined) {
                        setDataset(prev => ({...prev, userCPF: responseUser.cpf}));
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
                console.log(newDataset);
                setDatasets(datasets => [...(datasets || []), newDataset]);
                handleCloseCreateModal();
                setDataset({ name: "", description: "", userCPF: data.userCPF });
            } 
        } catch (err: any) {
            if (err.response.data && err.response) {
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