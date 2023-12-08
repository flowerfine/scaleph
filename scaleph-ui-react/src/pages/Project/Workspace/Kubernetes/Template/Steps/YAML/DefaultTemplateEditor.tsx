import React, {useEffect, useRef} from "react";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {connect} from "umi";

const FlinkKubernetesTemplateYAMLStepDefaultEditor: React.FC = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  return (
    <Editor
      width="730"
      height="600px"
      language="yaml"
      theme="vs-white"
      value={props.flinkKubernetesTemplateSteps.templateYamlWithDefault}
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

const mapModelToProps = ({flinkKubernetesTemplateSteps}: any) => ({flinkKubernetesTemplateSteps})
export default connect(mapModelToProps)(FlinkKubernetesTemplateYAMLStepDefaultEditor);
