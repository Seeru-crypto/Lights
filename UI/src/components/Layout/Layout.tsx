import { Outlet } from 'react-router-dom'
import Navbar from "../Navbar/Navbar.tsx";

const   Layout = () => {
    return (
        <>
            <Navbar />
            <Outlet />
        </>
    )
}

export default Layout