import {API_PATH} from "../../utils.ts";

const postRequestHeaders = new Headers();
postRequestHeaders.append("Content-Type", "application/json");

const postOptions: RequestInit = {
    method: 'POST',
    headers: postRequestHeaders,
};

export function POST<T>(payload: T): Promise<Response> {
    return fetch(API_PATH, {
        ...postOptions,
        body: JSON.stringify(payload),
    });
}