import {nanoid} from 'nanoid';

export const INIT_ROW_VALUES = {
    input: '',
};

export const INIT_DATA = {
    key: nanoid(),
    level: 0,
    rowValues: {
        ...INIT_ROW_VALUES,
    },
};

export const INIT_CHECK_DATA = {
    key: nanoid(),
    level: 0,
    type: 1,
    children: [
        {
            rowValues: {
                input: '',
            },
            disabled: true,
            key: nanoid(),
            level: 1,
        },
        {
            key: nanoid(),
            type: 1,
            level: 2,
            disabled: true,
            children: [
                {
                    rowValues: {
                        input: '',
                    },
                    key: nanoid(),
                    level: 2,
                },
                {
                    key: nanoid(),
                    rowValues: {
                        input: '',
                    },
                    level: 2,
                },
            ],
        },
    ],
};

export const MORE_INIT_DATA = {
    key: nanoid(),
    level: 1,
    type: 1,
    children: [
        {
            rowValues: {
                input: '',
            },
            key: nanoid(),
            level: 1,
        },
        {
            key: nanoid(),
            type: 2,
            level: 2,
            children: [
                {
                    key: nanoid(),
                    level: 2,
                    rowValues: {
                        input: '',
                    },
                },
                {
                    key: nanoid(),
                    rowValues: {
                        input: '',
                    },
                    level: 2,
                },
            ],
        },
    ],
};

export type IRow = typeof INIT_ROW_VALUES;
