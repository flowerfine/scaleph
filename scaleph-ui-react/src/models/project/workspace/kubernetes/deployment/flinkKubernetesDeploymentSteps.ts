import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import YAML from "yaml";

export interface StateType {
  deployment: WsFlinkKubernetesDeployment,
  deploymentYaml: string,
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    editDeployment: Effect;
  };

  reducers: {
    updateDeployment: Reducer<StateType>;
  };
}

const model: ModelType = {
  namespace: "flinkKubernetesDeploymentSteps",

  state: {
    deployment: null,
    deploymentYaml: null,
  },

  effects: {
    *editDeployment({payload}, {call, put}) {
      const response = yield call(WsFlinkKubernetesDeploymentService.asYaml, payload);
      yield put({
        type: 'updateDeployment',
        payload: {deployment: payload, deploymentYaml: YAML.stringify(response.data)}
      });
    },
  },

  reducers: {
    updateDeployment(state, {payload}) {
      return {
        ...state,
        deployment: payload.deployment,
        deploymentYaml: payload.deploymentYaml
      };
    },
  },
};

export default model;
