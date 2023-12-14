import React from "react";
import AddTaskViewModel from "./ViewModel/AddTaskModalViewModel";
import { Button, Form, Modal, Spinner } from "react-bootstrap";
import { useViewModelBinding } from "../../pages/LogInPage/useBinding";

export interface AddTaskModalViewProps {
    viewModel: AddTaskViewModel;
}

const AddTaskModalView = ({ viewModel }: AddTaskModalViewProps) => {
    const vm = useViewModelBinding(viewModel);
    return (
        <Modal show={vm.show} onHide={vm.onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Add a new task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="addTaskForm.title">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Title"
                            required
                            isInvalid={vm.highlightTitleAsError}
                            disabled={vm.isProcessing}
                            onChange={vm.onChangeTitle}
                        />
                    </Form.Group>
                    <Form.Group
                        className="mb-3"
                        controlId="addTaskForm.priority"
                    >
                        <Form.Label>Priority</Form.Label>
                        <Form.Select
                            value={vm.priority}
                            disabled={vm.isProcessing}
                            onChange={vm.onChangePriority}
                        >
                            <option>Select priority</option>
                            {vm.priorityOptions.map((option) => {
                                return (
                                    <option
                                        key={option.value}
                                        value={option.value}
                                    >
                                        {option.label}
                                    </option>
                                );
                            })}
                        </Form.Select>
                    </Form.Group>

                    <Form.Group
                        className="mb-3"
                        controlId="addTaskForm.description"
                    >
                        <Form.Label>Description</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={5}
                            disabled={vm.isProcessing}
                            value={vm.description}
                            onChange={vm.onChangeDescription}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={vm.onCancel}>
                    Close
                </Button>
                <Button
                    variant="primary"
                    onClick={vm.onSave}
                    disabled={vm.isProcessing}
                    style={{ position: "relative" }}
                >
                    <div
                        style={{
                            position: "absolute",
                            top: 0,
                            left: 0,
                            width: "100%",
                            height: "100%",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                        }}
                    >
                        <Spinner
                            animation="border"
                            size="sm"
                            hidden={!vm.isProcessing}
                        />
                    </div>
                    <span style={{ opacity: vm.isProcessing ? 0 : 1 }}>
                        Create
                    </span>
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default React.memo(AddTaskModalView);
