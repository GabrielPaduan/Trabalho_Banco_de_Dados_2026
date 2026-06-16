import { Navigate, Route, Routes } from "react-router-dom"
import UserLogin from "../pages/UserLogin/UserLogin"
import CreateUser from "../pages/CreateUser/CreateUser"
import { ProtectedRoutes } from "../components/ProtectedRoutes"
import { Dashboard } from "../pages/Dashboard/Dashboard"
import { UpdateUser } from "../pages/UpdateUser/UpdateUser"
import { DatasetView } from "../pages/DatasetView/DatasetView"
import { VersionView } from "../pages/VersionView/VersionView"

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<UserLogin />} />
            <Route path="/usuario/cadastrar" element={<CreateUser />} />
            
            <Route element={<ProtectedRoutes />}>
                <Route path="/perfil" element={<UpdateUser />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/dataset/:id" element={<DatasetView />} />
                <Route path="/version/:id" element={<VersionView />} />
            </Route>

            <Route path="*" element={<Navigate to="/login"/>} />
        </Routes>
    )
}