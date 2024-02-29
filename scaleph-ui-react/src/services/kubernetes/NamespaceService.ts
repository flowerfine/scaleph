import {ResponseBody} from '@/typings';
import {request} from '@umijs/max';
import {Namespace} from './typings';

export const KubernetesNamespaceService = {
  url: '/api/kubernetes/namespace',

  list: async (clusterCredentialId: number | undefined) => {
    return request<ResponseBody<Array<Namespace>>>(`${KubernetesNamespaceService.url}/${clusterCredentialId}`, {
      method: 'GET',
    })
  },

};
