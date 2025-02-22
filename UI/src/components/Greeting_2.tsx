import {useState} from 'react';
import {useStompClient} from "react-stomp-hooks";

interface ITrafficBroadCast {
    id: string,
    status: string,
    timeSent: string,
    name: string
}

const Greeting_2 = () => {
    const POST_SLUG = "/app/hello"
    const [lights, setLights] = useState<ITrafficBroadCast[]>([]);

    function updateLightList(signal: ITrafficBroadCast) {
        const newList = lights.filter(a => a.id !== signal.id)
        newList.push(signal);
        newList.sort((a, b) => a.name.localeCompare(b.name))
        setLights(newList)
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
        <div>
            <SendingMessages />



        </div>
    );};

export default Greeting_2