import { Box, Button, Collapse, Container, Icon, Typography } from "@mui/material";
import React from "react";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
// import PersonIcon from '@mui/icons-material/Person';

export function DefaultHeader() {
    const [openMenuLogin, setOpenMenuLogin] = React.useState(false);
    const { logout, loggedUser } = useAuth();
    const navigate = useNavigate();
    
    function handleMenuLogin() {
        if (openMenuLogin){
            setOpenMenuLogin(false);
        } else {
            setOpenMenuLogin(true);
        }
    }

    function handleLogout() {
      logout();
    }

   return (
      <Container component={"header"} sx={{ display: "flex", justifyContent: "center", alignItems: "center" }} >
        <Box
            sx={{ backgroundColor: 'background.paper', borderRadius: "50%", cursor: 'pointer', ":hover": { boxShadow: 3 }, transition: 'all 1s ease', ...(openMenuLogin && {
              borderTopLeftRadius: "5%", borderTopRightRadius: "5%", borderBottomLeftRadius: "5%", borderBottomRightRadius: "5%", boxShadow: 3
            }), '@media (max-width: 800px)': { padding: 0.5, gap: 0 }, display: "flex", justifyContent: "center", alignItems: "center", gap: 1, flexDirection: "column" }} 
            onClick={handleMenuLogin}
            >
              <Box>
                <Icon sx={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column", padding: 1, fontSize: 70, color: 'text.primary', '@media (max-width: 800px)': { padding: "0px", fontSize: "50px" } }}>person</Icon>
                <Typography variant="body2" color="text.primary" sx={{ '@media ( min-width: 320px) and (max-width: 800px)': { display: 'none' } }}>
                    Olá, {loggedUser?.name}!
                </Typography>
              </Box>
              <Collapse in={openMenuLogin} timeout={900} sx={{ width: '100%', '@media ( min-width: 320px) and (max-width: 800px)': { width: '80%' } }}>
                <Box sx={{ display: "flex", justifyContent: "center" }}>
                  <Button
                    variant="contained"
                    color="primary"
                    sx={{ padding: '5px', '@media ( min-width: 320px) and (max-width: 800px)': { paddingRight: 1 } }}
                    onClick={(e) => {
                            e.stopPropagation();
                            navigate("/editarUsuario");
                        }
                    } 
                  >
                    <Typography color="text.secondary" sx={{ '@media ( min-width: 320px) and (max-width: 800px)': { fontSize: '0.8rem', paddingRight: 0 } }}>
                      Editar
                    </Typography>
                  </Button>
                </Box>
                <Box sx={{ display: "flex", justifyContent: "center" }}>
                  <Button
                    variant="contained"
                    color="error"
                    sx={{ padding: '5px', '@media ( min-width: 320px) and (max-width: 800px)': { paddingRight: 1 } }}
                    onClick={(e) => {
                            e.stopPropagation();
                            handleLogout();
                        }
                    } 
                  >
                    <Typography color="text.secondary" sx={{ '@media ( min-width: 320px) and (max-width: 800px)': { fontSize: '0.8rem', paddingRight: 0 } }}>
                      Sair
                    </Typography>
                  </Button>
                </Box>
                
              </Collapse>
          
        </Box>
      </Container>
  );
}