/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Book } from '../models/Book';
import type { BookOverviewProjection } from '../models/BookOverviewProjection';
import type { CreateBookRequestDTO } from '../models/CreateBookRequestDTO';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class BookControllerService {

    /**
     * @param requestBody
     * @returns BookOverviewProjection OK
     * @throws ApiError
     */
    public static createBook(
        requestBody: CreateBookRequestDTO,
        baseUrl: string,
    ): CancelablePromise<BookOverviewProjection> {
        console.log(requestBody)
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'POST',
            url: '/books',
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * @param bookId
     * @returns Book OK
     * @throws ApiError
     */
    public static getBookById(
        bookId: number,
        baseUrl: string,
    ): CancelablePromise<Book> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'GET',
            url: '/books/{bookId}',
            path: {
                'bookId': bookId,
            },
        });
    }

    /**
     * @param bookId
     * @returns string OK
     * @throws ApiError
     */
    public static deleteBookById(
        bookId: number,
    ): CancelablePromise<string> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/books/{bookId}',
            path: {
                'bookId': bookId,
            },
        });
    }

    /**
     * @param bookId
     * @returns BookOverviewProjection OK
     * @throws ApiError
     */
    public static getBookOverviewById(
        bookId: number,
        baseUrl: string,
    ): CancelablePromise<BookOverviewProjection> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'GET',
            url: '/books/{bookId}/overview',
            path: {
                'bookId': bookId,
            },
        });
    }

    /**
     * @param bookId
     * @returns Book OK
     * @throws ApiError
     */
    public static getBookByIdCached(
        bookId: number,
        baseUrl: string,
    ): CancelablePromise<Book> {
        return __request({...OpenAPI, BASE:baseUrl}, {
            method: 'GET',
            url: '/books/{bookId}/cached',
            path: {
                'bookId': bookId,
            },
        });
    }

}
