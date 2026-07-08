import { Container, ListItem, ListItemButton, ListItemIcon, ListItemText } from "@mui/material";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import SpaceDashboardIcon from '@mui/icons-material/SpaceDashboard';
import AssessmentIcon from '@mui/icons-material/Assessment';
import ManageAccountsIcon from '@mui/icons-material/ManageAccounts';
import LogoutIcon from '@mui/icons-material/Logout';

export function SideMenu() {
    const { logout } = useAuth();
    const navigate = useNavigate();

    return (
        <Container component="aside" disableGutters sx={{ width: "15%", paddingTop: 1, borderRight: "1px solid lightgray" }} maxWidth={false}>
            <ListItem disablePadding sx={{ ":hover": {backgroundColor: "lightgray"} }}>
                <ListItemButton onClick={() => navigate("/dashboard")}>
                    <ListItemIcon>
                        <SpaceDashboardIcon color="primary"/>
                    </ListItemIcon>
                    <ListItemText>
                        Dashboard
                    </ListItemText>
                </ListItemButton>
            </ListItem>
            <ListItem disablePadding sx={{ ":hover": {backgroundColor: "lightgray"} }}>
                <ListItemButton onClick={() => navigate("/graphs")}>
                    <ListItemIcon>
                        <AssessmentIcon color="primary" />
                    </ListItemIcon>
                    <ListItemText>
                        Relatórios
                    </ListItemText>
                </ListItemButton>
            </ListItem>
            <ListItem disablePadding sx={{ ":hover": {backgroundColor: "lightgray"} }}>
                <ListItemButton onClick={() => navigate("/perfil")}>
                    <ListItemIcon>
                        <ManageAccountsIcon color="primary" />
                    </ListItemIcon>
                    <ListItemText>
                        Conta
                    </ListItemText>
                </ListItemButton>
            </ListItem>
            <ListItem disablePadding sx={{ ":hover": {backgroundColor: "lightgray"} }}>
                <ListItemButton onClick={logout}>
                    <ListItemIcon>
                        <LogoutIcon color="error"/>
                    </ListItemIcon>
                    <ListItemText>
                        Sair
                    </ListItemText>
                </ListItemButton>
            </ListItem>
        </Container>
    )
}