package Designovel.Capstone.domain.mallType;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mall_type")
public class MallType {

    @Id
    @Column(name = "mall_type_id")
    private String mallTypeId;

    @Column(name = "mall_type_name", unique = true, nullable = false)
    private String mallTypeName;
}