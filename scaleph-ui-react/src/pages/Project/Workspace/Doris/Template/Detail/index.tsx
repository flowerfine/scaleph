import {useIntl, useLocation} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {PageContainer, ProCard} from "@ant-design/pro-components";
import {Divider, Statistic} from "antd";
import RcResizeObserver from 'rc-resize-observer';
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";
import YAML from "yaml";

const DorisTemplateDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesTemplate
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [yaml, setYaml] = useState<string>(null);
  const [responsive, setResponsive] = useState(false);

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  useEffect(() => {
    WsDorisTemplateService.asYaml(data).then((response) => {
      if (response.success) {
        console.log('WsDorisTemplateService', response)
        setYaml(YAML.stringify(response.data))
      }
    });
  }, []);

  return (
    <PageContainer title={intl.formatMessage({id: 'pages.project.doris.template.detail'})}>
      <RcResizeObserver
        key="resize-observer"
        onResize={(offset) => {
          setResponsive(offset.width < 596);
        }}
      >
        <ProCard.Group title="集群组件" direction={responsive ? 'column' : 'row'}>
          <ProCard bordered hoverable>
            <Statistic title="FE节点" value={79.0} precision={2}/>
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'}/>
          <ProCard bordered hoverable>
            <Statistic title="BE节点" value={112893.0} precision={2}/>
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'}/>
          <ProCard bordered hoverable>
            <Statistic title="计算节点" value={93} suffix="/ 100"/>
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'}/>
          <ProCard bordered hoverable>
            <Statistic title="Broker节点" value={112893.0}/>
          </ProCard>
        </ProCard.Group>
        <Divider type={'horizontal'}/>
        <ProCard.Group title="YAML" direction={responsive ? 'column' : 'row'}>
          <ProCard bordered>
            <Editor
              width="730"
              height="600px"
              language="yaml"
              theme="vs-white"
              value={yaml}
              options={{
                selectOnLineNumbers: true,
                readOnly: true,
                minimap: {
                  enabled: false
                }
              }}
              onMount={handleEditorDidMount}
            />
          </ProCard>
        </ProCard.Group>
      </RcResizeObserver>
    </PageContainer>
  );
}

export default DorisTemplateDetailWeb;
