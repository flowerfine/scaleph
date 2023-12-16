import {useIntl} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {WsDorisOperatorInstance} from "@/services/project/typings";
import {ProCard} from "@ant-design/pro-components";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import {WsDorisOperatorInstanceService} from "@/services/project/WsDorisOperatorInstanceService";
import YAML from "yaml";

const DorisInstanceDetailYAML: React.FC<{ data: WsDorisOperatorInstance }> = ({data}) => {
  const intl = useIntl();
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [yaml, setYaml] = useState<string>(null);

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  useEffect(() => {
    const yamlData = {...data}
    yamlData.deployed = undefined
    WsDorisOperatorInstanceService.asYaml(yamlData).then((response) => {
      if (response.success) {
        setYaml(YAML.stringify(response.data))
      }
    });
  }, []);

  return (
    <ProCard.Group title={intl.formatMessage({id: 'pages.project.doris.instance.detail.yaml'})}
                   direction={'row'}>
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
  );
}

export default DorisInstanceDetailYAML;
