import { BrowserRouter } from "react-router-dom"
import { AppRoutes } from "./routes"
import { CssBaseline } from "@mui/material"
import { AuthProvider } from "./contexts/AuthContext"

function App() {
  return (
    <BrowserRouter>
      <CssBaseline />
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
