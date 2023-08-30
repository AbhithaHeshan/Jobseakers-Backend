package lk.creativelabs.jobseekers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.creativelabs.jobseekers.entity.emberded.Address;
import lk.creativelabs.jobseekers.entity.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientCommenDTO {
    private long clientId;
    private String owner;
    private String businessName;
    private String businessType;
    private Address address;
    private String businessRegistrationDocUri;
    private String businessRegistrationNo;
    private String email;
    private String tel;
    private String profileImageUri;
    private String userName;
    private String password;
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    private String role;

    public ClientCommenDTO(long clientId,String owner, Address address, String businessName, String businessType, String email, String tel, String profileImageUri) {
        this.owner = owner;
        this.businessName = businessName;
        this.businessType = businessType;
        this.address = address;
        this.clientId = clientId;
        this.email = email;
        this.tel = tel;
        this.profileImageUri = profileImageUri;

    }
}
