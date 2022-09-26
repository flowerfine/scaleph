import { GraphCutSelectionCommand, NsGraphCutSelection } from './graph-cut';
import { GraphParamsSettingCommand, NsGraphParamsSetting } from './graph-params-modal';
import { GraphPublishCommand, NsGraphPublish } from './graph-publish';
import { EditNodeCommand, NsEditNode } from './step-edit-modal';
/** 注册成为可以执行的命令 */

export const commandContributions = [
  {
    ...NsGraphCutSelection,
    CommandHandler: GraphCutSelectionCommand,
  },
  {
    ...NsEditNode,
    CommandHandler: EditNodeCommand,
  },
  {
    ...NsGraphParamsSetting,
    CommandHandler: GraphParamsSettingCommand,
  },
  {
    ...NsGraphPublish,
    CommandHandler: GraphPublishCommand,
  },
];
