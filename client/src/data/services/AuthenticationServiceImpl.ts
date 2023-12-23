import { Result } from "../../common/Result";
import {
    AuthenticationServiceError,
    AuthenticationServiceByUserNameAndPassword,
    AuthenticationServiceCheckToken,
    AuthenticationTokenPair,
} from "../../domain/interfaces/services/AuthenticationService";
import { DefaultApi } from "../API";

export default class AuthenticationServiceImpl
    implements
        AuthenticationServiceByUserNameAndPassword,
        AuthenticationServiceCheckToken
{
    public constructor(private api: DefaultApi) {}

    public authenticateByUserName = async (
        username: string,
        password: string
    ): Promise<Result<AuthenticationTokenPair, AuthenticationServiceError>> => {
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
