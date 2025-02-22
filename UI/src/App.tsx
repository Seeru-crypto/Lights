import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Greeting_2 from "./components/Greeting_2.tsx";

function App() {
    return (
        <div>
            <div>
                <img src={viteLogo} className="logo" alt="Vite logo"/>
                <img src={reactLogo} className="logo react" alt="React logo"/>
            </div>
            <h1>Lights UI</h1>
            <Greeting_2/>
        </div>
    )
}

export default App
