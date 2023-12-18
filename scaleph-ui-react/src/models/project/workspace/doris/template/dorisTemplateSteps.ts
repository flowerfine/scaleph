import {WsDorisOperatorTemplate} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
import {WsDorisOperatorTemplateService} from "@/services/project/WsDorisOperatorTemplateService";

export interface StateType {
  template: WsDorisOperatorTemplate,
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
  namespace: "dorisTemplateSteps",

  state: {
    template: null,
    templateYaml: null,
    templateYamlWithDefault: null
  },

  effects: {
    *editTemplate({payload}, {call, put}) {
      const {data} = yield call(WsDorisOperatorTemplateService.asYaml, payload);
      const response = yield call(WsDorisOperatorTemplateService.asYaml, payload);
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
