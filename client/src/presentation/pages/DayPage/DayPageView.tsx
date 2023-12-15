import React, { useEffect } from "react";
import { Button, ButtonGroup, Dropdown, Table } from "react-bootstrap";

import styles from "./styles.module.scss";
import DayPageViewViewModel from "./ViewModel/DayPageViewModel";
import ActionButtonView from "./ActionButton/ActionButtonView";
import useUpdatesFrom from "../LogInPage/useUpdatesFrom";

export interface DayPageViewProps {
    viewModel: DayPageViewViewModel;
}

const DayPageView = ({ viewModel: vm }: DayPageViewProps) => {
    useUpdatesFrom(vm.isLoading, vm.rows);

    useEffect(() => {
        vm.load();
    }, []);

    return (
        <div className={styles.dayPageView}>
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
                        const actionButtonViewModel = row.actionButtonViewModel;
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
    );
};

export default React.memo(DayPageView);
