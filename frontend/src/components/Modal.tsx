import * as React from 'react';
import Backdrop from '@mui/material/Backdrop';
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import Fade from '@mui/material/Fade';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

interface ModalProps<T> {
    title: string;
    buttonText: string;
    fetchFunc: (data: T) => void;
    data: T | null;
    modalStatus: boolean;
    handleClose: () => void;
    children: React.ReactNode
}

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
  display: "flex",
  justifyContent: "center",
  AlignItems: "center",
  flexDirection: "column",
  gap: 2
};      

export function ConfirmModal<T>({title, buttonText, fetchFunc, data, modalStatus, handleClose, children} : ModalProps<T>) {
    return (
        <Modal
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            open={modalStatus}
            onClose={handleClose}
            closeAfterTransition
            slots={{ backdrop: Backdrop }}
            slotProps={{
            backdrop: {
                timeout: 500,
            },
            }}
        >
            <Fade in={modalStatus}>
            <Box sx={style}>
                <Typography id="transition-modal-title" variant="h6" component="h2">
                    {title}
                </Typography>
                {/* Textfields  */}
                <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                    { children }
                </Box>
                {/* Action Buttons */}
                <Box sx={{ display: "flex", justifyContent: "right", gap: 2 }}>
                    <Button variant='contained' onClick={handleClose}>
                        Fechar
                    </Button>
                    <Button variant='contained' onClick={data ? () => fetchFunc(data) : () => console.log("Dado atribuído inválido!")}>
                        { buttonText }
                    </Button>
                </Box>
            </Box>
        </Fade>
        </Modal>
    )
}