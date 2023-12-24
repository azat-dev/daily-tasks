import React from "react";

import AddTaskModalView from "../../presentation/modals/AddTaskModal/AddTaskModalView";
import EditTaskModalView from "../../presentation/modals/EditTaskModal/EditTaskModalView";
import { CurrentModalState } from "../model/DI/coordinator/AppCoordinator";

interface ModalPresenterProps {
    currentModal: CurrentModalState | null;
}

const ModalPresenter = ({ currentModal }: ModalPresenterProps) => {
    if (!currentModal) {
        return null;
    }

    switch (currentModal.type) {
        case "addTask": {
            return <AddTaskModalView viewModel={currentModal.viewModel} />;
        }

        case "editTask":
            return <EditTaskModalView viewModel={currentModal.viewModel} />;

        default:
            return null;
    }
};

export default React.memo(ModalPresenter);
