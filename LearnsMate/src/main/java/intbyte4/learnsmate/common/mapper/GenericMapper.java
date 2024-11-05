package intbyte4.learnsmate.common.mapper;

public interface GenericMapper<D, E> {
    D toDTO(E entity);
    E toEntity(D dto);
}