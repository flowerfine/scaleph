import {WsDorisOperatorInstance, WsDorisOperatorTemplate} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
import {WsDorisOperatorTemplateService} from "@/services/project/WsDorisOperatorTemplateService";
import {WsDorisOperatorInstanceService} from "@/services/project/WsDorisOperatorInstanceService";

export interface StateType {
  instance: WsDorisOperatorInstance,
  instanceYaml: string
  instanceStatusYaml: string
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
  namespace: "dorisInstanceDetail",

  state: {
    instance: null,
    instanceYaml: null,
    instanceStatusYaml: null
  },

  effects: {
    *editInstance({payload}, {call, put}) {
      const {data} = yield call(WsDorisOperatorInstanceService.selectOne, payload);
      const {yaml} = yield call(WsDorisOperatorInstanceService.asYaml, data);
      const {statusYaml} = yield call(WsDorisOperatorInstanceService.status, data);
      yield put({type: 'updateInstance',
        payload: {
          instance: payload,
          instanceYaml: YAML.stringify(yaml),
          instanceStatusYaml: YAML.stringify(statusYaml)
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
        instanceStatusYaml: payload.instanceStatusYaml,
      };
    },
  },
};

export default model;
