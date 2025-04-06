import styles from "./FancyButton.module.scss"


type TButtonType = "delete" | "create"

export interface IFancyButton {
    label: string;
    type?: TButtonType
    onClick: () => void;
}

const FancyButton = (props: IFancyButton) => {

    function getClassName(): string {
        if (props.type=="delete") return styles.delete
        else return styles.create
    }

    return (
        <button className={`${styles.container} ${getClassName()}`} onClick={() => props.onClick()}>{props.label}</button>

    )
}
export default FancyButton;