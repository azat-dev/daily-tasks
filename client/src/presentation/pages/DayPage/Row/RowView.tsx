import React from "react";
import RowViewModel from "./ViewModel/RowViewModel";
import useUpdatesFrom from "../../../utils/useUpdatesFrom";
import ActionButtonView from "../ActionButton/ActionButtonView";

import styles from "./styles.module.scss";

interface RowViewProps {
    viewModel: RowViewModel;
}

const RowView = ({ viewModel: vm }: RowViewProps) => {
    useUpdatesFrom(vm.title, vm.status, vm.priority, vm.createdAt, vm.isActive);

    return (
        <tr
            className={styles.row}
            data-active={!!vm.isActive.value}
            onClick={vm.onClick}
        >
            <td>{vm.title.value}</td>
            <td>{vm.status.value}</td>
            <td>{vm.priority.value}</td>
            <td>{`${vm.createdAt.value}`}</td>
            <td>
                <ActionButtonView vm={vm.actionButtonViewModel} />
            </td>
        </tr>
    );
};

export default React.memo(RowView);
