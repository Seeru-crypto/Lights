import {useSubscription} from "react-stomp-hooks";
import {useState} from "react";
import {Space, Table} from "antd";
import styles from "./LightTable.module.scss"
import {API_PATH} from "../../utils.ts";
import FancyButton from "../FancyButton/FancyButton.tsx";

interface ITrafficBroadCast {
    id: string,
    status: string,
    timeSent: string,
    formattedTime?:string
    name: string,
    delay: string,
    lightColor: LIGHT_COLOR
}

type LIGHT_COLOR = "RED" | "YELLOW" | "GREEN";

const LightTable = () => {
    const GET_SLUG = "/get/lights"
    const [lights, setLights] = useState<ITrafficBroadCast[]>([]);

    function subscribe() {
        useSubscription(GET_SLUG, (message) => {
            let data = JSON.parse(message.body) as ITrafficBroadCast;
            const tempList = data.timeSent.split(".");
            data = {...data, formattedTime: tempList[0]}
            updateLightList(data);
        });
    }

    function updateLightList(signal: ITrafficBroadCast) {
        const newList = lights.filter(a => a.id !== signal.id)
        newList.push(signal);
        newList.sort((a, b) => a.name.localeCompare(b.name))
        setLights(newList)
    }

    function removeLightFromList(lightId: string) {
        const newList = lights.filter(a => a.id !== lightId)
        setLights(newList);
    }

    const getLightColor = (color: LIGHT_COLOR) => {
        switch (color) {
            case "YELLOW": {
                return <div className={styles.yellow_light}></div>
            }
            case "GREEN":{
                return <div className={styles.green_light}></div>
            }
            case "RED":{
                return <div className={styles.red_light}></div>
            }

        }
    }

    subscribe();

    async function deleteLight(id: string) {
        const url = `${API_PATH}/${id}`

        await fetch(url, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(() => removeLightFromList(id) );
    }

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
            title: 'current color',
            render: (record: ITrafficBroadCast) => getLightColor(record.lightColor),
            key: 'lightColor',
            width: "5rem"
        },
        {
            title: 'Time updated',
            dataIndex: 'formattedTime',
            key: 'formattedTime',
            width: "5rem"
        },
        {
            title: 'delay (in ms)',
            dataIndex: 'delay',
            key: 'delay',
            width: "5rem"
        },
        {
            title: 'Action',
            key: 'action',
            render: (record: ITrafficBroadCast) => (
                <Space size="middle">
                    <FancyButton type={"delete"} label={"Delete"} onClick={() => deleteLight(record.id)} />
                </Space>
            ),
            width: "5rem"
        },
    ];

    return (
        <div className={styles.container}>
            <h1>Table</h1>
            <Table
                className={styles.table}
                rowKey={"id"}
                rowClassName={styles.row}
                pagination={false}
                dataSource={lights}
                columns={columns} />
        </div>

    )
}

export default LightTable;