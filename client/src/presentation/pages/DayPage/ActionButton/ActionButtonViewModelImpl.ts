import ActionButtonViewModel, {
    ActionButtonViewModelDelegate,
} from "./ActionButtonViewModel";
import Value from "../../LogInPage/Value";
import { value } from "../../LogInPage/DefaultValue";

class ActionButtonViewModelImpl implements ActionButtonViewModel {
    public isActive: Value<boolean>;

    public constructor(
        public startedAt: Date | null = null,
        public delegate: ActionButtonViewModelDelegate | null = null
    ) {
        this.isActive = value(!!startedAt);
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

    updateState = (startedAt: Date | null): void => {
        if (startedAt) {
            this.isActive.set(true);
            return;
        }

        this.isActive.set(false);
    };
}

export default ActionButtonViewModelImpl;
