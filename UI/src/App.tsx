import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import {StompSessionProvider} from "react-stomp-hooks";
import LightTable from "./components/LightTable/LightTable.tsx";
import LightForm from "./components/LightForm/LightForm.tsx";
import {message} from "antd";
import {useState} from "react";

function App() {
    const API_PATH: string = "ws://localhost:8080/wsServer";
    const [messageApi, contextHolder] = message.useMessage();
    const [isDisabled, setIsDisabled] = useState(true)

    function onConnect() {
        setIsDisabled(false);
        messageApi.success("Connected via websocket", 5)
    }

    return (
        <div>
            {contextHolder}
            <StompSessionProvider
                url={API_PATH}
                onConnect={() => onConnect()}
            >

                <div>
                    <img src={viteLogo} className="logo" alt="Vite logo"/>
                    <img src={reactLogo} className="logo react" alt="React logo"/>
                </div>
                <h1>Traffic Lights</h1>
                <LightForm isDisabled={isDisabled}/>
                <LightTable/>
            </StompSessionProvider>

        </div>
    )
}

export default App
