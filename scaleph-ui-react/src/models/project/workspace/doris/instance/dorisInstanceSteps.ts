import {WsDorisInstance, WsDorisTemplate} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";
import {WsDorisInstanceService} from "@/services/project/WsDorisInstanceService";

export interface StateType {
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
    instance: null,
    instanceYaml: null,
    instanceYamlWithDefault: null
  },

  effects: {
    *editInstance({payload}, {call, put}) {
      const {data} = yield call(WsDorisInstanceService.asYaml, payload);
      const response = yield call(WsDorisInstanceService.asYaml, payload);
      yield put({type: 'updateInstance',
        payload: {
          instance: payload,
          instanceYaml: YAML.stringify(data),
          instanceYamlWithDefault: YAML.stringify(response.data)
        }
      });
    },
  },

  reducers: {
    updateInstance(state, {payload}) {
      return {
        ...state,
        instance: payload.instance,
        instanceYaml: payload.instanceYaml,
        instanceYamlWithDefault: payload.instanceYamlWithDefault,
      };
    },
  },
};

export default model;
