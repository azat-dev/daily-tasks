import { useEffect, useState } from "react";
import ActionButtonViewModel, {
    ActionButtonViewModelState,
} from "./ActionButtonViewModel";
import { ActionButtonViewProps } from "./ActionButtonView";
import { on } from "events";

const useActionButtonViewModel = ({
    startedAt,
    onStart,
    onStop,
    onDelete,
    onDoLaterWeek,
    onDoLaterMonth,
}: ActionButtonViewProps): ActionButtonViewModel => {
    const [state, setState] = useState<ActionButtonViewModelState>(() => {
        if (startedAt) {
            return {
                type: "active",
                startedAt,
            };
        }
        return {
            type: "notActive",
        };
    });

    useEffect(() => {
        if (startedAt) {
            setState({
                type: "active",
                startedAt,
            });
            return;
        }
        setState({
            type: "notActive",
        });
    }, [startedAt]);

    const onClickStart = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        onStart();
    };

    const onClickDoLaterWeek = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        onDoLaterWeek();
    };

    const onClickDoLaterMonth = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        onDoLaterMonth();
    };

    const onClickStop = (e: any) => {
        e.stopPropagation();
        e.preventDefault();

        onStop();
    };

    const onClickDelete = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        onDelete();
    };

    return {
        state,
        onClickStart,
        onClickStop,
        onClickDoLaterWeek,
        onClickDoLaterMonth,
        onClickDelete,
    };
};

export default useActionButtonViewModel;
