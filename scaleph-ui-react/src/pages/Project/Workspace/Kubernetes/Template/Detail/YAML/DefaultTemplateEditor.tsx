import {useModel} from "umi";
import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {
  WsFlinkKubernetesTemplateService
} from "@/services/project/WsFlinkKubernetesTemplateService";

const DefaultTemplateEditor: React.FC = () => {
  const [editorValue, setEditorValue] = useState<string>()
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {deploymentTemplate} = useModel('deploymentTemplateYAMLEditor', (model) => ({
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
        WsFlinkKubernetesTemplateService.asTemplateWithDefault(data).then((response) => {
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
  );
}

export default DefaultTemplateEditor;
