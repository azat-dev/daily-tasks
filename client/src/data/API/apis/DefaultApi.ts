/* tslint:disable */
/* eslint-disable */
/**
 * Daily Tasks API
 * Describes the API of Daily Tasks
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import * as runtime from '../runtime';
import type {
  AddNewTaskToBacklogResponse,
  ApiPublicAuthTokenPostRequest,
  ApiPublicAuthTokenRefreshPostRequest,
  ApiPublicAuthTokenVerifyPostRequest,
  BacklogType,
  GetTokenResponse,
  NewTaskData,
  RefreshTokenResponse,
  SignUpResponse,
  StartTaskResponse,
  StopTaskResponse,
  Task,
} from '../models/index';
import {
    AddNewTaskToBacklogResponseFromJSON,
    AddNewTaskToBacklogResponseToJSON,
    ApiPublicAuthTokenPostRequestFromJSON,
    ApiPublicAuthTokenPostRequestToJSON,
    ApiPublicAuthTokenRefreshPostRequestFromJSON,
    ApiPublicAuthTokenRefreshPostRequestToJSON,
    ApiPublicAuthTokenVerifyPostRequestFromJSON,
    ApiPublicAuthTokenVerifyPostRequestToJSON,
    BacklogTypeFromJSON,
    BacklogTypeToJSON,
    GetTokenResponseFromJSON,
    GetTokenResponseToJSON,
    NewTaskDataFromJSON,
    NewTaskDataToJSON,
    RefreshTokenResponseFromJSON,
    RefreshTokenResponseToJSON,
    SignUpResponseFromJSON,
    SignUpResponseToJSON,
    StartTaskResponseFromJSON,
    StartTaskResponseToJSON,
    StopTaskResponseFromJSON,
    StopTaskResponseToJSON,
    TaskFromJSON,
    TaskToJSON,
} from '../models/index';

export interface ApiPublicAuthTokenPostOperationRequest {
    apiPublicAuthTokenPostRequest: ApiPublicAuthTokenPostRequest;
}

export interface ApiPublicAuthTokenRefreshPostOperationRequest {
    apiPublicAuthTokenRefreshPostRequest: ApiPublicAuthTokenRefreshPostRequest;
}

export interface ApiPublicAuthTokenVerifyPostOperationRequest {
    apiPublicAuthTokenVerifyPostRequest: ApiPublicAuthTokenVerifyPostRequest;
}

export interface ApiWithAuthTasksBacklogBacklogTypeForDayGetRequest {
    backlogType: BacklogType;
    day: Date;
}

export interface ApiWithAuthTasksBacklogBacklogTypeForDayPostRequest {
    backlogType: BacklogType;
    day: Date;
    newTaskData: NewTaskData;
}

export interface ApiWithAuthTasksTaskIdCompletePostRequest {
    taskId: number;
}

export interface ApiWithAuthTasksTaskIdDeleteRequest {
    taskId: number;
}

export interface ApiWithAuthTasksTaskIdGetRequest {
    taskId: number;
}

export interface ApiWithAuthTasksTaskIdMoveToBacklogPostRequest {
    taskId: number;
}

export interface ApiWithAuthTasksTaskIdStartPostRequest {
    taskId: number;
}

export interface ApiWithAuthTasksTaskIdStopPostRequest {
    taskId: number;
}

/**
 * 
 */
export class DefaultApi extends runtime.BaseAPI {

