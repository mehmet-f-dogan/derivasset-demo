/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Author } from '../models/Author';
import type { AuthorOverviewProjection } from '../models/AuthorOverviewProjection';
import type { CreateAuthorRequestDTO } from '../models/CreateAuthorRequestDTO';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class AuthorControllerService {

    /**
     * @param requestBody
     * @returns AuthorOverviewProjection OK
     * @throws ApiError
     */
    public static createAuthor(
        requestBody: CreateAuthorRequestDTO,
        baseUrl: string
    ): CancelablePromise<AuthorOverviewProjection> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'POST',
            url: '/authors',
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * @param authorId
     * @returns Author OK
     * @throws ApiError
     */
    public static getAuthorById(
        authorId: number,
        baseUrl: string
    ): CancelablePromise<Author> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'GET',
            url: '/authors/{authorId}',
            path: {
                'authorId': authorId,
            },
        });
    }

    /**
     * @param authorId
     * @returns string OK
     * @throws ApiError
     */
    public static deleteAuthorById(
        authorId: number,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/authors/{authorId}',
            path: {
                'authorId': authorId,
            },
        });
    }

    /**
     * @param authorId
     * @returns AuthorOverviewProjection OK
     * @throws ApiError
     */
    public static getAuthorOverviewById(
        authorId: number,
        baseUrl: string

    ): CancelablePromise<AuthorOverviewProjection> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'GET',
            url: '/authors/{authorId}/overview',
            path: {
                'authorId': authorId,
            },
        });
    }

    /**
     * @param authorId
     * @returns Author OK
     * @throws ApiError
     */
    public static getAuthorByIdCached(
        authorId: number,
        baseUrl: string

    ): CancelablePromise<Author> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'GET',
            url: '/authors/{authorId}/cached',
            path: {
                'authorId': authorId,
            },
        });
    }

}
