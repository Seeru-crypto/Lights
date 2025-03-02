import styles from "./Generator.module.scss"
import {Button} from "antd";
import {useState} from "react";

const Generator = () => {
    const [result, setResult] = useState("");
    const [maxCharacters, setMaxcharacters] = useState("");
    // first select max characters and then a random string is generated according to that length

    function generate() {

        setResult("res")
    }

    return (
        <div className={styles.container}>
            <h2>Generator</h2>
            <div className={styles.results}>
                <p>Result</p>
                <p>{result}</p>
            </div>


            <Button onClick={() => generate()}>Generate</Button>


        </div>
    )

}

export default Generator