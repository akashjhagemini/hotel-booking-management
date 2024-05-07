package com.akash.hotelbookingmanagement.repository;

import com.akash.hotelbookingmanagement.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Iterable<Room> findAllByType(String type);
}
