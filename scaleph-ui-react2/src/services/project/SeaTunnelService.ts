import {ResponseBody} from '@/typings';
import {request} from '@umijs/max';

export const WsSeaTunnelService = {
    url: '/api/seatunnel',

    getDnds: async (engine: string) => {
        return request<ResponseBody<Array<Record<string, any>>>>(`${WsSeaTunnelService.url}/dag/dnd/${engine}`, {
            method: 'GET',
        });
    },
};
