import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import { RechartsDevtools } from '@recharts/devtools';
import type { GenericGraphData } from '../util/DTO';

interface GraphProps {
  data: GenericGraphData[];
  graphType: number;
}

// #endregion
export default function GenericBarChart({data, graphType}: GraphProps) {
  return (
    <BarChart
      style={{ width: '100%', maxWidth: '100%', maxHeight: '70vh', aspectRatio: 1.618 }}
      responsive
      data={data}
      margin={{
        top: 5,
        right: 0,
        left: 0,
        bottom: 5,
      }}
    >
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="name" tick={{ fontSize: "16px"}} />
      <YAxis dataKey="access" width="auto" />
      <Tooltip />
      <Legend />
      {
        graphType == 0 && (
          <Bar dataKey="access" fill="#82ca9d" activeBar={{ fill: '#82ca9d', stroke: '#8884d8' }} />
        ) 
      }
      <Bar dataKey="downloads" fill="#8884d8" activeBar={{ fill: '#8884d8', stroke: '#82ca9d' }} />
      <RechartsDevtools />
    </BarChart>
  );
};