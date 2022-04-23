package cn.sliew.breeze.service.convert;

import java.util.List;

/**
 * do和dto对象转换接口
 *
 * @param <E>
 * @param <D>
 * @author gleiyu
 */

public interface BaseConvert<E, D> {
    /**
     * dto转换为do
     *
     * @param dto dto object
     * @return E entity
     */
    E toDo(D dto);

    /**
     * do转换为dto
     *
     * @param entity do object
     * @return D dto
     */
    D toDto(E entity);

    /**
     * dto list 转换为 do list
     *
     * @param dtoList dto List
     * @return List<E> entity list
     */
    List<E> toDo(List<D> dtoList);

    /**
     * do list 转换为 dto list
     *
     * @param entityList do list
     * @return List<D> dto list
     */
    List<D> toDto(List<E> entityList);

}
