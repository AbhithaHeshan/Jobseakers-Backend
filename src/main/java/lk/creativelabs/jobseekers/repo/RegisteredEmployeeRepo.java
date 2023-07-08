package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.RegisteredEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredEmployeeRepo extends JpaRepository<RegisteredEmployee,Long> {


}
