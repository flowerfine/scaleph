import {ResponseBody} from '@/app.d';
import {request} from 'umi';
import {Namespace} from './typings';

export const KubernetesNamespaceService = {
  url: '/api/kubernetes/namespace',

  list: async (clusterCredentialId: number | undefined) => {
    return request<ResponseBody<Array<Namespace>>>(`${KubernetesNamespaceService.url}/${clusterCredentialId}`, {
      method: 'GET',
    })
  },

};
