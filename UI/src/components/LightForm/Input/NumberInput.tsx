import styles from "./Input.module.scss"

interface INumberInput {
    value: number;
    label: string;
    onChange: (value: number) => void;
    placeholder?: string;
}

const NumberInput = ({value, label, onChange, placeholder }: INumberInput) => {
    return (
        <div className={styles.container}>
            <span className={styles.label}>{label}</span>
            <input  min="5" max="300" type="number" className={styles.input} value={value} onChange={(e) => onChange(parseInt(e.target.value))} placeholder={placeholder}></input>
        </div>
    )
}

export default NumberInput;
