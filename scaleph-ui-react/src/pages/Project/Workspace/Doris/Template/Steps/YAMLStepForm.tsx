import React, {useEffect, useRef} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {connect} from "umi";

const DorisTemplateYAML: React.FC = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

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
        value={props.dorisTemplateSteps.templateYaml}
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

const mapModelToProps = ({dorisTemplateSteps}: any) => ({dorisTemplateSteps})
export default connect(mapModelToProps)(DorisTemplateYAML);
