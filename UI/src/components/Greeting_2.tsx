import {useState} from 'react';
import {StompSessionProvider, useStompClient, useSubscription} from "react-stomp-hooks";

interface ITrafficBroadCast {
    id: string,
    status: string,
    timeSent: string,
    name: string
}

const Greeting_2 = () => {
    const API_PATH: string = "ws://localhost:8080/wsServer";
    const GET_SLUG = "/get/lights"
    const POST_SLUG = "/app/hello"
    const [lights, setLights] = useState<ITrafficBroadCast[]>([]);

    function updateLightList(signal: ITrafficBroadCast) {
        const newList = lights.filter(a => a.id !== signal.id)
        newList.push(signal);
        newList.sort((a, b) => a.name.localeCompare(b.name))
        setLights(newList)
    }


    function SubscribingComponent() {

        //Subscribe to /topic/test, and use handler for all received messages
        //Note that all subscriptions made through the library are automatically removed when their owning component gets unmounted.
        //If the STOMP connection itself is lost they are however restored on reconnect.
        //You can also supply an array as the first parameter, which will subscribe to all destinations in the array
        useSubscription(GET_SLUG, (message) => {
            const data = JSON.parse(message.body) as ITrafficBroadCast;
            console.log(`update for ${data.name}`)
            updateLightList(data);
        });

        return <div></div>;
    }

    function SendingMessages() {
        const stompClient = useStompClient();

        const sendMessage = () => {
            if (stompClient) {
                //Send Message
                stompClient.publish({
                    destination: POST_SLUG,
                    body: "Echo 123",
                });
            } else {
                //Handle error
            }
        };

        return <button onClick={sendMessage}>Send Message</button>;
    }

    return (
        //Initialize Stomp connection, will use SockJS for http(s) and WebSocket for ws(s)
        //The Connection can be used by all child components via the hooks or hocs.
        <StompSessionProvider
            url={API_PATH}
            //All options supported by @stomp/stompjs can be used here
        >
            <SubscribingComponent />
            <SendingMessages />

            <ul>
                {
                    lights.map((entry) => (
                        <li key={entry.id}>{entry.name} has status {entry.status}</li>
                    ))
                }
            </ul>

        </StompSessionProvider>
    );};

export default Greeting_2