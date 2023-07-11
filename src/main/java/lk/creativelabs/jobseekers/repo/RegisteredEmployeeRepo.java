package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.RegisteredEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegisteredEmployeeRepo extends JpaRepository<RegisteredEmployee,Long> {

      List<RegisteredEmployee>  findAllByEmployee_EmployeeId(long id);

}