    /**
     * Sign up a new user
     */
    async apiPublicAuthSignUpPostRaw(initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<SignUpResponse>> {
        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        const response = await this.request({
            path: `/api/public/auth/sign-up`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => SignUpResponseFromJSON(jsonValue));
    }

    /**
     * Sign up a new user
     */
    async apiPublicAuthSignUpPost(initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<SignUpResponse> {
        const response = await this.apiPublicAuthSignUpPostRaw(initOverrides);
        return await response.value();
    }

    /**
     * Get a new pair of tokens (access, refresh)
     */
    async apiPublicAuthTokenPostRaw(requestParameters: ApiPublicAuthTokenPostOperationRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<GetTokenResponse>> {
        if (requestParameters.apiPublicAuthTokenPostRequest === null || requestParameters.apiPublicAuthTokenPostRequest === undefined) {
            throw new runtime.RequiredError('apiPublicAuthTokenPostRequest','Required parameter requestParameters.apiPublicAuthTokenPostRequest was null or undefined when calling apiPublicAuthTokenPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        const response = await this.request({
            path: `/api/public/auth/token`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: ApiPublicAuthTokenPostRequestToJSON(requestParameters.apiPublicAuthTokenPostRequest),
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => GetTokenResponseFromJSON(jsonValue));
    }

    /**
     * Get a new pair of tokens (access, refresh)
     */
    async apiPublicAuthTokenPost(requestParameters: ApiPublicAuthTokenPostOperationRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<GetTokenResponse> {
        const response = await this.apiPublicAuthTokenPostRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     * Refresh a token
     */
    async apiPublicAuthTokenRefreshPostRaw(requestParameters: ApiPublicAuthTokenRefreshPostOperationRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<RefreshTokenResponse>> {
        if (requestParameters.apiPublicAuthTokenRefreshPostRequest === null || requestParameters.apiPublicAuthTokenRefreshPostRequest === undefined) {
            throw new runtime.RequiredError('apiPublicAuthTokenRefreshPostRequest','Required parameter requestParameters.apiPublicAuthTokenRefreshPostRequest was null or undefined when calling apiPublicAuthTokenRefreshPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        const response = await this.request({
            path: `/api/public/auth/token/refresh`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: ApiPublicAuthTokenRefreshPostRequestToJSON(requestParameters.apiPublicAuthTokenRefreshPostRequest),
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => RefreshTokenResponseFromJSON(jsonValue));
    }

    /**
     * Refresh a token
     */
    async apiPublicAuthTokenRefreshPost(requestParameters: ApiPublicAuthTokenRefreshPostOperationRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<RefreshTokenResponse> {
        const response = await this.apiPublicAuthTokenRefreshPostRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     * Check if a token is valid
     */
    async apiPublicAuthTokenVerifyPostRaw(requestParameters: ApiPublicAuthTokenVerifyPostOperationRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<void>> {
        if (requestParameters.apiPublicAuthTokenVerifyPostRequest === null || requestParameters.apiPublicAuthTokenVerifyPostRequest === undefined) {
            throw new runtime.RequiredError('apiPublicAuthTokenVerifyPostRequest','Required parameter requestParameters.apiPublicAuthTokenVerifyPostRequest was null or undefined when calling apiPublicAuthTokenVerifyPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        const response = await this.request({
            path: `/api/public/auth/token/verify`,
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: ApiPublicAuthTokenVerifyPostRequestToJSON(requestParameters.apiPublicAuthTokenVerifyPostRequest),
        }, initOverrides);

        return new runtime.VoidApiResponse(response);
    }

    /**
     * Check if a token is valid
     */
    async apiPublicAuthTokenVerifyPost(requestParameters: ApiPublicAuthTokenVerifyPostOperationRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<void> {
        await this.apiPublicAuthTokenVerifyPostRaw(requestParameters, initOverrides);
    }

    /**
     * Get a list of tasks in a specific backlog
     */
    async apiWithAuthTasksBacklogBacklogTypeForDayGetRaw(requestParameters: ApiWithAuthTasksBacklogBacklogTypeForDayGetRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Array<Task>>> {
        if (requestParameters.backlogType === null || requestParameters.backlogType === undefined) {
            throw new runtime.RequiredError('backlogType','Required parameter requestParameters.backlogType was null or undefined when calling apiWithAuthTasksBacklogBacklogTypeForDayGet.');
        }

        if (requestParameters.day === null || requestParameters.day === undefined) {
            throw new runtime.RequiredError('day','Required parameter requestParameters.day was null or undefined when calling apiWithAuthTasksBacklogBacklogTypeForDayGet.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/backlog/{backlog_type}/for/{day}`.replace(`{${"backlog_type"}}`, encodeURIComponent(String(requestParameters.backlogType))).replace(`{${"day"}}`, encodeURIComponent(String(requestParameters.day))),
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => jsonValue.map(TaskFromJSON));
    }

    /**
     * Get a list of tasks in a specific backlog
     */
    async apiWithAuthTasksBacklogBacklogTypeForDayGet(requestParameters: ApiWithAuthTasksBacklogBacklogTypeForDayGetRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Array<Task>> {
        const response = await this.apiWithAuthTasksBacklogBacklogTypeForDayGetRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     * Add a task in a backlog
     */
    async apiWithAuthTasksBacklogBacklogTypeForDayPostRaw(requestParameters: ApiWithAuthTasksBacklogBacklogTypeForDayPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<AddNewTaskToBacklogResponse>> {
        if (requestParameters.backlogType === null || requestParameters.backlogType === undefined) {
            throw new runtime.RequiredError('backlogType','Required parameter requestParameters.backlogType was null or undefined when calling apiWithAuthTasksBacklogBacklogTypeForDayPost.');
        }

        if (requestParameters.day === null || requestParameters.day === undefined) {
            throw new runtime.RequiredError('day','Required parameter requestParameters.day was null or undefined when calling apiWithAuthTasksBacklogBacklogTypeForDayPost.');
        }

        if (requestParameters.newTaskData === null || requestParameters.newTaskData === undefined) {
            throw new runtime.RequiredError('newTaskData','Required parameter requestParameters.newTaskData was null or undefined when calling apiWithAuthTasksBacklogBacklogTypeForDayPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        headerParameters['Content-Type'] = 'application/json';

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/backlog/{backlog_type}/for/{day}`.replace(`{${"backlog_type"}}`, encodeURIComponent(String(requestParameters.backlogType))).replace(`{${"day"}}`, encodeURIComponent(String(requestParameters.day))),
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
            body: NewTaskDataToJSON(requestParameters.newTaskData),
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => AddNewTaskToBacklogResponseFromJSON(jsonValue));
    }

    /**
     * Add a task in a backlog
     */
    async apiWithAuthTasksBacklogBacklogTypeForDayPost(requestParameters: ApiWithAuthTasksBacklogBacklogTypeForDayPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<AddNewTaskToBacklogResponse> {
        const response = await this.apiWithAuthTasksBacklogBacklogTypeForDayPostRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     * Complete a task
     */
    async apiWithAuthTasksTaskIdCompletePostRaw(requestParameters: ApiWithAuthTasksTaskIdCompletePostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<void>> {
        if (requestParameters.taskId === null || requestParameters.taskId === undefined) {
            throw new runtime.RequiredError('taskId','Required parameter requestParameters.taskId was null or undefined when calling apiWithAuthTasksTaskIdCompletePost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/{task_id}/complete`.replace(`{${"task_id"}}`, encodeURIComponent(String(requestParameters.taskId))),
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.VoidApiResponse(response);
    }

    /**
     * Complete a task
     */
    async apiWithAuthTasksTaskIdCompletePost(requestParameters: ApiWithAuthTasksTaskIdCompletePostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<void> {
        await this.apiWithAuthTasksTaskIdCompletePostRaw(requestParameters, initOverrides);
    }

    /**
     * Delete a task
     */
    async apiWithAuthTasksTaskIdDeleteRaw(requestParameters: ApiWithAuthTasksTaskIdDeleteRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<void>> {
        if (requestParameters.taskId === null || requestParameters.taskId === undefined) {
            throw new runtime.RequiredError('taskId','Required parameter requestParameters.taskId was null or undefined when calling apiWithAuthTasksTaskIdDelete.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/{task_id}`.replace(`{${"task_id"}}`, encodeURIComponent(String(requestParameters.taskId))),
            method: 'DELETE',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.VoidApiResponse(response);
    }

    /**
     * Delete a task
     */
    async apiWithAuthTasksTaskIdDelete(requestParameters: ApiWithAuthTasksTaskIdDeleteRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<void> {
        await this.apiWithAuthTasksTaskIdDeleteRaw(requestParameters, initOverrides);
    }

    /**
     * Get task data
     */
    async apiWithAuthTasksTaskIdGetRaw(requestParameters: ApiWithAuthTasksTaskIdGetRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<Task>> {
        if (requestParameters.taskId === null || requestParameters.taskId === undefined) {
            throw new runtime.RequiredError('taskId','Required parameter requestParameters.taskId was null or undefined when calling apiWithAuthTasksTaskIdGet.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/{task_id}`.replace(`{${"task_id"}}`, encodeURIComponent(String(requestParameters.taskId))),
            method: 'GET',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => TaskFromJSON(jsonValue));
    }

    /**
     * Get task data
     */
    async apiWithAuthTasksTaskIdGet(requestParameters: ApiWithAuthTasksTaskIdGetRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<Task> {
        const response = await this.apiWithAuthTasksTaskIdGetRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     * Move task to different backlog
     */
    async apiWithAuthTasksTaskIdMoveToBacklogPostRaw(requestParameters: ApiWithAuthTasksTaskIdMoveToBacklogPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<void>> {
        if (requestParameters.taskId === null || requestParameters.taskId === undefined) {
            throw new runtime.RequiredError('taskId','Required parameter requestParameters.taskId was null or undefined when calling apiWithAuthTasksTaskIdMoveToBacklogPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/{task_id}/move_to_backlog`.replace(`{${"task_id"}}`, encodeURIComponent(String(requestParameters.taskId))),
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.VoidApiResponse(response);
    }

    /**
     * Move task to different backlog
     */
    async apiWithAuthTasksTaskIdMoveToBacklogPost(requestParameters: ApiWithAuthTasksTaskIdMoveToBacklogPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<void> {
        await this.apiWithAuthTasksTaskIdMoveToBacklogPostRaw(requestParameters, initOverrides);
    }

    /**
     * Start
     */
    async apiWithAuthTasksTaskIdStartPostRaw(requestParameters: ApiWithAuthTasksTaskIdStartPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<StartTaskResponse>> {
        if (requestParameters.taskId === null || requestParameters.taskId === undefined) {
            throw new runtime.RequiredError('taskId','Required parameter requestParameters.taskId was null or undefined when calling apiWithAuthTasksTaskIdStartPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/{task_id}/start`.replace(`{${"task_id"}}`, encodeURIComponent(String(requestParameters.taskId))),
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => StartTaskResponseFromJSON(jsonValue));
    }

    /**
     * Start
     */
    async apiWithAuthTasksTaskIdStartPost(requestParameters: ApiWithAuthTasksTaskIdStartPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<StartTaskResponse> {
        const response = await this.apiWithAuthTasksTaskIdStartPostRaw(requestParameters, initOverrides);
        return await response.value();
    }

    /**
     * Stop a task
     */
    async apiWithAuthTasksTaskIdStopPostRaw(requestParameters: ApiWithAuthTasksTaskIdStopPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<runtime.ApiResponse<StopTaskResponse>> {
        if (requestParameters.taskId === null || requestParameters.taskId === undefined) {
            throw new runtime.RequiredError('taskId','Required parameter requestParameters.taskId was null or undefined when calling apiWithAuthTasksTaskIdStopPost.');
        }

        const queryParameters: any = {};

        const headerParameters: runtime.HTTPHeaders = {};

        if (this.configuration && this.configuration.accessToken) {
            const token = this.configuration.accessToken;
            const tokenString = await token("BearerAuth", []);

            if (tokenString) {
                headerParameters["Authorization"] = `Bearer ${tokenString}`;
            }
        }
        const response = await this.request({
            path: `/api/with-auth/tasks/{task_id}/stop`.replace(`{${"task_id"}}`, encodeURIComponent(String(requestParameters.taskId))),
            method: 'POST',
            headers: headerParameters,
            query: queryParameters,
        }, initOverrides);

        return new runtime.JSONApiResponse(response, (jsonValue) => StopTaskResponseFromJSON(jsonValue));
    }

    /**
     * Stop a task
     */
    async apiWithAuthTasksTaskIdStopPost(requestParameters: ApiWithAuthTasksTaskIdStopPostRequest, initOverrides?: RequestInit | runtime.InitOverrideFunction): Promise<StopTaskResponse> {
        const response = await this.apiWithAuthTasksTaskIdStopPostRaw(requestParameters, initOverrides);
        return await response.value();
    }

}
