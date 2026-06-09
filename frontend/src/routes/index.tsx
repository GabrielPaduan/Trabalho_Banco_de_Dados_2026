import { Navigate, Route, Routes } from "react-router-dom"
import UserLogin from "../pages/UserLogin/UserLogin"
import CreateUser from "../pages/CreateUser/CreateUser"
import { ProtectedRoutes } from "../components/ProtectedRoutes"

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<UserLogin />} />
            <Route path="/usuario/cadastrar" element={<CreateUser />} />
            
            <Route element={<ProtectedRoutes />}>
                {/* Aqui serão criadas as rotas que precisam de autenticação */}
            </Route>

            <Route path="*" element={<Navigate to="login"/>} />
        </Routes>
    )
}