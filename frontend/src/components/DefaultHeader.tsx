import { Box, Container, Icon, Typography } from "@mui/material";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

export function DefaultHeader() {
  const { loggedUser } = useAuth();
  const navigate = useNavigate();

  return (
    <Container component={"header"} disableGutters sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", px: 3, width: "100%", height: "20vh", borderBottom: "1px solid lightgray" }} maxWidth={false}>
      <Box>
        <Typography component={"h1"} variant="h2" sx={{ fontSize: "30px" }}>Feature Store</Typography>
      </Box>
      <Box
          sx={{ backgroundColor: 'background.paper', borderRadius: "50%", '@media (max-width: 800px)': { padding: 0.5, gap: 0 }, display: "flex", justifyContent: "center", alignItems: "center", gap: 1, flexDirection: "column" }} 
      >
        <Box onClick={() => navigate("/perfil")} sx={{ cursor: "pointer", display: "flex", flexDirection: "column", alignItems: "center" }}>
          <AccountCircleIcon sx={{ fontSize: "56px"}}/>
          <Typography variant="body2" color="text.primary" sx={{ '@media ( min-width: 320px) and (max-width: 800px)': { display: 'none' } }}>
              Olá, {loggedUser?.name}!
          </Typography>
        </Box>
      </Box>
    </Container>
  );
}