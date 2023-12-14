import React from "react";
import { Outlet } from "react-router-dom";
import CurrentBacklogPageViewModel from "./ViewModel/CurrentBacklogPageViewModel";

import styles from "./styles.module.scss";
import { Button } from "react-bootstrap";
import { useViewModelBinding } from "../LogInPage/useBinding";

export interface CurrentBacklogPageProps {
    viewModel: CurrentBacklogPageViewModel;
}

const CurrentBacklogPage = (props: CurrentBacklogPageProps) => {
    const vm = useViewModelBinding(props.viewModel);
    return (
        <div className={`${styles.currentBacklogPage} ms-sm-auto px-md-4`}>
            <div
                className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 mb-0"
                style={{ alignItems: "end" }}
            >
                <h3>Tasks</h3>
                <Button variant="primary" onClick={vm.onClickAddTask}>
                    Add Task
                </Button>
            </div>
            <div>
                <Outlet />
            </div>
        </div>
    );
};

export default React.memo(CurrentBacklogPage);
