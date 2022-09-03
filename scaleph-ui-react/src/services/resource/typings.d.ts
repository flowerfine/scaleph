export type DiResourceFile = {
    id?: number;
    projectId?: number;
    projectCode?: string;
    fileName?: string;
    fileType?: string;
    filePath?: string;
    fileSize?: number;
    createTime?: Date;
    updateTime?: Date;
}

export type DiResourceFileParam = QueryParam & {
    projectId?: string;
    fileName?: string;
}

export type Jar = {
  id?: number;
  group?: string;
  fileName?: string;
  path?: string;
  remark?: string;
  createTime?: Date;
  updateTime?: Date;
}

export type JarListParam = QueryParam & {
  group?: string;
  fileName?: string;
}

export type JarUploadParam = QueryParam & {
  group?: string;
  file: File;
  remark?: String;
}




