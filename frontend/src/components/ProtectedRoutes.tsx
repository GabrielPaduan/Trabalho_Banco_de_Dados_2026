import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { Box, CircularProgress } from "@mui/material";

export function ProtectedRoutes() {
    const { authenticated, loading } = useAuth();

    if (loading) {
        return <Box><CircularProgress /></Box>
    }

    return authenticated ? <Outlet /> : <Navigate to="/login" replace />  
}