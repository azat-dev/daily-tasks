import React from "react";
import AddTaskViewModel from "./AddTaskModalViewModel";
import { Button, Form, Modal, Spinner } from "react-bootstrap";

export interface AddTaskModalViewProps {
    viewModel: AddTaskViewModel;
}

const AddTaskModalView = ({ viewModel }: AddTaskModalViewProps) => {
    return (
        <Modal show={viewModel.show} onHide={viewModel.onHide}>
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
                            isInvalid={viewModel.highlightTitleAsError}
                            disabled={viewModel.isProcessing}
                            onChange={viewModel.onChangeTitle}
                        />
                    </Form.Group>
                    <Form.Group
                        className="mb-3"
                        controlId="addTaskForm.priority"
                    >
                        <Form.Label>Priority</Form.Label>
                        <Form.Select
                            value={viewModel.priority}
                            disabled={viewModel.isProcessing}
                            onChange={viewModel.onChangePriority}
                        >
                            <option>Select priority</option>
                            {viewModel.priorityOptions.map((option) => {
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
                            disabled={viewModel.isProcessing}
                            value={viewModel.description}
                            onChange={viewModel.onChangeDescription}
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={viewModel.onCancel}>
                    Close
                </Button>
                <Button
                    variant="primary"
                    onClick={viewModel.onSave}
                    disabled={viewModel.isProcessing}
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
                            hidden={!viewModel.isProcessing}
                        />
                    </div>
                    <span style={{ opacity: viewModel.isProcessing ? 0 : 1 }}>
                        Create
                    </span>
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default React.memo(AddTaskModalView);
