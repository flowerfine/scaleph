import {WsDorisOperatorInstance} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
import {WsDorisOperatorInstanceService} from "@/services/project/WsDorisOperatorInstanceService";

export interface StateType {
  instance: WsDorisOperatorInstance,
  instanceYaml: string
  instanceYamlWithDefault: string
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    editInstance: Effect;
  };

  reducers: {
    updateInstance: Reducer<StateType>;
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
      const {data} = yield call(WsDorisOperatorInstanceService.asYaml, payload);
      const response = yield call(WsDorisOperatorInstanceService.asYaml, payload);
      yield put({
        type: 'updateInstance',
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
