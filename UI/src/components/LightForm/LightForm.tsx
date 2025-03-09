import {useState} from "react";
import {Button, Input} from "antd";
import styles from "./LightForm.module.scss"
import {ITrafficLightDto} from "./ITrafficLightDto.ts";
import {POST} from "./httpClient.ts";

interface ILightForm {
    isDisabled: boolean;
}

const LightForm = ({isDisabled}: ILightForm) => {
    const [lightName, setLightName] = useState("");
    const [lightDelay, setLightDelay] = useState("0");

    function createLight() {
        const requestBody: ITrafficLightDto = {
            "name": lightName,
            "delay": parseInt(lightDelay)
        }
        POST(requestBody)
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
            <Button disabled={isDisabled} onClick={() => createLight()}>submit</Button>
        </div>
    )
}

export default LightForm