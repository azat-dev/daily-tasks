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
 * @interface GetTokenResponse
 */
export interface GetTokenResponse {
    /**
     * 
     * @type {string}
     * @memberof GetTokenResponse
     */
    access: string;
    /**
     * 
     * @type {string}
     * @memberof GetTokenResponse
     */
    refresh: string;
}

/**
 * Check if a given object implements the GetTokenResponse interface.
 */
export function instanceOfGetTokenResponse(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "access" in value;
    isInstance = isInstance && "refresh" in value;

    return isInstance;
}

export function GetTokenResponseFromJSON(json: any): GetTokenResponse {
    return GetTokenResponseFromJSONTyped(json, false);
}

export function GetTokenResponseFromJSONTyped(json: any, ignoreDiscriminator: boolean): GetTokenResponse {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'access': json['access'],
        'refresh': json['refresh'],
    };
}

export function GetTokenResponseToJSON(value?: GetTokenResponse | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'access': value.access,
        'refresh': value.refresh,
    };
}

