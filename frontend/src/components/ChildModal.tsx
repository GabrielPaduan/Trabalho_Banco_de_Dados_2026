import { Box, Button, Modal, Typography } from "@mui/material";
import React from "react";

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
};

interface ChildModalProps {
    createFunc: () => Promise<void>,
    children: React.ReactNode
}

export function ChildModal({ createFunc, children } : ChildModalProps) {
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  const handleSubmit = async () => {
        try {
            await createFunc();      
            setOpen(false); 
        } catch (error) {
            console.error("Erro na execução da função externa:", error);
        }
    };

  return (
    <React.Fragment>
      <Button onClick={handleOpen}>Adicionar Fonte de Dados</Button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="child-modal-title"
        aria-describedby="child-modal-description"
      >
        <Box sx={{ ...style, width: 300, display: "flex", gap: 2, flexDirection: "column" }}>
            <Typography id="child-modal-title" component={"h2"} variant="h6">Preencha os Campos</Typography>
            {children}
            <Box sx={{ display: "flex", justifyContent: "space-between" }}>
                <Button variant="contained" onClick={handleSubmit}>Adicionar</Button>
                <Button variant="contained" onClick={handleClose}>Fechar</Button>
            </Box>
        </Box>
      </Modal>
    </React.Fragment>
  );
}