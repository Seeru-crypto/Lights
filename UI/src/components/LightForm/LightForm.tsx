import {useState} from "react";
import {message} from "antd";
import Input from "./Input/Input.tsx";
import styles from "./LightForm.module.scss"
import {ITrafficLightDto} from "./ITrafficLightDto.ts";
import FancyButton from "../FancyButton/FancyButton.tsx";
import {apiService} from "../../services/api";
import NumberInput from "./Input/NumberInput.tsx";

interface ILightForm {
    isDisabled: boolean;
}

const LightForm = ({isDisabled}: ILightForm) => {
    const [lightName, setLightName] = useState<string>("");
    const [lightDelay, setLightDelay] = useState<number>(5);
    const [messageApi, contextHolder] = message.useMessage();

    async function createLight() {
        const requestBody: ITrafficLightDto = {
            "name": lightName,
            "delay": lightDelay * 1000
        }
        try {
            await apiService.createLight(requestBody);
            messageApi.success("Light created successfully");
            // Clear form
            setLightName("");
            setLightDelay(5);
        } catch (error) {
            messageApi.error("Failed to create light");
        }
    }

    return (
        <div className={styles.container}>
            {contextHolder}
            <div className={styles.input_container}>
                <h3>Create new light</h3>
                <Input value={lightName} onChange={(e: string) => setLightName(e)} placeholder="light name" label="Light name"/>
                <NumberInput value={lightDelay} onChange={(e: number) => setLightDelay(e)} placeholder="light delay (s)" label="Light delay"/>
            </div>
            <FancyButton type={"create"} onClick={() => createLight()} label={"submit"} />
        </div>
    )
}

export default LightForm