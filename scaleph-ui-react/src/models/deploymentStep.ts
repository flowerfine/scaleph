import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";

export interface StateType {
  templateId: number,
  deployment: WsFlinkKubernetesDeployment,
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    queryTemplate: Effect;
    editDeployment: Effect;
  };

  reducers: {
    updateDeployment: Reducer<StateType>;
    updateDeploymentOnly: Reducer<StateType>;
  };
}

const model: ModelType = {
  state: {
    templateId: null,
    deployment: null,
  },

  effects: {
    *queryTemplate({payload}, {call, put}) {
      const {data} = yield call(WsFlinkKubernetesDeploymentService.fromTemplate, payload);
      yield put({type: 'updateDeployment', payload: {templateId: payload, deployment: data}});
    },

    *editDeployment({payload}, {call, put}) {
      yield put({type: 'updateDeploymentOnly', payload: {deployment: payload}});
    },
  },

  reducers: {
    updateDeployment(state, {payload}) {
      return {
        ...state,
        templateId: payload.templateId,
        deployment: payload.deployment
      };
    },
    updateDeploymentOnly(state, {payload}) {
      return {
        ...state,
        deployment: payload.deployment
      };
    },
  },
};

export default model;
