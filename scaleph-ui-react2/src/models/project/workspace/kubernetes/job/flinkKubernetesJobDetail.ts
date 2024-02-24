import {WsFlinkKubernetesJob} from "@/services/project/typings";
import {Effect, Reducer} from "@umijs/max";
import {WsFlinkKubernetesJobService} from "@/services/project/WsFlinkKubernetesJobService";

export interface StateType {
  job: WsFlinkKubernetesJob,
}

export interface ModelType {
  namespace: string;

  state: StateType;

  effects: {
    queryJob: Effect;
  };

  reducers: {
    updateJob: Reducer<StateType>;
  };
}

const model: ModelType = {
  namespace: "flinkKubernetesJobDetail",

  state: {
    job: null
  },

  effects: {
    *queryJob({payload}, {call, put}) {
      const {data} = yield call(WsFlinkKubernetesJobService.selectOne, payload);
      yield put({type: 'updateJob', payload: {job: data}});
    },
  },

  reducers: {
    updateJob(state, {payload}) {
      return {
        ...state,
        job: payload.job
      };
    },
  },
};

export default model;
