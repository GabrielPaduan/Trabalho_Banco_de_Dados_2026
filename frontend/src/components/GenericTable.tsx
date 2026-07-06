import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import type { Version } from '../util/DTO';
import { format } from 'date-fns';
import DeleteIcon from '@mui/icons-material/Delete';
import DownloadIcon from '@mui/icons-material/Download';
import { Box, Button } from '@mui/material';
import { downloadVersionFile } from '../services/versionsService';
import { useNavigate } from 'react-router-dom';

interface GenericTableProps {
  listVersion: Version[]
}

export default function GenericTable(
  {listVersion} : GenericTableProps
) {
  const navigate = useNavigate();

  const handleDownload = async (versionId: number, numVersion: String) => {
    try {
          const blob = await downloadVersionFile(versionId);

          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          
          link.setAttribute('download', `versao_${numVersion}.csv`); 
          
          document.body.appendChild(link);
          link.click();
          
          link.remove();
          window.URL.revokeObjectURL(url);

      } catch (error) {
          console.error("Erro ao baixar o arquivo", error);
          alert("Não foi possível baixar o arquivo. Verifique o console.");
      }
  };

  return (
    <TableContainer component={Paper} sx={{ overflowY: "auto", height: "95%" }}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell align='center'>Número de Versão</TableCell>
            <TableCell align="center">Data de Criação</TableCell>
            <TableCell align="center">Versão Pai</TableCell>
            <TableCell align="center" colSpan={2}>Ações</TableCell> 
          </TableRow>
        </TableHead>
        <TableBody>
          { [...listVersion].sort((a, b) => {
            return String(b.numVersion).localeCompare(String(a.numVersion), undefined, {numeric: true})
          }).map((version) => (
            <TableRow
              key={version.id}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row" align='center' sx={{ cursor: "pointer" }} onClick={() => navigate(`/version/${version.id}`)}>
                {version.numVersion}
              </TableCell>
              <TableCell align="center" sx={{ cursor: "pointer" }} onClick={() => navigate(`/version/${version.id}`)}>{format(version.createdDate, "dd/MM/yyyy")}</TableCell>
              <TableCell align="center" sx={{ cursor: "pointer" }} onClick={() => navigate(`/version/${version.id}`)}>{version.baseVersionId ? listVersion.find((v) => v.id === version.baseVersionId)?.numVersion : "-"}</TableCell>
              <TableCell align="center"><Button onClick={() => handleDownload(version.id, version.numVersion)}><DownloadIcon /></Button></TableCell>
              {/* <TableCell align="center"><Button><DeleteIcon /></Button></TableCell> */}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
