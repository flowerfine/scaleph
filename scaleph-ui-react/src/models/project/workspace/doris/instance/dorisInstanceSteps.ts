import {WsDorisInstance, WsDorisTemplate} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";

export interface StateType {
  template: WsDorisTemplate,
  instance: WsDorisInstance,
  instanceYaml: string
  instanceYamlWithDefault: string
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
  namespace: "dorisInstanceSteps",

  state: {
    template: null,
    templateYaml: null,
    templateYamlWithDefault: null
  },

  effects: {
    *editTemplate({payload}, {call, put}) {
      const {data} = yield call(WsDorisTemplateService.asYaml, payload);
      const response = yield call(WsDorisTemplateService.asYaml, payload);
      yield put({type: 'updateTemplate',
        payload: {
          template: payload,
          templateYaml: YAML.stringify(data),
          templateYamlWithDefault: YAML.stringify(response.data)
        }
      });
    },
  },

  reducers: {
    updateTemplate(state, {payload}) {
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
