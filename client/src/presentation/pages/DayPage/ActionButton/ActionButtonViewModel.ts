export type ActionButtonViewModelState =
    | {
          type: "active";
          startedAt: Date;
      }
    | { type: "notActive" };

interface ActionButtonViewModel {
    state: ActionButtonViewModelState;
    onClickStart: (e: any) => void;
    onClickStop: (e: any) => void;
    onClickDoLaterWeek: (e: any) => void;
    onClickDoLaterMonth: (e: any) => void;
    onClickDelete: (e: any) => void;
}

export default ActionButtonViewModel;
