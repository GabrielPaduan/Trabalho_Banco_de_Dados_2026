import { Box, Button, Card, CardActions, CardContent, Typography } from "@mui/material"
import DeleteIcon from '@mui/icons-material/Delete';
import { format } from "date-fns"


interface DatasetCardsProps {
    title: string,
    desc: string,
    createdDate: string,
    deleteModal: () => void,
    active: boolean,
    navigate: () => void,
    minWidthPersonal: string
}

export function DatasetCard({title, desc, createdDate, active, deleteModal, navigate, minWidthPersonal} : DatasetCardsProps) {
    return (
        <Card sx={{ minWidth: {minWidthPersonal},  ":hover": {boxShadow: "3px 3px 3px lightgray, -3px 3px 3px lightgray"} }}>
            <CardContent sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <Typography sx={{ fontSize: "16px" }}>{title}</Typography>
                    {active &&
                        <Typography sx={{ fontSize: "12px", backgroundColor: "#B3FFF3", borderRadius: "10%", padding: "5px 10px" }}>Ativo</Typography>
                    }
                </Box>
                <Box>
                    <Typography sx={{ fontSize: "12px" }}>
                        {desc}
                    </Typography>
                </Box>
            </CardContent>
            <CardActions sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <Typography>{format(createdDate, "dd/MM/yyyy")}</Typography>
                {/* Colocar ícone */}
                <Box sx={{ display: "flex" }}>
                    <Button sx={{ fontSize: "12px", ":hover": {backgroundColor: "lightgray"} }} onClick={navigate}>Acesse</Button>
                    <Button sx={{ fontSize: "12px", minWidth: "auto", ":hover": {backgroundColor: "lightgray"} }} onClick={deleteModal}><DeleteIcon sx={{ color: "error" }}/></Button>
                </Box>
            </CardActions>
        </Card>
    )
}