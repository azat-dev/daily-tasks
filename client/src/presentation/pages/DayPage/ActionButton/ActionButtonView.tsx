import { Button, ButtonGroup, Dropdown } from "react-bootstrap";
import ActionButtonViewModel from "./ActionButtonViewModel";
import React from "react";
import useUpdatesFrom from "../../LogInPage/useUpdatesFrom";

export interface ActionButtonViewProps {
    vm: ActionButtonViewModel;
}

const ActionButtonView = ({ vm }: ActionButtonViewProps) => {
    useUpdatesFrom(vm.state);

    let button = (
        <Button variant="secondary" onClick={vm.onClickStop}>
            Stop
        </Button>
    );

    if (vm.state.value.type !== "active") {
        button = (
            <Button variant="secondary" onClick={vm.onClickStart}>
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
                <Dropdown.Item onClick={vm.onClickDoLaterWeek}>
                    Do Later (Week)
                </Dropdown.Item>
                <Dropdown.Item onClick={vm.onClickDoLaterMonth}>
                    Do Later (Month)
                </Dropdown.Item>
                <hr />
                <Dropdown.Item onClick={vm.onClickDelete}>Delete</Dropdown.Item>
            </Dropdown.Menu>
        </Dropdown>
    );
};

export default React.memo(ActionButtonView);
