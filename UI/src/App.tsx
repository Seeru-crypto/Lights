import './App.css'
import {Route, Routes} from "react-router-dom";
import Generator from "./components/Generator/Generator.tsx";
import Landing from "./components/Landing/Landing.tsx";
import Layout from "./components/Layout/Layout.tsx";

function App() {
    return (
        <Routes>
            <Route element={<Layout/>}>
                <Route path="/" element={<Landing/>}/>
                <Route path="/gen" element={<Generator/>}/>
            </Route>
        </Routes>
    )
}

export default App
