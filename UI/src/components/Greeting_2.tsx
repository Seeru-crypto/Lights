import {useEffect, useState} from 'react';
import {StompSessionProvider, useStompClient, useSubscription} from "react-stomp-hooks";

const Greeting_2 = () => {
    //Public API that will echo messages sent to it back to the client
    const API_PATH: string = "ws://localhost:8080/wsServer";
    const GET_SLUG = "/topic/greetings"
    const POST_SLUG = "/app/hello"
    const [messages, setMessages] = useState<string[]>([])
    const [lastMessage, setLastMessage] = useState("No message received yet");


    function SubscribingComponent() {

        //Subscribe to /topic/test, and use handler for all received messages
        //Note that all subscriptions made through the library are automatically removed when their owning component gets unmounted.
        //If the STOMP connection itself is lost they are however restored on reconnect.
        //You can also supply an array as the first parameter, which will subscribe to all destinations in the array
        useSubscription(GET_SLUG, (message) => {
            // console.log("new message ", message)
            setLastMessage(parseTime(message.body))
            const test: string[] = messages;
            test.push(parseTime(message.body))
            setMessages(test)
        });

        return <div>Last Message: {lastMessage}</div>;
    }

    useEffect(() => {
        console.log({messages})
    }, [messages])

    function SendingMessages() {
        //Get Instance of StompClient
        //This is the StompCLient from @stomp/stompjs
        //Note: This will be undefined if the client is currently not connected
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

    function parseTime(time: string) : string {
        console.log( {time})
        return time.slice(0, 24)
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
                    messages.map((entry, index) => (
                        <li key={index}>{entry}</li>
                    ))
                }
            </ul>

        </StompSessionProvider>
    );};

export default Greeting_2