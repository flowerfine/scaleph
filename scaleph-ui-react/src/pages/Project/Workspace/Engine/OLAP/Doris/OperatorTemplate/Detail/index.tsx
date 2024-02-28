import React, {useEffect, useRef, useState} from "react";
import {Divider, Space, Statistic} from "antd";
import {PageContainer, ProCard, StatisticCard} from "@ant-design/pro-components";
import {useIntl, useLocation} from "@umijs/max";
import RcResizeObserver from 'rc-resize-observer';
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {WsDorisOperatorTemplate} from "@/services/project/typings";
import {WsDorisOperatorTemplateService} from "@/services/project/WsDorisOperatorTemplateService";

const EngineOLAPDorisTemplateDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsDorisOperatorTemplate
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
    WsDorisOperatorTemplateService.asYaml(data).then((response) => {
      if (response.success) {
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
        <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.template.detail.component'})}
                       direction={responsive ? 'column' : 'row'}>
          <ProCard bordered hoverable>
            <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.fe'})}
                           statistic={{
                             value: data.feSpec?.replicas,
                             prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                             description: (
                               <Space>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                                   value={data.feSpec?.requests?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                                   value={data.feSpec?.requests?.memory}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                                   value={data.feSpec?.limits?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                                   value={data.feSpec?.limits?.memory}/>
                               </Space>
                             ),
                           }}
                           footer={
                             <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.feSpec?.image ? data.feSpec?.image : '-'}</div>
                           }
            />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'}/>
          <ProCard bordered hoverable>
            <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.be'})}
                           statistic={{
                             value: data.beSpec?.replicas,
                             prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                             description: (
                               <Space>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                                   value={data.beSpec?.requests?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                                   value={data.beSpec?.requests?.memory}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                                   value={data.beSpec?.limits?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                                   value={data.beSpec?.limits?.memory}/>
                               </Space>
                             ),
                           }}
                           footer={
                             <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.beSpec?.image ? data.beSpec?.image : '-'}</div>
                           }
            />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'}/>
          <ProCard bordered hoverable>
            <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.cn'})}
                           statistic={{
                             value: data.cnSpec?.replicas,
                             prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                             description: (
                               <Space>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                                   value={data.cnSpec?.requests?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                                   value={data.cnSpec?.requests?.memory}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                                   value={data.cnSpec?.limits?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                                   value={data.cnSpec?.limits?.memory}/>
                               </Space>
                             ),
                           }}
                           footer={
                             <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.cnSpec?.image ? data.cnSpec?.image : '-'}</div>
                           }
            />
          </ProCard>
          <Divider type={responsive ? 'horizontal' : 'vertical'}/>
          <ProCard bordered hoverable>
            <StatisticCard title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.broker'})}
                           statistic={{
                             value: data.brokerSpec?.replicas,
                             prefix: intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.replicas'}) + ' x',
                             description: (
                               <Space>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.cpu'})}
                                   value={data.brokerSpec?.requests?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.requests.memory'})}
                                   value={data.brokerSpec?.requests?.memory}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.cpu'})}
                                   value={data.brokerSpec?.limits?.cpu}/>
                                 <Statistic
                                   title={intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.limits.memory'})}
                                   value={data.brokerSpec?.limits?.memory}/>
                               </Space>
                             ),
                           }}
                           footer={
                             <div>{intl.formatMessage({id: 'pages.project.doris.template.steps.component.base.image'}) + ": " + data.brokerSpec?.image ? data.brokerSpec?.image : '-'}</div>
                           }
            />
          </ProCard>
        </ProCard.Group>
        <Divider type={'horizontal'}/>
        <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.template.detail.yaml'})}
                       direction={responsive ? 'column' : 'row'}>
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

export default EngineOLAPDorisTemplateDetailWeb;
