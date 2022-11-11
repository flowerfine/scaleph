import enUS from 'antd/es/locale/en_US';
import zhCN from 'antd/es/locale/zh_CN';

import {ModalFormProps} from '@/app.d';
import {
  IArgsBase,
  ICmdHooks as IHooks,
  ICommandContextProvider,
  ICommandHandler,
  ManaSyringe,
  NsGraph,
} from '@antv/xflow';
import type {HookHub} from '@antv/xflow-hook';
import {ConfigProvider, Modal} from 'antd';
import {render, unmount} from 'rc-util/lib/React/render';
import React, {useEffect, useRef, useState} from 'react';
import {getLocale} from 'umi';
import {CustomCommands} from '../constant';
import {JobService} from "@/services/project/job.service";
import Editor, {useMonaco} from "@monaco-editor/react";

const {inject, injectable, postConstruct} = ManaSyringe;

type ICommand = ICommandHandler<NsGraphPreview.IArgs,
  NsGraphPreview.IResult,
  NsGraphPreview.ICmdHooks>;

export namespace NsGraphPreview {
  /** Command: 用于注册named factory */
  export const command = CustomCommands.GRAPH_PREVIEW;
  /** hook name */
  export const hookKey = 'graphPreview';

  /** hook 参数类型 */
  export interface IArgs extends IArgsBase {
  }

  /** hook handler 返回类型 */
  export interface IResult {
    err: any;
  }

  /** hooks 类型 */
  export interface ICmdHooks extends IHooks {
    graphPreview: HookHub<IArgs, IResult>;
  }
}

@injectable({
  token: {token: ICommandHandler, named: NsGraphPreview.command.id}
})
/** 创建节点命令 */
export class GraphPreviewCommand implements ICommand {
  /** api */
  @inject(ICommandContextProvider) contextProvider: ICommand['contextProvider'];

  @postConstruct()
  init() {
  }

  /** 执行Cmd */
  execute = async () => {
    const ctx = this.contextProvider();
    const hooks = ctx.getHooks();
    const {args, hooks: runtimeHook} = ctx.getArgs();
    const result = await hooks.graphPreview.call(
      args,
      async () => {
        const graphMeta = await ctx.getGraphMeta();
        this.showModal(graphMeta);
        return {err: null};
      },
      runtimeHook,
    );
    ctx.setResult(result || {err: null});
    return this;
  };

  /** undo cmd */
  undo = async () => {
    if (this.isUndoable()) {
      const ctx = this.contextProvider();
      ctx.undo();
    }
    return this;
  };

  /** redo cmd */
  redo = async () => {
    if (!this.isUndoable()) {
      await this.execute();
    }
    return this;
  };

  isUndoable(): boolean {
    const ctx = this.contextProvider();
    return ctx.isUndoable();
  }

  getCurrentLocale = () => {
    const local: string = getLocale() as string;
    switch (local) {
      case 'zh-CN':
        return zhCN;
      case 'en-US':
        return enUS;
      default:
        return zhCN;
    }
  };

  showModal = (graphMeta: NsGraph.IGraphMeta) => {
    const container = document.createDocumentFragment();
    const data = {graphMeta: graphMeta, container: container};
    return render(
      <ConfigProvider locale={this.getCurrentLocale()}>
        <GraphPreviewForm
          visible
          data={data}
          onCancel={() => this.onCancel(container)}
        />
      </ConfigProvider>,
      container,
    );
  };

  onCancel = (container: DocumentFragment) => {
    unmount(container);
  };
}

const GraphPreviewForm: React.FC<ModalFormProps<{ graphMeta: NsGraph.IGraphMeta; container: DocumentFragment }>> = ({
                                                                                                                      data,
                                                                                                                      visible,
                                                                                                                      onCancel
                                                                                                                    }) => {
  const jobId = data.graphMeta.origin.id;
  const [conf, setConf] = useState<string>();

  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
    // or make sure that it exists by other ways
    if (monaco) {
      console.log("here is the monaco instance:", monaco);
    }
  }, [monaco]);

  useEffect(() => {
    JobService.previewJob(jobId).then((reponse) => {
      setConf(reponse.data)
    })
  }, []);

  const handleEditorDidMount = (editor, monaco) => {
    editorRef.current = editor;
  }

  return (
    <Modal
      open={visible}
      title={false}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onCancel={onCancel}
      footer={false}
    >
      <Editor
        width="730"
        height="600px"
        language="json"
        theme="vs-dark"
        value={conf}
        options={{
          selectOnLineNumbers: true,
          readOnly: true
        }}
        onMount={handleEditorDidMount}
      />
    </Modal>
  );
};
