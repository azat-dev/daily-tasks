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
 * Information about user
 * @export
 * @interface UserInfo
 */
export interface UserInfo {
    /**
     * 
     * @type {string}
     * @memberof UserInfo
     */
    username: string;
    /**
     * 
     * @type {string}
     * @memberof UserInfo
     */
    firstName?: string;
    /**
     * 
     * @type {string}
     * @memberof UserInfo
     */
    lastName?: string;
}

/**
 * Check if a given object implements the UserInfo interface.
 */
export function instanceOfUserInfo(value: object): boolean {
    let isInstance = true;
    isInstance = isInstance && "username" in value;

    return isInstance;
}

export function UserInfoFromJSON(json: any): UserInfo {
    return UserInfoFromJSONTyped(json, false);
}

export function UserInfoFromJSONTyped(json: any, ignoreDiscriminator: boolean): UserInfo {
    if ((json === undefined) || (json === null)) {
        return json;
    }
    return {
        
        'username': json['username'],
        'firstName': !exists(json, 'first_name') ? undefined : json['first_name'],
        'lastName': !exists(json, 'last_name') ? undefined : json['last_name'],
    };
}

export function UserInfoToJSON(value?: UserInfo | null): any {
    if (value === undefined) {
        return undefined;
    }
    if (value === null) {
        return null;
    }
    return {
        
        'username': value.username,
        'first_name': value.firstName,
        'last_name': value.lastName,
    };
}
