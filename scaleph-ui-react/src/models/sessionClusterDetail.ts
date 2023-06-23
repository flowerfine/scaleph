import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";

export interface StateType {
  sessionCluster: WsFlinkKubernetesSessionCluster,
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    queryTemplate: Effect;
  };

  reducers: {
    updateTemplate: Reducer<StateType>;
  };
}

const model: ModelType = {
  state: {
    sessionCluster: null,
  },

  effects: {
    *querySessionCluster({payload}, {call, put}) {
      const {data} = yield call(WsFlinkKubernetesSessionClusterService.selectOne, payload);
      yield put({type: 'updateSessionCluster', payload: {sessionCluster: data}});
    },
  },

  reducers: {
    updateSessionCluster(state, {payload}) {
      return {
        ...state,
        sessionCluster: payload.sessionCluster
      };
    },
  },
};

export default model;
