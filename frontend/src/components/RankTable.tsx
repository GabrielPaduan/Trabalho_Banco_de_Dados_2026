import Table from '@mui/material/Table';
import TableContainer from '@mui/material/TableContainer';
import Paper from '@mui/material/Paper';

interface GenericTableProps {
  children: React.ReactNode
}

export default function VersionTable(
  {children} : GenericTableProps
) {

  return (
    <TableContainer component={Paper} sx={{ overflowY: "auto", height: "100%" }}>
      <Table sx={{ minWidth: 650, height: "100%" }} aria-label="simple table">
        {children}
      </Table>
    </TableContainer>
  );
}
