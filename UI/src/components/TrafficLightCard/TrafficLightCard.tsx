import { Button, Card, Modal } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import { ITrafficLight } from '../../types/ITrafficLight';
import styles from './TrafficLightCard.module.scss';
import { useState } from 'react';

interface TextProps {
    children: React.ReactNode;
    strong?: boolean;
    type?: 'secondary';
    className?: string;
}

const Text = ({ children, strong, type, className, ...props }: TextProps) => {
    const classNames = [];
    if (strong) classNames.push(styles.strong);
    if (type === 'secondary') classNames.push(styles.secondary);
    return <span className={[classNames.join(' '), className].filter(Boolean).join(' ')} {...props}>{children}</span>;
};

interface TrafficLightCardProps {
    light: ITrafficLight;
    onDelete: (id: number) => void;
}

const TrafficLightCard = ({ light, onDelete }: TrafficLightCardProps) => {
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const handleDelete = () => {
        setIsDeleteModalOpen(true);
    };

    const handleConfirmDelete = () => {
        onDelete(light.lightId);
        setIsDeleteModalOpen(false);
    };

    const handleCancelDelete = () => {
        setIsDeleteModalOpen(false);
    };

    const getColorClass = (color: string) => {
        return `light${color.charAt(0).toUpperCase() + color.slice(1).toLowerCase()}`;
    };

    const getStatusEmoji = (color: string) => {
        switch (color.toLowerCase()) {
            case 'red':
                return '🔴';
            case 'yellow':
                return '🟡';
            case 'green':
                return '🟢';
            default:
                return '⚪';
        }
    };

    return (
        <>
            <Card 
                style={{ color: 'red' }}
                className={`${styles.card} ${styles[getColorClass(light.lightColor)]}`}
                title={
                    <div className={styles.header}>
                        <Text strong>{light.lightName}</Text>
                        <DeleteOutlined 
                            className={styles.deleteIcon} 
                            onClick={handleDelete}
                        />
                    </div>
                }
            >
                <div className={styles.content}>
                    <div className={styles.statusSection}>
                        <span className={styles.statusEmoji}>{getStatusEmoji(light.lightColor)}</span>
                        <Text className={styles.statusText}>{light.lightColor}</Text>
                    </div>
                    
                    <div className={styles.detailsSection}>
                        <div className={styles.detailItem}>
                            <Text type="secondary">Delay:</Text>
                            <Text>{light.delay}ms</Text>
                        </div>
                        <div className={styles.detailItem}>
                            <Text type="secondary">Status:</Text>
                            <Text>{light.isEnabled ? 'Active' : 'Disabled'}</Text>
                        </div>
                        <div className={styles.detailItem}>
                            <Text type="secondary">last update:</Text>
                            <Text>{light.timeSent}</Text>
                        </div>
                    </div>
                </div>
            </Card>

            <Modal
                title="Delete Traffic Light"
                open={isDeleteModalOpen}
                onOk={handleConfirmDelete}
                onCancel={handleCancelDelete}
                okText="Yes"
                cancelText="No"
                okType="danger"
            >
                <p>Are you sure you want to delete "{light.lightName}"?</p>
            </Modal>
        </>
    );
};

export default TrafficLightCard; 