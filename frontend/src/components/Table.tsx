import { Typography } from "@mui/material"

interface TableProps {
    title: string
}

export function Table({title} : TableProps) {
    return (
        <Typography component={"h2"} variant="h6">
            {title}
        </Typography>
    )
}