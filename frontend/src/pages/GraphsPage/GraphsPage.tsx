import { Box, Button, Container, Tab, TableBody, TableCell, TableHead, TableRow, Typography } from "@mui/material";
import { SideMenu } from "../../components/SideMenu";
import { DefaultHeader } from "../../components/DefaultHeader";
import GenericBarChart from "../../components/GenericBarChart";
import { useEffect, useState } from "react";
import { type AccessLog, type GenericGraphData, type Dataset, type GenericGraphDataDatasetRank } from "../../util/DTO";
import { useAuth } from "../../hooks/useAuth";
import { getAccessLogByDatasetId } from "../../services/accessLogService";
import { TabContext, TabList, TabPanel } from "@mui/lab";
import { deleteDatasetById, getDatasets } from "../../services/datasetService";
import RankTable from "../../components/RankTable";
import { getVersionByDatasetId } from "../../services/versionsService";
import DeleteIcon from '@mui/icons-material/Delete';
import { ConfirmModal } from "../../components/Modal";

export function GraphsPage() {
    const [graphsData, setGraphsData] = useState<GenericGraphData[]>([]);
    const [graphsDataRank, setGraphsDataRank] = useState<GenericGraphDataDatasetRank[]>([]);
    const [months, setMonths] = useState<String[]>([
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"    
    ])
    const [accessLog, setAccessLog] = useState<AccessLog[]>([])
    const { loggedUser } = useAuth();
    const [pageValue, setPageValue] = useState("1");
    const [dataset, setDataset] = useState<Dataset[]>([]);
    const [datasetDataGraph, setDatasetDataGraph] = useState<{id: number, datasetName: string, datasetOwner: string, children: number, lastLog: Date}[]>([])
    const [ deleteModalStatus, setDeleteModalStatus ] = useState<boolean>(false);
    const [ selectedId, setSelectedId] = useState<number>();

    const handleOpenDeleteModal = (id: number) => {
        setDeleteModalStatus(true);
        setSelectedId(id)
    }

    const handleCloseDeleteModal = () => {
        setDeleteModalStatus(false);
    }

    function handleChangeTab(event: React.SyntheticEvent, newValue: string) {
        setPageValue(newValue);
    }

    const deleteDataset = async (id: number) => {
        try {
            await deleteDatasetById(id);
            handleCloseDeleteModal();
            const newList = datasetDataGraph?.filter(dataset => dataset.id !== id);
            setDatasetDataGraph(newList);
        } catch (err: any) {
            if (err.response && err.response.data) {
                console.log(err.response.data);
            } else {
                console.log("Erro na comunicação com o servidor!")
            }
        }
    }

    useEffect(() => {
        const fetchData = async () => {
            if (loggedUser?.sub !== undefined) {
                const accessLogData = await getAccessLogByDatasetId(loggedUser?.sub);
                setAccessLog(accessLogData);
                const datasetData = await getDatasets(loggedUser?.sub);
                setDataset(datasetData);
                let datasetChildren = []
                let newDatasetDataGraph = [];
                let lastLogList = [];
                let sortedLogs = accessLogData.sort((a, b) => new Date(b.dateTime).getTime() - new Date(a.dateTime).getTime());
                for (let i = 0; i < datasetData.length; i++) {
                    datasetChildren.push((await getVersionByDatasetId(datasetData[i].id.toString())).length)
                    lastLogList.push(sortedLogs.filter((log) => log.datasetID == datasetData[i].id)[0].dateTime);
                    
                    newDatasetDataGraph.push({id: datasetData[i].id, datasetName: datasetData[i].name, datasetOwner: loggedUser?.name, children: datasetChildren[i], lastLog: lastLogList[i]})
                }
                console.log(newDatasetDataGraph);
                setDatasetDataGraph(newDatasetDataGraph);
            }
        }
        fetchData();
    }, [loggedUser])

    useEffect(() => {
        fillGraphsData();
        fillGraphsDataRanking();
    }, [accessLog, dataset]);

    const fillGraphsDataRanking = () => {
        if ((!accessLog || accessLog.length === 0) && (!dataset || dataset.length === 0)) return;

        let downloadsDataset = [];
        let listDatasetId = [];
        let countDatasetDownload = [];
        let listDatasetName = [];
        listDatasetId = accessLog.map((log) => log.datasetID);
        listDatasetId = removeDuplicatedArray(listDatasetId);
        for (let i = 0; i < listDatasetId.length; i++) {
            countDatasetDownload.push(accessLog.filter((log) => log.datasetID == listDatasetId[i] && log.operationType == 0).length);
            listDatasetName.push(dataset.filter((dataset) => dataset.id == listDatasetId[i])[0]?.name);
            downloadsDataset.push({ datasetName: listDatasetName[i], countDatasetDownload: countDatasetDownload[i] });
        }         
        
        let newGraphDataRank = [];
        for (let i = 0; i < downloadsDataset.length; i++) {
            newGraphDataRank.push({
                name: downloadsDataset[i].datasetName,
                downloads: downloadsDataset[i].countDatasetDownload
            })
        }
        setGraphsDataRank(newGraphDataRank);
    }

    const removeDuplicatedArray = (array: number[]) => {
        let exitArray: number[] = [];
        for (let i = 0; i < array.length; i++) {
            if (!exitArray.includes(array[i])) {
                exitArray.push(array[i]);
            } 
        }
        return exitArray;
    }

    const fillGraphsData = () => {
        if (!accessLog || accessLog.length === 0) return;

        let downloadsMonth = [];
        let accessMonth = [];
        for (let i = 1; i <= 12; i++) {
            let logs = accessLog.filter((log) => (new Date(log.dateTime).getMonth() + 1) == i);
            downloadsMonth.push(logs.filter((log) => log.operationType == 0).length);
            accessMonth.push(logs.filter((log) => log.operationType == 2).length);
        }

        let graphsDataArray = [];
        for (let i = 0; i < 12; i++) {
            graphsDataArray.push((
                {
                    name: months[i],
                    access: accessMonth[i], 
                    downloads: downloadsMonth[i]
                }
            ));
        }  

        setGraphsData(graphsDataArray);
    }

    return (
        <>
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
            
            <DefaultHeader />
            <Container component={"main"} sx={{ display: "flex", justifyContent: "left", height: "80vh" }} maxWidth={false} >
                <SideMenu />
                <Container component="section" disableGutters sx={{ width: "85%", padding: 2, paddingLeft: 4, display: "flex", flexDirection: "column", gap: 1 }} maxWidth={false}>
                    <Typography component={"h2"} variant="h6" sx={{ fontSize: "26px"}}>Relatórios Gráficos</Typography>
                    <TabContext value={pageValue}>
                        <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
                            <TabList
                                onChange={handleChangeTab}                 
                            >
                                <Tab label="Análise Acessos e Downloads" value="1" />
                                <Tab label="Ranking de Datasets" value="2" />
                                <Tab label="Datasets Com Mais Versões" value="3" />
                                <Tab label="Datasets Menos Visitados" value="4" />
                            </TabList>
                        </Box>
                        <TabPanel value={"1"} sx={{ height: "100%", p: 0, pt: 2}}>
                            <GenericBarChart
                                data={graphsData}
                                graphType={0}
                            />
                        </TabPanel>
                        <TabPanel value={"2"}  sx={{ height: "100%", display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                            <GenericBarChart
                                data={[...graphsDataRank].sort((a, b) => b.downloads - a.downloads).slice(0, 6)}
                                graphType={1}
                            />
                        </TabPanel>
                        <TabPanel value={"3"}  sx={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                            <RankTable>
                                <TableHead>
                                    <TableRow>
                                        <TableCell align='center'>Nome Dataset</TableCell>
                                        <TableCell align="center">Nome Autor</TableCell>
                                        <TableCell align="center">Quantidade Versões Filhas</TableCell>
                                    </TableRow>
                                    </TableHead>
                                    <TableBody>
                                    { [...datasetDataGraph].sort((a, b) => b.children - a.children).map((data) => (
                                        <TableRow
                                        key={data.id}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                        >
                                            <TableCell component="th" scope="row" align='center'>
                                                {data.datasetName}
                                            </TableCell>
                                            <TableCell component="th" scope="row" align='center'>
                                                {data.datasetOwner}
                                            </TableCell>
                                            <TableCell component="th" scope="row" align='center'>
                                                {data.children}
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                    </TableBody>
                            </RankTable>
                        </TabPanel>
                        <TabPanel value={"4"}  sx={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                            <RankTable>
                                <TableHead>
                                    <TableRow>
                                        <TableCell align='center'>Nome Dataset</TableCell>
                                        <TableCell align="center">Última Interação</TableCell>
                                        <TableCell align="center">Ação</TableCell>
                                    </TableRow>
                                    </TableHead>
                                    <TableBody>
                                    { [...datasetDataGraph].sort((a, b) => new Date(b.lastLog).getTime() - new Date(a.lastLog).getTime()).map((data) => (
                                        <TableRow
                                        key={data.id}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                        >
                                            <TableCell component="th" scope="row" align='center'>
                                                {data.datasetName}
                                            </TableCell>
                                            <TableCell component="th" scope="row" align='center'>
                                                {new Intl.DateTimeFormat('pt-BR', {
                                                    day: '2-digit',
                                                    month: 'long',
                                                    year: 'numeric',
                                                    hour: '2-digit',
                                                    minute: '2-digit'
                                                    }).format(new Date(data.lastLog))
                                                }
                                            </TableCell>
                                            <TableCell component="th" scope="row" align='center'>
                                                <Button onClick={() => handleOpenDeleteModal(data.id)}><DeleteIcon sx={{ color: "error" }}/></Button>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                    </TableBody>
                            </RankTable>
                        </TabPanel>
                    </TabContext>
                </Container>
            </Container>
        </>
    )
}