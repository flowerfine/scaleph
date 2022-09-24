import { GraphCutSelectionCommand, NsGraphCutSelection } from './graph-cut';
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
];
