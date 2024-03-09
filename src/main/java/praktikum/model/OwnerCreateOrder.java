package praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerCreateOrder {
    private String name;
    private String email;
    private String createdAt;
    private String updatedAt;
}