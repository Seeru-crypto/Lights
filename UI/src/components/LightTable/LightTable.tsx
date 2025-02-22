import {useSubscription} from "react-stomp-hooks";
import {useState} from "react";
import {Table} from "antd";
import styles from "./LightTable.module.scss"

interface ITrafficBroadCast {
    id: string,
    status: string,
    timeSent: string,
    name: string
}

const LightTable = () => {
    const GET_SLUG = "/get/lights"
    const [lights, setLights] = useState<ITrafficBroadCast[]>([]);


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
    }

    function updateLightList(signal: ITrafficBroadCast) {
        const newList = lights.filter(a => a.id !== signal.id)
        newList.push(signal);
        newList.sort((a, b) => a.name.localeCompare(b.name))
        setLights(newList)
    }

    SubscribingComponent();

    const columns = [
        {
            title: 'id',
            dataIndex: 'id',
            key: 'id',
            width: "1rem"
        },
        {
            title: 'name',
            dataIndex: 'name',
            key: 'name',
            width: "5rem"
        },
        {
            title: 'status',
            dataIndex: 'status',
            key: 'status',
            width: "5rem"
        },
    ];


    return (
        <div className={styles.container}>
            <h1>Table</h1>
            <Table className={styles.table} rowClassName={styles.row} dataSource={lights} columns={columns} />;
        </div>

    )


}

export default LightTable;