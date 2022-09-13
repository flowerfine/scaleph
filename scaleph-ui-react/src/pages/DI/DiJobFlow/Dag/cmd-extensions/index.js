import { GraphCutSelectionCommand, NsGraphCutSelection } from './graph-cut';
/** 注册成为可以执行的命令 */

export const commandContributions = [
  // {
  //   ...NsTestCmd,
  //   CommandHandler: TestAsyncCommand,
  // },
  // {
  //   ...NsDeployDagCmd,
  //   CommandHandler: DeployDagCommand,
  // },
  // {
  //   ...NsRenameNodeCmd,
  //   CommandHandler: RenameNodeCommand,
  // },
  {
    ...NsGraphCutSelection,
    CommandHandler: GraphCutSelectionCommand,
  },
];
