import React from "react";
import EditTaskModalViewModel from "./ViewModel/EditTaskModalViewModel";
import { Button, Form, Modal, Placeholder, Spinner } from "react-bootstrap";
import useUpdatesFrom from "../../utils/useUpdatesFrom";

export interface EditTaskModalViewProps {
    viewModel: EditTaskModalViewModel;
}

const EditTaskModalView = ({ viewModel: vm }: EditTaskModalViewProps) => {
    useUpdatesFrom(
        vm.show,
        vm.title,
        vm.priority,
        vm.description,
        vm.highlightTitleAsError,
        vm.isLoading,
        vm.isProcessing
    );

    return (
        <Modal
            show={vm.show.value}
            onHide={vm.onHide}
            onExited={vm.onUnMount}
            onEntered={() => vm.load()}
        >
            <Modal.Header closeButton>
                <Modal.Title>Edit task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="addTaskForm.title">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Title"
                            required
                            isInvalid={vm.highlightTitleAsError.value}
                            value={vm.title.value}
                            disabled={vm.isProcessing.value}
                            onChange={vm.onChangeTitle}
                        />
                    </Form.Group>
                    <Form.Group
                        className="mb-3"
                        controlId="addTaskForm.priority"
                    >
                        <Form.Label>Priority</Form.Label>
                        <Form.Select
                            value={vm.priority.value}
                            disabled={vm.isProcessing.value}
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
                            disabled={vm.isProcessing.value}
                            value={vm.description.value}
                            onChange={vm.onChangeDescription}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={vm.onCancel}>
                    Cancel
                </Button>
                <Button
                    variant="primary"
                    onClick={vm.onSave}
                    disabled={vm.isProcessing.value}
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
                            hidden={!vm.isProcessing.value}
                        />
                    </div>
                    <span style={{ opacity: vm.isProcessing.value ? 0 : 1 }}>
                        Save
                    </span>
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default React.memo(EditTaskModalView);
