import styles from "./Input.module.scss"

interface IInput {
    value: string;
    label: string;
    onChange: (value: string) => void;
    placeholder?: string;
}


const Input = ({value, onChange, placeholder, label}: IInput) => {
    return (
    <div className={styles.container}>
        <span className={styles.label}>{label}</span>
        <input className={styles.input} value={value} onChange={(e) => onChange(e.target.value)} placeholder={placeholder}></input>
    </div>
    )    
};

export default Input;