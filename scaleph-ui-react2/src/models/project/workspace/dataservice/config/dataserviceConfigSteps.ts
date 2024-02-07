import {Effect, Reducer} from "@umijs/max";
import {DataserviceConfigSaveParam} from "@/services/dataservice/typings";

export interface StateType {
  config: DataserviceConfigSaveParam | null
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    editConfig: Effect;
  };

  reducers: {
    updateConfig: Reducer<StateType>;
  };
}

const model: ModelType = {
  namespace: "dataserviceConfigSteps",

  state: {
    config: null,
  },

  effects: {
    *editConfig({payload}, {call, put}) {
      yield put({
        type: 'updateConfig',
        payload: {
          config: payload
        }
      });
    },
  },

  reducers: {
    updateConfig(state, {payload}) {
      return {
        ...state,
        config: payload.config,
      };
    },
  },
};

export default model;
