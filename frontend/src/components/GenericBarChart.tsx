import { 
  BarChart, 
  Bar, 
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  Legend, 
  ResponsiveContainer 
} from 'recharts';
import { RechartsDevtools } from '@recharts/devtools';

interface GraphProps<T> {
  data: T[];
  graphType: number;
}

export default function GenericBarChart<T>({ data, graphType }: GraphProps<T>) {
  return (
    <ResponsiveContainer width="100%" height={"100%"}>
      <BarChart
        data={data}
        margin={{
          top: 20,
          right: 30,
          left: 0,
          bottom: 60, // Aumentamos o bottom para caber os textos inclinados do eixo X
        }}
      >
        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e0e0e0" />
        
        <XAxis 
          dataKey="name" 
          tick={{ fontSize: 13, fill: '#666' }} // Fonte um pouco menor
          angle={graphType === 0 ? -45 : 0}
          textAnchor={graphType === 0 ? "end" : "middle"}
          interval={0} 
        />
        
        <YAxis 
          dataKey={graphType === 0 ? "access" : "downloads"} 
          tick={{ fontSize: 13, fill: '#666' }}
          axisLine={false} 
          tickLine={false} 
        />
        
        <Tooltip 
          cursor={{ fill: 'rgba(0, 0, 0, 0.05)' }} 
          contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}
        />
        
        <Legend wrapperStyle={{ paddingTop: '10px' }} />
        
        {graphType === 0 && (
          <Bar 
            dataKey="access" 
            name="Acessos"
            fill="#82ca9d" 
            radius={[4, 4, 0, 0]}
            maxBarSize={60} 
            activeBar={{ fill: '#62a87d' }} 
          />
        )}
        
        <Bar 
          dataKey="downloads" 
          name="Downloads"
          fill="#8884d8" 
          radius={[4, 4, 0, 0]} 
          maxBarSize={60}
          activeBar={{ fill: '#6b66ba' }} 
        />
        
        <RechartsDevtools />
      </BarChart>
    </ResponsiveContainer>
  );
}