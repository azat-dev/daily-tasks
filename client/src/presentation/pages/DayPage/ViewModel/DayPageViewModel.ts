import Value from "../../LogInPage/Value";
import { ActionButtonViewProps } from "../ActionButton/ActionButtonView";

export interface DayPageViewViewModelRow {
    key: string;
    title: string;
    createdAt: string;
    status: string;
    priority: string;
    isActive: boolean;
    actionButtonViewModel: ActionButtonViewProps;
}

interface DayPageViewViewModel {
    isLoading: Value<boolean>;
    rows: Value<DayPageViewViewModelRow[]>;
    load: () => Promise<void>;
}

export default DayPageViewViewModel;
