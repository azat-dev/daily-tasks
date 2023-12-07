import Spinner from "react-bootstrap/Spinner";

import styles from "./styles.module.scss";

const AuthProcessingPage = () => {
    return (
        <div className={styles.authProcessingPage}>
            <div className={styles.content}>
                <h3>Authenticating...</h3>
                <Spinner animation="border" />
            </div>
        </div>
    );
};

export default AuthProcessingPage;
