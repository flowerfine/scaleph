declare namespace Scaleph {
  type LogLogin = {
    id?: number;
    userName: string;
    loginTime: Date;
    ipAddress: string;
    loginType: Scaleph.Dict;
    clientInfo?: string;
    osInfo?: string;
    browserInfo?: string;
    actionInfo?: string;
    createTime?: Date;
    updateTime?: Date;
  };
}
