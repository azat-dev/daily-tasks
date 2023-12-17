import Value from "../../LogInPage/Value";

export interface ActionButtonViewModelOutput {
    isActive: Value<boolean>;
}

export interface ActionButtonViewModelDelegate {
    start: () => void;
    stop: () => void;
    doLaterWeek: () => void;
    doLaterMonth: () => void;
    delete: () => void;
}

export interface ActionButtonViewModelInput {
    onClickStart: (e: any) => void;
    onClickStop: (e: any) => void;
    onClickDoLaterWeek: (e: any) => void;
    onClickDoLaterMonth: (e: any) => void;
    onClickDelete: (e: any) => void;
}

export interface ActionButtonViewModelUpdateProperties {
    updateState(startedAt: Date | null): void;
}

export default interface ActionButtonViewModel
    extends ActionButtonViewModelInput,
        ActionButtonViewModelOutput,
        ActionButtonViewModelUpdateProperties {
    delegate: ActionButtonViewModelDelegate | null;
}
