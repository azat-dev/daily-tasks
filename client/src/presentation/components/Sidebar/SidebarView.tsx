import React from "react";
import { Nav } from "react-bootstrap";

import SidebarViewModel from "./SidebarViewModel";

import styles from "./styles.module.scss";

import logo from "../../../logo.svg";
import { ReactComponent as TasksIcon } from "bootstrap-icons/icons/card-checklist.svg";
import { ReactComponent as OtherIcon } from "bootstrap-icons/icons/app.svg";
import { Link, NavLink } from "react-router-dom";

export interface SidebarProps {
    viewModel: SidebarViewModel;
}

const SidebarView = ({ viewModel }: SidebarProps) => {
    return (
        <div className={styles.sidebar}>
            <Link
                to="/"
                className="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none pt-2 pb-4"
            >
                <img
                    src={logo}
                    className="bi pe-none me-2"
                    width="40"
                    height="32"
                />
                <span className="fs-4">Daily Tasks</span>
            </Link>
            {/* <hr className="pt-2 pb-3" /> */}
            <Nav
                className="flex-column"
                activeKey={viewModel.activeItemId}
                variant="pills"
            >
                <Nav.Item>
                    <Nav.Item className={styles.navGroupHeader}>
                        <TasksIcon className={styles.navGroupIcon} />
                        <span className={styles.navGroupTitle}>Backlogs</span>
                    </Nav.Item>
                    <Nav
                        className={styles.sidebarGroup}
                        activeKey={viewModel.activeItemId}
                    >
                        <Nav.Item>
                            <Nav.Link
                                as={NavLink}
                                to="/backlog/day"
                                eventKey="day"
                                style={{ paddingLeft: 20, display: "block" }}
                            >
                                Day
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link
                                as={NavLink}
                                to="/backlog/week"
                                eventKey="week"
                            >
                                Week
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link
                                as={NavLink}
                                to="/backlog/month"
                                eventKey="month"
                            >
                                Month
                            </Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Nav.Item>
                <Nav.Item className={styles.navGroupHeader}>
                    <OtherIcon className={styles.navGroupIcon} />
                    <span className={styles.navGroupTitle}>Other</span>
                </Nav.Item>
                <Nav
                    className={styles.sidebarGroup}
                    activeKey={viewModel.activeItemId}
                >
                    <Nav.Item>
                        <Nav.Link
                            as={NavLink}
                            to="/projects"
                            eventKey="projects"
                            style={{ paddingLeft: 20, display: "block" }}
                        >
                            Projects
                        </Nav.Link>
                    </Nav.Item>
                </Nav>
            </Nav>
        </div>
    );
};

export default React.memo(SidebarView);
