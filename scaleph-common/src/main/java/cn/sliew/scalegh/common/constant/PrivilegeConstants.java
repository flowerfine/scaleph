package cn.sliew.scalegh.common.constant;

import cn.sliew.scalegh.common.annotation.Desc;

/**
 * 权限编码
 * 1位前缀+3位模块code
 *
 * @author gleiyu
 */
public interface PrivilegeConstants {

    /**
     * 权限编码前缀
     */
    String PRIVILEGE_PREFIX = "p";
    /**
     * 角色编码前缀
     */
    String ROLE_PREFIX = "r";

    /**
     * 系统菜单
     */
    public interface ModuleCode {
        /**
         * 系统管理
         */
        String ADMIN = "adm";
        /**
         * 数据字典
         */
        String DICT = "dic";
        /**
         * 字典类型管理
         */
        String DICT_TYPE = "dct";
        /**
         * 数据字典管理
         */
        String DICT_DATA = "dcd";
        /**
         * 用户管理
         */
        String USER = "usr";
        /**
         * 角色管理
         */
        String ROLE = "rol";
        /**
         * 权限管理
         */
        String PRIVILEGE = "pvg";
        /**
         * 部门管理
         */
        String DEPT = "dep";

        /**
         * 参数设置
         */
        String SETTING = "set";
        /**
         * 元数据管理
         */
        String DATADEV = "dev";
        /**
         * 元数据-数据源管理
         */
        String DATADEV_DATASOURCE = "dts";

        /**
         * 项目管理
         */
        String DATADEV_PROJECT = "ddp";
        /**
         * 作业管理
         */
        String DATADEV_JOB = "ddj";

        /**
         * 目录管理
         */
        String DATADEV_DIR = "ddr";

        /**
         * 资源管理
         */
        String DATADEV_RESOURCE = "dde";
        /**
         * 集群管理
         */
        String DATADEV_CLUSTER = "ddc";

        /**
         * 数据标准
         */
        String STDATA = "std";
        /**
         * 数据标准-业务系统管理
         */
        String STDATA_SYSTEM = "sts";
        /**
         * 数据标准-参考数据类型管理
         */
        String STDATA_REF_DATA_TYPE = "stt";
        /**
         * 数据标准-参考数据管理
         */
        String STDATA_REF_DATA = "str";
        /**
         * 数据标准-参考数据映射管理
         */
        String STDATA_REF_DATA_MAP = "stm";
        /**
         * 数据标准-数据元管理
         */
        String STDATA_DATA_ELEMENT = "ste";
        /**
         * 工作台
         */
        String STUDIO = "sdo";
        String STUDIO_BOARD = "sdb";

        /**
         * 运维中心
         */
        String OPSCENTER = "opc";
        String OPSCENTER_BATCH = "obt";
        String OPSCENTER_REALTIME = "ort";
    }

    /**
     * 操作权限
     */
    public interface ActionCode {
        /**
         * 菜单显示
         */
        String SHOW = "0";
        /**
         * 新增
         */
        String ADD = "1";
        /**
         * 修改
         */
        String EDIT = "2";
        /**
         * 删除
         */
        String DELETE = "3";
        /**
         * 查询
         */
        String SELECT = "4";
        /**
         * 授权
         */
        String GRANT = "5";
        /**
         * 查看加密敏感信息
         */
        String SECURITY = "6";
        /**
         * 下载
         */
        String DOWNLOAD = "7";
    }

