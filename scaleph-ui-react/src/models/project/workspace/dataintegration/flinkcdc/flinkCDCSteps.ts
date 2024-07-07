import {WsArtifactFlinkCDC} from "@/services/project/typings";
import {Effect, Reducer} from "umi";
import YAML from "yaml";
import {WsArtifactFlinkCDCService} from "@/services/project/WsArtifactFlinkCDCService";

export interface StateType {
  instance: WsArtifactFlinkCDC | null,
  instanceYaml: string | null
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
  namespace: "flinkCDCSteps",

  state: {
    instance: null,
    instanceYaml: null,
  },

  effects: {
    *editInstance({payload}, {call, put}) {
      const {data} = yield call(WsArtifactFlinkCDCService.preview, payload);
      yield put({
        type: 'updateInstance',
        payload: {
          instance: payload,
          instanceYaml: YAML.stringify(data),
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
      };
    },
  },
};

export default model;
