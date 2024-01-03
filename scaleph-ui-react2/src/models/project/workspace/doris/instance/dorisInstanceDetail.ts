import {WsDorisOperatorInstance} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
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
      const param = {...data}
      param.deployed = undefined
      const response = yield call(WsDorisOperatorInstanceService.asYaml, param);
      const statusReponse = yield call(WsDorisOperatorInstanceService.status, param);
      yield put({
        type: 'updateInstance',
        payload: {
          instance: data,
          instanceYaml: YAML.stringify(response.data),
          instanceStatusYaml: YAML.stringify(statusReponse.data)
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
