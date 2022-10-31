import JarArtifactOptions from "@/pages/DEV/JobConfig/components/JarArtifact";
import {FlinkJobForJar} from "@/pages/DEV/Job/typings";
import JarResourceOptions from "@/pages/DEV/JobConfig/components/JarResource";

const JarJob: React.FC<{ data: FlinkJobForJar }> = ({data}) => {
  return (<div>
    <JarArtifactOptions data={data}/>
    <JarResourceOptions/>
  </div>);
}

export default JarJob;
