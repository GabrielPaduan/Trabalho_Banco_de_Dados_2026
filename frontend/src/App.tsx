import { BrowserRouter } from "react-router-dom"
import { AppRoutes } from "./routes"
import { CssBaseline, GlobalStyles } from "@mui/material"
import { AuthProvider } from "./contexts/AuthContext"

function App() {
  return (
    <BrowserRouter>
      <CssBaseline/>
      <GlobalStyles 
        styles={{ 
          '[hidden]': { display: 'none !important' } 
        }} 
      />
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
