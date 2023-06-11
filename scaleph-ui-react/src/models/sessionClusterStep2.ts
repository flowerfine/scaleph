import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";

export interface StateType {
  templateId: number,
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
    templateId: null,
    sessionCluster: null,
  },

  effects: {
    *queryTemplate({payload}, {call, put}) {
      const {data} = yield call(WsFlinkKubernetesSessionClusterService.fromTemplate, payload);
      yield put({type: 'updateSessionCluster', payload: {templateId: payload, sessionCluster: data}});
    },

    *editSessionCluster({payload}, {call, put}) {
      yield put({type: 'updateSessionClusterOnly', payload: {sessionCluster: payload}});
    },
  },

  reducers: {
    updateSessionCluster(state, {payload}) {
      return {
        ...state,
        templateId: payload.templateId,
        sessionCluster: payload.sessionCluster
      };
    },
    updateSessionClusterOnly(state, {payload}) {
      return {
        ...state,
        sessionCluster: payload.sessionCluster
      };
    },
  },
};

export default model;
