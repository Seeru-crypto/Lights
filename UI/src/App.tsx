import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import javaLogo from './assets/java.svg'
import './App.css'
import {StompSessionProvider} from "react-stomp-hooks";
import TrafficLightGrid from "./components/TrafficLightGrid/TrafficLightGrid";
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
        <div className="fade-in">
            {contextHolder}
            <StompSessionProvider
                url={API_PATH}
                onConnect={() => onConnect()}
            >
                <div className="header">
                    <img src={viteLogo} className="logo" alt="Vite logo"/>
                    <img src={reactLogo} className="logo react" alt="React logo"/>
                    <img src={javaLogo} className="logo java" alt="Java logo"/>
                    <h1>Traffic Lights</h1>
                </div>

                <div className="main-content">
                    <LightForm isDisabled={isDisabled}/>
                    <TrafficLightGrid/>
                </div>

                <article className="commentary_container">
                    <h2>What's going on here?</h2>
                    <ul className="commentary">
                        <li>When a light is created a HTTP post is sent to API, where a client entity and new thread is created.</li>
                        <li>The thread loops according to its own logic and if a change occurs, it is broadcast to subscribers via STOMP message</li>
                        <li>Due to the thread logic every light is completely independent from each other. In theory a race condition might occur in UI if a update and DELETE occurs at the same time, but i have not been able to create it.</li>
                    </ul>
                </article>
            </StompSessionProvider>
        </div>
    )
}

export default App
