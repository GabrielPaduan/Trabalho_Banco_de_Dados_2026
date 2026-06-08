import { Route, Routes } from "react-router-dom"
import UserLogin from "../pages/UserLogin/UserLogin"

export const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<UserLogin />} />
        </Routes>
    )
}