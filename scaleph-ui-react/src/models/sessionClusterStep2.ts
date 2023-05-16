import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import { Reducer, Effect } from "umi";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";

export interface StateType {
  template: WsFlinkKubernetesTemplate,
  sessionCluster: string
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
    template: null,
    sessionCluster: null
  },

  effects: {
    *queryTemplate({ payload }, { call, put }) {
      const { data } = yield call(WsFlinkKubernetesTemplateService.selectOne, payload);
      yield put({ type: 'updateTemplate', payload: data });
    },
  },

  reducers: {
    updateTemplate(state, { payload }) {
      console.log('modelType', payload)
      return {
        ...state,
        template: payload,
      };
    },

  },
};

export default model;
