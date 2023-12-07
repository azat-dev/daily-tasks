import React from "react";
import { Button, ButtonGroup, Dropdown, Table } from "react-bootstrap";

import styles from "./styles.module.scss";
import DayPageViewViewModel from "./DayPageViewModel";
import { Action } from "history";
import ActionButtonView from "./ActionButton/ActionButtonView";

export interface DayPageViewProps {
    viewModel: DayPageViewViewModel;
}

const DayPageView = ({ viewModel }: DayPageViewProps) => {
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
                    {viewModel.rows.map((row) => {
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
