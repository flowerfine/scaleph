import {useModel} from "umi";
import React, {useEffect, useRef, useState} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";

const TemplateEditor: React.FC = () => {
  const [conf, setConf] = useState<string>();

  const confData = "apiVersion: v1\n" +
    "kind: DeploymentDefaults\n" +
    "metadata:\n" +
    "  name: default\n" +
    "  namespace: default\n" +
    "spec:\n" +
    "  deploymentTargetName: '111'\n" +
    "  restoreStrategy:\n" +
    "    allowNonRestoredState: false";

  const editorRef = useRef(null);
  const monaco = useMonaco();

  const {setDeploymentTemplate} = useModel('deploymentTemplateYAMLEditor', (model) => ({
    setDeploymentTemplate: model.setDeploymentTemplate
  }));

  useEffect(() => {
    // do conditional chaining
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    setConf(confData)
  }, []);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  const handleValueChange = (value, event) => {
    setDeploymentTemplate(value)
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={conf}
      options={{
        selectOnLineNumbers: true,
        readOnly: false,
        minimap: {
          enabled: false
        }
      }}
      onMount={handleEditorDidMount}
      onChange={handleValueChange}
    />
  );
}

export default TemplateEditor;
