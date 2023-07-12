package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.Employee;
import lk.creativelabs.jobseekers.entity.RegisteredEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegisteredEmployeeRepo extends JpaRepository<RegisteredEmployee,Long> {

      List<RegisteredEmployee>  findAllByEmployee_EmployeeId(long id);

      @Query("select re.employee.userId from  RegisteredEmployee  re  where  re.client.clientId =: clientId")
      List<String>  findRegisteredEmployeeList(long clientId);

      List<RegisteredEmployee> findByClient_ClientId(Long clientId);

}
