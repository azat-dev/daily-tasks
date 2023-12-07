import Task from "../../../domain/models/Task";
import { ActionButtonViewProps } from "./ActionButton/ActionButtonView";
import ActionButtonViewModel from "./ActionButton/ActionButtonViewModel";

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
    isLoading: boolean;
    rows: DayPageViewViewModelRow[];
}

export default DayPageViewViewModel;
