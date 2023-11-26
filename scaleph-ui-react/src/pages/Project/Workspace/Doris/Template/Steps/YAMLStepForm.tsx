import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {Form} from "antd";

const DorisTemplateYAML: React.FC = () => {
  const editorRef = useRef(null);
  const monaco = useMonaco();
  const [form] = Form.useForm()

  const [yaml, setYaml] = useState<string>()

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  useEffect(() => {
    const values: Record<string, any> = form.getFieldsValue(true)
    console.log('DorisTemplateYAML', values)
  }, []);

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

  );
}

export default DorisTemplateYAML;
