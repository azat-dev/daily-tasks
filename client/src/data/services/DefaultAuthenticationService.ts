import { Result, ResultType } from "../../common/Result";
import {
    AuthenticationServiceError,
    IAuthenticationServiceByUserNameAndPassword,
    IAuthenticationServiceCheckToken,
    IAuthenticationTokenPair,
} from "../../domain/interfaces/services/AuthenticationService";
import { DefaultApi } from "../API";

export default class DefaultAuthenticationService
    implements
        IAuthenticationServiceByUserNameAndPassword,
        IAuthenticationServiceCheckToken
{
    private api: DefaultApi;

    constructor(api: DefaultApi) {
        this.api = api;
    }

    authenticateByUserName = async (
        username: string,
        password: string
    ): Promise<
        Result<IAuthenticationTokenPair, AuthenticationServiceError>
    > => {
        try {
            const response = await this.api.apiTokenPost({
                apiTokenPostRequest: {
                    username: username,
                    password: password,
                },
            });

            return {
                type: ResultType.Success,
                value: {
                    accessToken: response.access,
                    refreshToken: response.refresh,
                },
            };
        } catch (error: any) {
            if (error?.response?.status === 401) {
                return {
                    type: ResultType.Failure,
                    error: AuthenticationServiceError.WrongCredentials,
                };
            }

            return {
                type: ResultType.Failure,
                error: AuthenticationServiceError.InternalError,
            };
        }
    };

    isTokenValid = async (
        accessToken: string
    ): Promise<Result<boolean, AuthenticationServiceError>> => {
        try {
            await this.api.apiTokenVerifyPost({
                apiTokenVerifyPostRequest: {
                    token: accessToken,
                },
            });

            return {
                type: ResultType.Success,
                value: true,
            };
        } catch (error: any) {
            console.log(error);
            if (error?.response?.status === 401) {
                return {
                    type: ResultType.Success,
                    value: false,
                };
            }
            return {
                type: ResultType.Failure,
                error: AuthenticationServiceError.InternalError,
            };
        }
    };
}
