import { BrowserRouter } from "react-router-dom"
import { AppRoutes } from "./routes"
import { CssBaseline } from "@mui/material"

function App() {
  return (
    <BrowserRouter>
      <CssBaseline />
      <AppRoutes />
    </BrowserRouter>
  )
}

export default App
