import { Button, ButtonGroup, Dropdown } from "react-bootstrap";
import ActionButtonViewModel from "./ActionButtonViewModel";
import React from "react";
import useActionButtonViewModel from "./useActionButtonViewModel";

export interface ActionButtonViewProps {
    startedAt: Date | null;
    onStart: () => void;
    onStop: () => void;
    onDoLaterWeek: () => void;
    onDoLaterMonth: () => void;
    onDelete: () => void;
}

const ActionButtonView = (props: ActionButtonViewProps) => {
    const viewModel = useActionButtonViewModel(props);

    let button = (
        <Button variant="secondary" onClick={viewModel.onClickStop}>
            Stop
        </Button>
    );

    if (viewModel.state.type !== "active") {
        button = (
            <Button variant="secondary" onClick={viewModel.onClickStart}>
                Start
            </Button>
        );
    }

    return (
        <Dropdown as={ButtonGroup} size="sm">
            {button}

            <Dropdown.Toggle
                split
                id="dropdown-split-basic"
                variant="secondary"
            />

            <Dropdown.Menu variant="secondary">
                <Dropdown.Item onClick={viewModel.onClickDoLaterWeek}>
                    Do Later (Week)
                </Dropdown.Item>
                <Dropdown.Item onClick={viewModel.onClickDoLaterMonth}>
                    Do Later (Month)
                </Dropdown.Item>
                <hr />
                <Dropdown.Item onClick={viewModel.onClickDelete}>
                    Delete
                </Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default React.memo(ActionButtonView);
