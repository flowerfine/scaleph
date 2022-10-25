import SeaTunnel from "@/pages/DEV/JobConfig/components/SeaTunnelArtifact";
import SeaTunnelEnv from "@/pages/DEV/JobConfig/components/SeaTunnelEnv";
import {DiJob} from "@/services/project/typings";

const SeaTunnelJob: React.FC<{data: DiJob}> = ({data}) => {
  return (<div>
    <SeaTunnel data={data}/>
    <SeaTunnelEnv/>
  </div>);
}

export default SeaTunnelJob;
