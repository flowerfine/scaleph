import { Deferred, ICmdHooks as IHooks, IGraphCommandService, IModelService, NsGraph } from '@antv/xflow'
import type { HookHub } from '@antv/xflow-hook'
import { IArgsBase, ICommandHandler } from '@antv/xflow'
import { ICommandContextProvider, ManaSyringe } from '@antv/xflow'
import { CustomCommands } from '../constant'
import { ResponseBody } from '@/app.d'
import { ConfigProvider, Form, FormInstance, Input, Modal } from 'antd'
import SourceJdbcStepForm from '../steps/source-jdbc-step'
import { render, unmount } from 'rc-util/lib/React/render'

const { inject, injectable, postConstruct } = ManaSyringe
type ICommand = ICommandHandler<NsEditNode.IArgs, NsEditNode.IResult, NsEditNode.ICmdHooks>

export namespace NsEditNode {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.NODE_EDIT
  /** hook name */
  export const hookKey = 'editNode'
  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {
    nodeConfig: NsGraph.INodeConfig,
    editNodeService: IEditNodeService
  }

  export interface IEditNodeService {
    (graphData: NsGraph.IGraphData, graphMeta: NsGraph.IGraphMeta): Promise<ResponseBody<any>>
  }

  /** hook handler 返回类型 */
  export interface IResult {
    test: any
  }
  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    editNode: HookHub<IArgs, IResult>
  }
}

@injectable({
  token: { token: ICommandHandler, named: NsEditNode.command.id },
})
/** 创建节点命令 */
export class EditNodeCommand implements ICommand {
  /** api */
  @inject(ICommandContextProvider) contextProvider: ICommand['contextProvider'];

  @postConstruct()
  init() {

  }

  /** 执行Cmd */
  execute = async () => {
    const ctx = this.contextProvider()
    const hooks = ctx.getHooks()
    const { args, hooks: runtimeHook } = ctx.getArgs()

    const result = await hooks.editNode.call(
      args,
      async handlerArgs => {
        const { commandService, modelService, editNodeService, nodeConfig } = handlerArgs
        const graphMeta = await ctx.getGraphMeta();
        const x6Graph = await ctx.getX6Graph();
        const x6Nodes = x6Graph.getNodes();
        const x6Edges = x6Graph.getEdges();

        const nodes = x6Nodes.map(node => {
          const data = node.getData<NsGraph.INodeConfig>();
          const position = node.position();
          const size = node.size();
          const model = {
            ...data,
            ...position,
            ...size,
          }
          return model
        });
        const edges = x6Edges.map(edge => {
          const data = edge.getData<NsGraph.IEdgeConfig>();
          const model = {
            ...data,
          }
          return model;
        });
        const graphData = { nodes, edges };
        console.log('13413241234', x6Graph, commandService, modelService, graphData);
        showModal(true);
        // showModal(nodeConfig, graphData, graphMeta, commandService, modelService, editNodeService);


        // await ctx.getGraphMeta().then(d => {
        //   console.log('args 01', d.origin);
        // });
        return { test: true }
      },
      runtimeHook,
    )
    ctx.setResult(result || { test: '' })
    return this
  }

  /** undo cmd */
  undo = async () => {
    if (this.isUndoable()) {
      const ctx = this.contextProvider();
      ctx.undo();
    }
    return this
  }

  /** redo cmd */
  redo = async () => {
    if (!this.isUndoable()) {
      await this.execute();
    }
    return this;
  }

  isUndoable(): boolean {
    const ctx = this.contextProvider();
    return ctx.isUndoable();
  }
}

export type IModalInstance = ReturnType<typeof Modal.confirm>

function showModal(
  visible: boolean) {
  const container = document.createDocumentFragment();
  return (
    render(
      <SourceJdbcStepForm
        data=""
        visible={true}
        onVisibleChange={(visible) => { if (visible) { unmount(container) } }}
        onCancel={() => { unmount(container) }}
      ></SourceJdbcStepForm>,
      container
    )

  )
}

// function showModal(
//   node: NsGraph.INodeConfig,
//   graphData: NsGraph.IGraphData,
//   graphMeta: NsGraph.IGraphMeta,
//   commandService: IGraphCommandService,
//   modelService: IModelService,
//   editNodeService: NsEditNode.IEditNodeService) {

//   const defer = new Deferred<string | void>();

//   class ModalCache {
//     static modal: IModalInstance;
//     static form: FormInstance<any>;
//   }

//   /** modal确认保存逻辑 */
//   const onOk = async () => {
//     const { form, modal } = ModalCache

//     try {
//       modal.update({ okButtonProps: { loading: true } })
//       await form.validateFields();
//       const values = await form.getFieldsValue();
//       // const newName: string = values.newNodeName
//       /** 执行 backend service */
//       if (editNodeService) {
//         await editNodeService(graphData, graphMeta)

//         defer.resolve('');
//       }
//       /** 更新成功后，关闭modal */
//       onHide()
//     } catch (error) {
//       console.error(error)
//       /** 如果resolve空字符串则不更新 */
//       modal.update({ okButtonProps: { loading: false } })
//     }
//   }

//   /** modal销毁逻辑 */
//   const onHide = () => {
//     modal.destroy()
//     ModalCache.form = null as any
//     ModalCache.modal = null as any
//     container.destroy()
//   }

//   /** modal内容 */
//   const ModalContent = () => {
//     const [form] = Form.useForm<any>();
//     /** 缓存form实例 */
//     ModalCache.form = form

//     return <>asfasdf</>
//   }
//   /** 创建modal dom容器 */
//   const container = createContainer()
//   /** 创建modal */
//   const modal = Modal.confirm({
//     title: '重命名',
//     content: <ModalContent />,
//     getContainer: () => {
//       return container.element
//     },
//     okButtonProps: {
//       onClick: e => {
//         e.stopPropagation()
//         onOk()
//       },
//     },
//     onCancel: () => {
//       onHide()
//     },
//     afterClose: () => {
//       onHide()
//     },
//   })

//   /** 缓存modal实例 */
//   // ModalCache.modal = modal

//   /** showModal 返回一个Promise，用于await */
//   return defer.promise
// }

const createContainer = () => {
  const div = document.createElement('div')
  div.classList.add('xflow-modal-container')
  window.document.body.append(div)
  console.log(12312123);
  return {
    element: div,
    destroy: () => {
      window.document.body.removeChild(div)
    },
  }
}
