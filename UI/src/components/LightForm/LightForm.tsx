import {useEffect, useState} from "react";
import {Button, Input} from "antd";
import styles from "./LightForm.module.scss"

const LightForm = () => {
    const [lightName, setLightName] = useState("");
    const [lightDelay, setLightDelay] = useState("0");
    const API_PATH = "http://localhost:8080/lights"

    async function createLight() {

        const url = `${API_PATH}?name=${lightName}&delay=${lightDelay} `

        const response = await fetch(url, {
            method: "POST",
            body: JSON.stringify({}),
        });
        console.log({response})
    }

    useEffect(() => {
        console.log({lightDelay}, {lightName})
    }, [lightDelay, lightName])

    function setDelay(input: string) {
        if (input == "") {
            setLightDelay("0")
        }

        if (isNaN(parseInt(input))) {
            console.log("invalid number")
        }
        else {
            const modifiedInput = parseInt(input) * 1000;
            setLightDelay(modifiedInput.toString())
        }
    }

    function submitForm() {
        createLight();
    }

    return (
        <div>
            <h2>light form</h2>
            <div className={styles.container}>
                <div>
                    <Input onChange={(e) => setLightName(e.target.value)} placeholder="light name" />
                </div>

                <div>
                    <Input onChange={(e) => setDelay(e.target.value)} placeholder="light delay (s)" />
                </div>

            </div>
            <Button onClick={() => submitForm()}>submit</Button>

        </div>
    )
}

export default LightForm