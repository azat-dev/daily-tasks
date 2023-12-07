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

import { exists, mapValues } from '../runtime';
/**
 * 
 * @export
 * @interface ApiTokenVerifyPostRequest
 */
export interface ApiTokenVerifyPostRequest {
    /**
     * 
     * @type {string}
     * @memberof ApiTokenVerifyPostRequest
     */
    token: string;
}

/**
 * Check if a given object implements the ApiTokenVerifyPostRequest interface.
 */
export function instanceOfApiTokenVerifyPostRequest(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "token" in value;

    return isInstance;
}

export function ApiTokenVerifyPostRequestFromJSON(json: any): ApiTokenVerifyPostRequest {
    return ApiTokenVerifyPostRequestFromJSONTyped(json, false);
}

export function ApiTokenVerifyPostRequestFromJSONTyped(json: any, ignoreDiscriminator: boolean): ApiTokenVerifyPostRequest {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'token': json['token'],
    };
}

export function ApiTokenVerifyPostRequestToJSON(value?: ApiTokenVerifyPostRequest | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'token': value.token,
    };
}

