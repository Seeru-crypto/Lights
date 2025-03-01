import './Landing.css'
import {StompSessionProvider} from "react-stomp-hooks";
import {message} from "antd";
import {useState} from "react";
import LightForm from "../LightForm/LightForm.tsx";
import LightTable from "../LightTable/LightTable.tsx";

function Landing() {
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
                <h1>Traffic Lights</h1>
                <LightForm isDisabled={isDisabled}/>
                <LightTable/>
                <article className="commentary_container">
                    <h2>What´s going on here?</h2>
                    <ul className="commentary">
                        <li>When a light is created a HTTP post is sent to API, where a client entity and new thread is created.</li>
                        <li>The thread loops according to its own logic and if a change occurs, it is broadcast to subscribers via STOMP message</li>
                        <li>Due to the thread logic every light is completely independant from each other. In theory a race condition might occur in UI if a update and DELETE occurs at the same time, but i have not been able to create it.</li>
                    </ul>
                </article>
            </StompSessionProvider>
        </div>
    )
}

export default Landing
