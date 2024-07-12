package ait.cohort34.mentor_project.accounting.dao;

import ait.cohort34.mentor_project.accounting.model.PhotoUser;
import org.springframework.data.repository.CrudRepository;

public interface UserPhotoRepository extends CrudRepository<PhotoUser, Long> {
}

