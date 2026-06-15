import { type Theme, useTheme } from '@mui/material/styles';
import { Box, OutlinedInput, InputLabel, MenuItem, FormControl, Select, Chip, type SelectChangeEvent } from '@mui/material';
import type { DataFont } from '../util/DTO';

interface MultipleSelectChipProps {
    dataFont: DataFont[];
    selectedFontsIds: number[];
    onChange: (selected: number[]) => void;
}

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    slotProps: { paper: { style: { maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP, width: 250 } } },
};

export default function MultipleSelectChip({ dataFont, selectedFontsIds, onChange }: MultipleSelectChipProps) {
    const theme = useTheme();

    const handleChange = (event: SelectChangeEvent<typeof selectedFontsIds>) => {
        // Pega os IDs selecionados e manda direto pro pai, sem interceptações
        const value = event.target.value as number[];
        onChange(value);
    };

    return (
        <FormControl fullWidth>
            <InputLabel id="demo-multiple-chip-label">Fontes de Dados</InputLabel>
            <Select
                labelId="demo-multiple-chip-label"
                id="demo-multiple-chip"
                multiple
                value={selectedFontsIds}
                onChange={handleChange}
                input={<OutlinedInput id="select-multiple-chip" label="Fontes de Dados" />}
                renderValue={(selectedIds) => (
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                        {selectedIds.map((id) => {
                            // Converte o ID de volta para o Nome na hora de desenhar a bolha
                            const fontCompleta = dataFont.find(f => f.id === id);
                            return <Chip key={id} label={fontCompleta ? fontCompleta.name : 'Desconhecido'} size='small' />;
                        })}
                    </Box>
                )}
                MenuProps={MenuProps}
            >
                {/* Renderiza apenas a lista real de fontes */}
                {dataFont.map((font: DataFont) => (
                    <MenuItem
                        key={font.id}
                        value={font.id} // Passa o ID para o estado
                        style={{
                            fontWeight: selectedFontsIds.includes(font.id) ? theme.typography.fontWeightMedium : theme.typography.fontWeightRegular
                        }}
                    >
                        {font.name} {/* Mostra o Nome para o usuário */}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    );
}