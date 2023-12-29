import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import YAML from "yaml";

export interface StateType {
  sessionCluster: WsFlinkKubernetesSessionCluster,
  sessionClusterYaml: string,
  sessionClusterStatusYaml: string,
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    querySessionCluster: Effect;
  };

  reducers: {
    updateSessionCluster: Reducer<StateType>;
  };
}

const model: ModelType = {
  namespace: "flinkKubernetesSessionClusterDetail",

  state: {
    sessionCluster: null,
    sessionClusterYaml: null,
    sessionClusterStatusYaml: null,
  },

  effects: {
    * querySessionCluster({payload}, {call, put}) {
      const {data} = yield call(WsFlinkKubernetesSessionClusterService.selectOne, payload);
      const param = {...data}
      param.deployed = undefined
      param.state = undefined
      const response = yield call(WsFlinkKubernetesSessionClusterService.asYAML, param);
      const statusReponse = yield call(WsFlinkKubernetesSessionClusterService.status, param);
      yield put({
        type: 'updateSessionCluster',
        payload: {
          sessionCluster: data,
          sessionClusterYaml: YAML.stringify(response.data),
          sessionClusterStatusYaml: YAML.stringify(statusReponse.data)
        }
      });
    },
  },

  reducers: {
    updateSessionCluster(state, {payload}) {
      return {
        ...state,
        sessionCluster: payload.sessionCluster,
        sessionClusterYaml: payload.sessionClusterYaml,
        sessionClusterStatusYaml: payload.sessionClusterStatusYaml
      };
    },
  },
};

export default model;
