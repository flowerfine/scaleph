import { ModalFormProps } from '@/app.d';
import { Modal } from 'antd';

const SinkJdbcStepForm: React.FC<ModalFormProps<any>> = ({ data, visible, onCancel }) => {
  return (
    <Modal
      visible={visible}
      title="Sink jdbc"
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        console.log('submit', data);
      }}
    >
      123123
    </Modal>
  );
};

export default SinkJdbcStepForm;
