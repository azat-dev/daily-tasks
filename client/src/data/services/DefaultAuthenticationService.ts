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
            const response = await this.api.apiPublicAuthTokenPost({
                apiPublicAuthTokenPostRequest: {
                    username: username,
                    password: password,
                },
            });

            return Result.success({
                accessToken: response.access,
                refreshToken: response.refresh,
            });
        } catch (error: any) {
            if (error?.response?.status === 401) {
                return Result.failure(
                    AuthenticationServiceError.WrongCredentials
                );
            }

            return Result.failure(AuthenticationServiceError.InternalError);
        }
    };

    isTokenValid = async (
        accessToken: string
    ): Promise<Result<boolean, AuthenticationServiceError>> => {
        try {
            await this.api.apiPublicAuthTokenVerifyPost({
                apiPublicAuthTokenVerifyPostRequest: {
                    token: accessToken,
                },
            });

            return Result.success(true);
        } catch (error: any) {
            console.log(error);
            if (error?.response?.status === 401) {
                return Result.success(false);
            }

            return Result.failure(AuthenticationServiceError.InternalError);
        }
    };
}
