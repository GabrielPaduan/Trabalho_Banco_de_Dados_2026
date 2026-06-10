import { Navigate, Route, Routes } from "react-router-dom"
import UserLogin from "../pages/UserLogin/UserLogin"
import CreateUser from "../pages/CreateUser/CreateUser"
import { ProtectedRoutes } from "../components/ProtectedRoutes"
import { Dashboard } from "../pages/Dashboard/Dashboard"
import { UpdateUser } from "../pages/UpdateUser/UpdateUser"

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<UserLogin />} />
            <Route path="/usuario/cadastrar" element={<CreateUser />} />
            
            <Route element={<ProtectedRoutes />}>
                <Route path="/editarUsuario" element={<UpdateUser />} />
                <Route path="/dashboard" element={<Dashboard />} />
            </Route>

            <Route path="*" element={<Navigate to="/login"/>} />
        </Routes>
    )
}