package Designovel.Capstone.domain.mallType;


import lombok.Data;

@Data
public class MallTypeDTO {

    private String mallTypeId;

    private String mallTypeName;

    public MallTypeDTO(MallType mallType) {
        this.mallTypeId = mallType.getMallTypeId();
        this.mallTypeName = mallType.getMallTypeName();
    }
}
