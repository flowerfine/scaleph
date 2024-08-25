import {connect} from "umi";
import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import Editor, {loader, Monaco, useMonaco} from "@monaco-editor/react";
import * as monaco from "monaco-editor";
import YAML from "yaml";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";

loader.config({monaco})

const DeploymentYAMLStepForm: React.FC = (props: any) => {
  const editorRef = useRef(null);
  const monaco = useMonaco();

  const [yaml, setYaml] = useState<string>()

  useEffect(() => {
    monaco?.languages.typescript.javascriptDefaults.setEagerModelSync(true);
  }, [monaco]);

  const handleEditorDidMount = (editor, monaco: Monaco) => {
    editorRef.current = editor;
  }

  useEffect(() => {
    if (props.flinkKubernetesDeploymentSteps.deployment) {
      WsFlinkKubernetesDeploymentService.asYaml(props.flinkKubernetesDeploymentSteps.deployment).then(response => {
        if (response.data) {
          setYaml(YAML.stringify(response.data))
        }
      })
    }

  }, [props.flinkKubernetesDeploymentSteps.deployment]);

  return (
    <ProCard>
      <Editor
        width="730"
        height="600px"
        language="yaml"
        theme="vs-white"
        value={props.flinkKubernetesDeploymentSteps.deploymentYaml}
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
const mapModelToProps = ({flinkKubernetesDeploymentSteps}: any) => ({flinkKubernetesDeploymentSteps})
export default connect(mapModelToProps)(DeploymentYAMLStepForm);
