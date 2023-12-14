import CurrentBacklogPageViewModel from "./CurrentBacklogPageViewModel";
import Value from "../../LogInPage/Value";
import Task from "../../../../domain/models/Task";
import { value } from "../../LogInPage/DefaultValue";

export default class CurrentBacklogPageViewModelImpl
    implements CurrentBacklogPageViewModel
{
    // Propeties
    public isLoading: Value<boolean> = value(true);
    public tasks: Value<Task[]> = value([]);

    // Constructors
    public constructor(private runAddTaskFlow: () => void) {}

    // Methods
    public onClickAddTask = (e: any) => {
        e.preventDefault();
        e.stopPropagation();
        this.runAddTaskFlow();
    };
}
