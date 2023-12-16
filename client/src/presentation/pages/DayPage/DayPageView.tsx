import React, { useEffect } from "react";
import { Button, Table } from "react-bootstrap";

import DayPageViewViewModel from "./ViewModel/DayPageViewModel";
import ActionButtonView from "./ActionButton/ActionButtonView";
import useUpdatesFrom from "../LogInPage/useUpdatesFrom";

import styles from "./styles.module.scss";

export interface DayPageViewProps {
    viewModel: DayPageViewViewModel;
}

const DayPageView = ({ viewModel: vm }: DayPageViewProps) => {
    useUpdatesFrom(vm.isLoading, vm.rows);

    useEffect(() => {
        vm.load();
    }, []);

    return (
        <div className={`${styles.dayPageView} ms-sm-auto px-md-4`}>
            <div
                className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 mb-0"
                style={{ alignItems: "end" }}
            >
                <h3>Current tasks</h3>
                <Button variant="primary" onClick={vm.onAddTask}>
                    Add Task
                </Button>
            </div>
            <div>
                <div className={styles.content}>
                    <Table responsive="sm">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Status</th>
                                <th>Priority</th>
                                <th>Created At</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {vm.rows.value.map((row) => {
                                const actionButtonViewModel =
                                    row.actionButtonViewModel;
                                return (
                                    <tr key={row.key}>
                                        <td>{row.title}</td>
                                        <td>{row.status}</td>
                                        <td>{row.priority}</td>
                                        <td>{`${row.createdAt}`}</td>
                                        <td>
                                            <ActionButtonView
                                                {...actionButtonViewModel}
                                            />
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
};

export default React.memo(DayPageView);
