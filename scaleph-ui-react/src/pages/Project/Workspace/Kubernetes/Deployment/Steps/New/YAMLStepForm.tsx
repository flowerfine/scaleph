import React, {useEffect, useRef, useState} from "react";
import {ProCard} from "@ant-design/pro-components";
import {connect} from "@umijs/max";
import Editor, {Monaco, useMonaco} from "@monaco-editor/react";
import YAML from "yaml";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";

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
