import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import YAML from "yaml";

export interface StateType {
  sessionCluster: WsFlinkKubernetesSessionCluster,
  sessionClusterYaml: string
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
  namespace: "flinkKubernetesSessionClusterSteps",

  state: {
    sessionCluster: null,
    sessionClusterYaml: null,
  },

  effects: {
    *editSessionCluster({payload}, {call, put}) {
      const response = yield call(WsFlinkKubernetesSessionClusterService.asYAML, payload);
      yield put({type: 'updateSessionCluster', payload: {sessionCluster: payload, sessionClusterYaml: YAML.stringify(response.data)}});
    },
  },

  reducers: {
    updateSessionCluster(state, {payload}) {
      return {
        ...state,
        sessionCluster: payload.sessionCluster,
        sessionClusterYaml: payload.sessionClusterYaml
      };
    }
  },
};

export default model;
