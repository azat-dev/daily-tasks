import React from "react";

import styles from "./styles.module.scss";
import { Col, Container, Row } from "react-bootstrap";
import SidebarView from "../SidebarView";
import SidebarViewModel from "../SidebarViewModel";

export interface PageWithSidebarViewProps {
    activeItemId: "day" | "week" | "projects";
    children?: any;
}

const PageWithSidebarView = (props: PageWithSidebarViewProps) => {
    const sidebarViewModel: SidebarViewModel = {
        activeItemId: props.activeItemId,
    };

    return (
        <Container fluid className={styles.pageWithSidebar}>
            <Row>
                <Col md={3} lg={2} p={0}>
                    <SidebarView viewModel={sidebarViewModel} />
                </Col>
                <Col>{props.children ?? null}</Col>
            </Row>
        </Container>
    );
};

export default React.memo(PageWithSidebarView);
