import { Alert, Box, Button, Typography } from "@mui/material";

interface FormularioUserProps {
    title: string,
    buttonText: string,
    error?: string,
    onSubmit: (e: React.FormEvent<HTMLFormElement>) => void,
    children: React.ReactNode;
}

export default function UserForm({
    title, buttonText, error, onSubmit, children
}: FormularioUserProps) {
    return (
        <Box sx={{width:"70%", display: "flex", flexDirection: "column", alignItems: "center" }}>
            <Typography component="h1" variant="h5">
                {title}
            </Typography>

            {error && (
                <Alert severity="error" sx={{width: '100%'}}>
                    {error}
                </Alert>
            )}

            <Box component="form" onSubmit={onSubmit} sx={{ width: '100%', display: "flex", flexDirection: "column", alignItems: "center", gap: 1}}>
                {children}

                <Button type="submit" variant="contained">
                    {buttonText}
                </Button>
            </Box>
        </Box>
    )
}