import Value from "../../LogInPage/Value";

export type ActionButtonViewModelState =
    | {
          type: "active";
          startedAt: Date;
      }
    | { type: "notActive" };

export interface ActionButtonViewModelOutput {
    state: Value<ActionButtonViewModelState>;
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

export default interface ActionButtonViewModel
    extends ActionButtonViewModelInput,
        ActionButtonViewModelOutput {
    delegate: ActionButtonViewModelDelegate | null;
}
