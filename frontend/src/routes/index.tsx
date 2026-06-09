import { Route, Routes } from "react-router-dom"
import UserLogin from "../pages/UserLogin/UserLogin"
import CreateUser from "../pages/CreateUser/CreateUser"

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<UserLogin />} />
            <Route path="/usuario/cadastrar" element={<CreateUser />} />
        </Routes>
    )
}