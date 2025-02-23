import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Greeting_2 from "./components/Greeting_2.tsx";
import {StompSessionProvider} from "react-stomp-hooks";
import LightTable from "./components/LightTable/LightTable.tsx";
import LightForm from "./components/LightForm/LightForm.tsx";

function App() {
    const API_PATH: string = "ws://localhost:8080/wsServer";

    return (
        <div>
            <StompSessionProvider
                url={API_PATH}
                //All options supported by @stomp/stompjs can be used here
            >

            <div>
                <img src={viteLogo} className="logo" alt="Vite logo"/>
                <img src={reactLogo} className="logo react" alt="React logo"/>
            </div>
            <h1>Lights UI</h1>
            <Greeting_2/>
                <LightForm />
                <LightTable />
            </StompSessionProvider>

        </div>
    )
}

export default App
