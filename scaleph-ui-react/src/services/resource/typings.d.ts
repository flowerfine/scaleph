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