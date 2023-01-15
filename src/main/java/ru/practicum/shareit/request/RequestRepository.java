package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> getRequestsByRequestorId(Long userId, PageRequest pageRequest);

    @Query(" select r from Request  r " +
            "join Item i on i.request.id = r.id " +
            "where r.requestor.id <> ?1  and i.request.id <> 0" +
            "order by r.created desc ")
    Optional<List<Request>> getRequestsByItemOwner(Long userId, PageRequest pageRequest);

    List<Request> getAllByRequestorId(Long userId);


}
