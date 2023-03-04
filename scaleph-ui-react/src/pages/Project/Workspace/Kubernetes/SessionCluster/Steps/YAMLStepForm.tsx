import {useModel} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";

const SessionClusterYAMLStepForm: React.FC = () => {
  const [editorValue, setEditorValue] = useState<string>()
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {deploymentTemplate} = useModel('sessionClusterStep', (model) => ({
    deploymentTemplate: model.deploymentTemplate
  }));

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    if (deploymentTemplate) {
      try {
        const json = YAML.parse(deploymentTemplate)
        const data = {
          name: json.metadata?.name,
          metadata: json.metadata,
          spec: json.spec
        }
        WsFlinkKubernetesDeploymentTemplateService.asTemplateWithDefault(data).then((response) => {
          if (response.data) {
            setEditorValue(YAML.stringify(response.data))
          }
        })
      } catch (unused) {
      }
    }
  }, [deploymentTemplate]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <ProCard>
      <Editor
        width="730"
        height="600px"
        language="yaml"
        theme="vs-white"
        value={editorValue}
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

  );
}

export default SessionClusterYAMLStepForm
