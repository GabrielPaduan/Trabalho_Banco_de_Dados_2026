import { Box, Container, Tab, Typography } from "@mui/material";
import { SideMenu } from "../../components/SideMenu";
import { DefaultHeader } from "../../components/DefaultHeader";
import GenericBarChart from "../../components/GenericBarChart";
import { useEffect, useState } from "react";
import { type GenericGraphDataDatasetRank, type AccessLog, type GenericGraphData } from "../../util/DTO";
import { useAuth } from "../../hooks/useAuth";
import { getAccessLogByDatasetId } from "../../services/accessLogService";
import { useParams } from "react-router-dom";
import { previousDay } from "date-fns";
import { TabContext, TabList, TabPanel } from "@mui/lab";

export function GraphsPage() {
    const [graphsData, setGraphsData] = useState<GenericGraphData[]>([]);
    const [months, setMonths] = useState<String[]>([
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"    
    ])
    const [graphsDataDownload, setGraphsDataDownload] = useState<GenericGraphDataDatasetRank[]>([])
    const [downloadsPerMonth, setDownloadsPerMonth] = useState<number[]>([]);
    const [acccessPerMonth, setAccessPerMonth] = useState<number[]>([]);
    const [accessLog, setAccessLog] = useState<AccessLog[]>([])
    const { loggedUser } = useAuth();
    const [pageValue, setPageValue] = useState("1");

    function handleChangeTab(event: React.SyntheticEvent, newValue: string) {
        setPageValue(newValue);
    }

    useEffect(() => {
        const fetchData = async () => {
            if (loggedUser?.sub !== undefined) {
                const accessLogData = await getAccessLogByDatasetId(loggedUser?.sub);
                setAccessLog(accessLogData);
            }
        }
        fetchData();
    }, [loggedUser])

    useEffect(() => {
        fillGraphsData();
        fillGraphsDataRanking();
    }, [accessLog]);

    const fillGraphsDataRanking = () => {
        if (!accessLog || accessLog.length === 0) return;

        let downloadsDataset = [];
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
                        <TabPanel value={"1"} sx={{ height: "100%"}}>
                            <GenericBarChart
                                data={graphsData}
                                graphType={0}
                            />
                        </TabPanel>
                        <TabPanel value={"2"}  sx={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                            <GenericBarChart
                                data={graphsData}
                                graphType={1}
                            />
                        </TabPanel>
                        <TabPanel value={"3"}  sx={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                            
                        </TabPanel>
                        <TabPanel value={"4"}  sx={{ flex: 1, display: "flex", flexDirection: "column", minHeight: 0, p: 0, pt: 2 }} >
                            
                        </TabPanel>
                    </TabContext>
                </Container>
            </Container>
        </>
    )
}