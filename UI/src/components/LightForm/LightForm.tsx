import {useState} from "react";
import {Input, message} from "antd";
import styles from "./LightForm.module.scss"
import {ITrafficLightDto} from "./ITrafficLightDto.ts";
import FancyButton from "../FancyButton/FancyButton.tsx";
import {apiService} from "../../services/api";

interface ILightForm {
    isDisabled: boolean;
}

const LightForm = ({isDisabled}: ILightForm) => {
    const [lightName, setLightName] = useState("");
    const [lightDelay, setLightDelay] = useState("0");
    const [messageApi, contextHolder] = message.useMessage();

    async function createLight() {
        const requestBody: ITrafficLightDto = {
            "name": lightName,
            "delay": parseInt(lightDelay)
        }
        try {
            await apiService.createLight(requestBody);
            messageApi.success("Light created successfully");
            // Clear form
            setLightName("");
            setLightDelay("0");
        } catch (error) {
            messageApi.error("Failed to create light");
        }
    }

    function setDelay(input: string) {
        if (input == "") {
            setLightDelay("0")
        }

        if (isNaN(parseInt(input))) {
            console.log("invalid number")
        } else {
            const modifiedInput = parseInt(input) * 1000;
            setLightDelay(modifiedInput.toString())
        }
    }

    return (
        <div className={styles.container}>
            {contextHolder}
            <div className={styles.input_container}>
                <h3>Create new light</h3>
                <div className={styles.inputGrp}>
                    <label className={styles.inputLabel} htmlFor="light_name">Light name</label>
                    <Input name={"light_name"} disabled={isDisabled} onChange={(e) => setLightName(e.target.value)}
                           placeholder="light name"/>
                </div>

                <div className={styles.inputGrp}>
                    <label className={styles.inputLabel} htmlFor="light_delay">Light delay</label>
                    <Input name={"light_delay"} disabled={isDisabled} onChange={(e) => setDelay(e.target.value)}
                           placeholder="light delay (s)"/>
                </div>

            </div>
            <FancyButton type={"create"} onClick={() => createLight()} label={"submit"} />
        </div>
    )
}

export default LightForm