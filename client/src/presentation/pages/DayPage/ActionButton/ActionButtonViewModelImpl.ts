import ActionButtonViewModel, {
    ActionButtonViewModelDelegate,
    ActionButtonViewModelState,
} from "./ActionButtonViewModel";
import Value from "../../LogInPage/Value";
import { value } from "../../LogInPage/DefaultValue";

class ActionButtonViewModelImpl implements ActionButtonViewModel {
    public state: Value<ActionButtonViewModelState>;

    public constructor(
        public startedAt: Date | null = null,
        public delegate: ActionButtonViewModelDelegate | null = null
    ) {
        this.state = value({ type: "notActive" });
    }

    onClickStart = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        this.delegate!.start();
    };

    onClickDoLaterWeek = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        this.delegate!.doLaterWeek();
    };

    onClickDoLaterMonth = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        this.delegate!.doLaterMonth();
    };

    onClickStop = (e: any) => {
        e.stopPropagation();
        e.preventDefault();

        this.delegate!.stop();
    };

    onClickDelete = (e: any) => {
        e.stopPropagation();
        e.preventDefault();
        this.delegate!.delete();
    };
}

export default ActionButtonViewModelImpl;
