import {WsDorisTemplate, WsFlinkKubernetesTemplate} from "@/services/project/typings";
import { Reducer, Effect } from "umi";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import YAML from "yaml";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";

export interface StateType {
  template: WsDorisTemplate,
  templateYaml: string
  templateYamlWithDefault: string
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
    templateYaml: null,
    templateYamlWithDefault: null
  },

  effects: {
    *editTemplate({ payload }, { call, put }) {
      const { data } = yield call(WsDorisTemplateService.asYaml, payload);
      const response = yield call(WsDorisTemplateService.asYaml, payload);
      yield put({ type: 'updateTemplate', payload: {template: payload, templateYaml: YAML.stringify(data), templateYamlWithDefault: YAML.stringify(response.data)} });
    },
  },

  reducers: {
    updateTemplate(state, { payload }) {
      return {
        ...state,
        template: payload.template,
        templateYaml: payload.templateYaml,
        templateYamlWithDefault: payload.templateYamlWithDefault,
      };
    },
  },
};

export default model;
