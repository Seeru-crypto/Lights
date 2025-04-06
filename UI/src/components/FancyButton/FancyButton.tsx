import styles from "./FancyButton.module.scss"


type TButtonType = "delete" | "create"

export interface IFancyButton {
    label: string;
    type?: TButtonType
    onClick: () => void;
}

const FancyButton = (props: IFancyButton) => {
    return (
        <button className={styles.container} onClick={() => props.onClick()}>{props.label}</button>

    )

}
export default FancyButton;