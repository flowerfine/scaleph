import { WORKSPACE_CONF } from "@/constant";

const DiRealtimeJob: React.FC = () => {
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);
    return (<div>Di Realtime Job works + {projectId}</div>);
}

export default DiRealtimeJob;