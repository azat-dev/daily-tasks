import React from "react";
import AddTaskModalView from "../../presentation/modals/AddTaskModal/AddTaskModalView";
import { CurrentModalState } from "./model/AppModel";

interface ModalPresenterProps {
    currentModal: CurrentModalState;
}

const ModalPresenter = ({ currentModal }: ModalPresenterProps) => {
    switch (currentModal.type) {
        case "addTask": {
            return <AddTaskModalView viewModel={currentModal.viewModel} />;
        }

        default:
            return null;
    }
};

export default React.memo(ModalPresenter);
