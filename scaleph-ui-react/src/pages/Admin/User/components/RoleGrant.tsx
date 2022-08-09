import { ModalFormProps } from '@/app.d';
import { listAllRole } from '@/services/admin/role.service';
import { SecRole } from '@/services/admin/typings';
import { Modal, Transfer } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const RoleGrant: React.FC<ModalFormProps<SecRole>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [targetKeys, setTargetKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
  const [roleList, setRoleList] = useState<SecRole[]>([]);

  useEffect(() => {
    console.log(data);
    listAllRole().then((d) => {
      setRoleList(d);
    });
  }, []);

  const onSelectChange = (sourceSelectedKeys: string[], targetSelectedKeys: string[]) => {
    setSelectedKeys([...sourceSelectedKeys, ...targetSelectedKeys]);
  };

  const onChange = (nextTargetKeys: string[]) => {
    setTargetKeys(nextTargetKeys);
  };

  return (
    <Modal
      visible={visible}
      destroyOnClose={true}
      title={intl.formatMessage(
        { id: 'app.common.operate.grant.title' },
        { name: intl.formatMessage({ id: 'pages.admin.user.role' }) },
      ) + ' - ' + data.roleName}
      width={780}
      onCancel={onCancel}
      onOk={() => {
        console.log(targetKeys);
        onVisibleChange(false);
      }}
    >
      <Transfer
        dataSource={roleList}
        titles={[
          intl.formatMessage({ id: 'pages.admin.user.awaitGrant' }),
          intl.formatMessage({ id: 'pages.admin.user.granted' }),
        ]}
        listStyle={{width:'100%',minHeight:420 }}
        showSearch={true}
        pagination={true}
        targetKeys={targetKeys}
        selectedKeys={selectedKeys}
        onChange={onChange}
        onSelectChange={onSelectChange}
        render={(item) => item.roleName + ''}
        rowKey={(item) => item.id + ''}

      ></Transfer>
    </Modal>
  );
};

export default RoleGrant;
