import SeaTunnel from "@/pages/DEV/JobConfig/components/SeaTunnelArtifact";
import {DiJob} from "@/services/project/typings";
import JarResourceOptions from "@/pages/DEV/JobConfig/components/JarResource";

const SeaTunnelJob: React.FC<{ data: DiJob }> = ({data}) => {
  return (<div>
    <SeaTunnel data={data}/>
    <JarResourceOptions/>
  </div>);
}

export default SeaTunnelJob;
