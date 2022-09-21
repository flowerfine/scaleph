import {request} from "@@/exports";
import {ResponseBody} from "@/app.d";
import {FlinkJobConfigJar} from "@/services/dev/typings";

const url: string = '/api/flink/job-instance/jar';

export async function submit(row: FlinkJobConfigJar) {
  return request<ResponseBody<any>>(`${url}`, {
    method: 'PUT',
    data: {flinkJobConfigJarId: row.id},
  });
}