    /**
     * 0-菜单权限
     */
    @Desc("{\"id\":1,\"privilegeName\":\"系统管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String ADMIN_SHOW = PRIVILEGE_PREFIX + ModuleCode.ADMIN + ActionCode.SHOW;
    @Desc("{\"id\":2,\"privilegeName\":\"用户管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":1}")
    String USER_SHOW = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.SHOW;
    @Desc("{\"id\":3,\"privilegeName\":\"权限管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":1}")
    String PRIVILEGE_SHOW = PRIVILEGE_PREFIX + ModuleCode.PRIVILEGE + ActionCode.SHOW;
    @Desc("{\"id\":4,\"privilegeName\":\"数据字典\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":1}")
    String DICT_SHOW = PRIVILEGE_PREFIX + ModuleCode.DICT + ActionCode.SHOW;
    @Desc("{\"id\":5,\"privilegeName\":\"系统设置\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":1}")
    String SETTING_SHOW = PRIVILEGE_PREFIX + ModuleCode.SETTING + ActionCode.SHOW;
    @Desc("{\"id\":6,\"privilegeName\":\"数据开发\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV + ActionCode.SHOW;
    @Desc("{\"id\":7,\"privilegeName\":\"数据源\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":6}")
    String DATADEV_DATASOURCE_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.SHOW;
    @Desc("{\"id\":8,\"privilegeName\":\"项目管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_PROJECT_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.SHOW;
    @Desc("{\"id\":9,\"privilegeName\":\"作业管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_JOB_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.SHOW;
    @Desc("{\"id\":10,\"privilegeName\":\"资源管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_RESOURCE_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.SHOW;
    @Desc("{\"id\":11,\"privilegeName\":\"集群管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_CLUSTER_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.SHOW;
    @Desc("{\"id\":12,\"privilegeName\":\"数据标准\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String STDATA_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA + ActionCode.SHOW;
    @Desc("{\"id\":13,\"privilegeName\":\"参考数据\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":8}")
    String STDATA_REF_DATA_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.SHOW;
    @Desc("{\"id\":14,\"privilegeName\":\"数据映射\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":8}")
    String STDATA_REF_DATA_MAP_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.SHOW;
    @Desc("{\"id\":15,\"privilegeName\":\"数据元\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":8}")
    String STDATA_DATA_ELEMENT_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.SHOW;
    @Desc("{\"id\":16,\"privilegeName\":\"业务系统\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":8}")
    String STDATA_SYSTEM_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.SHOW;
    @Desc("{\"id\":17,\"privilegeName\":\"运维中心\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String OPSCENTER_SHOW = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER + ActionCode.SHOW;
    @Desc("{\"id\":18,\"privilegeName\":\"周期任务\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String OPSCENTER_BATCH_SHOW = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_BATCH + ActionCode.SHOW;
    @Desc("{\"id\":19,\"privilegeName\":\"实时任务\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String OPSCENTER_REALTIME_SHOW = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_REALTIME + ActionCode.SHOW;
    @Desc("{\"id\":20,\"privilegeName\":\"工作台\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String STUDIO_SHOW = PRIVILEGE_PREFIX + ModuleCode.STUDIO + ActionCode.SHOW;
    @Desc("{\"id\":21,\"privilegeName\":\"数据看板\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    String STUDIO_BOARD_SHOW = PRIVILEGE_PREFIX + ModuleCode.STUDIO_BOARD + ActionCode.SHOW;
    /**
     * 1-操作权限
     */
    @Desc("{\"id\":100001,\"privilegeName\":\"字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DICT_TYPE_SELECT = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.SELECT;
    @Desc("{\"id\":100002,\"privilegeName\":\"新增字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100001}")
    String DICT_TYPE_ADD = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.ADD;
    @Desc("{\"id\":100003,\"privilegeName\":\"删除字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100001}")
    String DICT_TYPE_DELETE = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.DELETE;
    @Desc("{\"id\":100004,\"privilegeName\":\"修改字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100001}")
    String DICT_TYPE_EDIT = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.EDIT;

    @Desc("{\"id\":100005,\"privilegeName\":\"数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DICT_DATA_SELECT = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.SELECT;
    @Desc("{\"id\":100006,\"privilegeName\":\"新增数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100005}")
    String DICT_DATA_ADD = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.ADD;
    @Desc("{\"id\":100007,\"privilegeName\":\"删除数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100005}")
    String DICT_DATA_DELETE = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.DELETE;
    @Desc("{\"id\":100008,\"privilegeName\":\"修改数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100005}")
    String DICT_DATA_EDIT = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.EDIT;

    @Desc("{\"id\":100009,\"privilegeName\":\"用户管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String USER_SELECT = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.SELECT;
    @Desc("{\"id\":100010,\"privilegeName\":\"新增用户\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100009}")
    String USER_ADD = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.ADD;
    @Desc("{\"id\":100011,\"privilegeName\":\"删除用户\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100009}")
    String USER_DELETE = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.DELETE;
    @Desc("{\"id\":100012,\"privilegeName\":\"修改用户\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100009}")
    String USER_EDIT = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.EDIT;

    @Desc("{\"id\":100013,\"privilegeName\":\"角色管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String ROLE_SELECT = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.SELECT;
    @Desc("{\"id\":100014,\"privilegeName\":\"新增角色\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100013}")
    String ROLE_ADD = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.ADD;
    @Desc("{\"id\":100015,\"privilegeName\":\"删除角色\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100013}")
    String ROLE_DELETE = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.DELETE;
    @Desc("{\"id\":100016,\"privilegeName\":\"修改角色\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100013}")
    String ROLE_EDIT = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.EDIT;
    @Desc("{\"id\":100017,\"privilegeName\":\"角色授权\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100013}")
    String ROLE_GRANT = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.GRANT;

    @Desc("{\"id\":100018,\"privilegeName\":\"部门管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DEPT_SELECT = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.SELECT;
    @Desc("{\"id\":100019,\"privilegeName\":\"新增部门\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100018}")
    String DEPT_ADD = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.ADD;
    @Desc("{\"id\":100020,\"privilegeName\":\"删除部门\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100018}")
    String DEPT_DELETE = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.DELETE;
    @Desc("{\"id\":100021,\"privilegeName\":\"修改部门\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100018}")
    String DEPT_EDIT = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.EDIT;
    @Desc("{\"id\":100022,\"privilegeName\":\"部门授权\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100018}")
    String DEPT_GRANT = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.GRANT;


    @Desc("{\"id\":100023,\"privilegeName\":\"数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_DATASOURCE_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.SELECT;
    @Desc("{\"id\":100024,\"privilegeName\":\"新增数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100023}")
    String DATADEV_DATASOURCE_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.ADD;
    @Desc("{\"id\":100025,\"privilegeName\":\"删除数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100023}")
    String DATADEV_DATASOURCE_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.DELETE;
    @Desc("{\"id\":100026,\"privilegeName\":\"修改数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100023}")
    String DATADEV_DATASOURCE_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.EDIT;
    @Desc("{\"id\":100027,\"privilegeName\":\"查看密码\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100023}")
    String DATADEV_DATASOURCE_SECURITY = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.SECURITY;

    @Desc("{\"id\":100028,\"privilegeName\":\"业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String STDATA_SYSTEM_SELECT = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.SELECT;
    @Desc("{\"id\":100029,\"privilegeName\":\"新增业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100028}")
    String STDATA_SYSTEM_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.ADD;
    @Desc("{\"id\":100030,\"privilegeName\":\"删除业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100028}")
    String STDATA_SYSTEM_DELETE = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.DELETE;
    @Desc("{\"id\":100031,\"privilegeName\":\"修改业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100028}")
    String STDATA_SYSTEM_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.EDIT;

    @Desc("{\"id\":100032,\"privilegeName\":\"项目管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_PROJECT_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.SELECT;
    @Desc("{\"id\":100033,\"privilegeName\":\"新增项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100032}")
    String DATADEV_PROJECT_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.ADD;
    @Desc("{\"id\":100034,\"privilegeName\":\"删除项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100032}")
    String DATADEV_PROJECT_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.DELETE;
    @Desc("{\"id\":100035,\"privilegeName\":\"修改项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100032}")
    String DATADEV_PROJECT_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.EDIT;

    @Desc("{\"id\":100036,\"privilegeName\":\"作业管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_JOB_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.SELECT;
    @Desc("{\"id\":100037,\"privilegeName\":\"新增项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100036}")
    String DATADEV_JOB_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.ADD;
    @Desc("{\"id\":100038,\"privilegeName\":\"删除项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100036}")
    String DATADEV_JOB_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.DELETE;
    @Desc("{\"id\":100039,\"privilegeName\":\"修改项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100036}")
    String DATADEV_JOB_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.EDIT;

    @Desc("{\"id\":100040,\"privilegeName\":\"目录管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_DIR_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.SELECT;
    @Desc("{\"id\":100041,\"privilegeName\":\"新增目录\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100040}")
    String DATADEV_DIR_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.ADD;
    @Desc("{\"id\":100042,\"privilegeName\":\"删除目录\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100040}")
    String DATADEV_DIR_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.DELETE;
    @Desc("{\"id\":100043,\"privilegeName\":\"修改目录\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100040}")
    String DATADEV_DIR_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.EDIT;

    @Desc("{\"id\":100044,\"privilegeName\":\"资源管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_RESOURCE_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.SELECT;
    @Desc("{\"id\":100045,\"privilegeName\":\"新增资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100044}")
    String DATADEV_RESOURCE_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.ADD;
    @Desc("{\"id\":100046,\"privilegeName\":\"删除资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100044}")
    String DATADEV_RESOURCE_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.DELETE;
    @Desc("{\"id\":100047,\"privilegeName\":\"修改资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100044}")
    String DATADEV_RESOURCE_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.EDIT;
    @Desc("{\"id\":100048,\"privilegeName\":\"下载资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100044}")
    String DATADEV_RESOURCE_DOWNLOAD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.DOWNLOAD;

    @Desc("{\"id\":100049,\"privilegeName\":\"集群管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String DATADEV_CLUSTER_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.SELECT;
    @Desc("{\"id\":100050,\"privilegeName\":\"新增集群\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100049}")
    String DATADEV_CLUSTER_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.ADD;
    @Desc("{\"id\":100051,\"privilegeName\":\"删除集群\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100049}")
    String DATADEV_CLUSTER_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.DELETE;
    @Desc("{\"id\":100052,\"privilegeName\":\"修改集群\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100049}")
    String DATADEV_CLUSTER_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.EDIT;

    @Desc("{\"id\":100053,\"privilegeName\":\"数据元管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String STDATA_DATA_ELEMENT_SELECT = PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.SELECT;
    @Desc("{\"id\":100054,\"privilegeName\":\"新增数据元\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100053}")
    String STDATA_DATA_ELEMENT_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.ADD;
    @Desc("{\"id\":100055,\"privilegeName\":\"删除数据元\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100053}")
    String STDATA_DATA_ELEMENT_DELETE = PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.DELETE;
    @Desc("{\"id\":100056,\"privilegeName\":\"修改数据元\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100053}")
    String STDATA_DATA_ELEMENT_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.EDIT;

    @Desc("{\"id\":100057,\"privilegeName\":\"参考数据类型管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String STDATA_REF_DATA_TYPE_SELECT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.SELECT;
    @Desc("{\"id\":100058,\"privilegeName\":\"新增参考数据类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100057}")
    String STDATA_REF_DATA_TYPE_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.ADD;
    @Desc("{\"id\":100059,\"privilegeName\":\"删除参考数据类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100057}")
    String STDATA_REF_DATA_TYPE_DELETE = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.DELETE;
    @Desc("{\"id\":100060,\"privilegeName\":\"修改参考数据类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100057}")
    String STDATA_REF_DATA_TYPE_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.EDIT;

    @Desc("{\"id\":100061,\"privilegeName\":\"参考数据管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String STDATA_REF_DATA_SELECT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.SELECT;
    @Desc("{\"id\":100062,\"privilegeName\":\"新增参考数据\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100061}")
    String STDATA_REF_DATA_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.ADD;
    @Desc("{\"id\":100063,\"privilegeName\":\"删除参考数据\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100061}")
    String STDATA_REF_DATA_DELETE = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.DELETE;
    @Desc("{\"id\":100064,\"privilegeName\":\"修改参考数据\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100061}")
    String STDATA_REF_DATA_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.EDIT;

    @Desc("{\"id\":100065,\"privilegeName\":\"参考数据映射管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String STDATA_REF_DATA_MAP_SELECT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.SELECT;
    @Desc("{\"id\":100066,\"privilegeName\":\"新增参考数据映射\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100065}")
    String STDATA_REF_DATA_MAP_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.ADD;
    @Desc("{\"id\":100067,\"privilegeName\":\"删除参考数据映射\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100065}")
    String STDATA_REF_DATA_MAP_DELETE = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.DELETE;
    @Desc("{\"id\":100068,\"privilegeName\":\"修改参考数据映射\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100065}")
    String STDATA_REF_DATA_MAP_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.EDIT;

    @Desc("{\"id\":100069,\"privilegeName\":\"周期任务\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String OPSCENTER_BATCH_SELECT = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_BATCH + ActionCode.SELECT;
    @Desc("{\"id\":100070,\"privilegeName\":\"实时任务\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    String OPSCENTER_REALTIME_SELECT = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_REALTIME + ActionCode.SELECT;
}
