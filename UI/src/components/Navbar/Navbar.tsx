import { NavLink } from 'react-router-dom'
import styles from "./Navbar.module.scss"

const Navbar = () => {
    return (
        <div className={styles.container}>
            <NavLink to="/gen"   className={({ isActive }) => isActive ? styles.active : styles.btn}>
                Generator
            </NavLink>

            <NavLink to="/" className={({ isActive }) => isActive ? styles.active : styles.btn}>
                Landing
            </NavLink>
        </div>
    )
}

export default Navbar