import React from "react";
import RowViewModel from "./ViewModel/RowViewModel";
import useUpdatesFrom from "../../LogInPage/useUpdatesFrom";
import ActionButtonView from "../ActionButton/ActionButtonView";

interface RowViewProps {
    viewModel: RowViewModel;
}

const RowView = ({ viewModel: vm }: RowViewProps) => {
    useUpdatesFrom(vm.title, vm.status, vm.priority, vm.createdAt);

    return (
        <tr>
            <td>{vm.title.value}</td>
            <td>{vm.status.value}</td>
            <td>{vm.priority.value}</td>
            <td>{`${vm.createdAt.value}`}</td>
            <td>
                <ActionButtonView {...vm.actionButtonViewModel} />
            </td>
        </tr>
    );
};

export default React.memo(RowView);
