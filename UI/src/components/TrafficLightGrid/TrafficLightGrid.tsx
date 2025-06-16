import { useState } from 'react';
import { useSubscription } from 'react-stomp-hooks';
import { message } from 'antd';
import TrafficLightCard from '../TrafficLightCard/TrafficLightCard';
import { ITrafficLight } from '../../types/ITrafficLight';
import { ITrafficLightBroadcastMessage } from '../../types/ITrafficLightBroadcastMessage';
import styles from './TrafficLightGrid.module.scss';
import { apiService } from '../../services/api';

const GET_SLUG = '/get/lights';

const TrafficLightGrid = () => {
    const [lights, setLights] = useState<ITrafficLight[]>([]);
    const [messageApi, contextHolder] = message.useMessage();

    useSubscription(GET_SLUG, (message) => {
        const data: ITrafficLightBroadcastMessage = JSON.parse(message.body);
        updateLight(data);
    });

    const updateLight = (data: ITrafficLightBroadcastMessage) => {
        setLights(prevLights => {
            const index = prevLights.findIndex(light => light.lightId === data.id);
            if (index === -1) {
                // New light
                return [...prevLights, {
                    lightId: data.id,
                    lightName: data.name,
                    lightColor: data.lightColor,
                    delay: parseInt(data.delay),
                    isEnabled: true,
                    lightDirection: data.lightDirection,
                    timeSent: data.timeSent
                }];
            } else {
                // Update existing light
                const updatedLights = [...prevLights];
                updatedLights[index] = {
                    ...updatedLights[index],
                    lightColor: data.lightColor,
                    lightDirection: data.lightDirection,
                    timeSent: data.timeSent
                };
                return updatedLights;
            }
        });
    };

    const handleDelete = async (id: number) => {
        try {
            await apiService.deleteLight(id);
            setLights(prevLights => prevLights.filter(light => light.lightId !== id));
            messageApi.success("Light successfully deleted");
        } catch (error) {
            console.error('Error deleting light:', error);
            messageApi.error("Failed to delete light");
        }
    };

    return (
        <div className={styles.grid}>
            {contextHolder}
            {lights.map(light => (
                <TrafficLightCard
                    key={light.lightId}
                    light={light}
                    onDelete={handleDelete}
                />
            ))}
        </div>
    );
};

export default TrafficLightGrid; 