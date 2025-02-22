import styles from "./Greeting.module.scss"
import { useState, useCallback, useEffect } from 'react';
import useWebSocket, { ReadyState } from 'react-use-websocket';

const Greeting = () => {
    //Public API that will echo messages sent to it back to the client
    const API_PATH: string = "ws://localhost:8080/wsServer";
    const [socketUrl, setSocketUrl] = useState('wss://echo.websocket.org');
    const [messageHistory, setMessageHistory] = useState < MessageEvent < any >[] >([]);

    const { sendMessage, lastMessage, readyState } = useWebSocket(socketUrl);

    useEffect(() => {
        if (lastMessage !== null) {
            setMessageHistory((prev) => prev.concat(lastMessage));
        }
    }, [lastMessage]);

    const handleClickChangeSocketUrl = useCallback(
        () => setSocketUrl(API_PATH),
        []
    );

    const handleClickSendMessage = useCallback(() => sendMessage('Hello'), []);

    const connectionStatus = {
        [ReadyState.CONNECTING]: 'Connecting',
        [ReadyState.OPEN]: 'Open',
        [ReadyState.CLOSING]: 'Closing',
        [ReadyState.CLOSED]: 'Closed',
        [ReadyState.UNINSTANTIATED]: 'Uninstantiated',
    }[readyState];

    return (
        <div className={styles.container}>
            <button onClick={handleClickChangeSocketUrl}>
                Click Me to change Socket Url
            </button>
            <button
                onClick={handleClickSendMessage}
                disabled={readyState !== ReadyState.OPEN}
            >
                Click Me to send 'Hello'
            </button>
            <span>The WebSocket is currently {connectionStatus}</span>
            {lastMessage ? <span>Last message: {lastMessage.data}</span> : null}
            <ul>
                {messageHistory.map((message, idx) => (
                    <span key={idx}>{message ? message.data : null}</span>
                ))}
            </ul>
        </div>
    );
};

export default Greeting


const socket = new WebSocket("ws://localhost:8080/ws-server");
socket.onopen = () => console.log("Connected!");
socket.onerror = (error) => console.error("WebSocket Error:", error);
